package codeOrchestra.colt.as.model.beans.air.descriptor

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class IOSDescriptorModel implements IModelElement {
    boolean highResolution = true
    String devices = "all"

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
    }
}
