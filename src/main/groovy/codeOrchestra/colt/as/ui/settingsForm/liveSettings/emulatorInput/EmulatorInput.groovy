package codeOrchestra.colt.as.ui.settingsForm.liveSettings.emulatorInput

import codeOrchestra.colt.core.ui.components.inputForms.base.TitledInputBase
import codeOrchestra.groovyfx.FXBindable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.scene.control.ChoiceBox
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.text.Text

/**
 * @author Dima Kruk
 */
class EmulatorInput extends TitledInputBase{
    @FXBindable boolean selected

    protected final HBox titleHBox = new HBox(spacing: 10)
    protected final RadioButton radioButton = new RadioButton()
    protected ChoiceBox choiceBox = new ChoiceBox()

    @FXBindable String value

    EmulatorInput() {
        HBox.setMargin(radioButton, new Insets(8, 0, 0, 0))
        titleHBox.children.addAll(radioButton, choiceBox)
        setLeftAnchor(titleHBox, 10)
        setRightAnchor(titleHBox, 10)

        radioButton.textProperty().bindBidirectional(title())
        radioButton.selectedProperty().bindBidirectional(selected())
        choiceBox.disableProperty().bind(selected().not())

        children.addAll(titleHBox)

        choiceBox.setPrefSize(160, 30)
        choiceBox.items.addAll("480", "720", "1080", "iPap", "iPadRetina", "iPhone", "iPhoneRetina", "iPhone5Retina", "NexusOne", "SamsungGalaxyS", "SamsungGalaxyTab")
        choiceBox.valueProperty().bindBidirectional(value())

        choiceBox.selectionModel.selectedItemProperty().addListener({ ObservableValue observableValue, String t, String newValue ->
            if(newValue) {
                final Text text = new Text(newValue)
                text.snapshot(null, null)
                choiceBox.prefWidth = text.getLayoutBounds().getWidth() + 35
            }
        } as ChangeListener)
    }

    void setToggleGroup(ToggleGroup value) {
        radioButton.toggleGroup = value
    }
}
