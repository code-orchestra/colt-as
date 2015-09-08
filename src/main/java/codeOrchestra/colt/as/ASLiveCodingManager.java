package codeOrchestra.colt.as;

import codeOrchestra.colt.as.compiler.fcsh.FCSHException;
import codeOrchestra.colt.as.compiler.fcsh.FCSHManager;
import codeOrchestra.colt.as.compiler.fcsh.make.AsMaker;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.MakeException;
import codeOrchestra.colt.as.digest.EmbedDigest;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.run.Target;
import codeOrchestra.colt.as.session.ASLiveCodingSession;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFile;
import codeOrchestra.colt.as.socket.command.impl.PongTraceCommand;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.colt.core.AbstractLiveCodingManager;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.errorhandling.ErrorHandler;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.session.LiveCodingSession;
import codeOrchestra.colt.core.session.SocketWriterAdapter;
import codeOrchestra.colt.core.session.listener.LiveCodingAdapter;
import codeOrchestra.colt.core.session.sourcetracking.SourcesTrackerCallback;
import codeOrchestra.colt.core.session.sourcetracking.SourcesTrackerThread;
import codeOrchestra.util.*;
import codeOrchestra.util.templates.TemplateCopyUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingManager extends AbstractLiveCodingManager<AsProject, SocketWriterAdapter> {

    private static final Logger LOG = Logger.getLogger(LiveCodingManager.class);

    private SourcesTrackerThread sourceTrackerThread;
    private boolean compilationInProgress;

    private List<ASSourceFile> changedFiles = new ArrayList<>();

    private SessionHandleListener finisherThreadLiveCodingListener = new SessionHandleListener();

    private final Object runMonitor = new Object();

    private List<String> deliveryMessages = new ArrayList<>();
    private Map<String, List<String>> deliveryMessagesHistory = new HashMap<>();

    // full path -> list of embeds
    private Map<String, List<EmbedDigest>> embedDigests = new HashMap<>();
    private Set<String> usedEmbedExtensions = new HashSet<>();

    private List<ASSourceFile> changesBuffer = new ArrayList<>();
    private final Object changesBufferMonitor = new Object();

    private int packageId = 1;

    private SourcesTrackerCallback sourcesTrackerCallback = sourceFile -> {
        if (sourceFile instanceof ASSourceFile) {
            if (isPaused()) {
                synchronized (changesBufferMonitor) {
                    changesBuffer.add((ASSourceFile) sourceFile);
                }
                return;
            }

            reportChangedFile((ASSourceFile) sourceFile);
        }
    };

    public ASLiveCodingManager() {
        addListener(finisherThreadLiveCodingListener);
    }

    @Override
    protected void doFlush() {
        synchronized (changesBufferMonitor) {
            for (ASSourceFile sourceFile : changesBuffer) {
                reportChangedFile(sourceFile);
                ThreadUtils.sleep(100);
            }

            changesBuffer.clear();
        }
    }

    private CompilationResult compileProject(boolean production) {
        try {
            compilationInProgress = true;

            AsProject currentProject = AsProject.getCurrentProject();

            if (!production) {
                FileUtils.clear(new File(ASPathUtils.getIncrementalOutputDir(currentProject)));
            }

            // COLT-186
            String htmlTemplatePath = currentProject.getProjectPaths().getHTMLTemplatePath();
            if (StringUtils.isNotEmpty(htmlTemplatePath)) {
                File htmlTemplateDir = new File(htmlTemplatePath);
                if (htmlTemplateDir.exists() && htmlTemplateDir.isDirectory()) {
                    FileUtils.copyDir(htmlTemplateDir, currentProject.getOutputDir(), false);
                }
            }

            AsMaker lcsMaker = new AsMaker(false);
            lcsMaker.setProductionMode(production);
            try {
                return lcsMaker.make();
            } catch (MakeException e) {
                ErrorHandler.handle(e, "Error while running " + (production ? "production" : "base live") + " compilation");
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

    public synchronized void runIncrementalCompilation() {
        AsProject currentProject = AsProject.getCurrentProject();
        try {
            compilationInProgress = true;

            List<ASSourceFile> changedFilesSnapshot;
            synchronized (runMonitor) {
                changedFilesSnapshot = new ArrayList<>(changedFiles);
                changedFiles.clear();
            }

            try {
                FCSHManager.instance().startCPUProfiling();
            } catch (FCSHException e) {
                ErrorHandler.handle(e, "Error while starting profling");
            }

            AsMaker lcsMaker = new AsMaker(changedFilesSnapshot);
            try {
                if (lcsMaker.make().isOk()) {
                    try {
                        FCSHManager.instance().stopCPUProfiling();
                    } catch (FCSHException e) {
                        ErrorHandler.handle(e, "Error while stopping profling");
                    }

                    // Copy the swc to the incremental dir
                    try {
                        FileUtils.copyFileChecked(new File(ASPathUtils.getSourceIncrementalSWCPath(currentProject)), new File(ASPathUtils.getTargetIncrementalSWCPath(currentProject, packageId)), false);
                    } catch (IOException e) {
                        ErrorHandler.handle(e, "Error while copying incremental compilation artifact");
                    }

                    // Extract and copy the artifact
                    try {
                        UnzipUtil.unzip(new File(ASPathUtils.getSourceIncrementalSWCPath(currentProject)), FileUtils.getTempDir());
                    } catch (IOException e) {
                        ErrorHandler.handle(e, "Error while unzipping incremental compilation artifact");
                    }

                    // Copy the swf from swc and copy to the incremental dir
                    File extractedSWF = new File(FileUtils.getTempDir(), "library.swf");
                    if (extractedSWF.exists()) {
                        File artifact = new File(ASPathUtils.getIncrementalSWFPath(currentProject, packageId));
                        try {
                            FileUtils.copyFileChecked(extractedSWF, artifact, false);
                        } catch (IOException e) {
                            ErrorHandler.handle(e, "Error while copying incremental compilation artifact");
                        }

                        for (String deliveryMessage : deliveryMessages) {
                            if (StringUtils.isNotEmpty(deliveryMessage)) {
                                sendLiveCodingMessage(deliveryMessage);
                            } else {
                                LOG.debug("No updatable changes were made sine last compilation");
                            }
                        }
                        deliveryMessages.clear();

                        fireCodeUpdate();

                        incrementPackageNumber();
                    }

                    tryRunIncrementalCompilation();
                }
            } catch (MakeException e) {
                ErrorHandler.handle(e, "Error while compiling");
            }
        } finally {
            compilationInProgress = false;
        }
    }

    private void reportChangedFile(ASSourceFile sourceFile) {
        if (sourceFile.isAsset()) {
            List<EmbedDigest> embedDigestsByFullPath = embedDigests.get(sourceFile.getFile().getPath());

            Map<String, List<String>> mimeTypeToSourceAttributes = new HashMap<>();
            for (EmbedDigest embedDigest : embedDigestsByFullPath) {
                List<String> sourceAttributes = mimeTypeToSourceAttributes.get(embedDigest.getMimeType());
                if (sourceAttributes == null) {
                    sourceAttributes = new ArrayList<>();
                    mimeTypeToSourceAttributes.put(embedDigest.getMimeType(), sourceAttributes);
                }

                if (!sourceAttributes.contains(embedDigest.getSource())) {
                    sourceAttributes.add(embedDigest.getSource());
                }
            }

            for (String mimeType : mimeTypeToSourceAttributes.keySet()) {
                tryUpdateAsset(sourceFile, mimeType, mimeTypeToSourceAttributes.get(mimeType));
            }
        } else if (sourceFile.isCompilable()) {
            synchronized (runMonitor) {
                changedFiles.add(sourceFile);
            }
            tryRunIncrementalCompilation();
        }
    }

    private synchronized void tryUpdateAsset(ASSourceFile assetFile, String mimeType, List<String> sourceAttributes) {
        long timeStamp = System.currentTimeMillis();

        // 0 - clear the incremental sources dir
        AsProject currentProject = AsProject.getCurrentProject();
        FileUtils.clear(currentProject.getOrCreateIncrementalSourcesDir());

        // 1 - copy the changed asset to the root of incremental dir
        try {
            FileUtils.copyFileChecked(assetFile.getFile(), new File(currentProject.getOrCreateIncrementalSourcesDir(), assetFile.getFile().getName()), false);
        } catch (IOException e) {
            throw new RuntimeException("Can't copy the asset file: " + assetFile.getFile().getPath(), e);
        }

        // 2 - copy/modify the source template file
        String classPostfix = assetFile.getFile().getName().replace(".", "_").replace(" ", "_") + timeStamp;
        String className = "Asset_" + classPostfix;
        File templateFile = new File(ASPathUtils.getTemplatesDir(), StringUtils.isEmpty(mimeType) ? "Asset_update_template.as" : "Asset_update_mimetype_template.as");
        File targetFile = new File(currentProject.getOrCreateIncrementalSourcesDir(), "codeOrchestra/liveCoding/load/" + className + ".as");

        Map<String, String> replacements = new HashMap<>();
        replacements.put("{CLASS_POSTFIX}", classPostfix);
        replacements.put("{RELATIVE_PATH}", "/" + assetFile.getFile().getName());
        replacements.put("{MIME_TYPE}", mimeType);

        try {
            TemplateCopyUtil.copy(templateFile, targetFile, replacements);
        } catch (IOException e) {
            throw new RuntimeException("Can't copy the asset update source file: " + templateFile.getPath(), e);
        }

        // 3 - compile
        AsMaker lcsMaker = new AsMaker(Collections.singletonList(new ASSourceFile(targetFile, currentProject.getOrCreateIncrementalSourcesDir().getPath())), true);
        try {
            if (lcsMaker.make().isOk()) {
                // Extract and copy the artifact
                try {
                    UnzipUtil.unzip(new File(ASPathUtils.getSourceIncrementalSWCPath(currentProject)), FileUtils.getTempDir());
                } catch (IOException e) {
                    ErrorHandler.handle(e, "Error while unzipping incremental compilation artifact (asset)");
                }

                // 4 - copy the incremental swf
                File extractedSWF = new File(FileUtils.getTempDir(), "library.swf");
                if (extractedSWF.exists()) {
                    File artifact = new File(ASPathUtils.getIncrementalSWFPath(currentProject, packageId));
                    try {
                        FileUtils.copyFileChecked(extractedSWF, artifact, false);
                    } catch (IOException e) {
                        ErrorHandler.handle(e, "Error while copying incremental compilation artifact (asset)");
                    }

                    // 5 - send the message
                    for (String sourceAttribute : sourceAttributes) {
                        StringBuilder sb = new StringBuilder("asset");
                        sb.append(":").append("codeOrchestra.liveCoding.load.").append(className).append(":");
                        sb.append(sourceAttribute).append(":");
                        if (StringUtils.isEmpty(mimeType)) {
                            sb.append(":");
                        } else {
                            sb.append(mimeType).append(":");
                        }
                        sb.append(timeStamp);

                        sendLiveCodingMessage(sb.toString());

                        try {
                            Thread.sleep(60);
                        } catch (InterruptedException ignored) {
                        }
                    }

                    // 6 - increment package number
                    incrementPackageNumber();

                    fireCodeUpdate();
                }
            }
        } catch (MakeException e) {
            ErrorHandler.handle(e, "Error while compiling");
        }
    }

    public void sendLiveCodingMessage(String message) {
        for (LiveCodingSession liveCodingSession : currentSessions.values()) {
            liveCodingSession.sendLiveCodingMessage(message, String.valueOf(packageId), true);
        }
    }

    private void tryRunIncrementalCompilation() {
        synchronized (runMonitor) {
            if (changedFiles.isEmpty()) {
                return;
            }
        }

        if (!compilationInProgress) {
            runIncrementalCompilation();
        }
    }

    private void incrementPackageNumber() {
        packageId++;
    }

    public void addDeliveryMessageToHistory(String broadcastId, String deliveryMessage) {
        List<String> history = deliveryMessagesHistory.get(broadcastId);
        if (history == null) {
            history = new ArrayList<>();
            deliveryMessagesHistory.put(broadcastId, history);
        }
        history.add(deliveryMessage);
    }

    public void addDeliveryMessage(String deliveryMessage) {
        deliveryMessages.add(deliveryMessage);
    }

    public void resetEmbeds(List<EmbedDigest> embeds) {
        embedDigests.clear();
        usedEmbedExtensions.clear();

        for (EmbedDigest embedDigest : embeds) {
            List<EmbedDigest> storedEmbeds = getEmbedDigests(embedDigest.getFullPath());
            if (storedEmbeds == null) {
                storedEmbeds = new ArrayList<>();
                embedDigests.put(embedDigest.getFullPath(), storedEmbeds);
            }

            storedEmbeds.add(embedDigest);

            String extension = FileUtils.getFileExtension(embedDigest.getFile());
            if (extension != null) {
                usedEmbedExtensions.add(extension);
            }
        }
    }

    public Set<String> getUsedEmbedExtensions() {
        return usedEmbedExtensions;
    }

    private List<EmbedDigest> getEmbedDigests(String fullPath) {
        return embedDigests.get(fullPath);
    }

    public void resetSession() {
        packageId = 1;
        paused = false;
        changedFiles.clear();
    }

    @Override
    public void dispose() {
        super.dispose();

        finisherThreadLiveCodingListener.dispose();

        if (sourceTrackerThread != null) {
            sourceTrackerThread.stopRightThere();
        }

        changedFiles.clear();
        deliveryMessages.clear();
        deliveryMessagesHistory.clear();
        embedDigests.clear();
    }

    @Override
    public void startSession(String broadcastId, String clientId, Map<String, String> clientInfo, SocketWriterAdapter socketWriter) {
        boolean noSessionsWereActive = currentSessions.isEmpty();

        int sessionNumber = currentSessions.size() + 1;

        LiveCodingSession newSession = new ASLiveCodingSession(broadcastId, clientId, clientInfo, System.currentTimeMillis(), socketWriter, sessionNumber);
        currentSessions.put(clientId, newSession);

        if (noSessionsWereActive) {
            resetSession();
            startListeningForSourcesChanges();
        }

        fireSessionStart(newSession);
    }

    public void startListeningForSourcesChanges() {
        List<File> watchedDirs = new ArrayList<>();
        AsProject currentProject = AsProject.getCurrentProject();
        for (String sourceDirPath : currentProject.getProjectPaths().getSourcePaths()) {
            File sourceDir = new File(sourceDirPath);
            if (sourceDir.exists() && sourceDir.isDirectory()) {
                watchedDirs.add(sourceDir);
            }
        }
        for (String assetDirPath : currentProject.getProjectPaths().getAssetPaths()) {
            File assetDir = new File(assetDirPath);
            if (assetDir.exists() && assetDir.isDirectory()) {
                watchedDirs.add(assetDir);
            }
        }

        sourceTrackerThread = new SourcesTrackerThread(sourcesTrackerCallback, watchedDirs);
        sourceTrackerThread.start();
    }

    public void stopListeningForSourcesChanges() {
        if (sourceTrackerThread != null) {
            sourceTrackerThread.stopRightThere();
            sourceTrackerThread = null;
        }
    }

    public void sendBaseUrl(LiveCodingSession session, String baseUrl) {
        session.sendLiveCodingMessage("base-url:" + baseUrl, String.valueOf(packageId), false);
    }

    public String getWebOutputAddress() {
        return ProjectHelper.getCurrentProject().getWebOutputPath();
    }

    private void restoreSessionState(LiveCodingSession session) {
        List<String> history = deliveryMessagesHistory.get(session.getBroadcastId());
        if (history != null) {
            for (String deliveryMessage : history) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // ignore
                }
                session.sendMessageAsIs(deliveryMessage);
            }
        }
    }

    public void stopSession(LiveCodingSession liveCodingSession) {
        if (liveCodingSession.isDisposed()) {
            return;
        }

        liveCodingSession.dispose();

        currentSessions.remove(liveCodingSession.getClientId());

        if (currentSessions.isEmpty()) {
            resetSession();
            stopListeningForSourcesChanges();
        }

        fireSessionEnd(liveCodingSession);
    }

    private class SessionHandleListener extends LiveCodingAdapter {
        // clientId -> session finisher thread
        private Map<String, SessionFinisher> sessionFinisherThreads = new HashMap<>();

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

            if (AsProject.getCurrentProject().getProjectBuildSettings().getLaunchTarget() != Target.SWF) {
                sendBaseUrl(session, getWebOutputAddress());
            }

            restoreSessionState(session);

            LOG.info("Established connection #" + session.getSessionNumber() + " with client: " + session.getBasicClientInfo());
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

            LOG.info("Closed connection #" + session.getSessionNumber());
        }

        public void dispose() {
            sessionFinisherThreads.values().forEach(SessionFinisher::stopRightThere);
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

        private ASLiveCodingSession getLiveCodingSession() {
            return (ASLiveCodingSession) currentSessions.get(clientId);
        }

        private void ping() {
            lastPing = System.currentTimeMillis();
            getLiveCodingSession().getSocketWrapper().sendMessage(PING_COMMAND);
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
                    AsProject coltAsProject = AsProject.getCurrentProject();
                    if (coltAsProject == null || coltAsProject.getProjectLiveSettings().disconnectOnTimeout()) {
                        stopSession(liveCodingSession);
                    }
                }
            }
        }
    }

}