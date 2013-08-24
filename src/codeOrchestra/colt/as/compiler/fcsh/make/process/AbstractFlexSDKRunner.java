package codeOrchestra.colt.as.compiler.fcsh.make.process;

import codeOrchestra.colt.as.compiler.fcsh.MaximumCompilationsCountReachedException;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.MakeException;
import codeOrchestra.colt.as.flex.FlexSDKSettings;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.COLTAsProjectBuildSettings;
import codeOrchestra.util.ProjectHelper;
import codeOrchestra.util.StringUtils;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public abstract class AbstractFlexSDKRunner {

  private static final String DEFAULT_CONFIG_FILE_NAME = "flex-config.xml";
  private static final String DEFAULT_CONFIG_FILE_DIR = "frameworks";

  protected File configFile;
  protected COLTAsProjectBuildSettings compilerSettings;

  public AbstractFlexSDKRunner(File configFile) {
    this.configFile = configFile;
    this.compilerSettings = ProjectHelper.<COLTAsProject>getCurrentProject().getProjectBuildSettings();
  }

  public abstract CompilationResult run() throws MakeException, MaximumCompilationsCountReachedException;

  public abstract String getErrorLogFilePath();

  protected abstract String getCommandName();

  protected List<String> getCommandArguments() {
    List<String> commandArguments = new ArrayList<>();

    boolean firstConfigSet = false;

    // Default SDK configuration file
    if (compilerSettings.useDefaultSDKConfiguration()) {
      String defaultConfigFileArg = "-load-config=" + getDefaultConfigurationFilePath();
      commandArguments.add(defaultConfigFileArg);

      firstConfigSet = true;
    }

    // Custom configuration file
    if (compilerSettings.useCustomSDKConfiguration()) {
      String customConfigFileArg = "-load-config" + (firstConfigSet ? "+=" : "=") + compilerSettings.getCustomConfigPath();
      commandArguments.add(customConfigFileArg);
    } else {
      // Module configuration file
      String configFileArg = "-load-config" + (firstConfigSet ? "+=" : "=") + configFile.getPath();
      commandArguments.add(configFileArg);
    }

    // Additional compiler options
    if (!StringUtils.isEmpty(compilerSettings.getCompilerOptions())) {
      String[] additionalArgs = new Commandline("commandtoken " + compilerSettings.getCompilerOptions()).getArguments();
      for (String additionalArgument : additionalArgs) {
        commandArguments.add(additionalArgument);
      }
    }

    return commandArguments;
  }

  public static String protect(String result) {
    if (result.contains(" ")) {
      return "\"" + result + "\"";
    }
    return result;
  }

  private static String getDefaultConfigurationFilePath() {
    String flexSDKPath = FlexSDKSettings.getDefaultFlexSDKPath();
    return flexSDKPath + File.separator + DEFAULT_CONFIG_FILE_DIR + File.separator + DEFAULT_CONFIG_FILE_NAME;
  }

}
