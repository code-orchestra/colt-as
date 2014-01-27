package codeOrchestra.colt.as.ui.dialog

import codeOrchestra.colt.core.ui.dialog.UpdateDialog
import codeOrchestra.colt.core.update.tasks.UpdateTask
import codeOrchestra.util.FileUtils
import codeOrchestra.util.PathUtils
import codeOrchestra.util.SystemInfo
import javafx.stage.Window

/**
 * @author Dima Kruk
 */
class InstallFlexSDKDialog extends UpdateDialog {

    boolean inited = false

    InstallFlexSDKDialog(Window owner) {
        super(owner)

        if (SystemInfo.isMac) {
            task = new UpdateTask("http://codeorchestra.s3.amazonaws.com/colt_packages/flex_sdk_mac.zip",
                    new File(PathUtils.getApplicationBaseDir(), "flex_sdk").getPath())
        } else if (SystemInfo.isWindows) {
            task = new UpdateTask("http://codeorchestra.s3.amazonaws.com/colt_packages/flex_sdk_win.zip",
                    new File(PathUtils.getApplicationBaseDir(), "flex_sdk").getPath())
        } else {
            throw new IllegalStateException("Unsupported OS: " + System.getProperty("os.name"));
        }
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
        if (SystemInfo.isWindows) {
            File file = new File(PathUtils.getApplicationBaseDir().path + File.separator + "flex_sdk" + File.separator + "bin" + File.separator + "placement.txt")
            String str = 'Root="' + PathUtils.getApplicationBaseDir().path + File.separator + "bin" + '"'
            FileUtils.write(file, str)
        }
        inited = true
        super.updateComplete()
    }

    @Override
    protected void startUpdate() {
        if (!inited) {
            super.startUpdate()
        } else {
            codeOrchestra.colt.as.model.ModelStorage.instance.project.projectBuildSettings.sdkModel.isValidFlexSDK = true
            isSuccess = true
            stage.hide()
        }
    }
}
