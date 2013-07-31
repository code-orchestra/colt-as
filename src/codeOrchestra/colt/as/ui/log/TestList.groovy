package codeOrchestra.colt.as.ui.log

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.util.Callback
import javafx.collections.ObservableList as FXObservableList

/**
 * @author Dima Kruk
 */
class TestList extends Application {
    @Override
    void start(Stage primaryStage) throws Exception {
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);

        FXObservableList<LogMessage> list = FXCollections.observableArrayList(
                new LogMessage("", Level.DEBUG, "debug", 10, ""),
                new LogMessage("", Level.ALL, "debug", 10, ""),
                new LogMessage("", Level.FATAL, "debug", 10, ""),
                new LogMessage("", Level.ERROR, "error", 10, ""),
                new LogMessage("", Level.INFO, "info", 10, ""),
                new LogMessage("", Level.TRACE, "info", 10, ""),
                new LogMessage("", Level.WARN, "warn", 10, ""),
        )
        ListView<LogMessage> lv = new ListView<>(list)
        lv.cellFactory = new Callback<ListView<LogMessage>, ListCell<LogMessage>>() {
            @Override
            ListCell<LogMessage> call(ListView<LogMessage> p) {
                return new LogCell()
            }
        }
        pane.children.add(lv)
        primaryStage.show()
    }

    public static void main(String[] args) {
        Application.launch(TestList, args);
    }
}
