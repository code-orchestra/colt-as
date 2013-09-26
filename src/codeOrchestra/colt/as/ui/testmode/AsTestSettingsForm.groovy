package codeOrchestra.colt.as.ui.testmode

import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.ui.testmode.TestSettingsForm
import codeOrchestra.util.ProjectHelper

/**
 * @author Dima Kruk
 */
class AsTestSettingsForm extends TestSettingsForm {
    @Service ColtAsController coltController
    private AsProject project

    @Override
    protected void initProject(Project value) {
        super.initProject(value)
        project = value as AsProject
    }

    @Override
    protected void init() {
        super.init()
        List<String> paths = project.projectPaths.getSourcePaths()
        addDirectories(paths)
    }

    @Override
    protected void startTest() {
        super.startTest()
        coltController.startBaseCompilation()
    }

    @Override
    protected void startRecord() {
        super.startRecord()
        coltController.startBaseCompilation()
    }
}
