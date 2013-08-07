package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
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
