package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LauncherModel
import com.aquafx_project.AquaFx
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class LauncherFormController implements Initializable {

    @FXML VBox launcherVB

    @FXML ToggleGroup launcher
    @FXML RadioButton defaultRBtn
    @FXML RadioButton playerRBtn

    @FXML TextField playerTF
    @FXML Button browsBtn

    private List<Control> controls

    LauncherModel model = ModelStorage.instance.project.projectLiveSettings.launcherModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        AquaFx.setGroupBox(launcherVB)

        controls = [playerTF, browsBtn]

        controls*.disable = true

        playerRBtn.setUserData(controls)

        launcher.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.launcherType = LauncherType.values()[launcher.toggles.indexOf(new_toggle)]
            ((List<Control>) new_toggle?.userData)*.disable = false
        } as ChangeListener)

        (model.launcherType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                controls*.disable = true
                LauncherType launcherType = LauncherType.valueOf("" + newVal)
                Toggle selected = launcher.toggles[launcherType.ordinal()]
                selected.selected = true
            }
        } as ChangeListener)

        if (!model.launcherType) {
            model.launcherType = "DEFAULT"
        }

        bindModel()
    }

    void bindModel() {
        playerTF.textProperty().bindBidirectional(model.flashPlayerPath())
    }

    @FXML
    void browseHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.extensionFilters.add(new FileChooser.ExtensionFilter("APP", "*.app"))
        File file = fileChooser.showOpenDialog(playerTF.scene.window)
        if (file) {
            playerTF.text = file.path
        }
    }
}
