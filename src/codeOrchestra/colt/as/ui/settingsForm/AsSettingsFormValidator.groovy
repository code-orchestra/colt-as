package codeOrchestra.colt.as.ui.settingsForm

import codeOrchestra.colt.as.model.ModelStorage
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.scene.Node as FXNode
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import javafx.util.Duration

/**
 * @author Dima Kruk
 */
@Singleton
class AsSettingsFormValidator {
    AsSettingsForm form

    void init(AsSettingsForm form) {
        this.form = form
    }

    void validateForSDKPath() {
        scrollTo(form.sdkSettings)
//        if (!ModelStorage.instance.project.projectBuildSettings.sdkModel.isValidFlexSDK) {
//            scrollTo(form.sdkSettings)
//        }
    }

    private scrollTo(FXNode node) {
        form.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        if (form.separator.close) {
            form.separator.close = false
        }
        Timeline timeline = new Timeline(new KeyFrame(new Duration(50), {
            Bounds bounds = form.content.boundsInLocal
            Bounds parentBounds = node.boundsInParent
            form.vvalue = parentBounds.maxY/(bounds.height - form.height)
        } as EventHandler));
        timeline.play();
    }
}
