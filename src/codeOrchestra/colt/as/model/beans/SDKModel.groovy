package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical
import javafx.collections.FXCollections
import javafx.collections.ObservableList as FXObservableList

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

    boolean isValidFlexSDK

    FXObservableList<String> availablePlayerVersions = FXCollections.observableArrayList()

    void clear() {
        flexSDKPath = ""
        useFlexConfig = false
        useCustomConfig = false
        customConfigPath = ""

        isValidFlexSDK = false

        availablePlayerVersions.clear()
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
