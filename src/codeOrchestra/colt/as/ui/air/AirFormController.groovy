package codeOrchestra.colt.as.ui.air

import codeOrchestra.colt.as.model.beans.air.AIRModel
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
abstract class AirFormController implements Initializable{

    @FXML protected GridPane optionsGP
    @FXML protected TextField sdkTF
    @FXML protected Button sdkBtn
    @FXML protected TextField keystoreTF
    @FXML protected Button keystoreBtn
    @FXML protected PasswordField storepassTF

    List<AirOption> optionsList = new ArrayList<AirOption>()

    @FXML protected ListView contentList

    @FXML protected Button generateBtn
    @FXML protected Button cencelBtn

    private Stage dialogStage
    protected AIRModel model

    boolean isGenerated = false

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        generateBtn.onAction = {
            //TODO: generate logic
            //if generated
            isGenerated = true
            close()
        } as EventHandler

        cencelBtn.onAction = {
            close()
        } as EventHandler
    }

    void initViewWithModel(AIRModel model) {
        this.model = model

        initOptions()
    }

    protected abstract void initOptions();

    protected void close() {
        optionsList*.unbindProperty()
        dialogStage.close()
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage
    }
}
