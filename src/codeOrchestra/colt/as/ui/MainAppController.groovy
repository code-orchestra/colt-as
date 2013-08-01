package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ToggleButton
import javafx.scene.Node as FXNode
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane

/**
 * @author Dima Kruk
 */
class MainAppController implements Initializable {
    ToggleGroup toggleGroup
    @FXML ToggleButton runBnt
    @FXML ToggleButton buildBtn
    @FXML ToggleButton settingsBtn

    @FXML BorderPane borderPane

    Log log = new Log()
    SettingsForm sForm = new SettingsForm()

    MainAppController() {
        toggleGroup = new ToggleGroup()
    }

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup.toggles.addAll(runBnt, buildBtn, settingsBtn)
        runBnt.onAction = {
            borderPane.center = log.getPane()

        } as EventHandler

        settingsBtn.onAction = {
            borderPane.center = sForm.getPane()
        } as EventHandler

        buildBtn.onAction = {

        } as EventHandler
    }
}
