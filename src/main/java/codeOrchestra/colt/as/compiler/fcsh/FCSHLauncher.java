package codeOrchestra.colt.as.compiler.fcsh;
import codeOrchestra.colt.as.flex.FlexSDKSettings;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.model.AsProjectBuildSettings;
import codeOrchestra.colt.as.model.AsProjectLiveSettings;
import codeOrchestra.util.LocalhostUtil;
import codeOrchestra.util.ProjectHelper;
import codeOrchestra.util.process.JavaLauncher;
import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class FCSHLauncher extends JavaLauncher implements IFCSHLauncher {

    public static final boolean PROFILING_ON = System.getProperty("colt.profiling.on") != null;

    public static final boolean NATIVE_FCSH = false;

    public FCSHLauncher() {
        super(null);

        StringBuilder programParameters = new StringBuilder();

        String applicationHome;
        AsProject currentProject = ProjectHelper.getCurrentProject();
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

        programParameters.append(" -jar ");
        programParameters.append(protect(FlexSDKSettings.getDefaultFlexSDKPath() + "/liblc/fcsh.jar"));

        setProgramParameter(programParameters.toString());

        StringBuilder jvmParameters = new StringBuilder();
        jvmParameters.append("-Xms1000m -Xmx1000m -Dsun.io.useCanonCaches=false ");


        if (PROFILING_ON) {
            jvmParameters.append("-agentpath:libyjpagent.jnilib ");
        }
        setWorkingDirectory(new File(FlexSDKSettings.getDefaultFlexSDKPath(), "bin"));
        setVirtualMachineParameter(jvmParameters.toString());
    }

    @Override
    public void runBeforeStart() {
    }

}
