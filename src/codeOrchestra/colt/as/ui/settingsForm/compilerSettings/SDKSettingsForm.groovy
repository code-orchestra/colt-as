package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class SDKSettingsForm extends FormGroup {

    private LTBForm sdkPath
    private CTBForm defConf
    private CTBForm customConf

    private SDKModel model = ModelStorage.instance.project.projectBuildSettings.sdkModel

    SDKSettingsForm() {
        sdkPath = new LTBForm(text: "Flex SDK Path:", type: FormType.BUTTON, browseType: BrowseType.DIRECTORY)
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
                sdkPath.textField.styleClass.remove("error-input")
            } catch (FlexSDKNotPresentException ignored) {
                model.isValidFlexSDK = false
                sdkPath.textField.styleClass.add("error-input")
            }
        } as ChangeListener<String>)

        bindModel()
    }

    void bindModel() {
        sdkPath.textField.textProperty().bindBidirectional(model.flexSDKPath())
        defConf.checkBox.selectedProperty().bindBidirectional(model.useFlexConfig())
        customConf.checkBox.selectedProperty().bindBidirectional(model.useCustomConfig())
        customConf.textField.textProperty().bindBidirectional(model.customConfigPath())
    }
}
