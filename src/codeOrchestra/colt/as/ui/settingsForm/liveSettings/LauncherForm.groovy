package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LauncherModel
import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class LauncherForm extends FormGroup {

    private ToggleGroup launcher

    private RTBForm defaultPlayer
    private RTBForm player

    private LauncherModel model = ModelStorage.instance.project.projectLiveSettings.launcherModel

    LauncherForm() {
        title = "Launcher"

        defaultPlayer = new RTBForm(text: "System default application", type: FormType.SIMPLE)
        player = new RTBForm(text: "Flash Player", type: FormType.BUTTON)

        init()
    }

    void init() {
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
