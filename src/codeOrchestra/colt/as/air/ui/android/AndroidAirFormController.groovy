package codeOrchestra.colt.as.air.ui.android

import codeOrchestra.colt.as.air.AirAndroidApkBuildScriptGenerator
import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.AirOption
import codeOrchestra.colt.as.model.COLTAsProject
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class AndroidAirFormController extends AirFormController  {
    @Override
    protected void initOptions() {
        model = runTargetModel.androidAirModel

        optionsList.add(new AirOption("AIR SDK", model.airSDKPath(), codeOrchestra.colt.as.air.ui.AirOptionType.DIRECTORY, optionsGP, 0))
        optionsList.add(new AirOption("-keystore", model.keystorePath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, optionsGP, 1))
        optionsList.last().eFilter = new FileChooser.ExtensionFilter("p12", "*.p12")
        optionsList.add(new AirOption("-storepass", model.storePass(), codeOrchestra.colt.as.air.ui.AirOptionType.PASSWORD, optionsGP, 2))
    }

    @Override
    protected AirBuildScriptGenerator createBuildScriptGenerator(COLTAsProject project) {
        return new AirAndroidApkBuildScriptGenerator(project)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.androidScript = scriptPath
    }
}
