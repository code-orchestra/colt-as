package codeOrchestra.colt.as.ui.air

import codeOrchestra.colt.as.model.beans.air.AIRModel
import groovy.io.FileType
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.util.Callback
import codeOrchestra.colt.as.model.COLTAsProject


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

    @FXML protected ListView<FileCellBean> contentList

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
            println(getCheckedFiles())
            //close()
        } as EventHandler

        cencelBtn.onAction = {
            close()
        } as EventHandler

        contentList.cellFactory = { ListView<FileCellBean> p ->
            return new FileCell()
        } as Callback

        COLTAsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project
        File dir = project.outputDir
        String outName = project.getProjectBuildSettings().outputFilename
        dir.eachFileRecurse (FileType.FILES) { file ->
            contentList.items.add(new FileCellBean(file, file.name.endsWith(outName)))
        }
    }

    List<File> getCheckedFiles() {
        contentList.items.findAll{it.checked}.collect{it.file}
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
