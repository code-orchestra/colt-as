package codeOrchestra.colt.as.ui.propertyTabPane

import javafx.fxml.FXMLLoader
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Region
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
            vBox.spacing = 5

            //paths
            FXNode projectPaths = FXMLLoader.load(getClass().getResource("projectPaths/projectPaths_form.fxml"))
            vBox.children.add(projectPaths)
            //paths

            //liveSettings
            FXNode target = FXMLLoader.load(getClass().getResource("liveSettings/target_form.fxml"))
            vBox.children.add(target)

            FXNode launcher = FXMLLoader.load(getClass().getResource("liveSettings/launcher_form.fxml"))
            vBox.children.add(launcher)

            FXNode liveSettings = FXMLLoader.load(getClass().getResource("liveSettings/liveSettings_form.fxml"))
            vBox.children.add(liveSettings)

            FXNode lSettings = FXMLLoader.load(getClass().getResource("liveSettings/settings_form.fxml"))
            vBox.children.add(lSettings)
            //liveSettings

            //compilerSettings
            FXNode sdkSettings = FXMLLoader.load(getClass().getResource("compilerSettings/sdkSettings_form.fxml"))
            vBox.children.add(sdkSettings)

            FXNode buildSettings = FXMLLoader.load(getClass().getResource("compilerSettings/buildSettings_form.fxml"))
            vBox.children.add(buildSettings)

            FXNode productionBuild = FXMLLoader.load(getClass().getResource("compilerSettings/productionBuild_form.fxml"))
            vBox.children.add(productionBuild)

            FXNode compilerSettings = FXMLLoader.load(getClass().getResource("compilerSettings/compilerSettings_form.fxml"))
            vBox.children.add(compilerSettings)
            //compilerSettings

            scrollPane.content = vBox
            scrollPane.fitToWidth = true

        }

        return scrollPane
    }

}
