package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.ui.components.inputForms.*
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter

/**
 * @author Dima Kruk
 */
class BuildSettingsForm extends FormGroup {

    private LTBForm fileName
    private LTBForm outPath

    private CBForm player

    private CTBForm rsl
    private CTBForm locale
    private CTBForm exclude
    private CTBForm interrupt

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    private SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    BuildSettingsForm() {
        fileName = new LTBForm(text: "Output file name:", type: FormType.TEXT_FIELD)
        outPath = new LTBForm(text: "Output path:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)

        player = new CBForm()

        rsl = new CTBForm(text: "Use Framework as Runtime Shared Library (RSL)", type: FormType.SIMPLE)
        locale = new CTBForm(text: "Non-default locale settings", type: FormType.TEXT_FIELD)
        exclude = new CTBForm(text: "Exclude unused code from incremental compilation linking", type: FormType.SIMPLE)
        interrupt = new CTBForm(text: "Interrupt compilation by timeout (seconds)", type: FormType.TEXT_FIELD)

        children.addAll(fileName, outPath, player, rsl, locale, exclude, interrupt)

        init()
    }

    void init() {

        player.errorLabel.visible = false

        interrupt.numeric = true

        if (sdkModel.isValidFlexSDK) {
            initChoiceBox()
        } else {
            error(true)
        }

        model.targetPlayerVersion().addListener({ ObservableValue<? extends String> observableValue, String oldValue, String newValue ->
            //model.targetPlayerVersion can't be empty
            if (newValue?.isEmpty()) {
                //fix model value
                model.targetPlayerVersion = oldValue
            }
            if(sdkModel.isValidFlexSDK && model.useMaxVersion && newValue != null && !player.choiceBox.items.contains(newValue)) {
                //fix model value
                model.targetPlayerVersion = oldValue
            }
        } as ChangeListener)

        player.choiceBox.valueProperty().addListener({ ObservableValue<? extends String> observableValue, String oldValue, String newValue ->
            if(newValue != null && !newValue?.isEmpty()) {
                if(!player.choiceBox.items.contains(newValue)) {
                    player.choiceBox.items.add(newValue)
                    error(true)
                } else {
                    if(player.errorLabel.visible) {
                        player.choiceBox.items.remove(oldValue)
                    }
                    error(false)
                }
            } else {
                error(false)
            }

        } as ChangeListener)

        sdkModel.isValidFlexSDK().addListener({ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue->
            String modelValue = model.targetPlayerVersion
            player.choiceBox.items.clear()
            if (newValue) {
                List<String> versions = initChoiceBox()
                if(!model.useMaxVersion && modelValue != null && !modelValue.isEmpty()) {
                    if (versions.contains(modelValue)) {
                        model.targetPlayerVersion = modelValue
                        error(false)
                    } else {
                        player.choiceBox.items.add(modelValue)
                        model.targetPlayerVersion = modelValue
                        error(true)
                    }
                } else {
                    model.targetPlayerVersion = versions.first()
                    error(false)
                }
            } else {
                player.choiceBox.items.add(modelValue)
                model.targetPlayerVersion = modelValue
                error(true)
            }
        } as ChangeListener<Boolean>)

        model.useMaxVersion().addListener({ ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue ->
            if (newValue && sdkModel.isValidFlexSDK) {
                initChoiceBox(true)
            }
        } as ChangeListener)

        bindModel()
    }

    private List<String> initChoiceBox(boolean setFirst = false) {
        FlexSDKManager manager = FlexSDKManager.instance
        List<String> versions = manager.getAvailablePlayerVersions(new File(sdkModel.flexSDKPath))
        player.choiceBox.items.clear()
        player.choiceBox.items.addAll(versions)
        if (setFirst) {
            model.targetPlayerVersion = versions.first()
        }
        return versions
    }

    void bindModel() {
        fileName.textField.textProperty().bindBidirectional(model.outputFileName())
        outPath.textField.textProperty().bindBidirectional(model.outputPath())
        player.checkBox.selectedProperty().bindBidirectional(model.useMaxVersion())

        player.choiceBox.valueProperty().bindBidirectional(model.targetPlayerVersion())

        rsl.checkBox.selectedProperty().bindBidirectional(model.rsl())

        locale.checkBox.selectedProperty().bindBidirectional(model.nonDefaultLocale())
        locale.textField.textProperty().bindBidirectional(model.localeSettings())

        exclude.checkBox.selectedProperty().bindBidirectional(model.excludeDeadCode())

        interrupt.checkBox.selectedProperty().bindBidirectional(model.interrupt())
        interrupt.textField.textProperty().bindBidirectional(model.interruptValue(), new IntegerStringConverter() as StringConverter<Number>)
    }

    private void error(boolean b) {
        player.errorLabel.visible = b
        player.errorLabel.text = sdkModel.isValidFlexSDK ? "Incorrect player version specified" : "Incorrect Flex SDK path specified"
    }
}
