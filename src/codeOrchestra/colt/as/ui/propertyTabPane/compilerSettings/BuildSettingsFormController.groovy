package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.ui.components.inputForms.CBForm
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class BuildSettingsFormController implements Initializable {

//    @FXML LTBForm mainClass
    @FXML LTBForm fileName
    @FXML LTBForm outPath

    @FXML CBForm player

    @FXML CTBForm rsl
    @FXML CTBForm locale
    @FXML CTBForm exclude
    @FXML CTBForm interrupt

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
//        mainClass.extensionFilters.addAll(new FileChooser.ExtensionFilter("AS", "*.as"), new FileChooser.ExtensionFilter("MXML", "*.mxml"))

        player.errorLabel.visible = false

        bindModel()
    }

    void bindModel() {
//        mainClass.textField.textProperty().bindBidirectional(model.mainClass())
        fileName.textField.textProperty().bindBidirectional(model.outputFileName())
        outPath.textField.textProperty().bindBidirectional(model.outputPath())

        model.targetPlayerVersion().bindBidirectional(player.choiceBox.valueProperty())
        player.choiceBox.valueProperty().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            if(t1 != null && !t1?.isEmpty()) {
                if(!player.choiceBox.items.contains(t1)) {
                    player.choiceBox.items.add(t1)
                    error(true)
                } else {
                    if(player.errorLabel.visible) {
                        player.choiceBox.items.remove(t)
                    }
                    error(false)
                }
            } else {
                error(false)
            }

        } as ChangeListener)

        sdkModel.isValidFlexSDK().addListener({ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue->
            String value = model.targetPlayerVersion
            player.choiceBox.items.clear()
            if (newValue) {
                FlexSDKManager manager = FlexSDKManager.instance
                List<String> versions = manager.getAvailablePlayerVersions(new File(sdkModel.flexSDKPath))
                player.choiceBox.items.addAll(versions)
                if (versions.size() > 0 && versions.contains(value)) {
                    model.targetPlayerVersion = value
                    error(false)
                } else if(!value.isEmpty()) {
                    player.choiceBox.items.add(value)
                    model.targetPlayerVersion = value
                    error(true)
                }
            } else {
                player.choiceBox.items.add(value)
                model.targetPlayerVersion = value
                error(true)
            }
        } as ChangeListener<Boolean>)

        rsl.checkBox.selectedProperty().bindBidirectional(model.rsl())

        locale.checkBox.selectedProperty().bindBidirectional(model.nonDefaultLocale())
        locale.textField.textProperty().bindBidirectional(model.localeSettings())

        exclude.checkBox.selectedProperty().bindBidirectional(model.excludeDeadCode())

        interrupt.checkBox.selectedProperty().bindBidirectional(model.interrupt())
        interrupt.textField.textProperty().bindBidirectional(model.interruptValue())
    }

    private void error(boolean b) {
        player.errorLabel.visible = b
        player.errorLabel.text = sdkModel.isValidFlexSDK ? "Incorrect player version specified" : "Incorrect Flex SDK path specified"
    }
}
