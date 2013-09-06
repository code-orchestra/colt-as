package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.core.ui.components.inputForms.LabeledTitledInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroupNew

/**
 * @author Dima Kruk
 */
class CompilerSettingsForm extends FormGroupNew{
    private LabeledTitledInput options

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    CompilerSettingsForm() {
        options = new LabeledTitledInput(title: "Additional compiler options:")

        children.add(options)

        init()
    }

    void init() {
        bindModel()
    }

    void bindModel() {
        options.text().bindBidirectional(model.compilerOptions())
    }
}
