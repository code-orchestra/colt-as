package codeOrchestra.colt.as.ui.log

import javafx.scene.image.Image

/**
 * @author Dima Kruk
 */
public enum Level {

    OFF,
    FATAL,
    ERROR(new Image(Level.class.getResource("../images/messages/error.png").toExternalForm())),
    WARN(new Image(Level.class.getResource("../images/messages/warning.png").toExternalForm())),
    INFO(new Image(Level.class.getResource("../images/messages/information.png").toExternalForm())),
    DEBUG(new Image(Level.class.getResource("../images/messages/debug_gray.png").toExternalForm())),
    TRACE,
    ALL

    private Image image

    private Level() {

    }

    private Level(Image image) {
        this.image = image
    }

    public Image getImage() {
        return image
    }

}