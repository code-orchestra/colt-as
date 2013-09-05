package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated
import codeOrchestra.colt.as.ui.settingsForm.ValidatedForm
import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node as FXNode
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class SDKSettingsForm extends ValidatedForm{

    private LTBForm sdkPath
    private CTBForm defConf
    private CTBForm customConf

    private SDKModel model = ModelStorage.instance.project.projectBuildSettings.sdkModel

    SDKSettingsForm() {
        sdkPath = new LTBForm(title: "Flex SDK Path:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)
        defConf = new CTBForm(text: "Use default SDK compiler configuration file", type: FormType.SIMPLE)
        customConf = new CTBForm(text: "Use custom compiler configuration file", type: FormType.BUTTON)

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
        defConf.checkBox.selectedProperty().bindBidirectional(model.useFlexConfig())
        customConf.checkBox.selectedProperty().addListener({ ObservableValue<? extends Boolean> observableValue, Boolean t, Boolean t1 ->
            validateIsFile(customConf.textField)
        } as ChangeListener)
        customConf.checkBox.selectedProperty().bindBidirectional(model.useCustomConfig())
        customConf.text().addListener({ javafx.beans.Observable observable ->
            validateIsFile(customConf.textField)
        } as InvalidationListener)
        customConf.text().bindBidirectional(model.customConfigPath())
    }

    @Override
    Parent validated() {
        Parent result = validateIsFile(customConf.textField)

        if (model.isValidFlexSDK) {
            sdkPath.textField.styleClass.remove("error-input")
        } else {
            if (!sdkPath.textField.styleClass.contains("error-input")){
                sdkPath.textField.styleClass.add("error-input")
            }
            result = sdkPath.textField
        }
        return result
    }
}
