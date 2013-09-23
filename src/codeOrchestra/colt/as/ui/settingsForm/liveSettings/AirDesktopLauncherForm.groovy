package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.beans.air.AirDesktopLauncherModel
import codeOrchestra.colt.core.ui.components.inputForms.LabeledTitledInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class AirDesktopLauncherForm extends FormGroup {
    private AirDesktopLauncherModel model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectLiveSettings.airDesktopLauncherModel

    AirDesktopLauncherForm() {
        title = "Launcher"

        children.add(new LabeledTitledInput(title: "ADL options:", bindProperty: model.adlOptions()))
    }
}
