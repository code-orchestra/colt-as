package codeOrchestra.colt.as.ui.propertyTabPane.liveSettings

import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import com.aquafx_project.AquaFx
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.GridPane

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class LiveSettingsFormController implements Initializable {

    @FXML GridPane settingsGP;

    @FXML ToggleGroup methods;
    @FXML RadioButton annotatedRBtn;
    @FXML RadioButton allRBtn;

    @FXML CheckBox pausedCB;
    @FXML CheckBox gsLiveCB;

    @FXML TextField maxLoopTF;

    public LiveSettingsModel model = ModelStorage.instance.project.projectLiveSettings.liveSettingsModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        AquaFx.setGroupBox(settingsGP)

        pausedCB.setDisable(true)

        methods.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.liveType = LiveMethods.values()[methods.toggles.indexOf(new_toggle)].preferenceValue
        } as ChangeListener)

        (model.liveType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                LiveMethods liveType = LiveMethods.parseValue("" +newVal)
                Toggle selected = methods.toggles[liveType.ordinal()]
                selected.selected = true
            }
        } as ChangeListener)

        if (!model.liveType) {
            model.liveType = LiveMethods.ANNOTATED.preferenceValue
        }

        bindModel()
    }

    void bindModel() {
        pausedCB.selectedProperty().bindBidirectional(model.startSessionPaused())
        gsLiveCB.selectedProperty().bindBidirectional(model.makeGSLive())
        maxLoopTF.textProperty().bindBidirectional(model.maxLoop())
    }
}
