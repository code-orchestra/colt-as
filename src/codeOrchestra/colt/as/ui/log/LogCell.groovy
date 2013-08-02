package codeOrchestra.colt.as.ui.log

import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

import static codeOrchestra.colt.as.ui.log.Level.*

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
                case FATAL:
                case ERROR:
                    style = "error"; break
                case WARN:
                    style = "warning"; break
                case INFO:
                    style = "info"; break
            }
            label.text = item.getMessageText(false)
            setGraphic(hBox)
            hBox.styleClass.add(style)
        }
    }
}
