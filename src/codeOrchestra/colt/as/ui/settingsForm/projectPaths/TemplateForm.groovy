package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class TemplateForm extends FormGroup {
    private LTBForm template

    private AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    TemplateForm() {
        template = new LTBForm(text: "HTML Template:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)

        children.add(template)

        init()
    }

    void init() {
        template.textField.textProperty().bindBidirectional(model.htmlTemplatePath())
    }
}
