package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical
import codeOrchestra.colt.as.run.LauncherType

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class LauncherModel  implements IModelElement{
    String launcherType = LauncherType.DEFAULT.toString()
    String flashPlayerPath = ""

    LauncherModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(launcherType(), flashPlayerPath())
    }

    @Override
    Closure buildXml(Project project) {
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
