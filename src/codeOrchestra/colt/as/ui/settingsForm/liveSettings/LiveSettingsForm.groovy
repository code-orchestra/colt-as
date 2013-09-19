package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledTitledInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledInput
import codeOrchestra.colt.core.ui.components.inputForms.RadioButtonInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.Pane
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter

/**
 * @author Dima Kruk
 */
class LiveSettingsForm extends FormGroup {

    private ToggleGroup methods

    private  RadioButtonInput annotated
    private  RadioButtonInput all

    private  CheckBoxInput paused
    private  CheckBoxInput gsLive

    private  LabeledTitledInput maxLoop

    private LiveSettingsModel model = ModelStorage.instance.project.projectLiveSettings.liveSettingsModel

    LiveSettingsForm() {
        title = "Live Settings"

        LabeledInput label = new LabeledInput(title: "Live Methods:")
        annotated = new RadioButtonInput(title: "Annotated with [Live]")
        all = new RadioButtonInput(title: "All the methods")

        paused = new CheckBoxInput(title: "Start Session Paused", disable: true)
        gsLive = new CheckBoxInput(title: "Make Getters/Setters Live")

        maxLoop = new LabeledTitledInput(title: "Max Loop Iterations:")

        children.addAll(new Pane(), label, annotated, all, new Pane(), paused, gsLive, maxLoop)

        init()
    }

    public void init() {
        methods = new ToggleGroup()
        methods.toggles.addAll(all.radioButton, annotated.radioButton)

        maxLoop.numeric = true

        bindModel()

        if (!model.liveType) {
            model.liveType = LiveMethods.ANNOTATED.preferenceValue
        } else {
            activateMethods(model.liveType)
        }
    }

    void activateMethods(String newVal) {
        LiveMethods liveMethods = LiveMethods.parseValue(newVal)
        methods.toggles[liveMethods.ordinal()].selected = true
    }

    void bindModel() {
        methods.selectedToggleProperty().addListener({ observableValue, Toggle old_toggle, Toggle new_toggle ->
            model.liveType = LiveMethods.values()[methods.toggles.indexOf(new_toggle)].preferenceValue
        } as ChangeListener)

        (model.liveType() as StringProperty).addListener({prop, oldVal, newVal ->
            if (newVal) {
                activateMethods(newVal as String)
            }
        } as ChangeListener)

        paused.selected().bindBidirectional(model.startSessionPaused())
        gsLive.selected().bindBidirectional(model.makeGSLive())
        maxLoop.text().bindBidirectional(model.maxLoop(), new IntegerStringConverter() as StringConverter<Number>)
    }
}
