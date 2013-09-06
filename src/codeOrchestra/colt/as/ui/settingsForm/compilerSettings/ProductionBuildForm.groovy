package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.CheckBoxForm

/**
 * @author Dima Kruk
 */
class ProductionBuildForm extends FormGroup {

    private CheckBoxForm compression
    private CheckBoxForm optimization

    private ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    ProductionBuildForm() {
        title = "Production build settings"

        compression = new CheckBoxForm(title: "SWF compression")
        optimization = new CheckBoxForm(title: "Compiling optimization")

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
