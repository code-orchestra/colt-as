package codeOrchestra.colt.as.ui.components

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.RadioButton

/**
 * @author Dima Kruk
 */
class RTBForm extends InputForm {

    @FXML RadioButton radioButton

    RTBForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rtb_form.fxml"))
        initLoader(fxmlLoader)

        button.disableProperty().bind(radioButton.selectedProperty().not())
        textField.disableProperty().bind(radioButton.selectedProperty().not())

        type = FormType.SIMPLE
    }

    public String getText() {
        return radioButton.textProperty().get();
    }

    public void setText(String value) {
        radioButton.textProperty().set(value);
    }

    public StringProperty textProperty() {
        return radioButton.textProperty();
    }
}
