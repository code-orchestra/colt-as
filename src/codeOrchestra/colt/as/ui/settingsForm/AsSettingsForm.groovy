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
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.util.Duration

/**
 * @author Dima Kruk
 */
class AsSettingsForm extends ScrollPane{

    private Button saveAndRunButton
    EventHandler saveRunAction

    AdvancedSeparator separator

    List<IFormValidated> validatedForms

    AsSettingsForm() {

        setId("settings-form")

        styleClass.add("scroll-pane-settings")

        validatedForms = new ArrayList<>()

        VBox vBox = new VBox()
        vBox.alignment = Pos.TOP_CENTER

        //paths
        ProjectPathsForm projectPaths = new ProjectPathsForm()
        projectPaths.maxWidth = 640.0
        validatedForms.add(projectPaths)
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
        validatedForms.add(template)
        advancedVBox.children.add(template)
        //liveSettings
        TargetForm target = new TargetForm()
        validatedForms.add(target)
        advancedVBox.children.add(target)


        LauncherForm launcher = new LauncherForm()
        validatedForms.add(launcher)
        advancedVBox.children.add(launcher)

        LiveSettingsForm liveSettings = new LiveSettingsForm()
        advancedVBox.children.add(liveSettings)

        SettingsForm lSettings = new SettingsForm()
        advancedVBox.children.add(lSettings)
        //liveSettings

        //compilerSettings
        SDKSettingsForm sdkSettings = new SDKSettingsForm()
        validatedForms.add(sdkSettings)
        advancedVBox.children.add(sdkSettings)

        BuildSettingsForm buildSettings = new BuildSettingsForm()
        validatedForms.add(buildSettings)
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
        saveAndRunButton.onAction = {
            if(validateForms()) {
                this.saveRunAction.handle(it)
            }
        } as EventHandler
    }

    public boolean validateForms() {
        Parent invalidNode = null
        validatedForms.each {
            Parent node = it.validated()
            if (node && invalidNode == null) {
                invalidNode = node
            }
        }
        if (invalidNode != null) {
            scrollTo(invalidNode)
        }
        return invalidNode == null
    }

    private scrollTo(Parent node) {
        if (separator.close) {
            separator.close = false
            Timeline timeline = new Timeline(new KeyFrame(new Duration(50), {
                scrollToNode(node)
            } as EventHandler));
            timeline.play();
        } else {
            scrollToNode(node)
        }
    }

    private scrollToNode(Parent node) {
        Bounds bounds = content.boundsInLocal
        Bounds nodeBounds = content.sceneToLocal(node.localToScene(node.layoutBounds))

        setVvalue(nodeBounds.minY/bounds.height)
    }
}
