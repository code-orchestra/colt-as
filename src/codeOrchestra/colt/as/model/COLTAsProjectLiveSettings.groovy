package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.model.beans.LauncherModel
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.core.model.COLTProjectLiveSettings

/**
 * @author Dima Kruk
 */
class COLTAsProjectLiveSettings extends COLTProjectLiveSettings<COLTAsProject>{

    public final SettingsModel settingsModel = new SettingsModel()
    public final LauncherModel launcherModel = new LauncherModel()
    public final LiveSettingsModel liveSettingsModel = new LiveSettingsModel()

    void clear() {
        settingsModel.clear()
        launcherModel.clear()
        liveSettingsModel.clear()
    }

    public LauncherType getLauncherType() {
        try {
            return LauncherType.valueOf(launcherModel.launcherType);
        } catch (IllegalArgumentException e) {
            return LauncherType.DEFAULT;
        }
    }

    public String getFlashPlayerPath() {
        return launcherModel.flashPlayerPath;
    }

    public void setFlashPlayerPath(String flashPlayerPath) {
        launcherModel.flashPlayerPath = flashPlayerPath;
    }

    public String getWebAddress() {
        // TODO: implement
        return null;
    }

    public LiveMethods getLiveMethods() {
        return LiveMethods.parseValue(liveSettingsModel.liveType);
    }

    public boolean clearMessagesOnSessionStart() {
        return settingsModel.clearLog;
    }

    public boolean disconnectOnTimeout() {
        return settingsModel.disconnectOnTimeout;
    }

    public boolean startSessionPaused() {
        return liveSettingsModel.startSessionPaused;
    }

    public boolean makeGettersSettersLive() {
        return liveSettingsModel.makeGSLive;
    }

    public int getMaxIterationsCount() {
        try {
            return Integer.valueOf(liveSettingsModel.maxLoop)
        } catch (Throwable t) {
            return 1000;
        }
    }

    @Override
    Closure buildXml() {
        return {
            settings(settingsModel.buildXml())
            launch(launcherModel.buildXml())
            live(liveSettingsModel.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        settingsModel.buildModel(node.settings)
        launcherModel.buildModel(node.launch)
        liveSettingsModel.buildModel(node.live)
    }
}
