package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.colt.core.errorhandling.ErrorHandler
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroupNew
import groovy.io.FileType
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Callback
import codeOrchestra.colt.as.model.AsProject
import org.controlsfx.control.ButtonBar


/**
 * @author Dima Kruk
 */
abstract class AirFormController extends VBox{

    protected FormGroupNew options

    protected ListView<FileCellBean> contentList

    protected Button generateBtn
    protected Button cancelBtn

    private Stage dialogStage
    protected AIRModel model
    protected RunTargetModel runTargetModel

    protected ButtonBar buttonBar

    boolean isGenerated = false

    AirFormController() {
        options = new FormGroupNew(title: "Options:")
        options.styleClass.remove("fieldset")

        FormGroupNew packageContents = new FormGroupNew(title: "Package Contents:")
        contentList = new ListView<>()
//        contentList.style = "-fx-background-insets: 0"
        contentList.prefHeight = 200
        contentList.minHeight = 50
        contentList.minWidth = 100
        setVgrow(contentList, Priority.ALWAYS)
        packageContents.children.add(contentList)

        buttonBar = new ButtonBar()
        generateBtn = new Button("Generate")
        generateBtn.defaultButton = true
        ButtonBar.setType(generateBtn, ButtonBar.ButtonType.OK_DONE)
        cancelBtn = new Button("Cancel")
        ButtonBar.setType(cancelBtn, ButtonBar.ButtonType.CANCEL_CLOSE)
        buttonBar.buttons.addAll(generateBtn, cancelBtn)

        AnchorPane anchorPane = new AnchorPane()
        AnchorPane.setLeftAnchor(buttonBar, 10)
        AnchorPane.setRightAnchor(buttonBar, 10)
        anchorPane.children.add(buttonBar)

        FormGroupNew actions = new FormGroupNew()
        actions.first = true
        actions.children.add(anchorPane)

        children.addAll(options, packageContents, actions)

        setAlignment(Pos.TOP_CENTER)
        setPrefWidth(460)
        setFillWidth(true)

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

    abstract void unbindProperty()

    protected void close() {
        unbindProperty()
        dialogStage.close()
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage
    }
}
