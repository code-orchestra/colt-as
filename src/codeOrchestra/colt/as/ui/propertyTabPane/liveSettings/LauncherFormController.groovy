package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LauncherModel
import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class LauncherFormController implements Initializable {

    ToggleGroup launcher

    @FXML RTBForm defaultPlayer
    @FXML RTBForm player

    LauncherModel model = ModelStorage.instance.project.projectLiveSettings.launcherModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        launcher = new ToggleGroup()
        launcher.toggles.addAll(defaultPlayer.radioButton, player.radioButton)

        player.extensionFilters.add(new FileChooser.ExtensionFilter("APP", "*.app"))

        bindModel()

        if (!model.launcherType) {
            model.launcherType = "DEFAULT"
        } else {
            activateLauncher(model.launcherType)
        }
    }

    void activateLauncher(String newVal) {
        LauncherType launcherType = LauncherType.valueOf("" + newVal)
        launcher.toggles[launcherType.ordinal()].selected = true
    }

    void bindModel() {
        launcher.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.launcherType = LauncherType.values()[launcher.toggles.indexOf(new_toggle)]
        } as ChangeListener)

        (model.launcherType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                activateLauncher("" + newVal)
            }
        } as ChangeListener)

        player.textField.textProperty().bindBidirectional(model.flashPlayerPath())
    }
}
