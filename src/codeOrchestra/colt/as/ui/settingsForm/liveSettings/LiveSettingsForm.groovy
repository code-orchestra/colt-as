package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
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

    private  RTBForm annotated
    private  RTBForm all

    private  CTBForm paused
    private  CTBForm gsLive

    private  LTBForm maxLoop

    private LiveSettingsModel model = ModelStorage.instance.project.projectLiveSettings.liveSettingsModel

    LiveSettingsForm() {
        title = "Live Settings"

        LTBForm label = new LTBForm(text: "Live Methods:", type: FormType.SIMPLE)
        annotated = new RTBForm(text: "Annotated with [Live]", type: FormType.SIMPLE)
        all = new RTBForm(text: "All the methods", type: FormType.SIMPLE)

        paused = new CTBForm(text: "Start Session Paused", type: FormType.SIMPLE, disable: true)
        gsLive = new CTBForm(text: "Make Getters/Setters Live", type: FormType.SIMPLE)

        maxLoop = new LTBForm(text: "Max Loop Iterations:")

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

        paused.checkBox.selectedProperty().bindBidirectional(model.startSessionPaused())
        gsLive.checkBox.selectedProperty().bindBidirectional(model.makeGSLive())
        maxLoop.textField.textProperty().bindBidirectional(model.maxLoop(), new IntegerStringConverter() as StringConverter<Number>)
    }
}
