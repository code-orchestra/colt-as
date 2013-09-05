package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class ProductionBuildForm extends FormGroup {

    private CTBForm compression
    private CTBForm optimization

    private ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    ProductionBuildForm() {
        title = "Production build settings"

        compression = new CTBForm(title: "SWF compression")
        optimization = new CTBForm(title: "Compiling optimization")

        children.addAll(compression, optimization)

        init()
    }

    void init() {
        bindModel()
    }

    void bindModel() {
        compression.selected().bindBidirectional(model.compression())
        optimization.selected().bindBidirectional(model.optimization())
    }
}
