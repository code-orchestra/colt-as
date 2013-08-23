package codeOrchestra.colt.as.ui.propertyTabPane.projectPaths

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.COLTAsProjectPaths
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class ProjectPathsFormController implements Initializable {
    @FXML FilesetInput sources
    @FXML FilesetInput libraries
    @FXML FilesetInput assets

    @FXML LTBForm mainClass

    COLTAsProjectPaths model = ModelStorage.instance.project.projectPaths
    BuildModel buildModel = ModelStorage.instance.project.projectBuildSettings.buildModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainClass.extensionFilters.addAll(new FileChooser.ExtensionFilter("AS", "*.as"), new FileChooser.ExtensionFilter("MXML", "*.mxml"))

        bindModel()
        sources.files = "src/"
        libraries.files = "lib/"
        assets.files = "assets/"
    }

    void bindModel() {
        mainClass.textField.textProperty().bindBidirectional(buildModel.mainClass())

        sources.files().bindBidirectional(model.sources())
        libraries.files().bindBidirectional(model.libraries())
        assets.files().bindBidirectional(model.assets())
    }
}
