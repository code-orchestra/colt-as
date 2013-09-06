package codeOrchestra.colt.as.air.ui.ios

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.AirIosIpaBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.PasswordInput
import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class IOSAirFormController extends AirFormController {
    LabeledActionInput sdk
    LabeledActionInput profile
    LabeledActionInput keystore
    PasswordInput storepass

    @Override
    protected void initOptions() {
        model = runTargetModel.iosAirModel

        sdk = new LabeledActionInput(title: "AIR SDK:", browseType: BrowseType.DIRECTORY)
        sdk.text().bindBidirectional(model.airSDKPath())

        profile = new LabeledActionInput(title: "Provisioning profile:", browseType: BrowseType.FILE)
        profile.extensionFilters.add(new FileChooser.ExtensionFilter(".mobileprovision", "*.mobileprovision"))
        profile.text().bindBidirectional(model.provisionPath())

        keystore = new LabeledActionInput(title: "Keystore:", browseType: BrowseType.FILE)
        keystore.extensionFilters.add(new FileChooser.ExtensionFilter("p12", "*.p12"))
        keystore.text().bindBidirectional(model.keystorePath())

        storepass = new PasswordInput(title: "Storepass:")
        storepass.text().bindBidirectional(model.storePass())

        options.children.addAll(sdk, profile, keystore, storepass)
    }

    @Override
    protected AirBuildScriptGenerator createBuildScriptGenerator(AsProject project) {
        return new AirIosIpaBuildScriptGenerator(project)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.iosScript = scriptPath
    }

    @Override
    void unbindProperty() {
        sdk.text().unbindBidirectional(model.airSDKPath())
        profile.text().unbindBidirectional(model.provisionPath())
        keystore.text().unbindBidirectional(model.keystorePath())
        storepass.text().unbindBidirectional(model.storePass())
    }
}
