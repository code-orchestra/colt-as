package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.as.model.beans.air.descriptor.IOSDescriptorModel
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class IOSAirModel extends AirModel implements IModelElement {
    String provisionPath = ""

    IOSDescriptorModel additionalDescriptorModel = new IOSDescriptorModel()

    @Override
    Closure buildXml(Project project) {
        return {
            'provision-path'(provisionPath)
            'keystore-path'(keystorePath)
            'pass'(storePass)
            'use-custom-template'(useCustomTemplate)
            'template-path'(templatePath)
            'descriptor'(descriptorModel.buildXml(project))
            'additional-descriptor'(additionalDescriptorModel.buildXml(project))
        }
    }

    @Override
    void buildModel(Object node) {
        provisionPath = node.'provision-path'
        keystorePath = node.'keystore-path'
        storePass = node.'pass'
        useCustomTemplate = node.'use-custom-template' == true
        templatePath = node.'template-path'
        descriptorModel.buildModel(node.'descriptor')
        additionalDescriptorModel.buildModel(node.'additional-descriptor')
    }
}
