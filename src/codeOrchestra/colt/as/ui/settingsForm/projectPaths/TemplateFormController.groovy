package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import javafx.fxml.FXML
import javafx.fxml.Initializable

/**
 * @author Dima Kruk
 */
class TemplateFormController implements Initializable {
    @FXML LTBForm template

    AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        template.textField.textProperty().bindBidirectional(model.htmlTemplatePath())
    }
}
