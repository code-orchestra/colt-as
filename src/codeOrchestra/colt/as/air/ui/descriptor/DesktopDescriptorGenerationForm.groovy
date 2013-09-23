package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel
import codeOrchestra.colt.as.air.util.DescriptorConverter

/**
 * @author Dima Kruk
 */
class DesktopDescriptorGenerationForm extends DescriptorGenerationForm {

    DesktopDescriptorGenerationForm(AirModel model) {
        super(model)
        root.children.remove(options)
    }

    @Override
    protected String generateTemplate(File outFile) {
        DescriptorConverter.makeTemplateForDesktop(descriptorModel, outFile)
        return outFile.path
    }
}
