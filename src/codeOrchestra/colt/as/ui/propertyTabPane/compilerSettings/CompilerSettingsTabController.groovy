package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class CompilerSettingsTabController implements Initializable {
    @FXML TextField optionsTF

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        bindModel()
    }

    void bindModel() {
        optionsTF.textProperty().bindBidirectional(model.compilerOptions())
    }

    @FXML
    void excludeClassesHandler(ActionEvent actionEvent) {

    }
}
