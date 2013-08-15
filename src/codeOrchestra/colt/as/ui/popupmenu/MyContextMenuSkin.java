package codeOrchestra.colt.as.ui.popupmenu;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.EmbeddedTextContextMenuContent;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.application.ConditionalFeature;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

/**
 * @author Dima Kruk
 */
public class MyContextMenuSkin implements Skin<MyContextMenu> {
    private MyContextMenu popupMenu;

    private final Pane mainRoot = new Pane();
    private final Pane pane = new Pane();
    private final Pane innerPane = new Pane();
    private final Region root;

    // Fix for RT-18247
    private final EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override public void handle(KeyEvent event) {
            if (event.getEventType() != KeyEvent.KEY_PRESSED) return;

            // We only care if the root container still has focus
            if (! root.isFocused()) return;

            final KeyCode code = event.getCode();
            switch (code) {
                case ENTER:
                case SPACE: popupMenu.hide(); return;
                default:    return;
            }
        }
    };

    public MyContextMenuSkin(final MyContextMenu popupMenu) {
        this.popupMenu = popupMenu;

        // When a contextMenu is shown, requestFocus on its content to enable
        // keyboard navigation.
        popupMenu.addEventHandler(Menu.ON_SHOWN, new EventHandler<Event>() {
            @Override public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) cmContent.requestFocus();

                root.addEventHandler(KeyEvent.KEY_PRESSED, keyListener);
            }
        });
        popupMenu.addEventHandler(Menu.ON_HIDDEN, new EventHandler<Event>() {
            @Override public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) cmContent.requestFocus();

                root.removeEventHandler(KeyEvent.KEY_PRESSED, keyListener);
            }
        });

        if (PlatformImpl.isSupported(ConditionalFeature.INPUT_TOUCH) &&
                popupMenu.getStyleClass().contains("text-input-context-menu")) {
            root = new EmbeddedTextContextMenuContent(popupMenu);
        } else {
            root = new ContextMenuContent(popupMenu);
        }
        root.idProperty().bind(popupMenu.idProperty());
        root.styleProperty().bind(popupMenu.styleProperty());
        root.getStyleClass().addAll(popupMenu.getStyleClass()); // TODO needs to handle updates

        /*
        ** only add this if we're on an embedded
        ** platform that supports 5-button navigation
        */
        if (Utils.isTwoLevelFocus()) {
            new TwoLevelFocusPopupBehavior(popupMenu); // needs to be last.
        }

        VBox itemsContainer = ((ContextMenuContent) root).getItemsContainer();
        innerPane.prefWidthProperty().bind(itemsContainer.widthProperty().add(-55));
        innerPane.prefHeightProperty().bind(itemsContainer.heightProperty().add(-20));
        pane.setLayoutX(-20);
        pane.setLayoutY(-20);
        pane.getChildren().add(innerPane);

        pane.getStyleClass().add("popup-bl");

        mainRoot.getChildren().add(pane);
        mainRoot.getChildren().add(root);
    }

    @Override public MyContextMenu getSkinnable() {
        return popupMenu;
    }

    @Override public Node getNode() {
        return mainRoot;
    }

    @Override public void dispose() {
        innerPane.prefWidthProperty().unbind();
        innerPane.prefHeightProperty().unbind();
        root.idProperty().unbind();
        root.styleProperty().unbind();
    }
}
