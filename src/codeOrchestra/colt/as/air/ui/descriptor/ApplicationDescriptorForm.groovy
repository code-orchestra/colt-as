package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonActionInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup

/**
 * @author Dima Kruk
 */
class ApplicationDescriptorForm extends FormGroup{
    private ToggleGroup descriptor
    private RadioButtonInput generated
    private RadioButtonActionInput custom

    ApplicationDescriptorForm() {
        title = "Application descriptor"

        generated = new RadioButtonInput(title: "Generated")
        custom = new RadioButtonActionInput(title: "Custom template:", buttonText: "Create")
        custom.action = {
            DescriptorGenerationForm form = new DescriptorGenerationForm()
            form.initOwner(this.scene.window)
            form.showAndWait()
        } as EventHandler

        descriptor = new ToggleGroup()
        descriptor.toggles.addAll(generated.radioButton, custom.radioButton)

        children.addAll(generated, custom)
    }
}
