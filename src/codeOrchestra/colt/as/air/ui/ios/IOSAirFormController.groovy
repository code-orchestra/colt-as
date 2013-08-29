package codeOrchestra.colt.as.air.ui.ios

import codeOrchestra.colt.as.air.AirBuildScriptGenerator
import codeOrchestra.colt.as.air.AirIosIpaBuildScriptGenerator
import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.AirOption
import codeOrchestra.colt.as.model.AsProject
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class IOSAirFormController extends AirFormController {
    @Override
    protected void initOptions() {
        model = runTargetModel.iosAirModel

        options.children.add(new AirOption("AIR SDK", model.airSDKPath(), codeOrchestra.colt.as.air.ui.AirOptionType.DIRECTORY))
        options.children.add(new AirOption("-provisioning-profile", model.provisionPath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, new FileChooser.ExtensionFilter(".mobileprovision", "*.mobileprovision")))
        options.children.add(new AirOption("-keystore", model.keystorePath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, new FileChooser.ExtensionFilter(".p12", "*.p12")))
        options.children.add(new AirOption("-storepass", model.storePass(), codeOrchestra.colt.as.air.ui.AirOptionType.PASSWORD))
    }

    @Override
    protected AirBuildScriptGenerator createBuildScriptGenerator(AsProject project) {
        return new AirIosIpaBuildScriptGenerator(project)
    }

    @Override
    protected void updateScriptPathValue(String scriptPath) {
        runTargetModel.iosScript = scriptPath
    }
}
