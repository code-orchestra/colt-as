package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.CheckBoxInput

/**
 * @author Dima Kruk
 */
class ProductionBuildForm extends FormGroup {

    private CheckBoxInput compression
    private CheckBoxInput optimization

    private ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    ProductionBuildForm() {
        title = "Production build settings"

        compression = new CheckBoxInput(title: "SWF compression")
        optimization = new CheckBoxInput(title: "Compiling optimization")

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
