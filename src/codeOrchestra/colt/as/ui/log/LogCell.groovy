package codeOrchestra.colt.as.ui.log

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextArea
import javafx.scene.image.ImageView
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
    ImageView imageView = new ImageView()
    Label label = new Label()
    TextFlow textFlow = new TextFlow()
    Hyperlink hyperlink = new Hyperlink()

    LogMessage lastItem

    LogCell() {
        super()
        imageView.fitHeight = 16
        imageView.fitWidth = 16

        label.wrapText = true

        hBox.spacing = 5
        hBox.children.addAll(imageView, label, hyperlink)

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
            imageView.image = item.level.image
            label.text = item.getMessageText(false)
            setGraphic(hBox)
        }
    }
}
