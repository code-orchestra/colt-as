package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.as.model.beans.air.descriptor.DescriptorModel
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
abstract class AirModel {
    String keystorePath = ""
    String storePass = ""

    boolean useCustomTemplate = true
    String templatePath = ""

    DescriptorModel descriptorModel = new DescriptorModel()
}
