package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.colt.core.errorhandling.ErrorHandler
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import groovy.io.FileType
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Callback
import codeOrchestra.colt.as.model.AsProject


/**
 * @author Dima Kruk
 */
abstract class AirFormController extends VBox{

    protected FormGroup options

    protected ListView<FileCellBean> contentList

    protected Button generateBtn
    protected Button cancelBtn

    private Stage dialogStage
    protected AIRModel model
    protected RunTargetModel runTargetModel

    boolean isGenerated = false

    AirFormController() {
        options = new FormGroup(title: "Options:")

        FormGroup formGroup = new FormGroup(title: "Package Contents:")
        contentList = new ListView<>()
        contentList.prefHeight = 200
        setVgrow(contentList, Priority.ALWAYS)
        formGroup.children.add(contentList)

        HBox hBox = new HBox(alignment:Pos.CENTER, spacing: 10)
        generateBtn = new Button("Generate")
        cancelBtn = new Button("Cancel")
        hBox.children.addAll(cancelBtn, generateBtn)

        children.addAll(options, formGroup, hBox)

        setAlignment(Pos.TOP_CENTER)
        setPrefWidth(400)

        initialize()
    }

    void initialize() {
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

        AsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project
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

    protected abstract AirBuildScriptGenerator createBuildScriptGenerator(AsProject project)

    protected abstract void updateScriptPathValue(String scriptPath)

    protected boolean runGeneration() {
        // 1 - save
        //TODO: save model
        AsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project

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
        options.children.each {
            if(it instanceof AirOption) {
                (it as AirOption)?.unbindProperty()}
            }
        dialogStage.close()
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage
    }
}
