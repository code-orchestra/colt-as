package codeOrchestra.colt.as.controller;

import codeOrchestra.colt.as.ASLiveCodingManager;
import codeOrchestra.colt.as.compiler.fcsh.FCSHException;
import codeOrchestra.colt.as.compiler.fcsh.FCSHManager;
import codeOrchestra.colt.as.compiler.fcsh.MaximumCompilationsCountReachedException;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.digest.DigestException;
import codeOrchestra.colt.as.digest.ProjectDigestHelper;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.controller.AbstractCOLTController;
import codeOrchestra.colt.core.controller.COLTControllerCallback;
import codeOrchestra.colt.core.errorhandling.ErrorHandler;
import codeOrchestra.colt.core.tasks.COLTTask;
import codeOrchestra.colt.core.tasks.COLTTaskWithProgress;
import codeOrchestra.colt.core.tasks.TasksManager;
import codeOrchestra.colt.core.ui.components.COLTProgressIndicator;
import codeOrchestra.util.ProjectHelper;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsController extends AbstractCOLTController<COLTAsProject> {

    public void startProductionCompilation(final COLTControllerCallback<CompilationResult, CompilationResult> callback, final boolean run, boolean sync) {
        // TODO: implement
    }

    public void startBaseCompilation(final COLTControllerCallback<CompilationResult, CompilationResult> callback, final boolean run, boolean sync) {
        // TODO: implement!

        // TODO: save project
        // TODO: clear livecoding messages

        TasksManager.getInstance().scheduleBackgroundTask(new COLTTaskWithProgress<CompilationResult>() {
            @Override
            protected String getName() {
                return "Live Build";
            }

            @Override
            protected CompilationResult call(COLTProgressIndicator progressIndicator) {

                // Building digests
                progressIndicator.setText("Building digests");
                ProjectDigestHelper projectDigestHelper = new ProjectDigestHelper(ProjectHelper.getCurrentProject());
                try {
                    projectDigestHelper.build();
                } catch (DigestException e) {
                    ErrorHandler.handle(e, "Error while building digests");
                    callback.onError(e, null);
                    return null;
                }
                progressIndicator.setProgress(30);

                // Restart FCSH
                progressIndicator.setText("Restaring FCSH");
                try {
                    FCSHManager.instance().restart();
                } catch (FCSHException e) {
                    ErrorHandler.handle(e, "Error while starting fcsh");
                    callback.onError(e, null);
                    return null;
                } catch (MaximumCompilationsCountReachedException e) {
                    ErrorHandler.handle("Maximum compilations count allowed in Demo mode is exceeded", "COLT Demo mode");
                    callback.onError(e, null);
                    return null;
                } catch (Throwable t) {
                    ErrorHandler.handle(t, "Error while starting fcsh");
                    callback.onError(t, null);
                    t.printStackTrace();
                    return null;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
                progressIndicator.setProgress(10);


                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    public void dispose() {
    }

}
