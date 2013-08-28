package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ASLiveCodingManager
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.ui.popupmenu.MyContextMenu
import codeOrchestra.colt.as.ui.propertyTabPane.SettingsForm
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.controller.ColtControllerCallback
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.rpc.security.ui.ShortCodeNotification
import codeOrchestra.colt.core.session.LiveCodingSession
import codeOrchestra.colt.core.session.SocketWriter
import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.tracker.GATracker
import codeOrchestra.colt.core.ui.ColtApplication
import codeOrchestra.colt.core.ui.components.ProgressIndicatorController
import codeOrchestra.colt.core.ui.components.log.Log
import codeOrchestra.colt.core.ui.components.log.LogFilter
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.colt.core.ui.components.log.LogWebView
import codeOrchestra.colt.core.ui.components.player.ActionPlayer
import codeOrchestra.colt.core.ui.components.player.ActionPlayerPopup
import codeOrchestra.colt.core.ui.components.sessionIndicator.SessionIndicatorController
import codeOrchestra.colt.core.ui.window.ApplicationGUI
import codeOrchestra.groovyfx.FXBindable
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.binding.StringBinding
import javafx.beans.property.BooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.control.MenuItem
import javafx.scene.control.ToggleButton

/**
 * @author Dima Kruk
 */
class ASApplicationGUI extends ApplicationGUI {

    private ActionPlayerPopup actionPlayerPopup

    @Service ColtAsController coltController
    @Service ASLiveCodingManager liveCodingManager

    @Lazy LogWebView logView = Log.instance.logWebView
    @Lazy SettingsForm settingsForm = new SettingsForm(saveRunAction:{
        runButton.onAction.handle(null)

        ToggleButton playAction = actionPlayerPopup.actionPlayer.play
        playAction.selected = true
        playAction.onAction.handle(null)
    } as EventHandler)

    List<ToggleButton> allFilters

    @FXBindable String applicationState

    ModelStorage model = codeOrchestra.colt.as.model.ModelStorage.instance

    @Override
    void initialize() {
        super.initialize()
        initLog(); initGoogleAnalytics()

        // build ui

        allFilters = [logFilterAll, logFilterErrors, logFilterWarnings, logFilterInfo, logFilterLog]

        navigationToggleGroup.toggles.addAll(runButton, buildButton, settingsButton)
        logFilterToggleGroup.toggles.addAll(allFilters)

        intiActionPlayerPopup()

        runButton.onAction = {
            if (!runButton.selected) {
                actionPlayerPopup.isShowing() ? actionPlayerPopup.hide() : actionPlayerPopup.show(runButton)
            }

            root.center = logView
            runButton.selected = true
        } as EventHandler

        settingsButton.onAction = {
            root.center = settingsForm
            settingsButton.selected = true
        } as EventHandler

        root.top = ShortCodeNotification.initNotification(root.top)

        buildButton.onAction = {
            coltController.startProductionCompilation()//todo: handle errors?
            buildButton.selected = true
        } as EventHandler

        MyContextMenu contextMenu = new MyContextMenu()
        contextMenu.setStyle("-fx-background-color: transparent;");
        ArrayList<MenuItem> items = ColtApplication.get().menuBar.popupMenuItems
        contextMenu.items.addAll(items)

        popupMenuButton.onAction = {
            Point2D point = popupMenuButton.parent.localToScreen(popupMenuButton.layoutX, popupMenuButton.layoutY)
            contextMenu.show(popupMenuButton, point.x + 5, point.y - 15 - items.size() * 25)
        } as EventHandler

        // progress monitor

        ProgressIndicatorController.instance.progressIndicator = progressIndicator

        sessionIndicator.visibleProperty().bind(progressIndicator.visibleProperty().not())
        SessionIndicatorController.instance.indicator = sessionIndicator

        // data binding

        navigationToggleGroup.selectedToggleProperty().addListener({ v, o, newValue ->
            int index = navigationToggleGroup.toggles.indexOf(navigationToggleGroup.selectedToggle)
            applicationState = ["Log", "Production Build", "Project Settings"][index]
        } as ChangeListener)

        logView.logMessages.addListener({ ListChangeListener.Change<? extends LogMessage> c ->
            updateLogFilter()
        } as ListChangeListener)

        logFilterToggleGroup.selectedToggleProperty().addListener({ o ->
            updateLogFilter()
        } as InvalidationListener)

        projectTitle.textProperty().bind(new StringBinding() {
            {
                super.bind(model.project.name(), applicationState())
            }
            @Override
            protected String computeValue() {
                model?.project?.name?.capitalize() + " / " + getApplicationState()
            }
        })

        root.centerProperty().addListener({ o, old, javafx.scene.Node newValue ->
            allFilters.each {it.visible = root.center == logView }
            updateLogFilter()
        } as ChangeListener)

        logFiltersContainer.widthProperty().addListener({ o, old, Number newValue ->
            updateLogFilter()
        } as ChangeListener)

        // start

        (model.project.newProject() as BooleanProperty).addListener({ ObservableValue<? extends Boolean> observableValue, Boolean t, Boolean t1 ->
            if (t1) {
                settingsButton.onAction.handle(null)
            } else {
                runButton.selected = true
                root.center = logView
            }
        } as ChangeListener)

        runButton.selected = true
        root.center = logView
    }

