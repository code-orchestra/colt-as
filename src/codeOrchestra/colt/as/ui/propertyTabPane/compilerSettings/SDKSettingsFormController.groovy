package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class SDKSettingsFormController implements Initializable {

    @FXML TextField sdkPathTF

    @FXML CheckBox defConfCB

    @FXML CheckBox customConfCB
    @FXML TextField customConfTF
    @FXML Button customConfBtn

    public SDKModel model = ModelStorage.instance.project.sdkModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customConfTF.disableProperty().bind(customConfCB.selectedProperty().not())
        customConfBtn.disableProperty().bind(customConfCB.selectedProperty().not())

        bindModel()
    }

    void bindModel() {
        sdkPathTF.textProperty().bindBidirectional(model.flexSDKPath())
        defConfCB.selectedProperty().bindBidirectional(model.useFlexConfig())
        customConfCB.selectedProperty().bindBidirectional(model.useCustomConfig())
        customConfTF.textProperty().bindBidirectional(model.customConfigPath())
    }

    @FXML
    void sdkBrowseHandler(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser()
        File file = directoryChooser.showDialog(sdkPathTF.scene.window)
        if (file) {
            sdkPathTF.text = file.path
        }
    }

    @FXML
    void customConfBrowseHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.extensionFilters.add(new FileChooser.ExtensionFilter("XML", "*.xml"))
        File file = fileChooser.showOpenDialog(customConfTF.scene.window)
        if (file) {
            customConfTF.text = file.path
        }
    }
}
