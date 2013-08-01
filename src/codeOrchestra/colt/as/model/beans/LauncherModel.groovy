package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class LauncherModel  implements IModelElement{
    String launcherType = "DEFAULT"
    String flashPlayerPath

    @Override
    Closure buildXml() {
        return {
            'launcher'(launcherType)
            'player-path'(flashPlayerPath)
        }
    }

    @Override
    void buildModel(Object node) {
        launcherType = node.'launcher'
        flashPlayerPath = node.'player-path'
    }
}
