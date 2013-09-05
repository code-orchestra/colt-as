package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import javafx.beans.property.StringProperty
import javafx.scene.control.PasswordField
import javafx.stage.FileChooser.ExtensionFilter

/**
 * @author Dima Kruk
 */
class AirOption extends LTBForm {
    StringProperty bindProperty

    AirOption(String title, StringProperty bindProperty, AirOptionType optionType, ExtensionFilter extensionFilter = null) {
        this.bindProperty = bindProperty
        this.title = title

        if(optionType == AirOptionType.PASSWORD) {
            PasswordField passwordField = new PasswordField()
            passwordField.layoutY = textField.layoutY
            passwordField.prefHeight = textField.prefHeight
            setLeftAnchor(passwordField, getLeftAnchor(textField))
            setRightAnchor(passwordField, getRightAnchor(textField))

            children.remove(textField)
            textField = passwordField
            children.add(textField)
        }

        this.title().bindBidirectional(bindProperty)

        if (extensionFilter) {
            extensionFilters.add(extensionFilter)
        }

        if (optionType.fileType) {
            type = FormType.BUTTON
            browseType = optionType == AirOptionType.FILE ? BrowseType.FILE : BrowseType.DIRECTORY
        } else {
            type = FormType.TEXT_FIELD
        }
    }

    void unbindProperty() {
        title().unbindBidirectional(bindProperty)
    }
}
