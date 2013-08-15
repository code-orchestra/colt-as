package codeOrchestra.colt.as.controller;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.core.controller.AbstractCOLTController;
import codeOrchestra.colt.core.controller.COLTControllerCallback;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsController extends AbstractCOLTController<COLTAsProject> {

    public void startProductionCompilation(final COLTControllerCallback<CompilationResult, CompilationResult> callback, final boolean run, boolean sync) {
        // TODO: implement!
    }

    public void startBaseCompilation(final COLTControllerCallback<CompilationResult, CompilationResult> callback, final boolean run, boolean sync) {
        // TODO: implement!
    }

    @Override
    public void dispose() {
    }

}
