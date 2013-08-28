package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup

/**
 * @author Dima Kruk
 */
class LiveSettingsFormController implements Initializable {

    ToggleGroup methods

    @FXML RTBForm annotated
    @FXML RTBForm all

    @FXML CTBForm paused
    @FXML CTBForm gsLive

    @FXML LTBForm maxLoop

    public LiveSettingsModel model = ModelStorage.instance.project.projectLiveSettings.liveSettingsModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methods = new ToggleGroup()
        methods.toggles.addAll(all.radioButton, annotated.radioButton)

        bindModel()

        if (!model.liveType) {
            model.liveType = LiveMethods.ANNOTATED.preferenceValue
        } else {
            activateMethods(model.liveType)
        }
    }

    void activateMethods(String newVal) {
        LiveMethods liveMethods = LiveMethods.parseValue(newVal)
        methods.toggles[liveMethods.ordinal()].selected = true
    }

    void bindModel() {
        methods.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.liveType = LiveMethods.values()[methods.toggles.indexOf(new_toggle)].preferenceValue
        } as ChangeListener)

        (model.liveType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                activateMethods(newVal as String)
            }
        } as ChangeListener)

        paused.checkBox.selectedProperty().bindBidirectional(model.startSessionPaused())
        gsLive.checkBox.selectedProperty().bindBidirectional(model.makeGSLive())
        maxLoop.textField.textProperty().bindBidirectional(model.maxLoop())
    }
}
