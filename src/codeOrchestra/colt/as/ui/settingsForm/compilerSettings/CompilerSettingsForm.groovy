package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class CompilerSettingsForm extends FormGroup{
    private LTBForm options

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    CompilerSettingsForm() {
        options = new LTBForm(text: "Additional compiler options:", type: FormType.TEXT_FIELD)

        children.add(options)

        init()
    }

    void init() {
        bindModel()
    }

    void bindModel() {
        options.textField.textProperty().bindBidirectional(model.compilerOptions())
    }
}
