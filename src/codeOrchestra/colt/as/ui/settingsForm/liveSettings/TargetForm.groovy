package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.android.AndroidAirFormController
import codeOrchestra.colt.as.air.ui.ios.IOSAirFormController
import codeOrchestra.colt.as.model.AsProjectBuildSettings
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.as.run.indexhtml.IndexHTMLGenerator
import codeOrchestra.colt.as.ui.settingsForm.AsSettingsForm
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.as.ui.settingsForm.ValidatedForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.InvalidationListener
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TargetForm extends ValidatedForm {

    AsSettingsForm ownerForm

    private ToggleGroup target
    private RTBForm swf
    private RTBForm http
    private RTBForm ios
    private RTBForm android

    private RunTargetModel model = ModelStorage.instance.project.projectBuildSettings.runTargetModel

    TargetForm() {
        title = "Target"

        swf = new RTBForm(text: "Compiled SWF")
        http = new RTBForm(text: "HTTP-shared to local network:", type: FormType.BUTTON, buttonText: "Generate index.html")
        ios = new RTBForm(text: "AIR (iOS):", type: FormType.BUTTON, buttonText: "Generate script")
        android = new RTBForm(text: "AIR (Android):", type: FormType.BUTTON, buttonText: "Generate script")

        children.addAll(swf, http, ios, android)

        init()
    }

    void init() {
        target = new ToggleGroup()
        target.toggles.addAll(swf.radioButton, http.radioButton, ios.radioButton, android.radioButton)

        http.changeButtonWidth(150)
        ios.changeButtonWidth(150)
        android.changeButtonWidth(150)

        bindModel()

        if (!model.target) {
            model.target = "SWF"
        } else {
            activateTarget(model.target)
        }

        http.button.onAction = {
            AsProjectBuildSettings buildSettings = ModelStorage.instance.project.getProjectBuildSettings()
            if (buildSettings.outputFilename) {
                model.httpIndex = IndexHTMLGenerator.generate(ModelStorage.instance.project)
            } else {
                ownerForm.validateForms(this)
            }
        } as EventHandler

        ios.button.onAction = {
            if(canShowDialog()) {
                showDialog(new IOSAirFormController(), "Apple iOS: customize launch", model)
            } else {
                ownerForm.validateForms(this)
            }
        } as EventHandler

        android.button.onAction = {
            if(canShowDialog()) {
                showDialog(new AndroidAirFormController(), "Android: customize launch", model)
            } else {
                ownerForm.validateForms(this)
            }
        } as EventHandler
    }

    void activateTarget(String newVal) {
        Target targetType = Target.valueOf("" + newVal)
        target.toggles[targetType.ordinal()].selected = true
        validated()
    }

    static boolean canShowDialog() {
        AsProjectBuildSettings buildSettings = ModelStorage.instance.project.getProjectBuildSettings()
        return buildSettings.outputPath && buildSettings.outputFilename
    }

    void showDialog(AirFormController controller, String title, RunTargetModel model) {
        VBox page = controller

        Stage dialogStage = new Stage()
        dialogStage.title = title
        dialogStage.initModality(Modality.WINDOW_MODAL)
        dialogStage.initOwner(swf.scene.window)
        dialogStage.scene = new Scene(page)

        controller.setDialogStage(dialogStage)
        controller.initViewWithModel(model)

        dialogStage.showAndWait()

        println "controller.isGenerated = $controller.isGenerated"
    }

    void bindModel() {
        target.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.target = Target.values()[target.toggles.indexOf(new_toggle)]
        } as ChangeListener)

        (model.target() as StringProperty).addListener({ prop, oldVal, newVal ->
            if (newVal) {
                activateTarget(newVal as String)
            }
        } as ChangeListener)

        http.textField.textProperty().addListener({ javafx.beans.Observable observable ->
            validated()
        } as InvalidationListener)
        http.textField.textProperty().bindBidirectional(model.httpIndex())

        ios.textField.textProperty().addListener({ javafx.beans.Observable observable ->
            validated()
        } as InvalidationListener)
        ios.textField.textProperty().bindBidirectional(model.iosScript())

        android.textField.textProperty().addListener({ javafx.beans.Observable observable ->
            validated()
        } as InvalidationListener)
        android.textField.textProperty().bindBidirectional(model.androidScript())

    }

    @Override
    Parent validated() {
        http.textField.styleClass.remove("error-input")
        ios.textField.styleClass.remove("error-input")
        android.textField.styleClass.remove("error-input")

        Target targetType = Target.valueOf(model.target)
        switch (targetType){
            case codeOrchestra.colt.as.run.Target.WEB_ADDRESS:
                return validateIsFile(http.textField)
                break
            case codeOrchestra.colt.as.run.Target.AIR_IOS:
                return validateIsFile(ios.textField)
                break
            case codeOrchestra.colt.as.run.Target.AIR_ANDROID:
                return validateIsFile(android.textField)
                break
            default:
                return null
        }
    }
}
