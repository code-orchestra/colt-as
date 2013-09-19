package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.scene.Parent

/**
 * @author Dima Kruk
 */
class AirLauncherForm extends FormGroup implements IFormValidated {

    RadioButtonInput device

    AirLauncherForm() {
        title = "Launcher"

        device = new RadioButtonInput(title: "Device")

        children.addAll(device)
    }

    @Override
    Parent validated() {
        return null
    }
}
