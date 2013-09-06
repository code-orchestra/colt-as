package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.CheckBoxForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.group.FormGroupNew

/**
 * @author Dima Kruk
 */
class SettingsForm extends FormGroupNew {

    private CheckBoxForm clearLog;
    private CheckBoxForm pingTimeout;

    private SettingsModel model = ModelStorage.instance.project.projectLiveSettings.settingsModel;

    SettingsForm() {
        clearLog = new CheckBoxForm(title: "Clear Messages Log on Session Start")
        pingTimeout = new CheckBoxForm(title: "Disconnect on ping timeout")

        children.addAll(clearLog, pingTimeout)

        init()
    }

    public void init() {
        bindModel()
    }

    void bindModel() {
        clearLog.selected().bindBidirectional(model.clearLog())
        pingTimeout.selected().bindBidirectional(model.disconnectOnTimeout())
    }
}
