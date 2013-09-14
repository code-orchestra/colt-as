package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ASLiveCodingManager
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.ui.productionBuildForm.AsProductionBuildForm
import codeOrchestra.colt.as.ui.settingsForm.AsSettingsForm
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.controller.ColtControllerCallback
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.session.LiveCodingSession
import codeOrchestra.colt.core.session.SocketWriter
import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.tracker.GATracker
import codeOrchestra.colt.core.ui.ApplicationGUI
import codeOrchestra.colt.core.ui.components.log.Log
import codeOrchestra.colt.core.ui.components.player.ActionPlayer
import codeOrchestra.colt.core.ui.dialog.ProjectDialogs
import codeOrchestra.util.ThreadUtils
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.control.ToggleButton
import org.testng.internal.thread.ThreadUtil

/**
 * @author Dima Kruk
 */
class ASApplicationGUI extends ApplicationGUI {

    @Service ColtAsController coltController
    private @Service ASLiveCodingManager liveCodingManager

    @Lazy AsSettingsForm settingsForm = new AsSettingsForm(saveRunAction:{
        ProjectDialogs.saveProjectDialog()
        runButton.onAction.handle(null)
        ToggleButton playAction = actionPlayerPopup.actionPlayer.play
        playAction.selected = true
        playAction.onAction.handle(null)
    } as EventHandler)

    @Lazy AsProductionBuildForm productionBuildForm = new AsProductionBuildForm(saveBuildAction: {
        if(settingsForm.validateForms()) {
            coltController.startProductionCompilation()
            root.center = logView
            runButton.selected = true
        } else {
            settingsButton.onAction.handle(null)
        }
    } as EventHandler)

    ModelStorage model = codeOrchestra.colt.as.model.ModelStorage.instance

    ASApplicationGUI() {
        init()
    }

    private void init() {

        projectType.text = "ActionScript"

        // build ui

        runButton.onAction = {
            if (!runButton.selected || isFirstTime) {
                isFirstTime = false
                actionPlayerPopup.showing ? actionPlayerPopup.hide() : actionPlayerPopup.show(runButton)
            }

            root.center = logView
            runButton.selected = true
        } as EventHandler

        settingsButton.onAction = {
            root.center = settingsForm
            settingsButton.selected = true
        } as EventHandler

        buildButton.onAction = {
            root.center = productionBuildForm
            buildButton.selected = true
        } as EventHandler

        // data binding

        bindTitle(model.project.name())

        // start

        (model.project.newProject() as BooleanProperty).addListener({ ObservableValue<? extends Boolean> observableValue, Boolean t, Boolean t1 ->
            if (t1) {
                settingsButton.onAction.handle(null)
            } else {
                runButton.selected = true
                root.center = logView
            }
        } as ChangeListener)

        settingsButton.selected = true
        root.center = settingsForm
    }

    protected initActionPlayerPopup() {
        super.initActionPlayerPopup()

        ActionPlayer playerControls = actionPlayerPopup.actionPlayer
        playerControls.play.onAction = {
            if(settingsForm.validateForms()) {
                playerControls.disable = true
                coltController.startBaseCompilation([
                        onComplete: { CompilationResult successResult ->
                            new Thread() {
                                @Override
                                void run() {
                                    ThreadUtils.sleep(3000)
                                    if (liveCodingManager.currentConnections.isEmpty()) {
                                        Platform.runLater({
                                            playerControls.stop.selected = true
                                            playerControls.disable = false
                                        })
                                    }
                                }
                            }.start()
                        },
                        onError: { Throwable t, CompilationResult errorResult ->
                            Platform.runLater({
                                playerControls.stop.selected = true
                                playerControls.disable = false
                            })
                        }
                ] as ColtControllerCallback, true, true)
            } else {
                playerControls.stop.selected = true
                playerControls.disable = false
                actionPlayerPopup.hide()
                settingsButton.onAction.handle(null)
            }
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

    @Override
    protected void initLog() {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(Log.instance);
        }
    }

    protected void initGoogleAnalytics() {
        GATracker.instance.trackPageView("/as/asProject.html", "asProject")
        GAController.instance.pageContainer = root.centerProperty()
        GAController.instance.registerEvent(runButton, "ActionMenu", "Run pressed")
        GAController.instance.registerEvent(settingsButton, "ActionMenu", "Settings pressed")
    }

}
