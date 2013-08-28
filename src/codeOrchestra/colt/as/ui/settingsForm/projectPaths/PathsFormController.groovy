package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import javafx.beans.property.ListProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.util.StringConverter

import javafx.collections.ObservableList as FXObservableList

/**
 * @author Dima Kruk
 */
class PathsFormController implements Initializable {
    public static final int FILE = 0
    public static final int DIRECTORY = 1

    @FXML Label titleLabel
    @FXML TextField textField
    @FXML Button add

    public int chooserType
    private String chooserTitle

    public FileChooser fileChooser
    public DirectoryChooser directoryChooser

    ListProperty<String> myPaths

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser()
        directoryChooser = new DirectoryChooser()

        add.onAction = {
            switch (chooserType) {
                case FILE:
                    addFiles()
                    break
                case DIRECTORY:
                    addDirectory()
                    break
            }
        } as EventHandler
    }

    void setModel(ListProperty<String> paths) {
        myPaths = paths
        textField.textProperty().bindBidirectional(paths, new StringConverter<FXObservableList<String>>() {
            @Override
            String toString(FXObservableList<String> t) {
                String result = ""
                t.eachWithIndex { String entry, int i ->
                    result += i > 0 ? "," : ""
                    result += entry
                }
                return result
            }

            @Override
            FXObservableList<String> fromString(String s) {
                FXObservableList<String> list = FXCollections.observableArrayList()
                list.addAll(s.split(",").toList())
                return list
            }
        })
    }

    private void addDirectory() {
        File file = directoryChooser.showDialog(textField.scene.window)
        if (file) {
            checkAndAdd(file.path)
        }
    }

    private void addFiles() {
        List<File> files = fileChooser.showOpenMultipleDialog(textField.scene.window)
        files?.each { File file ->
            checkAndAdd(file.path)
        }
    }

    private boolean checkAndAdd(String path) {
        boolean result = false;
        if (myPaths.get().indexOf(path) == -1) {
            result = myPaths.get().add(path)
        }
        return result;

    }

    public void setTitleText(String value) {
        titleLabel.text = value
    }

    public void setChooserTitle(String chooserTitle) {
        this.chooserTitle = chooserTitle
        switch (chooserType) {
            case FILE:
                fileChooser.title = this.chooserTitle
                break
            case DIRECTORY:
                directoryChooser.title = this.chooserTitle
                break
        }
    }
}
