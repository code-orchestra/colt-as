package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.ui.components.log.LogFilter
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.groovyfx.FXBindable
import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.layout.BorderPane
import codeOrchestra.colt.core.tracker.GATracker

/**
 * @author Dima Kruk
 */
class MainAppController implements Initializable {
    @FXML Label projectTitle

    ToggleGroup navigationToggleGroup = new ToggleGroup()
    @FXML ToggleButton runButton
    @FXML ToggleButton pauseButton
    @FXML ToggleButton buildButton
    @FXML ToggleButton settingsButton

    @FXML BorderPane borderPane

    Log log = new Log()
    SettingsForm sForm = new SettingsForm()

    ToggleGroup logFilterToggleGroup = new ToggleGroup()
    @FXML ToggleButton logFilterAll
    @FXML ToggleButton logFilterErrors
    @FXML ToggleButton logFilterWarnings
    @FXML ToggleButton logFilterInfo
    @FXML ToggleButton logFilterLog

    @FXBindable  Boolean liveSessionInProgress = false

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        GATracker tracker = GATracker.instance
        //GATracker.instance.tracker.trackPageViewFromReferrer("asProject.html", "asProject", "codeorchestra.com", "codeorchestra.com", "/index.html")
        tracker.trackPageView("/as/asProject.html", "asProject")

        println([runButton, pauseButton, buildButton, settingsButton])
        navigationToggleGroup.toggles.addAll(runButton, pauseButton, buildButton, settingsButton)
        logFilterToggleGroup.toggles.addAll(logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, logFilterLog)

        log.logWebView.logMessages.addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)
        logFilterToggleGroup.selectedToggleProperty().addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)

        runButton.onAction = {
            tracker.trackEvent("Menu", "Run pressed")
            tracker.trackPageView("/as/asLog.html", "asLog")
            liveSessionInProgress = true
            borderPane.center = log.logWebView
        } as EventHandler

        runButton.tooltip = new Tooltip("Run Livecoding Session")

        pauseButton.onAction = {
            tracker.trackEvent("Menu", "Pause pressed")
            tracker.trackPageView("/as/asLog.html", "asLog")
            if(borderPane.center == log.logWebView){
                liveSessionInProgress = false
            }else{
                borderPane.center = log.logWebView
            }

        } as EventHandler

        pauseButton.tooltip = new Tooltip("Stop Livecoding Session")

        settingsButton.onAction = {
            tracker.trackEvent("Menu", "Settings pressed")
            tracker.trackPageView("/as/asSettings.html", "asSettings")
            borderPane.center = sForm.getPane()
        } as EventHandler

        settingsButton.tooltip = new Tooltip("Livecoding Settings")

        buildButton.onAction = {
            tracker.trackEvent("Menu", "Build pressed")
            tracker.trackPageView("/as/asBuild.html", "asBuild")
        } as EventHandler

        buildButton.tooltip = new Tooltip("Production Build")

        projectTitle.textProperty().bind(codeOrchestra.colt.as.model.ModelStorage.instance.project.name())

        liveSessionInProgress().addListener({ o, Boolean oldValue, Boolean newValue ->
            runButton.visible = !newValue
            runButton.managed = !newValue
            runButton.selected = !newValue
            pauseButton.visible = newValue
            pauseButton.managed = newValue
            pauseButton.selected = newValue
        } as ChangeListener)

        borderPane.center = log.logWebView // todo
        runButton.selected = true // todo
    }

    private void updateLogFilter() {
        if(!logFilterToggleGroup.selectedToggle){
            logFilterAll.selected = true
            return
        }
        int filterIndex = [logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, getLogFilterLog()].indexOf(logFilterToggleGroup.selectedToggle)
        log.logWebView.filter(LogFilter.values()[filterIndex])
        logFilterErrors.text = "Errors (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.ERROR }.size() + ")"
        logFilterWarnings.text = "Warnings (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.WARN }.size() + ")"
        logFilterInfo.text = "Info (" + log.logWebView.logMessages.grep { LogMessage m -> m.level == Level.INFO }.size() + ")"
    }
}
