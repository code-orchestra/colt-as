package codeOrchestra.colt.as.rpc.impl;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.controller.COLTAsController;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.rpc.COLTAsRemoteService;
import codeOrchestra.colt.as.rpc.model.COLTCompilationResult;
import codeOrchestra.colt.as.rpc.model.COLTConnection;
import codeOrchestra.colt.as.rpc.model.COLTRemoteProject;
import codeOrchestra.colt.as.rpc.model.COLTState;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.controller.COLTController;
import codeOrchestra.colt.core.controller.COLTControllerCallbackEx;
import codeOrchestra.colt.core.rpc.AbstractCOLTRemoteService;
import codeOrchestra.colt.core.rpc.COLTRemoteException;
import codeOrchestra.colt.core.rpc.COLTRemoteTransferableException;
import codeOrchestra.colt.core.rpc.command.RemoteAsyncCommand;
import codeOrchestra.colt.core.rpc.command.RemoteCommand;
import codeOrchestra.colt.core.session.LiveCodingSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsRemoteServiceImpl extends AbstractCOLTRemoteService<COLTAsProject> implements COLTAsRemoteService {

    private COLTAsController controller = (COLTAsController) ServiceProvider.get(COLTController.class);

    @Override
    public void dispose() {
    }

    @Override
    public COLTCompilationResult runProductionCompilation(String securityToken, final boolean run) throws COLTRemoteTransferableException {
        return executeSecurilyAsyncInUI(securityToken, new RemoteAsyncCommand<COLTCompilationResult>() {
            @Override
            public String getName() {
                return "Production Compile" + (run ? " and Run" : "");
            }

            @Override
            public void execute(final COLTControllerCallbackEx<COLTCompilationResult> callback) {
                controller.startProductionCompilation(new COLTControllerCallbackEx<CompilationResult>() {
                    @Override
                    public void onComplete(CompilationResult successResult) {
                        callback.onComplete(new COLTCompilationResult(successResult));
                    }

                    @Override
                    public void onError(Throwable t, CompilationResult errorResult) {
                        callback.onError(t, errorResult != null ? new COLTCompilationResult(errorResult) : null);
                    }
                }, run, false);
            }
        });
    }

    @Override
    public COLTCompilationResult runBaseCompilation(String securityToken, final boolean run)
            throws COLTRemoteTransferableException {
        return executeSecurilyAsyncInUI(securityToken, new RemoteAsyncCommand<COLTCompilationResult>() {
            @Override
            public String getName() {
                return "Base Compile and Run";
            }

            @Override
            public void execute(final COLTControllerCallbackEx<COLTCompilationResult> callback) {
                controller.startBaseCompilation(new COLTControllerCallbackEx<CompilationResult>() {
                    @Override
                    public void onComplete(CompilationResult successResult) {
                        callback.onComplete(new COLTCompilationResult(successResult));
                    }

                    @Override
                    public void onError(Throwable t, CompilationResult errorResult) {
                        callback.onError(t, errorResult != null ? new COLTCompilationResult(errorResult) : null);
                    }
                }, run, false);
            }
        });
    }

    @Override
    public COLTCompilationResult runBaseCompilation(String securityToken) throws COLTRemoteTransferableException {
        return runBaseCompilation(securityToken, true);
    }

    @Override
    public COLTState getState(String securityToken) throws COLTRemoteTransferableException {
        return executeSecurily(securityToken, new RemoteCommand<COLTState>() {
            @Override
            public String getName() {
                return "Get COLT state";
            }

            @Override
            public COLTState execute() throws COLTRemoteException {
                COLTState state = new COLTState();

                List<COLTConnection> coltConnections = new ArrayList<>();
                List<LiveCodingSession> currentConnections = ServiceProvider.get(LiveCodingManager.class).getCurrentConnections();
                for (LiveCodingSession session : currentConnections) {
                    if (!session.isDisposed()) {
                        coltConnections.add(new COLTConnection(session));
                    }
                }
                state.setActiveConnections(coltConnections.toArray(new COLTConnection[coltConnections.size()]));

                return state;
            }
        });
    }

    @Override
    public void createProject(String securityToken, final COLTRemoteProject remoteProject)
            throws COLTRemoteTransferableException {
        executeSecurilyInUI(securityToken, new RemoteCommand<Void>() {
            @Override
            public String getName() {
                return "Create project under " + remoteProject.getPath();
            }

            @Override
            public Void execute() throws COLTRemoteException {
                File projectFile = new File(remoteProject.getPath());
                if (projectFile.exists()) {
                    projectFile.delete();
                }
                try {
                    projectFile.createNewFile();
                } catch (IOException e) {
                    throw new COLTRemoteException("Error while creating project file", e);
                }

                // TODO: implement
                /*
                LCSProject currentProject = LCSProject.getCurrentProject();
                if (currentProject != null) {
                    currentProject.setDisposed();
                }
                LiveCodingProjectViews.closeProjectViews();
                LCSProject newProject = LCSProject.createNew(remoteProject.getName(), remoteProject.getPath());
                remoteProject.copyTo(newProject);
                */

                return null;
            }
        });
    }

    public void loadProject(String securityToken, final String path) throws COLTRemoteTransferableException {
        executeSecurilyInUI(securityToken, new RemoteCommand<Void>() {
            @Override
            public String getName() {
                return "Load project " + path;
            }

            @Override
            public Void execute() throws COLTRemoteException {
                // TODO: implement
                /*
                try {
                    ProjectManager.getInstance().openProject(path, window);
                    return null;
                } catch (PartInitException e) {
                    throw new COLTRemoteException(e);
                }
                */

                return null;
            }
        });
    }

}
