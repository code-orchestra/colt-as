package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.as.model.beans.air.descriptor.AndroidDescriptorModel
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AndroidAirModel extends AirModel implements IModelElement {
    boolean useTemporary = true

    AndroidDescriptorModel additionalDescriptorModel = new AndroidDescriptorModel()

    @Override
    Closure buildXml(Project project) {
        return {
            'use-temporary'(useTemporary)
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
        useTemporary = node.'use-temporary' == true
        keystorePath = node.'keystore-path'
        storePass = node.'pass'
        useCustomTemplate = node.'use-custom-template' == true
        templatePath = node.'template-path'
        descriptorModel.buildModel(node.'descriptor')
        additionalDescriptorModel.buildModel(node.'additional-descriptor')
    }
}
