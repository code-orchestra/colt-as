package codeOrchestra.colt.as.ui.components

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane

/**
 * @author Dima Kruk
 */
class CBForm extends AnchorPane implements ITypedForm{
    @FXML Label label
    @FXML ChoiceBox choiceBox
    @FXML Label errorLabel

    FormType type

    CBForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cb_form.fxml"))
        fxmlLoader.root = this
        fxmlLoader.controller = this

        try {
            fxmlLoader.load()
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        type = FormType.CHOICE_BOX
    }

    public String getText() {
        return label.textProperty().get();
    }

    public void setText(String value) {
        label.textProperty().set(value);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }
}
