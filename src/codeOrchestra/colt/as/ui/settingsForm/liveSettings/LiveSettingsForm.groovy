package codeOrchestra.colt.as.ui.settingsForm.liveSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.RTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.LInputForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.LabelForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.RadioButtonForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.group.FormGroupNew
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
class LiveSettingsForm extends FormGroupNew {

    private ToggleGroup methods

    private  RadioButtonForm annotated
    private  RadioButtonForm all

    private  RadioButtonForm paused
    private  RadioButtonForm gsLive

    private  LInputForm maxLoop

    private LiveSettingsModel model = ModelStorage.instance.project.projectLiveSettings.liveSettingsModel

    LiveSettingsForm() {
        title = "Live Settings"

        LabelForm label = new LabelForm(title: "Live Methods:")
        annotated = new RadioButtonForm(title: "Annotated with [Live]")
        all = new RadioButtonForm(title: "All the methods")

        paused = new RadioButtonForm(title: "Start Session Paused", disable: true)
        gsLive = new RadioButtonForm(title: "Make Getters/Setters Live")

        maxLoop = new LInputForm(title: "Max Loop Iterations:")

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
