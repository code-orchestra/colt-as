package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.as.run.AirLauncherType
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.StringUtils
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AirLauncherModel implements IModelElement {
    String launcherType = AirLauncherType.DEVICE.name()
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
        if (StringUtils.isEmpty(emulatorValue)) {
            emulatorValue = "iPhone5Retina"
        }
    }

    AirLauncherType getType() {
        if (StringUtils.isEmpty(launcherType)) {
            return AirLauncherType.DEVICE
        }
        return AirLauncherType.valueOf(launcherType)
    }

}
