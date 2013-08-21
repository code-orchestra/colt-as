package codeOrchestra.colt.as.ui.components

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.CheckBox

/**
 * @author Dima Kruk
 */
class CTBForm extends InputForm {

    @FXML CheckBox checkBox

    CTBForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ctb_form.fxml"))
        initLoader(fxmlLoader)

        button.disableProperty().bind(checkBox.selectedProperty().not())
        textField.disableProperty().bind(checkBox.selectedProperty().not())

        type = FormType.SIMPLE
    }

    public String getText() {
        return checkBox.textProperty().get();
    }

    public void setText(String value) {
        checkBox.textProperty().set(value);
    }

    public StringProperty textProperty() {
        return checkBox.textProperty();
    }
}
