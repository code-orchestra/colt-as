package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import javafx.fxml.FXML
import javafx.fxml.Initializable

/**
 * @author Dima Kruk
 */
class SettingsFormController implements Initializable {

    @FXML CTBForm clearLog;
    @FXML CTBForm pingTimeout;

    SettingsModel model = ModelStorage.instance.project.projectLiveSettings.settingsModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindModel()
    }

    void bindModel() {
        clearLog.checkBox.selectedProperty().bindBidirectional(model.clearLog())
        pingTimeout.checkBox.selectedProperty().bindBidirectional(model.disconnectOnTimeout())
    }
}
