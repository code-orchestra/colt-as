package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical
import javafx.beans.property.StringProperty

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class SDKModel implements IModelElement{

    String flexSDKPath = ""
    boolean useFlexConfig = false
    boolean useCustomConfig = false
    String customConfigPath = ""

    boolean isValidFlexSDK = false

    SDKModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                flexSDKPath(),
                useFlexConfig(),
                useCustomConfig(),
                customConfigPath()
        )
    }

    @Override
    Closure buildXml() {
        return {
            'sdk-path'(PathUtils.makeRelative(flexSDKPath))
            'use-flex'(useFlexConfig)
            'use-custom'(useCustomConfig)
            'custom-config'(PathUtils.makeRelative(customConfigPath))
        }
    }

    @Override
    void buildModel(Object node) {
        flexSDKPath = PathUtils.makeAbsolute((node.'sdk-path')?.toString())
        useFlexConfig = node.'use-flex' == "true"
        useCustomConfig = node.'use-custom' == "true"
        customConfigPath = PathUtils.makeAbsolute((node.'custom-config'?.toString()))
    }
}
