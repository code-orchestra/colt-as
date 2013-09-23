package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.as.model.beans.air.IOSAirModel
import codeOrchestra.colt.as.model.beans.air.descriptor.IOSDescriptorModel
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import codeOrchestra.colt.as.air.util.DescriptorConverter

/**
 * @author Dima Kruk
 */
class IOSDescriptorGenerationForm extends DescriptorGenerationForm {

    private ToggleGroup devices
    private IOSDescriptorModel iosDescriptorModel

    IOSDescriptorGenerationForm(AirModel model) {
        super(model)

        iosDescriptorModel = (model as IOSAirModel).additionalDescriptorModel;

        devices = new ToggleGroup()

        options.children.addAll(new LabeledInput(title: "Devices:"),
                new RadioButtonInput(title: "All", toggleGroup: devices),
                new RadioButtonInput(title: "iPhone/iPod touch", toggleGroup: devices),
                new RadioButtonInput(title: "iPad", toggleGroup: devices),
                new CheckBoxInput(title: "High resolution", bindProperty: iosDescriptorModel.highResolution())
        )

        iosDescriptorModel.devices().addListener({ ObservableValue<? extends String> observableValue, String t, String newValue ->
            if (newValue) {
                activateDevices(newValue)
            }
        } as ChangeListener)
        devices.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            iosDescriptorModel.devices = Devices.values()[devices.toggles.indexOf(new_toggle)]
        } as ChangeListener)

        activateDevices(iosDescriptorModel.devices)
    }

    @Override
    protected String generateTemplate(File outFile) {
        DescriptorConverter.makeTemplateForIOS(descriptorModel, iosDescriptorModel, outFile)
        return outFile.path
    }

    void activateDevices(String newVal) {
        Devices targetType = Devices.valueOf("" + newVal)
        devices.toggles[targetType.ordinal()].selected = true
    }
}
