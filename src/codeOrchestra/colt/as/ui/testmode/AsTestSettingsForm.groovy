package codeOrchestra.colt.as.ui.testmode

import codeOrchestra.colt.as.model.AsProject
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
    protected void init() {
        super.init()
        project = ProjectHelper.currentProject
        List<String> paths = project.projectPaths.getSourcePaths()
        addDirectories(paths)
    }
}
