package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class RunTargetModel implements IModelElement{
    String target
    String httpIndex
    String iosScript
    AIRModel iosAirModel = new AIRModel()
    String androidScript
    AIRModel androidAirModel = new AIRModel()

    @Override
    Closure buildXml() {
        return {
            'run-target'(target)
            'http-index'(httpIndex)
            'ios-script'(path:PathUtils.makeRelative(iosScript), iosAirModel.buildXml())
            'android-script'(path:PathUtils.makeRelative(androidScript), androidAirModel.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        target = node.'run-target'
        httpIndex = node.'http-index'
        iosScript = PathUtils.makeAbsolute(node.'ios-script'.@path?.toString())
        iosAirModel.buildModel(node.'ios-script')
        androidScript = PathUtils.makeAbsolute(node.'android-script'.@path?.toString())
        androidAirModel.buildModel(node.'android-script')
    }
}
