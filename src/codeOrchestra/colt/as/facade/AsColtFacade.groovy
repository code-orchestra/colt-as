package codeOrchestra.colt.as.facade

import codeOrchestra.colt.as.ASLiveCodingManager
import codeOrchestra.colt.as.controller.ColtAsController
import codeOrchestra.colt.core.annotation.Service
import codeOrchestra.colt.core.facade.ColtFacade

/**
 * @author Alexander Eliseyev
 */
class AsColtFacade implements ColtFacade {

    @Service
    ColtAsController controller

    @Service
    ASLiveCodingManager liveCodingManager

    @Override
    void runSession() {
        controller.startBaseCompilation()
    }

    @Override
    void stopSession() {
        liveCodingManager.getCurrentConnections().each { liveCodingManager.stopSession(it) }
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
