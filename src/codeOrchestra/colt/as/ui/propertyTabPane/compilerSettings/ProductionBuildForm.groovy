package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import com.aquafx_project.AquaFx
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.layout.VBox

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class ProductionBuildForm implements Initializable {

    @FXML VBox buildVB

    @FXML CheckBox compressionCB
    @FXML CheckBox optimizationCB

    ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        AquaFx.setGroupBox(buildVB)

        bindModel()
    }

    void bindModel() {
        compressionCB.selectedProperty().bindBidirectional(model.compression())
        optimizationCB.selectedProperty().bindBidirectional(model.optimization())
    }
}
