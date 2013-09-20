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
class DescriptorModel implements IModelElement {
    String id
    String fileName
    String name
    String version

    boolean autoOrient = true
    boolean fullScreen = true

    @Override
    Closure buildXml(Project project) {
        return {
            'id-value'(id)
            'file-name'(fileName)
            'name-value'(name)
            'version-value'(version)
            'auto-orient'(autoOrient)
            'full-screen'(fullScreen)
        }
    }

    @Override
    void buildModel(Object node) {
        id = node.'id-value'
        fileName = node.'file-name'
        name = node.'name-value'
        version = node.'version-value'
        autoOrient = node.'auto-orient' == "true"
        fullScreen = node.'full-screen' == "true"
    }
}
