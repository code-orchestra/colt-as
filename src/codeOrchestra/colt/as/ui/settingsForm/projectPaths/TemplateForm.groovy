package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.as.ui.settingsForm.ValidatedForm
import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.scene.control.TextField

/**
 * @author Dima Kruk
 */
class TemplateForm extends ValidatedForm {
    private LTBForm template

    private AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    TemplateForm() {
        template = new LTBForm(title: "HTML Template:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)

        children.add(template)

        init()
    }

    void init() {
        template.text().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            validated()
        } as ChangeListener)
        template.text().bindBidirectional(model.htmlTemplatePath())
    }

    @Override
    Parent validated() {
        return validateIsDirectory(template.textField)
    }
}
