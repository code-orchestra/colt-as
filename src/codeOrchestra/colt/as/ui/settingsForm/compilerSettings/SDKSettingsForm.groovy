package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated

import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class SDKSettingsForm extends FormGroup implements IFormValidated{

    private LTBForm sdkPath
    private CTBForm defConf
    private CTBForm customConf

    private SDKModel model = ModelStorage.instance.project.projectBuildSettings.sdkModel

    SDKSettingsForm() {
        sdkPath = new LTBForm(title: "Flex SDK Path:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)
        defConf = new CTBForm(title: "Use default SDK compiler configuration file", type: FormType.SIMPLE)
        customConf = new CTBForm(title: "Use custom compiler configuration file", type: FormType.BUTTON)

        children.addAll(sdkPath, defConf, customConf)

        init()
    }

    public void init() {

        customConf.extensionFilters.add(new FileChooser.ExtensionFilter("XML", "*.xml"))

        model.flexSDKPath().addListener({ ObservableValue<? extends String> observable, String oldValue, String newValue ->
            FlexSDKManager manager = FlexSDKManager.instance
            try {
                manager.checkIsValidFlexSDKPath(newValue)
                model.isValidFlexSDK = true
            } catch (FlexSDKNotPresentException ignored) {
                model.isValidFlexSDK = false
            }
        } as ChangeListener<String>)

        model.isValidFlexSDK().addListener({ javafx.beans.Observable observable ->
            validated()
        } as InvalidationListener)

        bindModel()
    }

    void bindModel() {
        sdkPath.text().bindBidirectional(model.flexSDKPath())
        defConf.selected().bindBidirectional(model.useFlexConfig())
        customConf.selected().bindBidirectional(model.useCustomConfig())
        customConf.activateValidation()
        customConf.text().bindBidirectional(model.customConfigPath())
    }

    @Override
    Parent validated() {
        Parent result = customConf.validateValue() ? customConf : null

        if (model.isValidFlexSDK) {
            sdkPath.error = false
        } else {
            sdkPath.error = true
            result = sdkPath
        }
        return result
    }
}
