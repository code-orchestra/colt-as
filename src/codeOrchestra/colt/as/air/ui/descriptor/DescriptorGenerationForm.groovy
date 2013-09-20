package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.as.model.beans.air.descriptor.DescriptorModel
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledTitledInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.ButtonBar

/**
 * @author Dima Kruk
 */
class DescriptorGenerationForm extends Stage {
    VBox root

    protected ButtonBar buttonBar
    protected Button generateBtn
    protected Button cancelBtn
    protected FormGroup options

    private DescriptorModel descriptorModel

    DescriptorGenerationForm(AirModel model) {
        descriptorModel = model.descriptorModel
        setTitle("Create AIR Descriptor Template")
        initModality(Modality.WINDOW_MODAL)

        root = new VBox()
        scene = new Scene(root)

        FormGroup descriptor = new FormGroup(title: "AIR application descriptor", newChildren: [
                new LabeledTitledInput(title: "File name:", bindProperty: descriptorModel.fileName()),
                new LabeledActionInput(title: "Folder:", bindProperty: descriptorModel.outputPath(), browseType: BrowseType.DIRECTORY)
        ])
        descriptor.first = true


        FormGroup properties = new FormGroup(title: "Application properties", newChildren: [
                new LabeledTitledInput(title: "ID:", bindProperty: descriptorModel.id()),
                new LabeledTitledInput(title: "Name:", bindProperty: descriptorModel.name()),
                new LabeledTitledInput(title: "Version:", bindProperty: descriptorModel.version())
        ])

        options = new FormGroup(title: "Mobile options", newChildren: [
                new CheckBoxInput(title: "auto-orient", bindProperty: descriptorModel.autoOrient()),
                new CheckBoxInput(title: "full screen", bindProperty: descriptorModel.fullScreen())
        ])

        root.children.addAll(descriptor, properties, options)

        buttonBar = new ButtonBar()
        generateBtn = new Button("Create")
        generateBtn.defaultButton = true
        ButtonBar.setType(generateBtn, ButtonBar.ButtonType.OK_DONE)
        cancelBtn = new Button("Cancel")
        ButtonBar.setType(cancelBtn, ButtonBar.ButtonType.CANCEL_CLOSE)
        buttonBar.buttons.addAll(generateBtn, cancelBtn)

        AnchorPane anchorPane = new AnchorPane()
        AnchorPane.setLeftAnchor(buttonBar, 10)
        AnchorPane.setRightAnchor(buttonBar, 10)
        anchorPane.children.add(buttonBar)

        FormGroup actions = new FormGroup()
        actions.first = true
        actions.children.add(anchorPane)

        root.children.add(actions)

        root.setAlignment(Pos.TOP_CENTER)
        root.setPrefWidth(460)
        root.setFillWidth(true)
    }
}
