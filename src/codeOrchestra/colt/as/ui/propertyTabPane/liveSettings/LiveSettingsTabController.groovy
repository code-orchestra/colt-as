package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SettingsModel
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class LiveSettingsTabController implements Initializable {

    @FXML CheckBox clearLogCB;
    @FXML CheckBox pingTimeoutCB;

    SettingsModel model = ModelStorage.getInstance().project.settingsModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindModel()
    }

    void bindModel() {
        clearLogCB.selectedProperty().bindBidirectional(model.clearLog())
        pingTimeoutCB.selectedProperty().bindBidirectional(model.disconnectOnTimeout())
    }
}
