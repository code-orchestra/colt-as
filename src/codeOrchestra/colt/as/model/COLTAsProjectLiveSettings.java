package codeOrchestra.colt.as.model;

import codeOrchestra.colt.as.run.LauncherType;
import codeOrchestra.colt.as.run.LiveMethods;
import codeOrchestra.colt.as.run.Target;
import codeOrchestra.colt.core.model.COLTProjectLiveSettings;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsProjectLiveSettings extends COLTProjectLiveSettings<COLTAsProject> {

    private static final int DEFAULT_MAX_LOOPS_COUNT = 10000;

    public LauncherType getLauncherType() {
        // TODO: implement
        return null;
    }

    public void setLauncherType(LauncherType flashPlayer) {
//        getPreferenceStore().setValue("flashPlayerLauncher", flashPlayer == LauncherType.FLASH_PLAYER);
        // TODO: implement
    }

    public String getFlashPlayerPath() {
        // TODO: implement
        return null;
    }

    public void setFlashPlayerPath(String flashPlayerPath) {
        // TODO: implement
//        getPreferenceStore().setValue("flashPlayerPath", flashPlayerPath);
    }

    public String getWebAddress() {
        // TODO: implement
        return null;
    }

    public LiveMethods getLiveMethods() {
        // TODO: implement
//        String liveMethodsStringValue = getPreferenceStore().getString("liveMethods");
//        if (liveMethodsStringValue == null) {
//            return LiveMethods.ANNOTATED;
//        }
//
//        return LiveMethods.parseValue(liveMethodsStringValue);
        return null;
    }

    public Target getLaunchTarget() {
        // TODO: implement
//        return Target.parse(getPreferenceStore().getString("target"));
        return null;
    }

    public boolean clearMessagesOnSessionStart() {
        // TODO: implement
        return false;
    }

    public boolean disconnectOnTimeout() {
        // TODO: implement
        return false;
    }

    public boolean startSessionPaused() {
        // TODO: implement
        return false;
    }

    public boolean makeGettersSettersLive() {
        // TODO: implement
        return false;
    }

    public int getMaxIterationsCount() {
//        String maxLoopsStr = getPreferenceStore().getString("maxLoopIterations");
//        if (maxLoopsStr == null) {
//            return DEFAULT_MAX_LOOPS_COUNT;
//        }
//
//        return Integer.valueOf(maxLoopsStr);
        // TODO: implement
        return 0;
    }

}
