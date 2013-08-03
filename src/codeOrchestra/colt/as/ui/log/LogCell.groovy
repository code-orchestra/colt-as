package codeOrchestra.colt.as.ui.log

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.WindowEvent

import static codeOrchestra.colt.as.ui.log.Level.*

/**
 * @author Dima Kruk
 */
class LogCell extends ListCell<LogMessage> {
    HBox root = new HBox()
    Text logText = new Text()
    TextFlow logTextPane = new TextFlow()
    Rectangle spacer = new Rectangle()
    Hyperlink hyperlink = new Hyperlink()
    LogMessage lastItem

    LogCell() {
        super()
        hyperlink.alignment = Pos.TOP_RIGHT
        hyperlink.maxWidth = 150
        hyperlink.minWidth = 150
        logTextPane.children.add(logText)
        root.children.addAll(logTextPane, spacer, hyperlink)
        HBox.setHgrow(spacer, Priority.ALWAYS)
        hyperlink.alignment = Pos.TOP_RIGHT
        root.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                logTextPane.maxWidth = newValue - 150
            }
        })
        root.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            void handle(Event event) {
                println "event = $event"
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
            styleClass.add(style)
            logTextPane.maxWidth = Math.max(400, root.width - 170)
            setGraphic(root)
        }
    }
}
