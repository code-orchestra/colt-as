package codeOrchestra.colt.as.ui.propertyTabPane.projectPaths

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.COLTAsProjectPaths
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class ProjectPathsFormController implements Initializable {
    @FXML TextField templateTF

    //initialise from fxml by ids (Nested Controllers)
    @FXML PathsFormController sourcePathsController
    @FXML PathsFormController libraryPathsController
    @FXML PathsFormController assetsPathsController

    COLTAsProjectPaths model = ModelStorage.instance.project.projectPaths

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sourcePathsController.titleText = "Source Paths:"
        sourcePathsController.chooserType = PathsFormController.DIRECTORY
        sourcePathsController.chooserTitle = "Select Source directory"
        sourcePathsController.model = model.sources

        libraryPathsController.titleText = "Library Paths:"
        libraryPathsController.chooserType = PathsFormController.FILE
        libraryPathsController.fileChooser.extensionFilters.add(new FileChooser.ExtensionFilter("SWC", "*.swc"))
        libraryPathsController.chooserTitle = "Select Library files"
        libraryPathsController.model = model.libraries

        assetsPathsController.titleText = "Assets Paths:"
        assetsPathsController.chooserType = PathsFormController.DIRECTORY
        assetsPathsController.chooserTitle = "Select Assets directory"
        assetsPathsController.model = model.assets

        bindModel()
    }

    void bindModel() {
        templateTF.textProperty().bindBidirectional(model.htmlTemplatePath())
    }

    public void browseHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"))
        File file = fileChooser.showOpenDialog(templateTF.scene.window)
        if (file) {
            templateTF.text = file.path
        }
    }
}
