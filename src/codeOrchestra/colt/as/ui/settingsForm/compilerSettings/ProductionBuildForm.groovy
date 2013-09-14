package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxActionInput
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup

/**
 * @author Dima Kruk
 */
class ProductionBuildForm extends FormGroup {

    private LabeledActionInput outPath
    private CheckBoxInput compression
    private CheckBoxInput optimization

    private ProductionBuildModel model = ModelStorage.instance.project.projectBuildSettings.productionBuildModel

    ProductionBuildForm() {
        outPath = new LabeledActionInput(title: "Output path:", browseType: BrowseType.DIRECTORY, shortPathForProject: ModelStorage.instance.project)
        compression = new CheckBoxInput(title: "SWF compression")
        optimization = new CheckBoxInput(title: "Compiling optimization")

        children.addAll(outPath, compression, optimization)

        init()
    }

    void init() {
        bindModel()
    }

    void bindModel() {
        outPath.text().bindBidirectional(model.outputPath())
        compression.selected().bindBidirectional(model.compression())
        optimization.selected().bindBidirectional(model.optimization())
    }
}
