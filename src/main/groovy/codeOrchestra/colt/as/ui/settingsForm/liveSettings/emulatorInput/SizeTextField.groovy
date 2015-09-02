package codeOrchestra.colt.as.ui.settingsForm.liveSettings.emulatorInput

import javafx.scene.control.TextField

/**
 * @author Dima Kruk
 */
class SizeTextField extends TextField {

    SizeTextField() {
        setPrefSize(50, 30)
        setMaxSize(50, 30)
        setMinSize(50, 30)
    }
}
