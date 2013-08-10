package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.ui.components.log.LogFilter
import codeOrchestra.colt.core.ui.components.log.LogMessage
import javafx.beans.InvalidationListener
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

    ToggleGroup navigationToggleGroup = new ToggleGroup()
    @FXML ToggleButton runBnt
    @FXML ToggleButton buildBtn
    @FXML ToggleButton settingsBtn

    @FXML BorderPane borderPane

    Log log = new Log()
    SettingsForm sForm = new SettingsForm()

    ToggleGroup logFilterToggleGroup = new ToggleGroup()
    @FXML ToggleButton logFilterAll
    @FXML ToggleButton logFilterErrors
    @FXML ToggleButton logFilterWarnings
    @FXML ToggleButton logFilterInfo
    @FXML ToggleButton logFilterLog

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        GATracker tracker = GATracker.instance
        //GATracker.instance.tracker.trackPageViewFromReferrer("asProject.html", "asProject", "codeorchestra.com", "codeorchestra.com", "/index.html")
        tracker.trackPageView("/as/asProject.html", "asProject")

        navigationToggleGroup.toggles.addAll(runBnt, buildBtn, settingsBtn)
        logFilterToggleGroup.toggles.addAll(logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, logFilterLog)

        log.logWebView.logMessages.addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)
        logFilterToggleGroup.selectedToggleProperty().addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)

        runBnt.onAction = {
            tracker.trackEvent("Menu", "Run pressed")
            tracker.trackPageView("/as/asLog.html", "asLog")
            borderPane.center = log.logWebView

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


        borderPane.center = log.logWebView // todo
        runBnt.selected = true // todo
    }

    private void updateLogFilter() {
        int filterIndex = [logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, getLogFilterLog()].indexOf(logFilterToggleGroup.selectedToggle)
        log.logWebView.filter(LogFilter.values()[filterIndex])
        logFilterErrors.text = "Errors (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.ERROR }.size() + ")"
        logFilterWarnings.text = "Warnings (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.WARN }.size() + ")"
        logFilterInfo.text = "Info (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.INFO }.size() + ")"
    }
}
