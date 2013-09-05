package codeOrchestra.colt.as.ui.settingsForm

import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.scene.Parent
import javafx.scene.control.TextField

/**
 * @author Dima Kruk
 */
abstract class ValidatedForm extends FormGroup implements IFormValidated {

    protected static Parent validateIsNotEmpty(TextField field) {
        field.styleClass.remove("error-input")
        if (field.text.isEmpty()) {
            field.styleClass.add("error-input")
            return field
        }
        return null
    }

    protected static Parent validateIsFile(TextField field) {
        boolean validate
        field.styleClass.remove("error-input")
        if (!field.disable && !field.text.isEmpty()) {
            File file = new File(field.text)
            validate = file.exists() && file.isFile()
        } else {
            validate = true
        }

        if (!validate) {
            field.styleClass.add("error-input")
            return field
        }

        return null
    }

    protected static Parent validateIsDirectory(TextField field) {
        boolean validate
        field.styleClass.remove("error-input")
        if (!field.disable && !field.text.isEmpty()) {
            File file = new File(field.text)
            validate = file.exists() && file.isDirectory()
        } else {
            validate = true
        }

        if (!validate) {
            field.styleClass.add("error-input")
            return field
        }

        return null
    }
}
