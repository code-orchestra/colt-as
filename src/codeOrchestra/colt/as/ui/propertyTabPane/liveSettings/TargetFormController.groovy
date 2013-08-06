package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.ui.air.android.AndroidAirFormController
import codeOrchestra.colt.as.ui.air.ios.IOSAirFormCntroller
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import codeOrchestra.colt.as.ui.air.AirFormController
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
public class TargetFormController implements Initializable {

    @FXML GridPane targetPane

    @FXML ToggleGroup target
    @FXML RadioButton swfRBtn
    @FXML RadioButton httpRBtn
    @FXML RadioButton iosRBtn
    @FXML RadioButton androidRBtn

    @FXML TextField httpTF
    @FXML TextField iosTF
    @FXML TextField androidTF

    @FXML Button httpGBtn
    @FXML Button iosGBtn
    @FXML Button androidGBtn

    private List<Control> controls
    private RunTargetModel model = ModelStorage.instance.project.projectBuildSettings.runTargetModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        AquaFx.setGroupBox(targetPane)

        controls = [httpTF, iosTF, androidTF, httpGBtn, iosGBtn, androidGBtn]

        controls*.disable = true

        httpRBtn.setUserData([httpTF, httpGBtn])
        iosRBtn.setUserData([iosTF, iosGBtn])
        androidRBtn.setUserData([androidTF, androidGBtn])

        target.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.target = Target.values()[target.toggles.indexOf(new_toggle)]
            ((List<Control>) new_toggle?.userData)*.disable = false
        } as ChangeListener)

        (model.target() as StringProperty).addListener({ prop, oldVal, newVal ->
            if (newVal) {
                controls*.disable = true
                Target targetType = Target.valueOf("" + newVal)
                Toggle selected = target.toggles[targetType.ordinal()]
                selected.selected = true
            }
        } as ChangeListener)

        if (!model.target) {
            model.target = "SWF"
        }

        iosGBtn.onAction = {
            FXMLLoader loader = new FXMLLoader(AirFormController.class.getResource("air_form.fxml"))
            loader.setController(new IOSAirFormCntroller())
            VBox page = loader.load()
            Stage dialogStage = new Stage()
            dialogStage.title = "Apple iOS: customize launch"
            dialogStage.initModality(Modality.WINDOW_MODAL)
            dialogStage.initOwner(androidGBtn.scene.window)
            dialogStage.scene = new Scene(page)

            AirFormController controller = loader.controller
            controller.setDialogStage(dialogStage)
            controller.initViewWithModel(model.iosAirModel)

            dialogStage.showAndWait()

            println "controller.isGenerated = $controller.isGenerated"
        } as EventHandler

        androidGBtn.onAction = {
            FXMLLoader loader = new FXMLLoader(AirFormController.class.getResource("air_form.fxml"))
            loader.setController(new AndroidAirFormController())
            VBox page = loader.load()
            Stage dialogStage = new Stage()
            dialogStage.title = "Android: customize launch"
            dialogStage.initModality(Modality.WINDOW_MODAL)
            dialogStage.initOwner(androidGBtn.scene.window)
            dialogStage.scene = new Scene(page)

            AirFormController controller = loader.controller
            controller.setDialogStage(dialogStage)
            controller.initViewWithModel(model.iosAirModel)

            dialogStage.showAndWait()

            println "controller.isGenerated = $controller.isGenerated"
        } as EventHandler

        bindModel()
    }

    void bindModel() {
        httpTF.textProperty().bindBidirectional(model.httpIndex())
        iosTF.textProperty().bindBidirectional(model.iosScript())
        androidTF.textProperty().bindBidirectional(model.androidScript())
    }
}
