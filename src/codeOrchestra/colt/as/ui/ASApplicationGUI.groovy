package codeOrchestra.colt.as.ui

import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.ASLiveCodingManager
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.as.ui.productionBuildForm.AsProductionBuildForm
import codeOrchestra.colt.as.ui.settingsForm.AsSettingsForm
import codeOrchestra.colt.as.ui.testmode.AsTestSettingsForm
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.controller.ColtControllerCallback
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.session.LiveCodingSession
import codeOrchestra.colt.core.session.SocketWriter
import codeOrchestra.colt.core.session.listener.LiveCodingAdapter
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

/**
 * @author Dima Kruk
 */
class ASApplicationGUI extends ApplicationGUI {

    @Service ColtAsController coltController

    @Lazy AsSettingsForm settingsForm = new AsSettingsForm(saveRunAction:{
        if (runSession()) {
            ProjectDialogs.saveProjectDialog()
        }
    } as EventHandler)

    @Lazy AsProductionBuildForm productionBuildForm = new AsProductionBuildForm(saveBuildAction: {
        if(settingsForm.validateForms()) {
            coltController.startProductionCompilation()
            root.center = logView
            runButton.selected = true
            onProductionBuild()
        } else {
            settingsButton.onAction.handle(null)
        }
    } as EventHandler)

    ModelStorage model = codeOrchestra.colt.as.model.ModelStorage.instance

    ASApplicationGUI() {
        init()
    }

    @Override
    protected void showTestSettingsForm() {
        if (testSettingsForm == null) {
            testSettingsForm = new AsTestSettingsForm()
        }
        super.showTestSettingsForm()
    }

    private void init() {
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

        actionPlayerPopup.actionPlayer.add.onAction = {
            coltController.launch()
        } as EventHandler
    }

    @Override
    boolean validateSettingsForm() {
        return settingsForm.validateForms()
    }

    @Override
    protected void compile() {
        coltController.startBaseCompilation([
                onComplete: { CompilationResult successResult ->
                    onRunComplete()
                },
                onError: { Throwable t, CompilationResult errorResult ->
                    onRunError()
                }
        ] as ColtControllerCallback, true, true)
    }

    void build() {

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
        GAController.instance.registerEvent(runButton, "asActionMenu", "Run pressed")
        GAController.instance.registerEvent(buildButton, "asActionMenu", "Build pressed")
        GAController.instance.registerEvent(settingsButton, "asActionMenu", "Settings pressed")

        liveCodingManager.addListener(new LiveCodingAdapter(){
            @Override
            void onSessionStart(LiveCodingSession session) {
                String page = "/as/run"
                Target targetType = model.project.projectBuildSettings.runTargetModel.runTarget
                page += "/" + targetType.toString()
                switch (targetType){
                    case codeOrchestra.colt.as.run.Target.SWF:
                        page += "_" + model.project.projectLiveSettings.launcherModel.launcherType.toString()
                        break
                    case codeOrchestra.colt.as.run.Target.WEB_ADDRESS:
                        break
                    case codeOrchestra.colt.as.run.Target.AIR_IOS:
                    case codeOrchestra.colt.as.run.Target.AIR_ANDROID:
                        page += "_" + model.project.projectLiveSettings.airLauncherType.toString()
                        break
                    case codeOrchestra.colt.as.run.Target.AIR_DESKTOP:
                        break
                }
                page += ".html"
                GATracker.instance.trackPageView(page, "runJsProject")

                GATracker.instance.trackEventWithPage("asLiveMethods", model.project.projectLiveSettings.liveMethods.getPreferenceValue())

                String action = "use"
                SDKModel sdkModel = model.project.projectBuildSettings.sdkModel
                if (sdkModel.useFlexConfig) {
                    action += "_FlexConfig"
                }
                if (sdkModel.useCustomConfig) {
                    action += "_CustomConfig"
                }
                GATracker.instance.trackEventWithPage("asSDKConfig", action)
            }
        })
    }

    protected void onProductionBuild() {
        String page = "/as/runProductionBuild"

        page += ".html"
        GATracker.instance.trackPageView(page, "runProductionBuildAsProject")
    }

}
