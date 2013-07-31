package codeOrchestra.colt.as.ui.log

import javafx.collections.FXCollections
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.Node as FXNode
import javafx.collections.ObservableList as FXObservableList
import javafx.scene.layout.StackPane
import javafx.util.Callback

/**
 * @author Dima Kruk
 */
class Log {
    ListView<LogMessage> lv

    public FXNode getPane() {
        if (!lv) {
            FXObservableList<LogMessage> list = FXCollections.observableArrayList(
                    new LogMessage("", Level.DEBUG, "debug", 10, ""),
                    new LogMessage("", Level.ALL, "debug", 10, ""),
                    new LogMessage("", Level.FATAL, "debug", 10, ""),
                    new LogMessage("", Level.ERROR, "error", 10, ""),
                    new LogMessage("", Level.INFO, "info", 10, ""),
                    new LogMessage("", Level.TRACE, "info", 10, ""),
                    new LogMessage("", Level.WARN, "warn", 10, ""),
            )
            lv = new ListView<>(list)
            lv.cellFactory = new Callback<ListView<LogMessage>, ListCell<LogMessage>>() {
                @Override
                ListCell<LogMessage> call(ListView<LogMessage> p) {
                    return new LogCell()
                }
            }
        }
        return lv
    }
}
