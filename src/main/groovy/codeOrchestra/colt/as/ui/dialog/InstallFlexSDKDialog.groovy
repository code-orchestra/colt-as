package codeOrchestra.colt.as.ui.dialog
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.ui.dialog.UpdateDialog
import codeOrchestra.colt.core.update.tasks.UpdateTask
import codeOrchestra.util.PathUtils
import codeOrchestra.util.SystemInfo
import javafx.stage.Window

/**
 * @author Dima Kruk
 */
class InstallFlexSDKDialog extends UpdateDialog {

    InstallFlexSDKDialog(Window owner) {
        super(owner)
        String url;
        if (SystemInfo.isMac) {
            url = "https://github.com/code-orchestra/flex-sdk-livecoding/releases/download/4.14.1.18/flex_sdk_mac.tar.gz";
        } else if (SystemInfo.isWindows) {
            url = "https://github.com/code-orchestra/flex-sdk-livecoding/releases/download/4.14.1.18/flex_sdk_win.zip";
        } else {
            throw new IllegalStateException("Unsupported OS: ${System.getProperty("os.name")}");
        }
        task = new UpdateTask(url, new File(PathUtils.getApplicationBaseDir(), "flex_sdk").getPath())
    }

    @Override
    protected void initHeader() {
        super.initHeader()
        message = 'You need modified FlexSDK to work with ActionScript projects'
        comment = ""
    }

    @Override
    protected void initButtons() {
        super.initButtons()
        okButton.text = "Install"
    }

    @Override
    protected void updateComplete() {
        super.updateComplete()
        okButton.text = "Done"
    }

    @Override
    protected void hide() {
        SDKModel model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectBuildSettings.sdkModel
        String baseFlexSDKPath = codeOrchestra.colt.as.util.ASPathUtils.flexSDKPath
        isSuccess = true
        if (model.flexSDKPath == baseFlexSDKPath) {
            model.flexSDKPath = ""
            model.flexSDKPath = baseFlexSDKPath
        }
        super.hide()
    }
}