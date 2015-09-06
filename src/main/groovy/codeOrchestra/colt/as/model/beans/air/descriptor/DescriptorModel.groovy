package codeOrchestra.colt.as.model.beans.air.descriptor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class DescriptorModel implements IModelElement {
    String outputPath
    String outputFileName
    String id
    String fileName
    String name
    String version = "1.0.0"

    boolean autoOrient = true
    boolean fullScreen = true

    DescriptorModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(outputPath(),
                outputFileName(),
                id(),
                fileName(),
                name(),
                version())
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'output-path'(outputPath)
            'output-file-name'(outputFileName)
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
        outputPath = node.'output-path'
        outputFileName = node.'output-file-name'
        id = node.'id-value'
        fileName = node.'file-name'
        name = node.'name-value'
        version = node.'version-value'
        autoOrient = node.'auto-orient' == "true"
        fullScreen = node.'full-screen' == "true"
    }
}
