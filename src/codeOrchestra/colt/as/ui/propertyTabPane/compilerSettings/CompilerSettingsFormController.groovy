package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.ui.components.LTBForm
import javafx.fxml.FXML
import javafx.fxml.Initializable

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class CompilerSettingsFormController implements Initializable {
    @FXML LTBForm options

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        bindModel()
    }

    void bindModel() {
        options.textField.textProperty().bindBidirectional(model.compilerOptions())
    }
}
