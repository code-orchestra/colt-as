package codeOrchestra.colt.as.air.ui.ios

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.AirIosIpaBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirBuildForm
import codeOrchestra.colt.as.air.ui.PasswordInput
import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.as.model.beans.air.IOSAirModel
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class IOSAirBuildForm extends AirBuildForm {
    LabeledActionInput profile
    LabeledActionInput keystore
    PasswordInput storepass

    IOSAirModel model

    @Override
    protected void initOptions() {
        model = runTargetModel.iosAirModel

        profile = new LabeledActionInput(title: "Provisioning profile:", browseType: BrowseType.FILE)
        profile.extensionFilters.add(new FileChooser.ExtensionFilter(".mobileprovision", "*.mobileprovision"))
        profile.text().bindBidirectional(model.provisionPath())

        keystore = new LabeledActionInput(title: "Keystore:", browseType: BrowseType.FILE)
        keystore.extensionFilters.add(new FileChooser.ExtensionFilter("p12", "*.p12"))
        keystore.text().bindBidirectional(model.keystorePath())

        storepass = new PasswordInput(title: "Storepass:")
        storepass.text().bindBidirectional(model.storePass())

        options.children.addAll(profile, keystore, storepass)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.iosScript = scriptPath
    }

    @Override
    protected String generate(AsProject project) {
        return new AirIosIpaBuildScriptGenerator(project).generate(model, checkedFiles)
    }

    @Override
    void unbindProperty() {
        profile.text().unbindBidirectional(model.provisionPath())
        keystore.text().unbindBidirectional(model.keystorePath())
        storepass.text().unbindBidirectional(model.storePass())
    }

    @Override
    boolean validate() {
        return !(profile.validateValue() || keystore.validateValue() || storepass.validateValue())
    }
}
