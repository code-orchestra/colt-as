package codeOrchestra.colt.as.ui.air

import javafx.beans.property.StringProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter

/**
 * @author Dima Kruk
 */
class AirOption {
    AirOptionType optionType
    StringProperty bindProperty

    Label titleLabel
    TextField textField
    Button browseBtn

    ExtensionFilter eFilter

    AirOption(String title, StringProperty bindProperty, AirOptionType optionType, GridPane pane, int rowIndex) {
        this.optionType = optionType
        this.bindProperty = bindProperty
        titleLabel = new Label(title)
        pane.add(titleLabel, 0, rowIndex)

        textField = optionType == AirOptionType.PASSWORD ? new PasswordField() : new TextField()
        textField.textProperty().bindBidirectional(bindProperty)

        if (optionType.fileType) {
            pane.add(textField, 1, rowIndex)

            browseBtn = new Button("Browse...")
            browseBtn.maxWidth = Double.MAX_VALUE
            pane.add(browseBtn, 2, rowIndex)
            browseBtn.onAction = {
                optionType == AirOptionType.FILE ? openFile() : openDir()
            } as EventHandler
        } else {
            pane.add(textField, 1, rowIndex, 2, 1)
        }
    }

    void openFile() {
        FileChooser fileChooser = new FileChooser()
        if (eFilter) {
            fileChooser.extensionFilters.add(eFilter)
        }
        File file = fileChooser.showOpenDialog(textField.scene.window)
        if (file) {
            textField.text = file.path
        }
    }

    void openDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser()
        File file = directoryChooser.showDialog(textField.scene.window)
        if (file) {
            textField.text = file.path
        }
    }

    void unbindProperty() {
        textField.textProperty().unbindBidirectional(bindProperty)
    }
}
