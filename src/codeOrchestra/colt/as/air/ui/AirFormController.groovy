package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.colt.core.errorhandling.ErrorHandler
import groovy.io.FileType
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.util.Callback
import codeOrchestra.colt.as.model.COLTAsProject


/**
 * @author Dima Kruk
 */
abstract class AirFormController implements Initializable{

    @FXML protected GridPane optionsGP

    List<AirOption> optionsList = new ArrayList<AirOption>()

    @FXML protected ListView<FileCellBean> contentList

    @FXML protected Button generateBtn
    @FXML protected Button cancelBtn

    private Stage dialogStage
    protected AIRModel model
    protected RunTargetModel runTargetModel

    boolean isGenerated = false

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        generateBtn.onAction = {
            isGenerated = runGeneration()
            close()
        } as EventHandler

        cancelBtn.onAction = {
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

    void initViewWithModel(RunTargetModel model) {
        runTargetModel = model

        initOptions()
    }

    protected abstract void initOptions();

    protected abstract AirBuildScriptGenerator createBuildScriptGenerator(COLTAsProject project)

    protected abstract void updateScriptPathValue(String scriptPath)

    private boolean runGeneration() {
        // 1 - save
        //TODO: save model
        COLTAsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project

        // 2 - generate
        String scriptPath = null
        try {
            scriptPath = createBuildScriptGenerator(project).generate(model, getCheckedFiles());
        } catch (IOException e) {
            ErrorHandler.handle(e, "Error while generating AIR build script");
            return false
        }

        // 3 - update address
        updateScriptPathValue(scriptPath)

        return true
    }

    protected void close() {
        optionsList*.unbindProperty()
        dialogStage.close()
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage
    }
}
