package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LauncherModel
import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.RadioButtonActionInput
import codeOrchestra.colt.core.ui.components.inputFormsNew.RadioButtonInput
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.Parent
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class LauncherForm extends FormGroup implements IFormValidated {

    private ToggleGroup launcher

    private RadioButtonInput defaultPlayer
    private RadioButtonActionInput player

    private LauncherModel model = ModelStorage.instance.project.projectLiveSettings.launcherModel

    LauncherForm() {
        title = "Launcher"

        defaultPlayer = new RadioButtonInput(title: "System default application")
        player = new RadioButtonActionInput(title: "Flash Player")

        children.addAll(defaultPlayer, player)

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

        player.activateValidation()
        player.text().bindBidirectional(model.flashPlayerPath())
    }

    @Override
    Parent validated() {
        return player.validateValue() ? player : null
    }
}
