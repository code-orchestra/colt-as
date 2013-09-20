package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.beans.air.AndroidAirModel
import codeOrchestra.colt.as.model.beans.air.IOSAirModel
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical
import codeOrchestra.colt.as.run.Target

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class RunTargetModel implements IModelElement{
    String target = Target.SWF.name()
    String httpIndex = ""
    String iosScript = ""
    IOSAirModel iosAirModel = new IOSAirModel()
    String androidScript = ""
    AndroidAirModel androidAirModel = new AndroidAirModel()

    RunTargetModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                target(),
                httpIndex(),
                iosScript(),
                androidScript()
        )
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'run-target'(target)
            'http-index'(httpIndex)
            'ios-script'(path:PathUtils.makeRelative(iosScript, project), iosAirModel.buildXml(project))
            'android-script'(path:PathUtils.makeRelative(androidScript, project), androidAirModel.buildXml(project))
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

    Target getRunTarget() {
        return Target.parse(target)
    }

    String getAirSDKPath() {
        return codeOrchestra.colt.as.model.ModelStorage.instance.project.projectBuildSettings.sdkModel.flexSDKPath
    }

}
