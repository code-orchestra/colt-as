package codeOrchestra.colt.as.facade

import codeOrchestra.colt.as.ASLiveCodingManager
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.facade.AbstractColtFacade
import codeOrchestra.colt.core.ui.ApplicationGUI

/**
 * @author Alexander Eliseyev
 */
class AsColtFacade extends AbstractColtFacade {

    @Service
    ColtAsController controller

    @Service
    ASLiveCodingManager liveCodingManager

    AsColtFacade(ApplicationGUI value) {
        super(value)
    }

    @Override
    void runSession() {
        if (applicationGUI) {
            super.runSession()
        } else {
            controller.startBaseCompilation()
        }
    }

    @Override
    void stopSession() {
        liveCodingManager.stopAllSession()
    }

    @Override
    void pauseSession() {
        liveCodingManager.pause()
    }

    @Override
    void resumeSession() {
        liveCodingManager.flush()
    }

    @Override
    void restartSession() {
        stopSession()
        runSession()
    }

    @Override
    void openNewConnection() {
        controller.launch()
    }

    @Override
    void closeAllConnections() {
        stopSession()
    }

    @Override
    void runProductionBuild() {
        controller.startProductionCompilation()
    }

    @Override
    void dispose() {
    }

}
