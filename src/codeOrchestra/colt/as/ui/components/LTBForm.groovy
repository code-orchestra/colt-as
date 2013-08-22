package codeOrchestra.colt.as.ui.components

import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label

/**
 * @author Dima Kruk
 */
class LTBForm extends InputForm{

    @FXML Label label

    LTBForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ltb_form.fxml"))
        initLoader(fxmlLoader)

        type = FormType.TEXT_FIELD
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
