package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.core.ui.components.inputForms.*
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxWithTextInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledTitledInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter

/**
 * @author Dima Kruk
 */
class BuildSettingsForm extends FormGroup implements IFormValidated {

    private LabeledTitledInput fileName
    private LabeledActionInput outPath

    private CBForm player

    private CheckBoxInput rsl
    private CheckBoxWithTextInput locale
    private CheckBoxInput exclude
    private CheckBoxWithTextInput interrupt

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    private SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    BuildSettingsForm() {
        fileName = new LabeledTitledInput(title: "Output file name:")
        outPath = new LabeledActionInput(title: "Output path:", browseType: BrowseType.DIRECTORY)

        player = new CBForm()

        rsl = new CheckBoxInput(title: "Use Framework as Runtime Shared Library (RSL)")
        locale = new CheckBoxWithTextInput(title: "Non-default locale settings")
        exclude = new CheckBoxInput(title: "Exclude unused code from incremental compilation linking")
        interrupt = new CheckBoxWithTextInput(title: "Interrupt compilation by timeout (seconds)")

        children.addAll(fileName, outPath, player, rsl, locale, exclude, interrupt)

        init()
    }

    void init() {

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
            if(sdkModel.isValidFlexSDK && model.useMaxVersion && newValue != null && !player.values.contains(newValue)) {
                //fix model value
                model.targetPlayerVersion = oldValue
            }
        } as ChangeListener)

        player.value().addListener({ ObservableValue<? extends String> observableValue, String oldValue, String newValue ->
            if(newValue != null && !newValue?.isEmpty()) {
                if(!player.values.contains(newValue)) {
                    player.values.add(newValue)
                    error(true)
                } else {
                    if(player.errorMessage) {
                        player.values.remove(oldValue)
                    }
                    error(false)
                }
            } else {
                error(false)
            }

        } as ChangeListener)

        sdkModel.flexSDKPath().addListener({ ObservableValue<? extends String> observableValue, String t, String newValue ->
            updatePlayerVersion(sdkModel.isValidFlexSDK)
        } as ChangeListener)

        model.useMaxVersion().addListener({ ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue ->
            if (newValue && sdkModel.isValidFlexSDK) {
                initChoiceBox(true)
            }
        } as ChangeListener)

        bindModel()
    }

    private updatePlayerVersion(boolean isValidSDK) {
        String modelValue = model.targetPlayerVersion
        player.values.clear()
        if (isValidSDK) {
            List<String> versions = initChoiceBox()
            if(!model.useMaxVersion && modelValue != null && !modelValue.isEmpty()) {
                if (versions.contains(modelValue)) {
                    model.targetPlayerVersion = modelValue
                    error(false)
                } else {
                    player.values.add(modelValue)
                    model.targetPlayerVersion = modelValue
                    error(true)
                }
            } else {
                model.targetPlayerVersion = versions.first()
                error(false)
            }
        } else {
            player.values.add(modelValue)
            model.targetPlayerVersion = modelValue
            error(true)
        }
    }

    private List<String> initChoiceBox(boolean setFirst = false) {
        FlexSDKManager manager = FlexSDKManager.instance
        List<String> versions = manager.getAvailablePlayerVersions(new File(sdkModel.flexSDKPath))
        player.values.clear()
        player.values.addAll(versions)
        if (setFirst) {
            model.targetPlayerVersion = versions.first()
        }
        return versions
    }

    void bindModel() {
        fileName.text().bindBidirectional(model.outputFileName())

        outPath.text().bindBidirectional(model.outputPath())

        player.selected().bindBidirectional(model.useMaxVersion())

        player.value().bindBidirectional(model.targetPlayerVersion())

        rsl.selected().bindBidirectional(model.rsl())

        locale.selected().bindBidirectional(model.nonDefaultLocale())
        locale.text().bindBidirectional(model.localeSettings())

        exclude.selected().bindBidirectional(model.excludeDeadCode())

        interrupt.selected().bindBidirectional(model.interrupt())
        interrupt.text().bindBidirectional(model.interruptValue(), new IntegerStringConverter() as StringConverter<Number>)
    }

    private void error(boolean b) {
        if(b){
            player.errorMessage = sdkModel.isValidFlexSDK ? "Incorrect player version specified" : "Incorrect Flex SDK path specified"
        }else{
            player.errorMessage = null
        }
    }

    @Override
    Parent validated() {
        Parent result = null
        if (outPath.validateValue()) {
            result = outPath
        }
        if (fileName.validateValue()) {
            result = fileName
        }
        return result
    }
}
