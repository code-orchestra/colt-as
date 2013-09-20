package codeOrchestra.colt.as.air.ui.android

import codeOrchestra.colt.as.air.AirAndroidApkBuildScriptGenerator
import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirBuildForm
import codeOrchestra.colt.as.air.ui.PasswordInput
import codeOrchestra.colt.as.air.ui.descriptor.AndroidDescriptorGenerationForm
import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.as.model.beans.air.AndroidAirModel
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class AndroidAirBuildForm extends AirBuildForm {
    LabeledActionInput keystore
    PasswordInput storepass

    AndroidAirModel model

    @Override
    protected void initOptions() {
        model = runTargetModel.androidAirModel

        descriptor.initModel(model)
        descriptor.initGenerationForm(new AndroidDescriptorGenerationForm(model))

        keystore = new LabeledActionInput(title: "Keystore:", browseType: BrowseType.FILE)
        keystore.extensionFilters.add(new FileChooser.ExtensionFilter("p12", "*.p12"))
        keystore.text().bindBidirectional(model.keystorePath())

        storepass = new PasswordInput(title: "Storepass:")
        storepass.text().bindBidirectional(model.storePass())

        options.children.addAll(keystore, storepass)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.androidScript = scriptPath
    }

    @Override
    protected String generate(AsProject project) {
        return new AirAndroidApkBuildScriptGenerator(project).generate(model, checkedFiles)
    }

    @Override
    void unbindProperty() {
        keystore.text().unbindBidirectional(model.keystorePath())
        storepass.text().unbindBidirectional(model.storePass())
    }

    @Override
    boolean validate() {
        return !(keystore.validateValue() || storepass.validateValue())
    }
}
