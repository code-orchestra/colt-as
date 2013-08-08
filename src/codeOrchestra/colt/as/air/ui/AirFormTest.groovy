package codeOrchestra.colt.as.air.ui

import codeOrchestra.colt.as.air.ui.ios.IOSAirFormCntroller
import codeOrchestra.colt.as.model.COLTAsProject
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window

/**
 * @author Dima Kruk
 */
@SuppressWarnings(["GroovyAssignabilityCheck", "GroovyUnusedDeclaration"])
class AirFormTest extends Application {

    @Override
    void start(Stage primaryStage) throws Exception {

        Parent root = new AnchorPane();
        Button button = new Button("gen")
        root.children.add(button)
        button.onAction = {
            showDialog(button.scene.window)
        } as EventHandler
        primaryStage.setTitle("COLT 1.1");
        Scene scene = new Scene(root, 800, 700)
        primaryStage.setScene(scene);
        primaryStage.show();

        initModel()
    }

    void showDialog(Window window) {
        COLTAsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project


        FXMLLoader loader = new FXMLLoader(AirFormController.class.getResource("air_form.fxml"))
        IOSAirFormCntroller controller = new IOSAirFormCntroller()
        loader.setController(controller)

        VBox page = loader.load()

        Stage dialogStage = new Stage()
        dialogStage.title = "IOS"
        dialogStage.initModality(Modality.WINDOW_MODAL)
        dialogStage.initOwner(window)
        dialogStage.scene = new Scene(page)

        controller.setDialogStage(dialogStage)
        controller.initViewWithModel(project.getProjectBuildSettings().runTargetModel)

        dialogStage.showAndWait()
    }

    void initModel() {
        codeOrchestra.colt.as.model.ModelStorage.instance.load("<xml projectName='FeathersUI' projectType='AS'>\n" +
                "  <paths>\n" +
                "    <sources-list />\n" +
                "    <libraries-list />\n" +
                "    <assets-list />\n" +
                "    <html-template></html-template>\n" +
                "  </paths>\n" +
                "  <build>\n" +
                "    <sdk>\n" +
                "      <sdk-path></sdk-path>\n" +
                "      <use-flex>false</use-flex>\n" +
                "      <use-custom>false</use-custom>\n" +
                "      <custom-config></custom-config>\n" +
                "    </sdk>\n" +
                "    <build>\n" +
                "      <main-class></main-class>\n" +
                "      <output-name>FeathersUI.swf</output-name>\n" +
                "      <output-path>/Users/dimakruk/Documents/COLT/FeathersUI/colt_output</output-path>\n" +
                "      <player-version></player-version>\n" +
                "      <is-rsl>false</is-rsl>\n" +
                "      <custom-locale>false</custom-locale>\n" +
                "      <locale></locale>\n" +
                "      <is-exclude>false</is-exclude>\n" +
                "      <is-interrupt>false</is-interrupt>\n" +
                "      <interrupt-value>30</interrupt-value>\n" +
                "      <compiler-options></compiler-options>\n" +
                "    </build>\n" +
                "    <production>\n" +
                "      <compress>false</compress>\n" +
                "      <optimize>false</optimize>\n" +
                "    </production>\n" +
                "    <run-target>\n" +
                "      <run-target>AIR_IOS</run-target>\n" +
                "      <http-index></http-index>\n" +
                "      <ios-script path=''>\n" +
                "        <sdk-path>/Users/dimakruk/Dev/FDT/sdks/flex_sdk_4.6</sdk-path>\n" +
                "        <provision-path>/Users/dimakruk/Documents/xCode/digitalizm_Provisioning.mobileprovision</provision-path>\n" +
                "        <keystore-path>/Users/dimakruk/Documents/xCode/dimakruk.p12</keystore-path>\n" +
                "        <pass>12345</pass>\n" +
                "      </ios-script>\n" +
                "      <android-script path=''>\n" +
                "        <sdk-path></sdk-path>\n" +
                "        <provision-path></provision-path>\n" +
                "        <keystore-path></keystore-path>\n" +
                "        <pass></pass>\n" +
                "      </android-script>\n" +
                "    </run-target>\n" +
                "  </build>\n" +
                "  <live>\n" +
                "    <settings>\n" +
                "      <clear-log>false</clear-log>\n" +
                "      <disconnect>false</disconnect>\n" +
                "    </settings>\n" +
                "    <launch>\n" +
                "      <launcher>DEFAULT</launcher>\n" +
                "      <player-path></player-path>\n" +
                "    </launch>\n" +
                "    <live>\n" +
                "      <live-type>annotated</live-type>\n" +
                "      <paused>false</paused>\n" +
                "      <make-gs-live>false</make-gs-live>\n" +
                "      <max-loop>1000</max-loop>\n" +
                "    </live>\n" +
                "  </live>\n" +
                "</xml>")
    }

    public static void main(String[] args) {
        launch(AirFormTest, args);
    }
}
