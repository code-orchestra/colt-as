package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
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
class TemplateForm extends FormGroup implements IFormValidated {
    private LTBForm template

    private AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    TemplateForm() {
        template = new LTBForm(text: "HTML Template:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)

        children.add(template)

        init()
    }

    void init() {
        template.textField.textProperty().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            validated()
        } as ChangeListener)
        template.textField.textProperty().bindBidirectional(model.htmlTemplatePath())
    }

    @Override
    Parent validated() {
        boolean validate

        TextField field = template.textField
        if (!field.text.isEmpty()) {
            File file = new File(field.text)
            validate = file.exists() && file.isDirectory()
        } else {
            validate = true
        }
        if (validate) {
            field.styleClass.remove("error-input")
            return null
        } else {
            if (!field.styleClass.contains("error-input")) {
                field.styleClass.add("error-input")
            }
            return field
        }
    }
}
