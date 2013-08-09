package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import com.dmurph.tracking.JGoogleAnalyticsTracker
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import codeOrchestra.colt.core.tracker.GATracker

/**
 * @author Dima Kruk
 */
class MainAppController implements Initializable {
    @FXML Label projectTitle

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
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        GATracker tracker = GATracker.instance
        //GATracker.instance.tracker.trackPageViewFromReferrer("asProject.html", "asProject", "codeorchestra.com", "codeorchestra.com", "/index.html")
        tracker.trackPageView("/as/asProject.html", "asProject")

        toggleGroup.toggles.addAll(runBnt, buildBtn, settingsBtn)
        runBnt.onAction = {
            tracker.trackEvent("Menu", "Run pressed")
            tracker.trackPageView("/as/asLog.html", "asLog")
            borderPane.center = log.getPane()

        } as EventHandler

        settingsBtn.onAction = {
            tracker.trackEvent("Menu", "Settings pressed")
            tracker.trackPageView("/as/asSettings.html", "asSettings")
            borderPane.center = sForm.getPane()
        } as EventHandler

        buildBtn.onAction = {
            tracker.trackEvent("Menu", "Build pressed")
            tracker.trackPageView("/as/asBuild.html", "asBuild")
        } as EventHandler

        projectTitle.textProperty().bind(codeOrchestra.colt.as.model.ModelStorage.instance.project.name())
    }
}
