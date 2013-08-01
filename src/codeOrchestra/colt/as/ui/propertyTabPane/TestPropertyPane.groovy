package codeOrchestra.colt.as.ui.propertyTabPane

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TestPropertyPane extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("property_tab_pane.fxml"));
        primaryStage.setTitle("COLT 1.1");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();

        //AquaFx.style()
    }

    public static void main(String[] args) {
        Application.launch(TestPropertyPane, args);
    }
}
