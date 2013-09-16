package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.colt.core.errorhandling.ErrorHandler
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
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
abstract class AirForm extends VBox{

    protected FormGroup options

    protected ListView<FileCellBean> contentList

    protected Button generateBtn
    protected Button cancelBtn

    private Stage dialogStage
    protected AIRModel model
    protected RunTargetModel runTargetModel

    protected ButtonBar buttonBar

    boolean isGenerated = false

    AirForm() {
        options = new FormGroup(title: "Options:")
        options.styleClass.remove("fieldset")

        FormGroup packageContents = new FormGroup(title: "Package Contents:")
        contentList = new ListView<>()
        contentList.styleClass.add("list-view-simplified")
        contentList.prefHeight = 200
        contentList.minHeight = 50
        contentList.minWidth = 100
        setVgrow(contentList, Priority.ALWAYS)
        contentList.cellFactory = { ListView<FileCellBean> p ->
            return new FileCell()
        } as Callback
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

        FormGroup actions = new FormGroup()
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
            if (validate()) {
                isGenerated = runGeneration()
                close()
            }
        } as EventHandler

        cancelBtn.onAction = {
            close()
        } as EventHandler

        AsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project
        File dir = project.getOutputDir()
        String outName = project.getProjectBuildSettings().outputFilename
        File projectFile = new File(dir, outName)
        if (!projectFile.exists()) {
            contentList.items.add(new FileCellBean(projectFile, true))
        }
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

    abstract boolean validate()

    protected void close() {
        unbindProperty()
        dialogStage.close()
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage
    }
}
