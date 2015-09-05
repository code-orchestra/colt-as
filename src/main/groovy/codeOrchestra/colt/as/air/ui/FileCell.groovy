package codeOrchestra.colt.as.air.ui

import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox

/**
 * @author Dima Kruk
 */
class FileCell extends ListCell<FileCellBean> {
    HBox root
    CheckBox checkBox
    Label label

    FileCellBean model

    FileCell() {
        root = new HBox()
        root.spacing = 10
        checkBox = new CheckBox()
        checkBox.onAction = {
            model?.checked = checkBox.selected
        } as EventHandler
        label = new Label()
        root.children.addAll(checkBox, label)
    }

    @Override
    protected void updateItem(FileCellBean t, boolean b) {
        super.updateItem(t, b)

        if (empty) {
            setGraphic(null)
            model = null
        } else {
            if (t != model) {
                model = t
                label.text = model.file.name
                checkBox.selected = model.checked
                checkBox.disable = model.required
            }
            setGraphic(root)
        }

    }
}
