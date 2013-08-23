package codeOrchestra.colt.as.ui.propertyTabPane

import codeOrchestra.colt.core.ui.components.advancedSeparator.AdvancedSeparator
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import javafx.scene.Node as FXNode

/**
 * @author Dima Kruk
 */
class SettingsForm {
    VBox vBox
    ScrollPane scrollPane

    public FXNode getPane() {
        if (!scrollPane) {
            scrollPane = new ScrollPane()

            vBox = new VBox()
            vBox.maxWidth= 640.0

            //paths
            FXNode projectPaths = FXMLLoader.load(getClass().getResource("projectPaths/projectPaths_form.fxml"))
            vBox.children.add(projectPaths)
            //paths

            AdvancedSeparator separator = new AdvancedSeparator()
            vBox.children.add(separator)

            VBox advancedVBox = new VBox()
            separator.content = advancedVBox
            vBox.children.add(advancedVBox)

            FXNode template = FXMLLoader.load(getClass().getResource("projectPaths/template_form.fxml"))
            template.styleClass.remove("fieldset")
            advancedVBox.children.add(template)
            //liveSettings
            FXNode target = FXMLLoader.load(getClass().getResource("liveSettings/target_form.fxml"))
            advancedVBox.children.add(target)

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

            scrollPane.content = vBox
            scrollPane.fitToWidth = true

        }
        Platform.runLater({scrollPane.requestFocus()})

        return scrollPane
    }

}
