package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroupNew

/**
 * @author Dima Kruk
 */
class SettingsForm extends FormGroupNew {

    private CheckBoxInput clearLog;
    private CheckBoxInput pingTimeout;

    private SettingsModel model = ModelStorage.instance.project.projectLiveSettings.settingsModel;

    SettingsForm() {
        clearLog = new CheckBoxInput(title: "Clear Messages Log on Session Start")
        pingTimeout = new CheckBoxInput(title: "Disconnect on ping timeout")

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
