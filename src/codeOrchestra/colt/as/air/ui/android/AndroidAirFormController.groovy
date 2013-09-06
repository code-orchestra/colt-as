package codeOrchestra.colt.as.air.ui.android

import codeOrchestra.colt.as.air.AirAndroidApkBuildScriptGenerator
import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.PasswordInput
import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class AndroidAirFormController extends AirFormController  {
    LabeledActionInput sdk
    LabeledActionInput keystore
    PasswordInput storepass

    @Override
    protected void initOptions() {
        model = runTargetModel.androidAirModel

        sdk = new LabeledActionInput(title: "AIR SDK:", browseType: BrowseType.DIRECTORY)
        sdk.text().bindBidirectional(model.airSDKPath())

        keystore = new LabeledActionInput(title: "Keystore:", browseType: BrowseType.FILE)
        keystore.extensionFilters.add(new FileChooser.ExtensionFilter("p12", "*.p12"))
        keystore.text().bindBidirectional(model.keystorePath())

        storepass = new PasswordInput(title: "Storepass:")
        storepass.text().bindBidirectional(model.storePass())

        options.children.addAll(sdk, keystore, storepass)
    }

    @Override
    protected AirBuildScriptGenerator createBuildScriptGenerator(AsProject project) {
        return new AirAndroidApkBuildScriptGenerator(project)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.androidScript = scriptPath
    }

    @Override
    void unbindProperty() {
        sdk.text().unbindBidirectional(model.airSDKPath())
        keystore.text().unbindBidirectional(model.keystorePath())
        storepass.text().unbindBidirectional(model.storePass())
    }
}
