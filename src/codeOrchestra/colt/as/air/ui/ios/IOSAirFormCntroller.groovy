package codeOrchestra.colt.as.air.ui.ios

import codeOrchestra.colt.as.air.ui.AirFormController
import codeOrchestra.colt.as.air.ui.AirOption
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class IOSAirFormCntroller extends AirFormController {
    @Override
    protected void initOptions() {
        optionsList.add(new AirOption("AIR SDK", model.airSDKPath(), codeOrchestra.colt.as.air.ui.AirOptionType.DIRECTORY, optionsGP, 0))
        optionsList.add(new AirOption("-provisioning-profile", model.keystorePath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, optionsGP, 1))
        optionsList.last().eFilter = new FileChooser.ExtensionFilter(".mobileprovision", "*.mobileprovision")
        optionsList.add(new AirOption("-keystore", model.keystorePath(), codeOrchestra.colt.as.air.ui.AirOptionType.FILE, optionsGP, 2))
        optionsList.last().eFilter = new FileChooser.ExtensionFilter(".p12", "*.p12")
        optionsList.add(new AirOption("-storepass", model.storePass(), codeOrchestra.colt.as.air.ui.AirOptionType.PASSWORD, optionsGP, 3))
    }
}
