package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.core.ui.components.inputForms.*
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.CInputForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.CheckBoxForm
import codeOrchestra.colt.core.ui.components.inputFormsNew.LActionFrom
import codeOrchestra.colt.core.ui.components.inputFormsNew.LInputForm
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter

/**
 * @author Dima Kruk
 */
class BuildSettingsForm extends FormGroup implements IFormValidated {

    private LInputForm fileName
    private LActionFrom outPath

    private CBForm player

    private CheckBoxForm rsl
    private CInputForm locale
    private CheckBoxForm exclude
    private CInputForm interrupt

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    private SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    BuildSettingsForm() {
        fileName = new LInputForm(title: "Output file name:")
        outPath = new LActionFrom(title: "Output path:", browseType: BrowseType.DIRECTORY)

        player = new CBForm()

        rsl = new CheckBoxForm(title: "Use Framework as Runtime Shared Library (RSL)")
        locale = new CInputForm(title: "Non-default locale settings")
        exclude = new CheckBoxForm(title: "Exclude unused code from incremental compilation linking")
        interrupt = new CInputForm(title: "Interrupt compilation by timeout (seconds)")

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

        sdkModel.isValidFlexSDK().addListener({ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue->
            String modelValue = model.targetPlayerVersion
            player.values.clear()
            if (newValue) {
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
        player.values.clear()
        player.values.addAll(versions)
        if (setFirst) {
            model.targetPlayerVersion = versions.first()
        }
        return versions
    }

    void bindModel() {
        fileName.activateValidation()
        fileName.text().bindBidirectional(model.outputFileName())

        outPath.activateValidation()
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
