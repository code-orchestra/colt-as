package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical

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
    Closure buildXml(Project project) {
        return {
            'sdk-path'(PathUtils.makeRelative(flexSDKPath, project))
            'use-flex'(useFlexConfig)
            'use-custom'(useCustomConfig)
            'custom-config'(PathUtils.makeRelative(customConfigPath, project))
        }
    }

    @Override
    void buildModel(Object node) {
        flexSDKPath = PathUtils.makeAbsolute((node.'sdk-path')?.toString())
        //check SDK path
        FlexSDKManager manager = FlexSDKManager.instance
        try {
            manager.checkIsValidFlexSDKPath(flexSDKPath)
            isValidFlexSDK = true
        } catch (FlexSDKNotPresentException ignored) {
            isValidFlexSDK = false
        }
        //
        useFlexConfig = node.'use-flex' == "true"
        useCustomConfig = node.'use-custom' == "true"
        customConfigPath = PathUtils.makeAbsolute((node.'custom-config'?.toString()))
    }
}
