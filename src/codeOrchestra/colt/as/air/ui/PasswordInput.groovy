package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.core.ui.components.inputForms.base.TitledInputBase
import codeOrchestra.groovyfx.FXBindable
import javafx.scene.control.Label
import javafx.scene.control.PasswordField

/**
 * @author Dima Kruk
 */
class PasswordInput extends TitledInputBase {
    protected final Label label = new Label()
    PasswordField passwordField = new PasswordField(layoutY: 23, prefHeight: 30)

    @FXBindable String text

    PasswordInput() {
        setLeftAnchor(label, 19)
        setRightAnchor(label, 48)

        label.textProperty().bindBidirectional(title())

        children.add(label)

        setLeftAnchor(passwordField, 10)
        setRightAnchor(passwordField, 86)

        passwordField.textProperty().bindBidirectional(text())

        children.add(passwordField)
    }
}
