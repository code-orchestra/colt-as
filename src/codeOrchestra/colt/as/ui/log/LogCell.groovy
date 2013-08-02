package codeOrchestra.colt.as.ui.log

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.text.Text

import static codeOrchestra.colt.as.ui.log.Level.*

/**
 * @author Dima Kruk
 */
class LogCell extends ListCell<LogMessage> {
    HBox root = new HBox()
    Text logText = new Text()
    Pane logTextPane = new Pane()
    Hyperlink hyperlink = new Hyperlink()
    LogMessage lastItem

    LogCell() {
        super()
        hyperlink.alignment = Pos.TOP_RIGHT
        hyperlink.maxWidth = 150
        hyperlink.minWidth = 150
        logTextPane.children.add(logText)
        root.children.addAll(logTextPane, hyperlink)
        hyperlink.alignment = Pos.TOP_LEFT
        HBox.setHgrow(logTextPane, Priority.ALWAYS)
        logTextPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                println "newValue = $newValue"
                logText.wrappingWidth = newValue - 20
            }
        })
        logText.setId("log-text")
    }

    @Override
    protected void updateItem(LogMessage logMessage, boolean b) {
        super.updateItem(logMessage, b)

        text = null
        if (empty) {
            lastItem = null
            graphic = null
        } else {
            lastItem = logMessage
            String style = ""
            switch (item.level) {
                case FATAL:
                case ERROR:
                    style = "error"; break
                case WARN:
                    style = "warning"; break
                case INFO:
                    style = "info"; break
            }
            logText.text = item.getMessage()
            hyperlink.text = item.source
            logText.wrappingWidth = Math.max(400, logTextPane.width - 20)
            styleClass.add(style)
            setGraphic(root)
        }
    }
}
