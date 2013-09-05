package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class SettingsForm extends FormGroup {

    private CTBForm clearLog;
    private CTBForm pingTimeout;

    private SettingsModel model = ModelStorage.instance.project.projectLiveSettings.settingsModel;

    SettingsForm() {
        clearLog = new CTBForm(title: "Clear Messages Log on Session Start")
        pingTimeout = new CTBForm(title: "Disconnect on ping timeout")

        children.addAll(clearLog, pingTimeout)

        init()
    }

    public void init() {
        bindModel()
    }

    void bindModel() {
        clearLog.checkBox.selectedProperty().bindBidirectional(model.clearLog())
        pingTimeout.checkBox.selectedProperty().bindBidirectional(model.disconnectOnTimeout())
    }
}
