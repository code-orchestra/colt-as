package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class RunTargetModel implements IModelElement{
    String target
    String httpIndex
    String iosScript
    String androidScript

    @Override
    Closure buildXml() {
        return {
            'run-target'(target)
            'http-index'(httpIndex)
            'ios-script'(iosScript)
            'android-script'(androidScript)
        }
    }

    @Override
    void buildModel(Object node) {
        target = node.'run-target'
        httpIndex = node.'http-index'
        iosScript = node.'ios-script'
        androidScript = node.'android-script'
    }
}
