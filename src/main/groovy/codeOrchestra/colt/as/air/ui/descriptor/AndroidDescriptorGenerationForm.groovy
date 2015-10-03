package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.as.air.util.DescriptorConverter
import codeOrchestra.colt.as.model.beans.air.AndroidAirModel
import codeOrchestra.colt.as.model.beans.air.descriptor.AndroidDescriptorModel

/**
 * @author Dima Kruk
 */
class AndroidDescriptorGenerationForm extends DescriptorGenerationForm {
    AndroidDescriptorModel airModel
    AndroidDescriptorGenerationForm(AirModel model) {
        super(model)
        airModel = (model as AndroidAirModel).additionalDescriptorModel
    }

    @Override
    protected String generateTemplate(File outFile) {
        DescriptorConverter.makeTemplateForAndroid(descriptorModel, outFile)
        return outFile.path
    }
}
