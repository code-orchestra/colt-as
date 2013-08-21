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
import codeOrchestra.colt.core.rpc.security.ui.ShortCodeNotification
import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.tracker.GATracker
import codeOrchestra.colt.core.ui.COLTApplication
import codeOrchestra.colt.core.ui.components.COLTProgressIndicatorController
import codeOrchestra.colt.core.ui.components.log.LogFilter
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.colt.core.ui.components.sessionIndicator.SessionIndicatorController
import javafx.beans.InvalidationListener
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane

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
    SettingsForm sForm = new SettingsForm()

    ToggleGroup logFilterToggleGroup = new ToggleGroup()
    @FXML ToggleButton logFilterAll
    @FXML ToggleButton logFilterErrors
    @FXML ToggleButton logFilterWarnings
    @FXML ToggleButton logFilterInfo
    @FXML ToggleButton logFilterLog

    @FXML ProgressIndicator progressIndicator
    @FXML ImageView sessionIndicator

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        initGA()

        navigationToggleGroup.toggles.addAll(runButton, buildButton, settingsButton)
        logFilterToggleGroup.toggles.addAll(logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, logFilterLog)

        log.logWebView.logMessages.addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)
        logFilterToggleGroup.selectedToggleProperty().addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)

        runButton.onAction = {

            COLTAsController coltController = (COLTAsController) ServiceProvider.get(COLTController.class)
            coltController.startBaseCompilation(new COLTControllerCallbackEx<CompilationResult>() {
                @Override
                void onComplete(CompilationResult successResult) {
                }

                @Override
                void onError(Throwable t, CompilationResult errorResult) {
                }
            }, true, true)

            borderPane.center = log.logWebView
        } as EventHandler

        settingsButton.onAction = {
            borderPane.center = sForm.getPane()
        } as EventHandler

        borderPane.top = ShortCodeNotification.initNotification(borderPane.top)

        buildButton.onAction = {
            COLTAsController coltController = (COLTAsController) ServiceProvider.get(COLTController.class)
            coltController.startProductionCompilation(new COLTControllerCallbackEx<CompilationResult>() {
                @Override
                void onComplete(CompilationResult successResult) {
                }

                @Override
                void onError(Throwable t, CompilationResult errorResult) {
                }
            }, true, true)
        } as EventHandler

        MyContextMenu contextMenu = new MyContextMenu()
        contextMenu.setStyle("-fx-background-color: transparent;");
        ArrayList<MenuItem> items = COLTApplication.get().menuItems
        contextMenu.items.addAll(items)

        menuBtn.onAction = {
            Point2D point = menuBtn.parent.localToScreen(menuBtn.layoutX, menuBtn.layoutY)
            contextMenu.show(menuBtn, point.x + 5, point.y - 15 - items.size() * 25)
        } as EventHandler

        projectTitle.textProperty().bind(codeOrchestra.colt.as.model.ModelStorage.instance.project.name())

        borderPane.center = log.logWebView // todo
        runButton.selected = true // todo

        COLTProgressIndicatorController.instance.progressIndicator = progressIndicator

        SessionIndicatorController.instance.indicator = sessionIndicator
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

    void initGA() {
        GATracker tracker = GATracker.instance
        tracker.trackPageView("/as/asProject.html", "asProject")
        GAController gaController = GAController.instance
        gaController.pageContainer = borderPane.centerProperty()

        gaController.registerPage(log.logWebView, "/as/asLog.html", "asLog")
        gaController.registerPage(sForm.getPane(), "/as/asSettings.html", "asSettings")

        gaController.registerEvent(runButton, "ActionMenu", "Run pressed")
        gaController.registerEvent(pauseButton, "ActionMenu", "Pause pressed")
        gaController.registerEvent(settingsButton, "ActionMenu", "Settings pressed")
    }
}
