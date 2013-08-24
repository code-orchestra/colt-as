package codeOrchestra.colt.as.ui.propertyTabPane

import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.ui.components.advancedSeparator.AdvancedSeparator
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node as FXNode
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * @author Dima Kruk
 */
class SettingsForm extends ScrollPane{

    Button saveAndRunButton

    SettingsForm() {

        setId("settings-form")

        VBox vBox = new VBox()
        vBox.alignment = Pos.TOP_CENTER

        //paths
        VBox projectPaths = FXMLLoader.load(getClass().getResource("projectPaths/projectPaths_form.fxml"))
        projectPaths.maxWidth = 640.0
        vBox.children.add(projectPaths)
        //paths

        AdvancedSeparator separator = new AdvancedSeparator()
        vBox.children.add(separator)

        VBox advancedVBox = new VBox()
        VBox.setMargin(advancedVBox, new Insets(0, 0, 72, 0))
        advancedVBox.padding = new Insets(0, 0, 18, 0)
        separator.content = advancedVBox
        vBox.children.add(advancedVBox)

        FXNode template = FXMLLoader.load(getClass().getResource("projectPaths/template_form.fxml"))
        template.styleClass.remove("fieldset")
        advancedVBox.children.add(template)
        //liveSettings
        FXNode target = FXMLLoader.load(getClass().getResource("liveSettings/target_form.fxml"))
        advancedVBox.children.add(target)

        saveAndRunButton = separator.saveButton

        FXNode launcher = FXMLLoader.load(getClass().getResource("liveSettings/launcher_form.fxml"))
        advancedVBox.children.add(launcher)

        FXNode liveSettings = FXMLLoader.load(getClass().getResource("liveSettings/liveSettings_form.fxml"))
        advancedVBox.children.add(liveSettings)

        FXNode lSettings = FXMLLoader.load(getClass().getResource("liveSettings/settings_form.fxml"))
        advancedVBox.children.add(lSettings)
        //liveSettings

        //compilerSettings
        FXNode sdkSettings = FXMLLoader.load(getClass().getResource("compilerSettings/sdkSettings_form.fxml"))
        advancedVBox.children.add(sdkSettings)

        FXNode buildSettings = FXMLLoader.load(getClass().getResource("compilerSettings/buildSettings_form.fxml"))
        advancedVBox.children.add(buildSettings)

        FXNode productionBuild = FXMLLoader.load(getClass().getResource("compilerSettings/productionBuild_form.fxml"))
        advancedVBox.children.add(productionBuild)

        FXNode compilerSettings = FXMLLoader.load(getClass().getResource("compilerSettings/compilerSettings_form.fxml"))
        advancedVBox.children.add(compilerSettings)
        //compilerSettings

        this.content = vBox
        this.fitToWidth = true

        GAController.instance.registerPage(this, "/as/asSettings.html", "asSettings")
    }
}
