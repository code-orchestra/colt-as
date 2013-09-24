package codeOrchestra.colt.as.ui.settingsForm.liveSettings.emulatorInput

import codeOrchestra.colt.core.ui.components.inputForms.base.TitledInputBase
import codeOrchestra.groovyfx.FXBindable
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox

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
    }

    void setToggleGroup(ToggleGroup value) {
        radioButton.toggleGroup = value
    }
}
