package codeOrchestra.colt.as.ui;

import codeOrchestra.colt.as.model.ModelStorage;
import javafx.event.ActionEvent;

/**
 * @author Dima Kruk
 */
public class AppController {
    public void saveHandler(ActionEvent actionEvent) {
        System.out.println("------------------");
        System.out.println(ModelStorage.getInstance().save());
        System.out.println("------------------");
    }

    public void loadHandler(ActionEvent actionEvent) {
        ModelStorage.getInstance().load("<xml>\n" +
                "  <sources-list />\n" +
                "  <libraries-list />\n" +
                "  <assets-list />\n" +
                "  <html-template />\n" +
                "  <sdk>\n" +
                "    <sdk-path />\n" +
                "    <use-flex>false</use-flex>\n" +
                "    <use-custom>false</use-custom>\n" +
                "    <custom-config />\n" +
                "  </sdk>\n" +
                "  <build>\n" +
                "    <main-class />\n" +
                "    <output-name />\n" +
                "    <output-path />\n" +
                "    <player-version />\n" +
                "    <is-rsl>false</is-rsl>\n" +
                "    <custom-locale>false</custom-locale>\n" +
                "    <locale />\n" +
                "    <is-exclude>false</is-exclude>\n" +
                "    <is-interrupt>false</is-interrupt>\n" +
                "    <interrupt-value>30</interrupt-value>\n" +
                "    <compiler-options />\n" +
                "  </build>\n" +
                "  <production>\n" +
                "    <compress>false</compress>\n" +
                "    <optimize>false</optimize>\n" +
                "  </production>\n" +
                "  <settings>\n" +
                "    <clear-log>false</clear-log>\n" +
                "    <disconnect>false</disconnect>\n" +
                "  </settings>\n" +
                "  <run-target>\n" +
                "    <run-target>SWF</run-target>\n" +
                "    <http-index />\n" +
                "    <ios-script />\n" +
                "    <android-script />\n" +
                "  </run-target>\n" +
                "  <launch>\n" +
                "    <launcher>DEFAULT</launcher>\n" +
                "    <player-path />\n" +
                "  </launch>\n" +
                "  <live>\n" +
                "    <live-type>ANNOTATED</live-type>\n" +
                "    <paused>false</paused>\n" +
                "    <make-gs-live>false</make-gs-live>\n" +
                "    <max-loop>1000</max-loop>\n" +
                "  </live>\n" +
                "</xml>");
    }
}
