package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonActionInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.event.EventHandler
import javafx.scene.control.ToggleGroup

/**
 * @author Dima Kruk
 */
class ApplicationDescriptorForm extends FormGroup{
    private ToggleGroup descriptor
    private RadioButtonInput generated
    private RadioButtonActionInput custom

    private AirModel model

    private DescriptorGenerationForm generationForm

    ApplicationDescriptorForm() {
        title = "Application descriptor"

        generated = new RadioButtonInput(title: "Generated")
        generated.selected = true
        custom = new RadioButtonActionInput(title: "Custom template:", buttonText: "Create")
        custom.action = {
            if (generationForm.owner != this.scene.window) {
                generationForm.initOwner(this.scene.window)
            }
            generationForm.showAndWait()
            custom.text = generationForm.templatePath
        } as EventHandler

        descriptor = new ToggleGroup()
        descriptor.toggles.addAll(generated.radioButton, custom.radioButton)

        children.addAll(generated, custom)
    }

    void initGenerationForm(DescriptorGenerationForm generationForm) {
        this.generationForm = generationForm
    }

    void initModel(AirModel model) {
        this.model = model
        custom.selected().bindBidirectional(model.useCustomTemplate())
        custom.bindProperty = model.templatePath()
    }
}
