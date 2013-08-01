package codeOrchestra.colt.as.ui.propertyTabPane.projectPaths

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList as FXObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class PathsFormController implements Initializable {

    public static final int FILE = 0
    public static final int DIRECTORY = 1

    @FXML Label titleLabel
    @FXML ListView<String> list
    @FXML Button add
    @FXML Button remove
    @FXML Button up
    @FXML Button down

    public int chooserType
    private String chooserTitle

    public FileChooser fileChooser
    public DirectoryChooser directoryChooser

    private StringProperty curSelectionProperty = new SimpleStringProperty()

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser()
        directoryChooser = new DirectoryChooser()

        remove.disable = true
        up.disable = true
        down.disable = true

        curSelectionProperty.bind(list.selectionModel.selectedItemProperty())
        curSelectionProperty.addListener({ prop, oldVal, newVal ->
            if (newVal) {
                remove.disable = false
                FXObservableList<String> items = list.items
                down.disable = !(items.size() > 1 && items.indexOf(newVal) < items.size() - 1)
                up.disable = !(items.size() > 1 && items.indexOf(newVal) > 0)
            } else {
                remove.disable = true
                up.disable = true
                down.disable = true
                list.selectionModel.clearSelection()
            }
        } as ChangeListener)
    }

    void setModel(FXObservableList<String> paths) {
        list.setItems(paths)
    }

    private void addDirectory() {
        File file = directoryChooser.showDialog(list.scene.window)
        if (file) {
            checkAndAdd(file.path)
        }
    }

    private void addFiles() {
        List<File> files = fileChooser.showOpenMultipleDialog(list.scene.window)
        files?.each { File file ->
            checkAndAdd(file.path)
        }
    }

    private boolean checkAndAdd(String path) {
        boolean result = false;
        if (list.items.indexOf(path) == -1) {
            result = list.items.add(path)
        }
        return result;

    }

    public void setTitleText(String value) {
        titleLabel.text = value
    }

    @FXML
    void addHandler(ActionEvent actionEvent) {
        switch (chooserType) {
            case FILE:
                addFiles()
                break
            case DIRECTORY:
                addDirectory()
                break
        }
    }

    @FXML
    void removeHandler(ActionEvent actionEvent) {
        list.items.remove(curSelectionProperty.value)
    }

    @FXML
    void upHandler(ActionEvent actionEvent) {
        String curSelection = curSelectionProperty.value
        list.selectionModel.clearSelection()
        int index = list.items.indexOf(curSelection) - 1
        list.items.remove(curSelection)
        list.items.add(index, curSelection)
        updateSelection(index)
    }

    @FXML
    void downHandler(ActionEvent actionEvent) {
        String curSelection = curSelectionProperty.value
        list.selectionModel.clearSelection()
        int index = list.items.indexOf(curSelection) + 1
        list.items.remove(curSelection)
        list.items.add(index, curSelection)
        updateSelection(index)
    }

    private void updateSelection(int index) {
        list.scrollTo(index)
        list.selectionModel.select(index)
        list.focusModel.focus(index)
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
