package codeOrchestra.colt.as.air.ui.descriptor

import codeOrchestra.colt.as.model.beans.air.AirModel

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
        return null
    }
}
