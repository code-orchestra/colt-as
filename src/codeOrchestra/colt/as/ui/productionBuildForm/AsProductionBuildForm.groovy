package codeOrchestra.colt.as.ui.productionBuildForm

import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.ProductionBuildForm
import codeOrchestra.colt.core.tracker.GAController
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

/**
 * @author Dima Kruk
 */
class AsProductionBuildForm extends ScrollPane{
    private Button saveAndBuildButton
    EventHandler saveBuildAction

    AsProductionBuildForm() {
        setId("settings-form")
        styleClass.add("scroll-pane-settings")
        this.fitToWidth = true

        VBox vBox = new VBox()
        vBox.alignment = Pos.TOP_CENTER
        this.content = vBox

        ProductionBuildForm productionBuild = new ProductionBuildForm()
        productionBuild.first = true
        vBox.children.add(productionBuild)

        AnchorPane pane = new AnchorPane()
        saveAndBuildButton = new Button(text: "Save & Build", prefWidth: 100)
        AnchorPane.setRightAnchor(saveAndBuildButton, 10)
        pane.children.add(saveAndBuildButton)
        vBox.children.add(pane)

        GAController.instance.registerPage(this, "/as/asProductionBuild.html", "asProductionBuild")
    }

    void setSaveBuildAction(EventHandler saveBuildAction) {
        this.saveBuildAction = saveBuildAction
        saveAndBuildButton.onAction = {
            this.saveBuildAction.handle(it)
        } as EventHandler
    }
}
