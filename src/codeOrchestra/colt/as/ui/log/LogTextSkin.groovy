package codeOrchestra.colt.as.ui.log

import com.sun.javafx.scene.control.skin.TextAreaSkin
import javafx.scene.control.TextArea

/**
 * @author Eugene Potapenko
 */
class LogTextSkin extends TextAreaSkin{
    LogTextSkin(TextArea textArea) {
        super(textArea)
    }

    @Override
    void layoutChildren(double v, double v1, double v2, double v3) {
        super.layoutChildren(v, v1, v2, v3)    //To change body of overridden methods use File | Settings | File Templates.
    }
}
