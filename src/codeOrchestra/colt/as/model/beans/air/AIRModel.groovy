package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AIRModel implements IModelElement {
    String airSDKPath = ""
    String provisionPath = ""
    String keystorePath = ""
    String storePass = ""

    AIRModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                airSDKPath(),
                provisionPath(),
                keystorePath(),
                storePass()
        )
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'sdk-path'(airSDKPath)
            'provision-path'(provisionPath)
            'keystore-path'(keystorePath)
            'pass'(storePass)
        }
    }

    @Override
    void buildModel(Object node) {
        airSDKPath = node.'sdk-path'
        provisionPath = node.'provision-path'
        keystorePath = node.'keystore-path'
        storePass = node.'pass'
    }
}
