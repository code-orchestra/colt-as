package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class SDKModel implements IModelElement{
    String flexSDKPath
    boolean useFlexConfig
    boolean useCustomConfig
    String customConfigPath

    @Override
    Closure buildXml() {
        return {
            'sdk-path'(flexSDKPath)
            'use-flex'(useFlexConfig)
            'use-custom'(useCustomConfig)
            'custom-config'(customConfigPath)
        }
    }

    @Override
    void buildModel(Object node) {
        flexSDKPath = node.'sdk-path'
        useFlexConfig = node.'use-flex' == "true"
        useCustomConfig = node.'use-custom' == "true"
        customConfigPath = node.'custom-config'
    }
}
