package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import com.dmurph.tracking.JGoogleAnalyticsTracker
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import codeOrchestra.colt.core.tracker.GATracker

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
        //GATracker.instance.tracker.trackPageViewFromReferrer("asProject.html", "asProject", "codeorchestra.com", "codeorchestra.com", "/index.html")
        GATracker.instance.tracker.trackPageView("/as/asProject.html", "asProject", "codeorchestra.com")

        toggleGroup.toggles.addAll(runBnt, buildBtn, settingsBtn)
        runBnt.onAction = {
            GATracker.instance.tracker.trackEvent("Menu", "Run pressed")
            GATracker.instance.tracker.trackPageView("/as/asLog.html", "asLog", "codeorchestra.com")
            borderPane.center = log.getPane()

        } as EventHandler

        settingsBtn.onAction = {
            GATracker.instance.tracker.trackEvent("Menu", "Settings pressed")
            GATracker.instance.tracker.trackPageView("/as/asSettings.html", "asSettings", "codeorchestra.com")
            borderPane.center = sForm.getPane()
        } as EventHandler

        buildBtn.onAction = {
            GATracker.instance.tracker.trackEvent("Menu", "Build pressed")
            GATracker.instance.tracker.trackPageView("/as/asBuild.html", "asBuild", "codeorchestra.com")
        } as EventHandler
    }
}