    private intiActionPlayerPopup() {
        actionPlayerPopup = new ActionPlayerPopup()

        ActionPlayer playerControls = actionPlayerPopup.actionPlayer
        playerControls.stylesheets.addAll(stylesheets)

        playerControls.play.onAction = {
            playerControls.disable = true
            coltController.startBaseCompilation(new ColtControllerCallback<CompilationResult, CompilationResult>() {
                @Override
                void onComplete(CompilationResult successResult) {
                    Platform.runLater({
                        playerControls.showAdd(true)
                        playerControls.disable = false
                    })
                }

                @Override
                void onError(Throwable t, CompilationResult errorResult) {
                    Platform.runLater({
                        playerControls.stop.selected = true
                        playerControls.disable = false
                    })
                }
            }, true, true)
        } as EventHandler

        playerControls.stop.onAction = {
            List<LiveCodingSession<SocketWriter>> connections = liveCodingManager.currentConnections
            connections.each {
                liveCodingManager.stopSession(it)
            }
        } as EventHandler

        playerControls.add.onAction = {
            coltController.launch()
        } as EventHandler
    }

    private static void initLog() {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(Log.instance);
        }
    }

    private void updateLogFilter() {
        if (!logFilterToggleGroup.selectedToggle) {
            logFilterAll.selected = true
            return
        }

        int filterIndex = allFilters.indexOf(logFilterToggleGroup.selectedToggle)
        logView.filter(LogFilter.values()[filterIndex])
        logFilterErrors.text = "Errors" + logFilterPrefix(Level.ERROR)
        logFilterWarnings.text = "Warnings" + logFilterPrefix(Level.WARN)
        logFilterInfo.text = "Info" + logFilterPrefix(Level.INFO)
        logFilterLog.text = "Log" + logFilterPrefix(Level.COMPILATION, Level.LIVE)
    }

    private String logFilterPrefix(Level... levels) {
        if(logView.logMessages.empty || logFiltersContainer.width < 300) return  ""
        " (" + logView.logMessages.grep { LogMessage m -> m.level in levels }.size() + ")"
    }

    void initGoogleAnalytics() {
        GATracker.instance.trackPageView("/as/asProject.html", "asProject")
        GAController.instance.pageContainer = root.centerProperty()
        GAController.instance.registerEvent(runButton, "ActionMenu", "Run pressed")
        GAController.instance.registerEvent(settingsButton, "ActionMenu", "Settings pressed")
    }

}
