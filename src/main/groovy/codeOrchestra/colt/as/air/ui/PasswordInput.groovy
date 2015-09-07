package codeOrchestra.colt.as.air.ui
import codeOrchestra.colt.core.ui.components.inputForms.base.InputWithErrorBase
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
/**
 * @author Dima Kruk
 */
class PasswordInput extends InputWithErrorBase {
    protected final Label label = new Label()

    PasswordInput() {
        setLeftAnchor(label, 19)
        setRightAnchor(label, 48)

        label.textProperty().bindBidirectional(title())

        children.add(label)

        children.remove(textField)
        textField.textProperty().unbindBidirectional(text())

        textField = new PasswordField(layoutY: 23, prefHeight: 30)

        setLeftAnchor(textField, 10)
        setRightAnchor(textField, 86)

        textField.textProperty().bindBidirectional(text())

        children.add(textField)

    }
}
