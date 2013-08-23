package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import javafx.fxml.FXML
import javafx.fxml.Initializable

/**
 * @author Dima Kruk
 */
class ProductionBuildFormController implements Initializable {

    @FXML CTBForm compression
    @FXML CTBForm optimization

    ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        bindModel()
    }

    void bindModel() {
        compression.checkBox.selectedProperty().bindBidirectional(model.compression())
        optimization.checkBox.selectedProperty().bindBidirectional(model.optimization())
    }
}
