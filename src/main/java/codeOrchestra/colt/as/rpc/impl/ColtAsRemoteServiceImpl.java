package codeOrchestra.colt.as.rpc.impl;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.controller.ColtAsController;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.rpc.ColtAsRemoteService;
import codeOrchestra.colt.as.rpc.model.ColtCompilationResult;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.controller.ColtController;
import codeOrchestra.colt.core.controller.ColtControllerCallbackEx;
import codeOrchestra.colt.core.rpc.AbstractColtRemoteService;
import codeOrchestra.colt.core.rpc.ColtRemoteTransferableException;
import codeOrchestra.colt.core.rpc.command.RemoteAsyncCommand;

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
                }, run);
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
                }, run);
            }
        });
    }

    @Override
    public ColtCompilationResult runBaseCompilation(String securityToken) throws ColtRemoteTransferableException {
        return runBaseCompilation(securityToken, true);
    }

}
