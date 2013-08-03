package codeOrchestra.colt.as.ui.log

import com.sun.javafx.scene.control.skin.VirtualFlow
import javafx.geometry.Pos
import javafx.scene.control.Hyperlink
import javafx.scene.control.ListCell
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

import static codeOrchestra.colt.as.ui.log.Level.*

/**
 * @author Dima Kruk
 * @author Eugene Potapenko
 */
class LogCell extends ListCell<LogMessage> {
    HBox root = new HBox()
    Text logText = new Text(id: "log-text")
    TextFlow logTextPane = new TextFlow(logText)
    Rectangle spacer = new Rectangle()
    Hyperlink hyperlink = new Hyperlink(alignment: Pos.TOP_RIGHT, minWidth: 150, maxWidth: 150, tooltip: new Tooltip())
    private LogMessage logMessage

    LogCell() {
        super()
        root.children.addAll(logTextPane, spacer, hyperlink)
        HBox.setHgrow(spacer, Priority.ALWAYS)
    }

    @Override
    protected void updateItem(LogMessage logMessage, boolean b) {
        super.updateItem(logMessage, b)
        text = null
        if (empty) {
            setGraphic(null)
            this.logMessage = null
        } else{
            if (this.logMessage != logMessage) {
                this.logMessage = logMessage
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
                hyperlink.tooltip.text = item.source
                styleClass.add(style)
            }
            setGraphic(root)
            if (parent?.parent?.parent instanceof VirtualFlow) {
                logTextPane.maxWidthProperty().bind((parent?.parent?.parent as VirtualFlow).widthProperty().add(-220))
            }
        }
    }
}
