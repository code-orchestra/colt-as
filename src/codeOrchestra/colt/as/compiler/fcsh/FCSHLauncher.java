package codeOrchestra.colt.as.compiler.fcsh;

import java.io.File;

import codeOrchestra.actionScript.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.flex.FlexSDKSettings;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.lcs.flex.FlexSDKSettings;
import codeOrchestra.lcs.project.LCSProject;
import codeOrchestra.lcs.project.LiveCodingSettings;
import codeOrchestra.util.ProjectHelper;
import codeOrchestra.utils.LocalhostUtil;
import codeOrchestra.utils.process.JavaLauncher;

import com.intellij.openapi.util.SystemInfo;

/**
 * @author Alexander Eliseyev
 */
public class FCSHLauncher extends JavaLauncher implements IFCSHLauncher {

  public static final boolean PROFILING_ON = System.getProperty("colt.profiling.on") != null;
  
  public static final boolean NATIVE_FCSH = true;
  
  public FCSHLauncher() {
    super(null);

    StringBuilder programParameters = new StringBuilder();

    String applicationHome;
    COLTAsProject currentProject = ProjectHelper.<COLTAsProject>getCurrentProject();
    if (currentProject != null) {
      applicationHome = currentProject.getProjectBuildSettings().getFlexSDKPath();
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
      LiveCodingSettings liveCodingSettings = currentProject.getLiveCodingSettings();
      programParameters.append(" -DcodeOrchestra.live.liveMethods=" + liveCodingSettings.getLiveMethods().getPreferenceValue());
      programParameters.append(" -DcodeOrchestra.live.gettersSetters=" + liveCodingSettings.makeGettersSettersLive());
      programParameters.append(" -DcodeOrchestra.live.maxLoops=" + liveCodingSettings.getMaxIterationsCount());
      programParameters.append(" -DcodeOrchestra.digestsDir=" + protect(currentProject.getDigestsDir().getPath()));
    }

    programParameters.append(" -jar ");
    programParameters.append(protect(FlexSDKSettings.getDefaultFlexSDKPath() + "/liblc/fcsh.jar"));

    setProgramParameter(programParameters.toString());
    
    StringBuilder jvmParameters = new StringBuilder();
    jvmParameters.append("-Xms1000m -Xmx1000m -Dsun.io.useCanonCaches=false ");

    
    if (PROFILING_ON) {
       jvmParameters.append("-agentpath:libyjpagent.jnilib ");
    }
    if (!SystemInfo.isWindows) {
      jvmParameters.append("-d32 ");
    }
    setWorkingDirectory(new File(FlexSDKSettings.getDefaultFlexSDKPath(), "bin"));
    setVirtualMachineParameter(jvmParameters.toString());
  }

  @Override
  public void runBeforeStart() {	
  }

}
