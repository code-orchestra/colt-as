package codeOrchestra.colt.as.ui.components

import javafx.beans.property.StringProperty
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
abstract class InputForm extends AnchorPane implements ITypedForm{
    @FXML protected TextField textField
    @FXML protected Button button

    FormType type
    //hak for fxml
    String formType

    enum BrowseType{
        FILE,
        DIRECTORY
    }

    BrowseType browseType = BrowseType.FILE

    ArrayList<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>()

    protected void initLoader(FXMLLoader loader) {
        loader.root = this
        loader.controller = this

        try {
            loader.load()
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        button.onAction = {
            switch (browseType) {
                case BrowseType.FILE:
                    FileChooser fileChooser = new FileChooser()
                    fileChooser.extensionFilters.addAll(extensionFilters)
                    File file = fileChooser.showOpenDialog(button.scene.window)
                    if(file) {
                        textField.text = file.path
                    }
                    break
                case BrowseType.DIRECTORY:
                    DirectoryChooser directoryChooser = new DirectoryChooser()
                    File file = directoryChooser.showDialog(button.scene.window)
                    if(file) {
                        textField.text = file.path
                    }
                    break
            }
        } as EventHandler
    }

    void setFormType(String type) {
        setType(FormType.valueOf(type))
    }

    String getButtonText() {
        return button.textProperty().get();
    }

    void setButtonText(String value) {
        button.textProperty().set(value);
    }

    void changeButtonWidth(double value) {
        setRightAnchor(textField, 86 + value - 67)
        button.prefWidth = value
    }

    StringProperty buttonTextProperty() {
        return button.textProperty();
    }

    Button getButton() {
        return button
    }

    TextField getTextField() {
        return textField
    }

    @Override
    void setType(FormType type) {
        this.type = type
        switch (type) {
            case FormType.SIMPLE:
                children.removeAll(textField, button)
                break
            case FormType.TEXT_FIELD:
                children.remove(button)
                if(!children.contains(textField)) {
                    children.add(textField)
                }
                break
            case FormType.BUTTON:
                if(!children.contains(textField)) {
                    children.add(textField)
                }
                if (!children.contains(button)) {
                    children.add(button)
                }
                break
        }
    }
}
