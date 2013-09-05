package codeOrchestra.colt.as.ui.settingsForm

import codeOrchestra.colt.as.model.ModelStorage
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.scene.Node as FXNode
import javafx.scene.control.ScrollPane
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
        if (!ModelStorage.instance.project.projectBuildSettings.sdkModel.isValidFlexSDK) {
            scrollTo(form.sdkSettings)
        }
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
