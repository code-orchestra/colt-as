package codeOrchestra.colt.as.model.beans.air.descriptor

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.StringUtils
import groovy.transform.Canonical
import codeOrchestra.colt.as.air.ui.descriptor.Devices

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class IOSDescriptorModel implements IModelElement {
    boolean highResolution = true
    String devices = Devices.ALL.name()

    IOSDescriptorModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(highResolution(),
                devices())
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'high-resolution'(highResolution)
            'devices-value'(devices)
        }
    }

    @Override
    void buildModel(Object node) {
        highResolution = node.'high-resolution' == "true"
        devices = node.'devices-value'
        if (StringUtils.isEmpty(devices)) {
            devices = Devices.ALL.name()
        }
    }
}
