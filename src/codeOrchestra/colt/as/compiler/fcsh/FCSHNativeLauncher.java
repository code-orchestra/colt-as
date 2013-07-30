package codeOrchestra.colt.as.compiler.fcsh;

import codeOrchestra.colt.as.flex.FlexSDKSettings;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.COLTAsProjectLiveSettings;
import codeOrchestra.colt.as.util.PathUtils;
import codeOrchestra.util.LocalhostUtil;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class FCSHNativeLauncher implements IFCSHLauncher {
	
	public ProcessBuilder createProcessBuilder() {
		ProcessBuilder processBuilder = new ProcessBuilder(PathUtils.getFlexSDKPath() + File.separator + "bin" + File.separator + "ColtFCSH.exe");
		
		StringBuilder programParameters = new StringBuilder();

    String applicationHome;
    COLTAsProject currentProject = COLTAsProject.getCurrentProject();
    if (currentProject != null) {
      applicationHome = currentProject.getProjectBuildSettings().getFlexSDKPath();
      if (!new File(applicationHome).exists()) {
        applicationHome = FlexSDKSettings.getDefaultFlexSDKPath();
      }
    } else {
      applicationHome = FlexSDKSettings.getDefaultFlexSDKPath();
    }

    programParameters.append(protect("-Dapplication.home=" + applicationHome));
    
    // Tracing parameters
    programParameters.append(" -DcodeOrchestra.trace.host=" + LocalhostUtil.getLocalhostIp());
    programParameters.append(" -DcodeOrchestra.trace.port=" + LoggerServerSocketThread.LOGGING_PORT);
    
    // Livecoding parameters
    if (currentProject != null) {
      COLTAsProjectLiveSettings liveCodingSettings = currentProject.getProjectLiveSettings();
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
