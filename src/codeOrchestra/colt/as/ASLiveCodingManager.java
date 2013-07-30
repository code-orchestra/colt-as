package codeOrchestra.colt.as;

import codeOrchestra.colt.as.compiler.fcsh.MaximumCompilationsCountReachedException;
import codeOrchestra.colt.as.compiler.fcsh.make.COLTAsMaker;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.MakeException;
import codeOrchestra.colt.as.digest.EmbedDigest;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.run.Target;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFile;
import codeOrchestra.colt.as.socket.command.impl.PongTraceCommand;
import codeOrchestra.colt.as.util.PathUtils;
import codeOrchestra.colt.core.AbstractLiveCodingManager;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.errorhandling.ErrorHandler;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.session.LiveCodingSession;
import codeOrchestra.colt.core.session.listener.LiveCodingAdapter;
import codeOrchestra.colt.core.session.listener.LiveCodingListener;
import codeOrchestra.colt.core.session.sourcetracking.SourcesTrackerThread;
import codeOrchestra.colt.core.socket.ClientSocketHandler;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingManager extends AbstractLiveCodingManager<COLTAsProject> implements LiveCodingManager<COLTAsProject> {

    private static final Logger LOG = Logger.getLogger(LiveCodingManager.class);

    private SourcesTrackerThread sourceTrackerThread;
    private boolean compilationInProgress;

    private List<ASSourceFile> changedFiles = new ArrayList<ASSourceFile>();

    private LiveCodingListener finisherThreadLiveCodingListener = new SessionHandleListener();

    private Object runMonitor = new Object();

    private List<String> deliveryMessages = new ArrayList<String>();
    private Map<String, List<String>> deliveryMessagesHistory = new HashMap<String, List<String>>();

    // full path -> list of embeds
    private Map<String, List<EmbedDigest>> embedDigests = new HashMap<String, List<EmbedDigest>>();

    private int packageId = 1;

    public ASLiveCodingManager() {
        addListener(finisherThreadLiveCodingListener);
    }

    private CompilationResult compileProject(boolean production) {
        try {
            compilationInProgress = true;

            COLTAsProject currentProject = COLTAsProject.getCurrentProject();

            if (!production) {
                FileUtils.clear(new File(PathUtils.getIncrementalOutputDir(currentProject)));
            }

            // COLT-186
            String htmlTemplatePath = currentProject.getProjectPaths().getHTMLTemplatePath();
            if (StringUtils.isNotEmpty(htmlTemplatePath)) {
                File htmlTemplateDir = new File(htmlTemplatePath);
                if (htmlTemplateDir.exists() && htmlTemplateDir.isDirectory()) {
                    FileUtils.copyDir(htmlTemplateDir, currentProject.getOutputDir(), false);
                }
            }

            COLTAsMaker lcsMaker = new COLTAsMaker(false);
            lcsMaker.setProductionMode(production);
            try {
                return lcsMaker.make();
            } catch (MakeException e) {
                ErrorHandler.handle(e, "Error while running " + (production ? "production" : "base live") + " compilation");
            } catch (MaximumCompilationsCountReachedException e) {
                ErrorHandler.handle("Maximum compilations count allowed in Demo mode is exceeded", "COLT Demo mode");
            }
        } finally {
            compilationInProgress = false;
        }
        return CompilationResult.ABORTED;
    }

    public CompilationResult runBaseCompilation() {
        return compileProject(false);
    }

    public CompilationResult runProductionCompilation() {
        return compileProject(true);
    }

    public void addDeliveryMessageToHistory(String broadcastId, String deliveryMessage) {
        List<String> history = deliveryMessagesHistory.get(broadcastId);
        if (history == null) {
            history = new ArrayList<String>();
            deliveryMessagesHistory.put(broadcastId, history);
        }
        history.add(deliveryMessage);
    }

    public void addDeliveryMessage(String deliveryMessage) {
        deliveryMessages.add(deliveryMessage);
    }

    public void resetEmbeds(List<EmbedDigest> embeds) {
        embedDigests.clear();
        for (EmbedDigest embedDigest : embeds) {
            List<EmbedDigest> storedEmbeds = getEmbedDigests(embedDigest.getFullPath());
            if (storedEmbeds == null) {
                storedEmbeds = new ArrayList<EmbedDigest>();
                embedDigests.put(embedDigest.getFullPath(), storedEmbeds);
            }

            storedEmbeds.add(embedDigest);
        }
    }

    private List<EmbedDigest> getEmbedDigests(String fullPath) {
        return embedDigests.get(fullPath);
    }

    @Override
    public void dispose() {
        super.dispose();
        sourceTrackerThread.stopRightThere();

        // TODO: implement
    }

    @Override
    public void startSession(String broadcastId, String clientId, Map<String, String> clientInfo, ClientSocketHandler clientSocketHandler) {
        boolean noSessionsWereActive = currentSessions.isEmpty();

        LiveCodingSession newSession = new LiveCodingSessionImpl(broadcastId, clientId, clientInfo, System.currentTimeMillis(), clientSocketHandler);
        currentSessions.put(clientId, newSession);

        if (noSessionsWereActive) {
            resetPackageId();
            startListeningForSourcesChanges();
        }

        fireSessionStart(newSession);
    }

    @Override
    public void stopSession(LiveCodingSession liveCodingSession) {
        // TODO: implement
    }

    private class SessionHandleListener extends LiveCodingAdapter {

        // clientId -> session finisher thread
        private Map<String, SessionFinisher> sessionFinisherThreads = new HashMap<String, LiveCodingManager.SessionFinisher>();

        @Override
        public void onSessionStart(LiveCodingSession session) {
            String clientId = session.getClientId();

            SessionFinisher sessionFinisherThread = getSessionFinisherThread(clientId);
            if (sessionFinisherThread == null) {
                sessionFinisherThread = new SessionFinisher(clientId);
                sessionFinisherThreads.put(clientId, sessionFinisherThread);
            } else {
                sessionFinisherThread.stopRightThere();
            }
            sessionFinisherThread.start();

            if (COLTAsProject.getCurrentProject().getProjectLiveSettings().getLaunchTarget() != Target.SWF) {
                sendBaseUrl(session, getWebOutputAddress());
            }

            restoreSessionState(session);

            LOG.info("Established a connection: broadcast ID: " + session.getBroadcastId() + ", cliend ID: " + clientId + ", client: " + session.getBasicClientInfo());
        }

        public SessionFinisher getSessionFinisherThread(String clientId) {
            return sessionFinisherThreads.get(clientId);
        }

        @Override
        public void onSessionEnd(LiveCodingSession session) {
            SessionFinisher sessionFinisherThread = getSessionFinisherThread(session.getClientId());
            if (sessionFinisherThread != null) {
                sessionFinisherThread.stopRightThere();
            }
        }
    }

    private class SessionFinisher extends Thread implements PongTraceCommand.PongListener {

        public static final int PING_TIMEOUT = 3000;
        public static final String PING_COMMAND = "ping";

        private boolean stop;

        private long lastPing;
        private long lastPong;

        private final String clientId;

        public SessionFinisher(String clientId) {
            this.clientId = clientId;
        }

        public void stopRightThere() {
            this.stop = true;
            PongTraceCommand.getInstance().removePongListener(this);
        }

        private LiveCodingSession getLiveCodingSession() {
            return currentSessions.get(clientId);
        }

        private void ping() {
            lastPing = System.currentTimeMillis();
            getLiveCodingSession().getSocketWriter().writeToSocket(PING_COMMAND);
        }

        @Override
        public void pong() {
            lastPong = System.currentTimeMillis();
        }

        @Override
        public void run() {
            this.stop = false;
            PongTraceCommand.getInstance().addPongListener(this);

            while (!stop) {
                LiveCodingSession liveCodingSession = getLiveCodingSession();
                if (liveCodingSession == null) {
                    continue;
                }

                ping();

                try {
                    Thread.sleep(PING_TIMEOUT);
                } catch (InterruptedException e) {
                    // do nothing
                }

                if (lastPong < lastPing) {
                    COLTAsProject coltAsProject = COLTAsProject.getCurrentProject();
                    if (coltAsProject == null || coltAsProject.getProjectLiveSettings().disconnectOnTimeout()) {
                        stopSession(liveCodingSession);
                    }
                }
            }
        }
    }

}
