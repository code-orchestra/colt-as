package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated

import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroupNew
import javafx.scene.Parent

/**
 * @author Dima Kruk
 */
class TemplateForm extends FormGroupNew implements IFormValidated {
    private LabeledActionInput template

    private AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    TemplateForm() {
        template = new LabeledActionInput(title: "HTML Template:", browseType: BrowseType.DIRECTORY)
        template.canBeEmpty = true

        children.add(template)

        init()
    }

    void init() {
        template.text().bindBidirectional(model.htmlTemplatePath())
    }

    @Override
    Parent validated() {
        return template.validateValue() ? template : null
    }
}
