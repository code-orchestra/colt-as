package codeOrchestra.colt.as.ui.settingsForm

import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.BuildSettingsForm
import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.CompilerSettingsForm
import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.ProductionBuildForm
import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.SDKSettingsForm
import codeOrchestra.colt.as.ui.settingsForm.liveSettings.LauncherForm
import codeOrchestra.colt.as.ui.settingsForm.liveSettings.LiveSettingsForm
import codeOrchestra.colt.as.ui.settingsForm.liveSettings.SettingsForm
import codeOrchestra.colt.as.ui.settingsForm.liveSettings.TargetForm
import codeOrchestra.colt.as.ui.settingsForm.projectPaths.ProjectPathsForm
import codeOrchestra.colt.as.ui.settingsForm.projectPaths.TemplateForm
import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.ui.components.advancedSeparator.AdvancedSeparator
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * @author Dima Kruk
 */
class AsSettingsForm extends ScrollPane{

    private Button saveAndRunButton
    EventHandler saveRunAction

    AdvancedSeparator separator
    SDKSettingsForm sdkSettings

    AsSettingsForm() {

        setId("settings-form")

        styleClass.add("scroll-pane-settings")

        VBox vBox = new VBox()
        vBox.alignment = Pos.TOP_CENTER

        //paths
        ProjectPathsForm projectPaths = new ProjectPathsForm()
        projectPaths.maxWidth = 640.0
        vBox.children.add(projectPaths)
        //paths

        separator = new AdvancedSeparator()
        vBox.children.add(separator)

        saveAndRunButton = separator.saveButton
        saveAndRunButton.onAction = saveRunAction

        VBox advancedVBox = new VBox()
        VBox.setMargin(advancedVBox, new Insets(0, 0, 72, 0))
        advancedVBox.padding = new Insets(0, 0, 18, 0)
        separator.content = advancedVBox
        vBox.children.add(advancedVBox)

        TemplateForm template = new TemplateForm()
        template.styleClass.remove("fieldset")
        advancedVBox.children.add(template)
        //liveSettings
        TargetForm target = new TargetForm()
        advancedVBox.children.add(target)


        LauncherForm launcher = new LauncherForm()
        advancedVBox.children.add(launcher)

        LiveSettingsForm liveSettings = new LiveSettingsForm()
        advancedVBox.children.add(liveSettings)

        SettingsForm lSettings = new SettingsForm()
        advancedVBox.children.add(lSettings)
        //liveSettings

        //compilerSettings
        sdkSettings = new SDKSettingsForm()
        advancedVBox.children.add(sdkSettings)

        BuildSettingsForm buildSettings = new BuildSettingsForm()
        advancedVBox.children.add(buildSettings)

        ProductionBuildForm productionBuild = new ProductionBuildForm()
        advancedVBox.children.add(productionBuild)

        CompilerSettingsForm compilerSettings = new CompilerSettingsForm()
        advancedVBox.children.add(compilerSettings)
        //compilerSettings

        this.content = vBox
        this.fitToWidth = true

        GAController.instance.registerPage(this, "/as/asSettings.html", "asSettings")
    }

    void setSaveRunAction(EventHandler saveRunAction) {
        this.saveRunAction = saveRunAction
        saveAndRunButton.onAction = saveRunAction
    }
}
