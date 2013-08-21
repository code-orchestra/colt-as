package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.components.CTBForm
import codeOrchestra.colt.as.ui.components.LTBForm
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class BuildSettingsFormController implements Initializable {

    @FXML LTBForm mainClass
    @FXML LTBForm fileName
    @FXML LTBForm outPath

    @FXML ChoiceBox playerVersionCB
    @FXML Label errorLabel

    @FXML CTBForm rsl
    @FXML CTBForm locale
    @FXML CTBForm exclude
    @FXML CTBForm interrupt

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
        mainClass.extensionFilters.addAll(new FileChooser.ExtensionFilter("AS", "*.as"), new FileChooser.ExtensionFilter("MXML", "*.mxml"))

        errorLabel.visible = false

        bindModel()
    }

    void bindModel() {
        mainClass.textField.textProperty().bindBidirectional(model.mainClass())
        fileName.textField.textProperty().bindBidirectional(model.outputFileName())
        outPath.textField.textProperty().bindBidirectional(model.outputPath())

        playerVersionCB.valueProperty().bindBidirectional(model.targetPlayerVersion())
        sdkModel.isValidFlexSDK().addListener({ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue->
            playerVersionCB.items.clear()
            if (newValue) {
                FlexSDKManager manager = FlexSDKManager.instance
                List<String> versions = manager.getAvailablePlayerVersions(new File(sdkModel.flexSDKPath))
                playerVersionCB.items.addAll(versions)
                if (!model.targetPlayerVersion) {
                    playerVersionCB.value = versions.first()
                }
            }
            errorLabel.visible = !newValue
        } as ChangeListener<Boolean>)

        rsl.checkBox.selectedProperty().bindBidirectional(model.rsl())

        locale.checkBox.selectedProperty().bindBidirectional(model.nonDefaultLocale())
        locale.textField.textProperty().bindBidirectional(model.localeSettings())

        exclude.checkBox.selectedProperty().bindBidirectional(model.excludeDeadCode())

        interrupt.checkBox.selectedProperty().bindBidirectional(model.interrupt())
        interrupt.textField.textProperty().bindBidirectional(model.interruptValue())
    }
}
