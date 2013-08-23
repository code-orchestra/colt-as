package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.android.AndroidAirFormController
import codeOrchestra.colt.as.air.ui.ios.IOSAirFormController
import codeOrchestra.colt.as.model.COLTAsProjectBuildSettings
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TargetFormController implements Initializable {

    ToggleGroup target
    @FXML RTBForm swf
    @FXML RTBForm http
    @FXML RTBForm ios
    @FXML RTBForm android

    private RunTargetModel model = ModelStorage.instance.project.projectBuildSettings.runTargetModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
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
            //todo: implement
        } as EventHandler

        ios.button.onAction = {
            if(canShowDialog()) {
                showDialog(new IOSAirFormController(), "Apple iOS: customize launch", model)
            } else {
                //TODO: show message
            }
        } as EventHandler

        android.button.onAction = {
            if(canShowDialog()) {
                showDialog(new AndroidAirFormController(), "Android: customize launch", model)
            } else {
                //TODO: show message
            }
        } as EventHandler
    }

    void activateTarget(String newVal) {
        Target targetType = Target.valueOf("" + newVal)
        target.toggles[targetType.ordinal()].selected = true
    }

    static boolean canShowDialog() {
        COLTAsProjectBuildSettings buildSettings = ModelStorage.instance.project.getProjectBuildSettings()
        return buildSettings.outputPath && buildSettings.outputFilename
    }

    void showDialog(AirFormController controller, String title, RunTargetModel model) {
        FXMLLoader loader = new FXMLLoader(AirFormController.class.getResource("air_form.fxml"))
        loader.setController(controller)
        VBox page = loader.load() as VBox

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

        http.textField.textProperty().bindBidirectional(model.httpIndex())
        ios.textField.textProperty().bindBidirectional(model.iosScript())
        android.textField.textProperty().bindBidirectional(model.androidScript())
    }
}
