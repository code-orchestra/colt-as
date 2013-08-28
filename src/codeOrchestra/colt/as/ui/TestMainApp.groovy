package codeOrchestra.colt.as.ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.scenicview.ScenicView

/**
 * @author Dima Kruk
 */
class TestMainApp extends Application {
    @Override
    void start(Stage primaryStage) throws Exception {
        //new groovy.ui.Console().run()
        Parent root = new ASApplicationGUI();
        primaryStage.setTitle("COLT — Code Orchestra Livecoding Tool");
        Scene scene = new Scene(root, 506, 820)
        primaryStage.setScene(scene);
        ScenicView.show(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(TestMainApp, args);
    }
}
