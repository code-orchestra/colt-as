package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.run.AirLauncherType
import codeOrchestra.colt.as.model.beans.air.AirLauncherModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.as.ui.settingsForm.liveSettings.emulatorInput.EmulatorInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.Parent
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup

/**
 * @author Dima Kruk
 */
class AirLauncherForm extends FormGroup implements IFormValidated {
    ToggleGroup launcher
    RadioButtonInput device
    EmulatorInput emulator

    private AirLauncherModel model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectLiveSettings.airLauncherModel

    AirLauncherForm() {
        title = "Launcher"
        launcher = new ToggleGroup()
        device = new RadioButtonInput(title: "Device", toggleGroup: launcher)
        emulator = new EmulatorInput(title: "Emulator", toggleGroup: launcher)

        children.addAll(device, emulator)

        bindModel()

        if (!model.launcherType) {
            model.launcherType = AirLauncherType.DEVICE.name()
        }
        activateLauncher(model.launcherType)
    }

    void bindModel() {
        launcher.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.launcherType = codeOrchestra.colt.as.run.AirLauncherType.values()[launcher.toggles.indexOf(new_toggle)]
        } as ChangeListener)

        (model.launcherType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                activateLauncher("" + newVal)
            }
        } as ChangeListener)

        emulator.value().bindBidirectional(model.emulatorValue())
    }

    void activateLauncher(String newVal) {
        println "newVal = $newVal"
        AirLauncherType launcherType = AirLauncherType.valueOf("" + newVal)
        launcher.toggles[launcherType.ordinal()].selected = true
    }

    @Override
    Parent validated() {
        return null
    }
}
