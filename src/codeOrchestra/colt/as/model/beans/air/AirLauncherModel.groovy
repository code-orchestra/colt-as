package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AirLauncherModel implements IModelElement {
    String launcherType = codeOrchestra.colt.as.run.AirLauncherType.DEVICE.name()
    String emulatorValue = "iPhone5Retina"

    AirLauncherModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(launcherType(),
                emulatorValue()
        )
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'launcher-type'(launcherType)
            'emulator-value'(emulatorValue)
        }
    }

    @Override
    void buildModel(Object node) {
        launcherType = node.'launcher-type'
        emulatorValue = node.'emulator-value'
        if (emulatorValue.isEmpty()) {
            emulatorValue = "iPhone5Retina"
        }
    }
}
