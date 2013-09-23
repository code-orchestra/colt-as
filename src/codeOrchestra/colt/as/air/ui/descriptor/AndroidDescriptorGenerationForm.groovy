package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.as.air.util.DescriptorConverter

/**
 * @author Dima Kruk
 */
class AndroidDescriptorGenerationForm extends DescriptorGenerationForm {
    AndroidDescriptorGenerationForm(AirModel model) {
        super(model)
    }

    @Override
    protected String generateTemplate(File outFile) {
        DescriptorConverter.makeTemplate(descriptorModel, outFile)
        return outFile.path
    }
}
