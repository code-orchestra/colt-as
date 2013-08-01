package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import com.aquafx_project.AquaFx
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class BuildSettingsFormController implements Initializable {

    @FXML VBox buildVB

    @FXML TextField mainClassTF
    @FXML TextField fileNameTF
    @FXML TextField outPathTF

    @FXML ChoiceBox playerVersionCB

    @FXML CheckBox rslCB

    @FXML CheckBox localeCB
    @FXML TextField localeTF

    @FXML CheckBox excludeCB

    @FXML CheckBox interruptCB
    @FXML TextField interruptTF

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
//        AquaFx.setGroupBox(buildVB)

        localeTF.disableProperty().bind(localeCB.selectedProperty().not())
        interruptTF.disableProperty().bind(interruptCB.selectedProperty().not())

        bindModel()
    }

    void bindModel() {
        mainClassTF.textProperty().bindBidirectional(model.mainClass())
        fileNameTF.textProperty().bindBidirectional(model.outputFileName())
        outPathTF.textProperty().bindBidirectional(model.outputPath())
        playerVersionCB.valueProperty().bindBidirectional(model.targetPlayerVersion())

        rslCB.selectedProperty().bindBidirectional(model.rsl())

        localeCB.selectedProperty().bindBidirectional(model.nonDefaultLocale())
        localeTF.textProperty().bindBidirectional(model.localeSettings())

        excludeCB.selectedProperty().bindBidirectional(model.excludeDeadCode())

        interruptCB.selectedProperty().bindBidirectional(model.interrupt())
        interruptTF.textProperty().bindBidirectional(model.interruptValue())
    }

    @FXML
    void mainClassBrowseHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.extensionFilters.addAll(new FileChooser.ExtensionFilter("AS", "*.as"), new FileChooser.ExtensionFilter("MXML", "*.mxml"))
        File file = fileChooser.showOpenDialog(mainClassTF.scene.window)
        if (file) {
            mainClassTF.text = file.path
        }
    }

    @FXML
    void outputPathBrowseHandler(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser()
        File file = directoryChooser.showDialog(outPathTF.scene.window)
        if (file) {
            outPathTF.text = file.path
        }
    }
}
