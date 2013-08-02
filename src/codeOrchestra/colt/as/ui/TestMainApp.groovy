package codeOrchestra.colt.as.ui

import com.aquafx_project.AquaFx
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Dima Kruk
 */
class TestMainApp extends Application {
    @Override
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_app.fxml"));
        primaryStage.setTitle("COLT 1.1");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();


    }

    public static void main(String[] args) {
        Application.launch(TestMainApp, args);
//        new groovy.ui.Console().run()
    }
}
