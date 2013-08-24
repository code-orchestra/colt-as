package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class ProductionBuildModel implements IModelElement {
    boolean compression = false
    boolean optimization = false

    ProductionBuildModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                compression(),
                optimization()
        )
    }

    @Override
    Closure buildXml() {
        return {
            compress(compression)
            optimize(optimization)
        }
    }

    @Override
    void buildModel(Object node) {
        compression = node.compress == "true"
        optimization = node.optimize == "true"
    }
}
