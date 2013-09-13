package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.model.beans.LauncherModel
import codeOrchestra.colt.as.model.beans.LiveSettingsModel
import codeOrchestra.colt.as.model.beans.SettingsModel
import codeOrchestra.colt.as.run.LauncherType
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.ProjectLiveSettings

/**
 * @author Dima Kruk
 */
class AsProjectLiveSettings extends ProjectLiveSettings<AsProject>{

    public final SettingsModel settingsModel = new SettingsModel()
    public final LauncherModel launcherModel = new LauncherModel()
    public final LiveSettingsModel liveSettingsModel = new LiveSettingsModel()

    public LauncherType getLauncherType() {
        try {
            return LauncherType.valueOf(launcherModel.launcherType);
        } catch (IllegalArgumentException e) {
            return LauncherType.DEFAULT;
        }
    }

    public void setLauncherType(LauncherType launcherType) {
        launcherModel.launcherType = launcherType.name();
    }

    public String getFlashPlayerPath() {
        return launcherModel.flashPlayerPath;
    }

    public void setFlashPlayerPath(String flashPlayerPath) {
        launcherModel.flashPlayerPath = flashPlayerPath;
    }

    public LiveMethods getLiveMethods() {
        return LiveMethods.parseValue(liveSettingsModel.liveType);
    }

    public void setLiveMethods(LiveMethods liveMethods) {
        liveSettingsModel.liveType = liveMethods.preferenceValue;
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
        return liveSettingsModel.maxLoop
    }

    @Override
    Closure buildXml(Project project) {
        return {
            settings(settingsModel.buildXml(project))
            launch(launcherModel.buildXml(project))
            live(liveSettingsModel.buildXml(project))
        }
    }

    @Override
    void buildModel(Object node) {
        settingsModel.buildModel(node.settings)
        launcherModel.buildModel(node.launch)
        liveSettingsModel.buildModel(node.live)
    }
}
