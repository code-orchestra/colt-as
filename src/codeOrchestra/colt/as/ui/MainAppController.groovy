package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.COLTAsController
import codeOrchestra.colt.as.ui.log.Log
import codeOrchestra.colt.as.ui.popupmenu.MyContextMenu
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import codeOrchestra.colt.core.ServiceProvider
import codeOrchestra.colt.core.controller.COLTController
import codeOrchestra.colt.core.controller.COLTControllerCallbackEx
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
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Point2D
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
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

    @FXML Button menuBtn

    Log log = new Log()
    Parent sForm


    ToggleGroup logFilterToggleGroup = new ToggleGroup()
    @FXML ToggleButton logFilterAll
    @FXML ToggleButton logFilterErrors
    @FXML ToggleButton logFilterWarnings
    @FXML ToggleButton logFilterInfo
    @FXML ToggleButton logFilterLog

    @FXBindable  Boolean liveSessionInProgress = false

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {

        sForm = FXMLLoader.load(getClass().getResource("_tmp-form2.fxml"))

        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        GATracker tracker = GATracker.instance
        //GATracker.instance.tracker.trackPageViewFromReferrer("asProject.html", "asProject", "codeorchestra.com", "codeorchestra.com", "/index.html")
        tracker.trackPageView("/as/asProject.html", "asProject")

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

            COLTAsController coltController = (COLTAsController) ServiceProvider.get(COLTController.class)
            coltController?.startBaseCompilation(new COLTControllerCallbackEx<CompilationResult>() {
                @Override
                void onComplete(CompilationResult successResult) {
                    liveSessionInProgress = false;
                }

                @Override
                void onError(Throwable t, CompilationResult errorResult) {
                    liveSessionInProgress = false;
                }
            }, true, true)

            borderPane.center = log.logWebView
            liveSessionInProgress = true
        } as EventHandler

        pauseButton.onAction = {
            tracker.trackEvent("Menu", "Pause pressed")
            liveSessionInProgress = false

        } as EventHandler

        settingsButton.onAction = {
            tracker.trackEvent("Menu", "Settings pressed")
            tracker.trackPageView("/as/asSettings.html", "asSettings")
            borderPane.center = sForm
        } as EventHandler

        buildButton.onAction = {
            tracker.trackEvent("Menu", "Build pressed")
            tracker.trackPageView("/as/asBuild.html", "asBuild")
        } as EventHandler

        MyContextMenu contextMenu = new MyContextMenu()
        contextMenu.setStyle("-fx-background-color: transparent;");
        MenuItem menuItem1 = new MenuItem("Save")
        menuItem1.accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN)
        menuItem1.onAction = {
            println("Save")
        } as EventHandler
        MenuItem menuItem2 = new MenuItem("Open")
        contextMenu.items.addAll(menuItem1, menuItem2)

        menuBtn.onAction = {
            Point2D point = menuBtn.parent.localToScreen(menuBtn.layoutX, menuBtn.layoutY)
            contextMenu.show(menuBtn, point.x + 5, point.y - 75)
        } as EventHandler

        projectTitle.textProperty().bind(codeOrchestra.colt.as.model.ModelStorage.instance.project.name())

        liveSessionInProgress().addListener({ o, Boolean oldValue, Boolean newValue ->
            runButton.visible = runButton.managed = runButton.selected = !newValue
            pauseButton.visible = pauseButton.managed = pauseButton.selected = newValue
        } as ChangeListener)

        borderPane.center = sForm // todo
        settingsButton.selected = true // todo
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
