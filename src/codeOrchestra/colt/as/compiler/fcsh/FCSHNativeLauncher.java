package codeOrchestra.colt.as.compiler.fcsh;

import java.io.File;

import codeOrchestra.actionScript.logging.transport.LoggerServerSocketThread;
import codeOrchestra.lcs.flex.FlexSDKSettings;
import codeOrchestra.lcs.project.LCSProject;
import codeOrchestra.lcs.project.LiveCodingSettings;
import codeOrchestra.utils.LocalhostUtil;
import codeOrchestra.utils.PathUtils;

/**
 * @author Alexander Eliseyev
 */
public class FCSHNativeLauncher implements IFCSHLauncher {
	
	public ProcessBuilder createProcessBuilder() {
		ProcessBuilder processBuilder = new ProcessBuilder(PathUtils.getFlexSDKPath() + File.separator + "bin" + File.separator + "ColtFCSH.exe");
		
		StringBuilder programParameters = new StringBuilder();

    String applicationHome;
    LCSProject currentProject = LCSProject.getCurrentProject();
    if (currentProject != null) {
      applicationHome = currentProject.getCompilerSettings().getFlexSDKPath();
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
      LiveCodingSettings liveCodingSettings = currentProject.getLiveCodingSettings();
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
