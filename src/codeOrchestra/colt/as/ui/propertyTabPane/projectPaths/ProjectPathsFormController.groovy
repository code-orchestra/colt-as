package codeOrchestra.colt.as.ui.propertyTabPane.projectPaths

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
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

    AsProjectPaths model = ModelStorage.instance.project.projectPaths
    BuildModel buildModel = ModelStorage.instance.project.projectBuildSettings.buildModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainClass.extensionFilters.addAll(new FileChooser.ExtensionFilter("Class", "*.as", "*.mxml"))

        mainClass.textField.textProperty().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            if (t1) {
                File file = new File(t1)
                if (file.exists() && file.isFile()) {
                    if (buildModel.outputFileName.isEmpty()) {
                        buildModel.outputFileName = file.name.replaceAll(/\.(as|mxml)$/, ".swf")
                    }
                }
            }
        } as ChangeListener)

        bindModel()
    }

    void bindModel() {
        mainClass.textField.textProperty().bindBidirectional(buildModel.mainClass())

        sources.files().bindBidirectional(model.sources())
        libraries.files().bindBidirectional(model.libraries())
        assets.files().bindBidirectional(model.assets())
    }
}
