package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class SettingsModel implements IModelElement{
    boolean clearLog
    boolean disconnectOnTimeout

    @Override
    Closure buildXml() {
        return {
            'clear-log'(clearLog)
            disconnect(disconnectOnTimeout)
        }
    }

    @Override
    void buildModel(Object node) {
        clearLog = node.'clear-log' == "true"
        disconnectOnTimeout = node.disconnect == "true"
    }
}
