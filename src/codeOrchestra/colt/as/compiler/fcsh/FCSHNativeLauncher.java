package codeOrchestra.colt.as.compiler.fcsh;

import codeOrchestra.colt.as.flex.FlexSDKSettings;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.model.AsProjectBuildSettings;
import codeOrchestra.colt.as.model.AsProjectLiveSettings;
import codeOrchestra.colt.as.run.Target;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.util.LocalhostUtil;

import java.io.File;
import java.util.EnumSet;

/**
 * @author Alexander Eliseyev
 */
public class FCSHNativeLauncher implements IFCSHLauncher {

    public ProcessBuilder createProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder(ASPathUtils.getFlexSDKPath() + File.separator + "bin" + File.separator + "ColtFCSH.exe");

        StringBuilder programParameters = new StringBuilder();

        String applicationHome;
        AsProject currentProject = AsProject.getCurrentProject();
        if (currentProject != null) {
            AsProjectBuildSettings projectBuildSettings = currentProject.getProjectBuildSettings();
            applicationHome = projectBuildSettings.getFlexSDKPath();

            if (!new File(applicationHome).exists()) {
                applicationHome = FlexSDKSettings.getDefaultFlexSDKPath();
            }
        } else {
            applicationHome = FlexSDKSettings.getDefaultFlexSDKPath();
        }

        programParameters.append(protect("-Dapplication.home=" + applicationHome));
        programParameters.append(" -Duser.language=en");
        programParameters.append(" -Duser.country=US");
        programParameters.append(" -Djava.awt.headless=true");


        // Tracing parameters
        programParameters.append(" -DcodeOrchestra.trace.host=" + LocalhostUtil.getLocalhostIp());
        programParameters.append(" -DcodeOrchestra.trace.port=" + LoggerServerSocketThread.LOGGING_PORT);

        // Livecoding parameters
        if (currentProject != null) {
            AsProjectLiveSettings liveCodingSettings = currentProject.getProjectLiveSettings();
            programParameters.append(" -DcodeOrchestra.live.liveMethods=" + liveCodingSettings.getLiveMethods().getPreferenceValue());
            programParameters.append(" -DcodeOrchestra.live.gettersSetters=" + liveCodingSettings.makeGettersSettersLive());
            programParameters.append(" -DcodeOrchestra.live.maxLoops=" + liveCodingSettings.getMaxIterationsCount());
            programParameters.append(" -DcodeOrchestra.digestsDir=" + protect(currentProject.getDigestsDir().getPath()));
        }

        processBuilder.environment().put("JETVMPROP", programParameters.toString());

        return processBuilder;
    }

    @Override
    public void runBeforeStart() {
    }

    public static String protect(String result) {
        if (result.contains(" ")) {
            return "\"" + result + "\"";
        }
        return result;
    }

}
