package codeOrchestra.colt.as.ui

import com.aquafx_project.AquaFx
import com.aquafx_project.controls.skin.AquaSkin
import com.aquafx_project.controls.skin.styles.styler.ScrollBarStyler
import com.sun.javafx.stage.FocusUngrabEvent
import javafx.application.Application
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.InputMethodEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import org.scenicview.ScenicView

/**
 * @author Dima Kruk
 */
class TestMainApp extends Application {
    @Override
    void start(Stage primaryStage) throws Exception {
        //new groovy.ui.Console().run()
        Parent root = FXMLLoader.load(getClass().getResource("main_app.fxml"));
        primaryStage.setTitle("COLT 1.1");
        Scene scene = new Scene(root, 800, 700)
        primaryStage.setScene(scene);
        //ScenicView.show(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(TestMainApp, args);
    }
}
