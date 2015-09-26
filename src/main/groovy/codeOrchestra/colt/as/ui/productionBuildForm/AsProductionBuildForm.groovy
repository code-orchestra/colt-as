package codeOrchestra.colt.as.ui.productionBuildForm

import codeOrchestra.colt.as.ui.settingsForm.compilerSettings.ProductionBuildForm
import codeOrchestra.colt.core.ui.components.scrollpane.SettingsScrollPane
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane

/**
 * @author Dima Kruk
 */
class AsProductionBuildForm extends SettingsScrollPane{
    private Button saveAndBuildButton
    EventHandler saveBuildAction

    ProductionBuildForm productionBuild

    AsProductionBuildForm() {
        productionBuild = new ProductionBuildForm()
        productionBuild.first = true
        mainContainer.children.add(productionBuild)

        AnchorPane pane = new AnchorPane()
        saveAndBuildButton = new Button(text: "Save & Build", prefWidth: 100)
        AnchorPane.setRightAnchor(saveAndBuildButton, 10)
        pane.children.add(saveAndBuildButton)
        mainContainer.children.add(pane)
    }

    void setSaveBuildAction(EventHandler saveBuildAction) {
        this.saveBuildAction = saveBuildAction
        saveAndBuildButton.onAction = {
            if(validateForms()) {
                this.saveBuildAction.handle(it)
            }
        } as EventHandler
    }

    public boolean validateForms() {
        return productionBuild.validated() == null
    }

}
