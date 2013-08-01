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
        ModelStorage.getInstance().load("<xml projectName='' projectType=''>\n" +
                "  <paths>\n" +
                "    <sources-list>\n" +
                "      <item>/Users/dimakruk/Documents/COLT/Starling/LiveUI/source/feathers</item>\n" +
                "    </sources-list>\n" +
                "    <libraries-list />\n" +
                "    <assets-list>\n" +
                "      <item>/Users/dimakruk/Documents/COLT/Starling/LiveUI/source/feathers</item>\n" +
                "    </assets-list>\n" +
                "    <html-template />\n" +
                "  </paths>\n" +
                "  <build>\n" +
                "    <sdk>\n" +
                "      <sdk-path>sdf</sdk-path>\n" +
                "      <use-flex>false</use-flex>\n" +
                "      <use-custom>true</use-custom>\n" +
                "      <custom-config>sdf</custom-config>\n" +
                "    </sdk>\n" +
                "    <build>\n" +
                "      <main-class>f</main-class>\n" +
                "      <output-name />\n" +
                "      <output-path />\n" +
                "      <player-version />\n" +
                "      <is-rsl>true</is-rsl>\n" +
                "      <custom-locale>false</custom-locale>\n" +
                "      <locale />\n" +
                "      <is-exclude>true</is-exclude>\n" +
                "      <is-interrupt>false</is-interrupt>\n" +
                "      <interrupt-value>30</interrupt-value>\n" +
                "      <compiler-options />\n" +
                "    </build>\n" +
                "    <production>\n" +
                "      <compress>false</compress>\n" +
                "      <optimize>true</optimize>\n" +
                "    </production>\n" +
                "    <run-target>\n" +
                "      <run-target>AIR_IOS</run-target>\n" +
                "      <http-index />\n" +
                "      <ios-script>sdf</ios-script>\n" +
                "      <android-script />\n" +
                "    </run-target>\n" +
                "  </build>\n" +
                "  <live>\n" +
                "    <settings>\n" +
                "      <clear-log>true</clear-log>\n" +
                "      <disconnect>false</disconnect>\n" +
                "    </settings>\n" +
                "    <launch>\n" +
                "      <launcher>FLASH_PLAYER</launcher>\n" +
                "      <player-path>sdf</player-path>\n" +
                "    </launch>\n" +
                "    <live>\n" +
                "      <live-type>annotated</live-type>\n" +
                "      <paused>false</paused>\n" +
                "      <make-gs-live>true</make-gs-live>\n" +
                "      <max-loop>1000</max-loop>\n" +
                "    </live>\n" +
                "  </live>\n" +
                "</xml>");
    }
}