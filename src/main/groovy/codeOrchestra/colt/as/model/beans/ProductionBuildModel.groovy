package codeOrchestra.colt.as.model.beans

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
class ProductionBuildModel implements IModelElement {
    String outputPath = ""
    boolean compression = false
    boolean optimization = false

    ProductionBuildModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                outputPath(),
                compression(),
                optimization()
        )
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'output-path'(PathUtils.makeRelative(outputPath, project))
            compress(compression)
            optimize(optimization)
        }
    }

    @Override
    void buildModel(Object node) {
        outputPath = PathUtils.makeAbsolute(node.'output-path'?.toString())
        compression = node.compress == "true"
        optimization = node.optimize == "true"
    }
}
