package codeOrchestra.colt.as.ui.testmode

import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.ui.testmode.TestSettingsForm
import codeOrchestra.util.ProjectHelper

/**
 * @author Dima Kruk
 */
class AsTestSettingsForm extends TestSettingsForm {
    private AsProject project

    AsTestSettingsForm() {

    }

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
}
