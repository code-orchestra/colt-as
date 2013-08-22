package codeOrchestra.colt.as.ui.propertyTabPane.projectPaths

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.COLTAsProjectPaths
import codeOrchestra.colt.as.ui.components.LTBForm
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        sourcePathsController.model = model.sources()
//
//        libraryPathsController.model = model.libraries()
//
//        assetsPathsController.model = model.assets()

        bindModel()
    }

    void bindModel() {

    }
}
