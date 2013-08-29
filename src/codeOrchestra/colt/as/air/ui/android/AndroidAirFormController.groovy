package codeOrchestra.colt.as.air.ui.android

import codeOrchestra.colt.as.air.AirAndroidApkBuildScriptGenerator
import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.AirOption
import codeOrchestra.colt.as.model.AsProject
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class AndroidAirFormController extends AirFormController  {
    @Override
    protected void initOptions() {
        model = runTargetModel.androidAirModel

        options.children.add(new AirOption("AIR SDK", model.airSDKPath(), codeOrchestra.colt.as.air.ui.AirOptionType.DIRECTORY))
        options.children.add(new AirOption("-keystore", model.keystorePath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, new FileChooser.ExtensionFilter("p12", "*.p12")))
        options.children.add(new AirOption("-storepass", model.storePass(), codeOrchestra.colt.as.air.ui.AirOptionType.PASSWORD))


    }

    @Override
    protected AirBuildScriptGenerator createBuildScriptGenerator(AsProject project) {
        return new AirAndroidApkBuildScriptGenerator(project)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.androidScript = scriptPath
    }
}
