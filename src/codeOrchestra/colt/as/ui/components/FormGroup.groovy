package codeOrchestra.colt.as.ui.components

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/**
 * @author Dima Kruk
 */
class FormGroup extends VBox {
    private static final Insets TITLED = new Insets(26, 0, 23, 0)
    private static final Insets NOT_TITLED = new Insets(3, 0, 23, 0)

    @FXML private Label label

    String title

    FormGroup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("form_group.fxml"))
        fxmlLoader.root = this
        fxmlLoader.controller = this

        try {
            fxmlLoader.load()
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        makeTitled(false)

        setSpacing(23)
    }

    private void makeTitled(boolean b) {
        label.visible = label.managed = b
        setPadding(b ? TITLED : NOT_TITLED)
    }


    void setTitle(String value) {
        title = value
        if (title) {
            label.text = title
            makeTitled(true)
        } else {
            makeTitled(false)
        }
    }
}
