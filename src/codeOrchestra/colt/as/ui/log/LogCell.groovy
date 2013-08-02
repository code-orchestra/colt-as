package codeOrchestra.colt.as.ui.log

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.layout.Border
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.scene.web.WebView

/**
 * @author Dima Kruk
 */
class LogCell extends ListCell<LogMessage> {
    HBox hBox = new HBox()
    Label label = new Label()
    Hyperlink hyperlink = new Hyperlink()

    LogMessage lastItem

    LogCell() {
        super()
        label.wrapText = true
        hBox.styleClass.add("li")
        hBox.children.addAll(label, hyperlink)
        HBox.setHgrow(label, Priority.ALWAYS)
    }

    @Override
    protected void updateItem(LogMessage t, boolean b) {
        super.updateItem(t, b)

        text = null
        if (empty) {
            lastItem = null
            graphic = null
        } else {
            lastItem = t
            String style = ""
            switch (item.level){
                case codeOrchestra.colt.as.ui.log.Level.FATAL:
                case codeOrchestra.colt.as.ui.log.Level.ERROR:
                    style = "error"
                    break
                case codeOrchestra.colt.as.ui.log.Level.WARN:
                    style = "warning"
                    break
                case codeOrchestra.colt.as.ui.log.Level.INFO:
                    style = "info"
                    break
                case codeOrchestra.colt.as.ui.log.Level.OFF:
                case codeOrchestra.colt.as.ui.log.Level.DEBUG:
                case codeOrchestra.colt.as.ui.log.Level.TRACE:
                case codeOrchestra.colt.as.ui.log.Level.ALL:
                    break
            }
            label.text = item.getMessageText(false)
            setGraphic(hBox)
            hBox.styleClass.add(style)
        }
    }
}
