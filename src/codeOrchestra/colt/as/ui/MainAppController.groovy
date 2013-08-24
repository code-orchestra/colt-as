package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.COLTAsController
import codeOrchestra.colt.as.model.ModelStorage
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
import codeOrchestra.groovyfx.FXBindable
import javafx.beans.InvalidationListener
import javafx.beans.binding.StringBinding
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox

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

    @FXML BorderPane root

    @FXML Button popupMenuButton

    Log log = new Log()
    SettingsForm sForm = new SettingsForm()

    @FXML HBox logFiltersContainer

    ToggleGroup logFilterToggleGroup = new ToggleGroup()

    @FXML ToggleButton logFilterAll
    @FXML ToggleButton logFilterErrors
    @FXML ToggleButton logFilterWarnings
    @FXML ToggleButton logFilterInfo
    @FXML ToggleButton logFilterLog

    List<ToggleButton> allFilters

    @FXML ProgressIndicator progressIndicator
    @FXML ImageView sessionIndicator

    @FXBindable String applicationState

    ModelStorage model = codeOrchestra.colt.as.model.ModelStorage.instance

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(log);
        }

        initGA()

        // build ui

        allFilters = [logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, logFilterLog]

        navigationToggleGroup.toggles.addAll(runButton, buildButton, settingsButton)
        logFilterToggleGroup.toggles.addAll(allFilters)

        runButton.onAction = {
            COLTAsController coltController = (COLTAsController) ServiceProvider.get(COLTController.class)
            coltController.startBaseCompilation()
            root.center = log.logWebView
        } as EventHandler

        settingsButton.onAction = {
            root.center = sForm.getPane()
        } as EventHandler

        root.top = ShortCodeNotification.initNotification(root.top)

        buildButton.onAction = {
            COLTAsController coltController = (COLTAsController) ServiceProvider.get(COLTController.class)
            coltController.startProductionCompilation()
        } as EventHandler

        MyContextMenu contextMenu = new MyContextMenu()
        contextMenu.setStyle("-fx-background-color: transparent;");
        ArrayList<MenuItem> items = COLTApplication.get().menuBar.popupMenuItems
        contextMenu.items.addAll(items)

        popupMenuButton.onAction = {
            Point2D point = popupMenuButton.parent.localToScreen(popupMenuButton.layoutX, popupMenuButton.layoutY)
            contextMenu.show(popupMenuButton, point.x + 5, point.y - 15 - items.size() * 25)
        } as EventHandler

        // progress monitor

        COLTProgressIndicatorController.instance.progressIndicator = progressIndicator

        sessionIndicator.visibleProperty().bind(progressIndicator.visibleProperty().not())
        SessionIndicatorController.instance.indicator = sessionIndicator

        // data binding

        navigationToggleGroup.selectedToggleProperty().addListener({ v, o, newValue ->
            int index = navigationToggleGroup.toggles.indexOf(navigationToggleGroup.selectedToggle)
            applicationState = ["Log", "Production Build", "Project Settings"][index]
            println "applicationState = $applicationState"
        } as ChangeListener)

        log.logWebView.logMessages.addListener({ v, o, newValue ->
            updateLogFilter()
        } as InvalidationListener)

        logFilterToggleGroup.selectedToggleProperty().addListener({ javafx.beans.Observable observable ->
            updateLogFilter()
        } as InvalidationListener)

        projectTitle.textProperty().bind(new StringBinding() {
            {
                super.bind(model.project.name(), applicationState())
            }
            @Override
            protected String computeValue() {
                model.project.name + " / " + getApplicationState()
            }
        })

        root.centerProperty().addListener({ o, old, javafx.scene.Node newValue ->
            allFilters.each {it.visible = root.center == log.logWebView }
            updateLogFilter()
        } as ChangeListener)

        logFiltersContainer.widthProperty().addListener({ o, old, Number newValue ->
            updateLogFilter()
        } as ChangeListener)

        // start

        root.center = log.logWebView // todo
        runButton.selected = true // todo
    }

    private void updateLogFilter() {
        if (!logFilterToggleGroup.selectedToggle) {
            logFilterAll.selected = true
            return
        }

        int filterIndex = allFilters.indexOf(logFilterToggleGroup.selectedToggle)
        log.logWebView.filter(LogFilter.values()[filterIndex])
        logFilterErrors.text = "Errors" + logFilterPrefix(Level.ERROR)
        logFilterWarnings.text = "Warnings" + logFilterPrefix(Level.WARN)
        logFilterInfo.text = "Info" + logFilterPrefix(Level.INFO)
        logFilterLog.text = "Log" + logFilterPrefix(Level.COMPILATION, Level.LIVE)
    }

    private String logFilterPrefix(Level... levels) {
        if(log.logWebView.logMessages.empty || logFiltersContainer.width < 300) return  ""
        " (" + log.logWebView.logMessages.grep { LogMessage m -> m.level in levels }.size() + ")"
    }

    void initGA() {
        GATracker.instance.trackPageView("/as/asProject.html", "asProject")
        GAController.instance.pageContainer = root.centerProperty()
        GAController.instance.registerEvent(runButton, "ActionMenu", "Run pressed")
        GAController.instance.registerEvent(pauseButton, "ActionMenu", "Pause pressed")
        GAController.instance.registerEvent(settingsButton, "ActionMenu", "Settings pressed")
    }
}
