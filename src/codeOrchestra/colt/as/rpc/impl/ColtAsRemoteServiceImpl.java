package codeOrchestra.colt.as.rpc.impl;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.controller.ColtAsController;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.rpc.ColtAsRemoteService;
import codeOrchestra.colt.as.rpc.model.ColtCompilationResult;
import codeOrchestra.colt.as.rpc.model.ColtRemoteProject;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.controller.ColtController;
import codeOrchestra.colt.core.controller.ColtControllerCallbackEx;
import codeOrchestra.colt.core.rpc.AbstractColtRemoteService;
import codeOrchestra.colt.core.rpc.ColtRemoteException;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.command.RemoteAsyncCommand;
import codeOrchestra.colt.core.rpc.command.RemoteCommand;

import java.io.File;
import java.io.IOException;

/**
 * @author Alexander Eliseyev
 */
public class ColtAsRemoteServiceImpl extends AbstractColtRemoteService<AsProject> implements ColtAsRemoteService {

    private ColtAsController controller = (ColtAsController) ServiceProvider.get(ColtController.class);

    @Override
    public void dispose() {
    }

    @Override
    public ColtCompilationResult runProductionCompilation(String securityToken, final boolean run) throws ColtRemoteTransferableException {
        return executeSecurilyAsyncInUI(securityToken, new RemoteAsyncCommand<ColtCompilationResult>() {
            @Override
            public String getName() {
                return "Production Compile" + (run ? " and Run" : "");
            }

            @Override
            public void execute(final ColtControllerCallbackEx<ColtCompilationResult> callback) {
                controller.startProductionCompilation(new ColtControllerCallbackEx<CompilationResult>() {
                    @Override
                    public void onComplete(CompilationResult successResult) {
                        callback.onComplete(new ColtCompilationResult(successResult));
                    }

                    @Override
                    public void onError(Throwable t, CompilationResult errorResult) {
                        callback.onError(t, errorResult != null ? new ColtCompilationResult(errorResult) : null);
                    }
                }, run, false);
            }
        });
    }

    @Override
    public ColtCompilationResult runBaseCompilation(String securityToken, final boolean run)
            throws ColtRemoteTransferableException {
        return executeSecurilyAsyncInUI(securityToken, new RemoteAsyncCommand<ColtCompilationResult>() {
            @Override
            public String getName() {
                return "Base Compile and Run";
            }

            @Override
            public void execute(final ColtControllerCallbackEx<ColtCompilationResult> callback) {
                controller.startBaseCompilation(new ColtControllerCallbackEx<CompilationResult>() {
                    @Override
                    public void onComplete(CompilationResult successResult) {
                        callback.onComplete(new ColtCompilationResult(successResult));
                    }

                    @Override
                    public void onError(Throwable t, CompilationResult errorResult) {
                        callback.onError(t, errorResult != null ? new ColtCompilationResult(errorResult) : null);
                    }
                }, run, false);
            }
        });
    }

    @Override
    public ColtCompilationResult runBaseCompilation(String securityToken) throws ColtRemoteTransferableException {
        return runBaseCompilation(securityToken, true);
    }

    @Override
    public void createProject(String securityToken, final ColtRemoteProject remoteProject)
            throws ColtRemoteTransferableException {
        executeSecurilyInUI(securityToken, new RemoteCommand<Void>() {
            @Override
            public String getName() {
                return "Create project under " + remoteProject.getPath();
            }

            @Override
            public Void execute() throws ColtRemoteException {
                File projectFile = new File(remoteProject.getPath());
                if (projectFile.exists()) {
                    projectFile.delete();
                }
                try {
                    projectFile.createNewFile();
                } catch (IOException e) {
                    throw new ColtRemoteException("Error while creating project file", e);
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

    public void loadProject(String securityToken, final String path) throws ColtRemoteTransferableException {
        executeSecurilyInUI(securityToken, new RemoteCommand<Void>() {
            @Override
            public String getName() {
                return "Load project " + path;
            }

            @Override
            public Void execute() throws ColtRemoteException {
                // TODO: implement
                /*
                try {
                    ProjectManager.getInstance().openProject(path, window);
                    return null;
                } catch (PartInitException e) {
                    throw new ColtRemoteException(e);
                }
                */

                return null;
            }
        });
    }

}
