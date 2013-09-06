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
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonActionInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroupNew
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TargetForm extends FormGroupNew implements IFormValidated {

    AsSettingsForm ownerForm

    private ToggleGroup target
    private RadioButtonInput swf
    private RadioButtonActionInput http
    private RadioButtonActionInput ios
    private RadioButtonActionInput android

    private RunTargetModel model = ModelStorage.instance.project.projectBuildSettings.runTargetModel

    TargetForm() {
        title = "Target"

        swf = new RadioButtonInput(title: "Compiled SWF")
        http = new RadioButtonActionInput(title: "HTTP-shared to local network:", buttonText: "Generate index.html")
        ios = new RadioButtonActionInput(title: "AIR (iOS):", buttonText: "Generate script")
        android = new RadioButtonActionInput(title: "AIR (Android):", buttonText: "Generate script")

        children.addAll(swf, http, ios, android)

        init()
    }

    void init() {
        target = new ToggleGroup()
        target.toggles.addAll(swf.radioButton, http.radioButton, ios.radioButton, android.radioButton)

        http.buttonWidth = 150
        ios.buttonWidth = 150
        android.buttonWidth = 150

        bindModel()

        if (!model.target) {
            model.target = "SWF"
        } else {
            activateTarget(model.target)
        }

        http.action = {
            AsProjectBuildSettings buildSettings = ModelStorage.instance.project.getProjectBuildSettings()
            if (buildSettings.outputFilename) {
                model.httpIndex = IndexHTMLGenerator.generate(ModelStorage.instance.project)
            } else {
                ownerForm.validateForms(this)
            }
        } as EventHandler

        ios.action = {
            if(canShowDialog()) {
                showDialog(new IOSAirFormController(), "Apple iOS: customize launch", model)
            } else {
                ownerForm.validateForms(this)
            }
        } as EventHandler

        android.action = {
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

        http.activateValidation()
        http.text().bindBidirectional(model.httpIndex())

        ios.activateValidation()
        ios.text().bindBidirectional(model.iosScript())

        android.activateValidation()
        android.text().bindBidirectional(model.androidScript())

    }

    @Override
    Parent validated() {
        Target targetType = Target.valueOf(model.target)
        switch (targetType){
            case codeOrchestra.colt.as.run.Target.WEB_ADDRESS:
                return http.validateValue() ? http : null
                break
            case codeOrchestra.colt.as.run.Target.AIR_IOS:
                return ios.validateValue() ? ios : null
                break
            case codeOrchestra.colt.as.run.Target.AIR_ANDROID:
                return android.validateValue() ? android : null
                break
            default:
                return null
        }
    }
}
