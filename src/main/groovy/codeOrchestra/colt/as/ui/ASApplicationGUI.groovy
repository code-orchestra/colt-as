package codeOrchestra.colt.as.ui
import codeOrchestra.colt.as.ASLiveCodingLanguageHandler
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.ui.dialog.InstallFlexSDKDialog
import codeOrchestra.colt.as.ui.productionBuildForm.AsProductionBuildForm
import codeOrchestra.colt.as.ui.settingsForm.AsSettingsForm
import codeOrchestra.colt.as.ui.testmode.AsTestSettingsForm
import codeOrchestra.colt.as.util.ASPathUtils
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.controller.ColtControllerCallback
import codeOrchestra.colt.core.loading.LiveCodingHandlerManager
import codeOrchestra.colt.core.ui.ApplicationGUI
import codeOrchestra.colt.core.ui.ColtApplication
import codeOrchestra.colt.core.ui.components.log.Log
import codeOrchestra.colt.core.ui.dialog.ProjectDialogs
import codeOrchestra.colt.core.ui.dialog.UpdateDialog
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
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
        runBuild()
    } as EventHandler)

    ModelStorage model = codeOrchestra.colt.as.model.ModelStorage.instance

    ASApplicationGUI() {
        init()
        println "runTime = ${System.currentTimeMillis() - ColtApplication.timeStarted}"
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
            if (!runButton.selected && it != null) {
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

        if (ColtApplication.IS_PLUGIN_MODE) {
            root.center = logView
            runButton.selected = true
        } else {
            settingsButton.selected = true
            root.center = settingsForm
        }
        Platform.runLater({
            if (!ASPathUtils.checkFlexSDK()) {
                UpdateDialog dialog = new InstallFlexSDKDialog(ColtApplication.get().primaryStage)
                dialog.show()
                if (!dialog.isSuccess){
                    println "need message"
                }
            }
        } as Runnable)
    }

    protected initActionPlayerPopup() {
        super.initActionPlayerPopup()

        actionPlayerPopup.actionPlayer.add.onAction = {
            coltController.launch()
        } as EventHandler
    }

    @Override
    boolean validateSettingsForm() {
        if (!ASPathUtils.checkFlexSDK()) {
            UpdateDialog dialog = new InstallFlexSDKDialog(ColtApplication.get().primaryStage)
            dialog.show()
            if (!dialog.isSuccess){
                println "need message"
                return false
            }
        }
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

    void runBuild() {
        if (!ASPathUtils.checkFlexSDK()) {
            UpdateDialog dialog = new InstallFlexSDKDialog(ColtApplication.get().primaryStage)
            dialog.show()
            if (!dialog.isSuccess){
                println "need message"
                return
            }
        }
        if (!settingsForm.validateForms()) {
            settingsButton.onAction.handle(null)
        } else if(!productionBuildForm.validateForms()) {
            buildButton.onAction.handle(null)
        } else {
            coltController.startProductionCompilation()
            root.center = logView
            runButton.selected = true
            onProductionBuild()
        }
    }

    @Override
    protected void initLog() {
        if (LiveCodingHandlerManager.instance.currentHandler != null) {
            ((ASLiveCodingLanguageHandler) LiveCodingHandlerManager.instance.currentHandler).setLoggerService(Log.instance);
        }
    }

    protected void onProductionBuild() {
    }
}