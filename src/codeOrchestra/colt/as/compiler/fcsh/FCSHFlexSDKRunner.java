package codeOrchestra.colt.as.compiler.fcsh;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.MakeException;
import codeOrchestra.colt.as.compiler.fcsh.make.process.AbstractFlexSDKRunner;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.ProjectHelper;
import codeOrchestra.util.StringUtils;
import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class FCSHFlexSDKRunner extends AbstractFlexSDKRunner {

  private static final String COMPILE_ERRORS_LOG_FILE_NAME = "compile_errors.log";

  private FSCHCompilerKind compilerKind;

  public FCSHFlexSDKRunner(File configFile, FSCHCompilerKind compilerKind) {
    super(configFile);
    this.compilerKind = compilerKind;
  }

  @Override
  public CompilationResult run() throws MakeException, MaximumCompilationsCountReachedException {
    FCSHManager fcshManager = FCSHManager.instance();

    try {
      switch (compilerKind) {
      case MXMLC:
        return fcshManager.mxmlc(getCommandArguments());      
      case COMPC:
        return fcshManager.compc(getCommandArguments());      
      case BASE_MXMLC:
        return fcshManager.baseMXMLC(getCommandArguments());
      case BASE_COMPC:
        return fcshManager.baseCOMPC(getCommandArguments());
      case INCREMENTAL_COMPC:
        return fcshManager.incrementalCOMPC(getCommandArguments());
      }
    } catch (FCSHException e) {
      throw new MakeException("Error while executing in compiler shell", e);
    }

    throw new IllegalStateException("Illegal compiler kind: " + compilerKind);
  }

  @Override
  protected List<String> getCommandArguments() {
    List<String> commandArguments = new ArrayList<String>();

    // Custom configuration file
    if (compilerSettings.useCustomSDKConfiguration()) {
      // Use default SDK flex config?
      String loadConfigOperator = compilerSettings.useDefaultSDKConfiguration() ? "+=" : "=";
      String customConfigFileArg = "-load-config" + loadConfigOperator + compilerSettings.getCustomConfigPath();
      commandArguments.add(customConfigFileArg);
    }
    
    // COLT-generated config file
    boolean atLeastOneConfigIsPresent = compilerSettings.useCustomSDKConfiguration() || compilerSettings.useDefaultSDKConfiguration();    
    String configFileArg = "-load-config" + (atLeastOneConfigIsPresent ? "+=" : "=") + copyConfigToTempDir(configFile.getPath());
    commandArguments.add(configFileArg);    

    // Additional compiler options
    if (!StringUtils.isEmpty(compilerSettings.getCompilerOptions())) {
      String[] additionalArgs = new Commandline("commandtoken " + compilerSettings.getCompilerOptions()).getArguments();
      for (String additionalArgument : additionalArgs) {
        commandArguments.add(additionalArgument);
      }
    }

    return commandArguments;
  }

  private String copyConfigToTempDir(String configPath) {
    File tempConfigDir = new File(FileUtils.getTempDir(), "flexConfigs");

    File configFile = new File(configPath);
    if (!configFile.exists()) {
      throw new RuntimeException("Config file doesn't exist: " + configPath);
    }

    File configFileTarget = new File(tempConfigDir, configFile.getName().replace(" ", "_"));

    try {
      FileUtils.copyFileChecked(configFile, configFileTarget, true);
    } catch (IOException e) {
      throw new RuntimeException("Can't copy config to " + configFileTarget);
    }
    return configFileTarget.getPath();
  }

  @Override
  public String getErrorLogFilePath() {
    return new File(ProjectHelper.<COLTAsProject>getCurrentProject().getBaseDir(), COMPILE_ERRORS_LOG_FILE_NAME).getPath();
  }

  @Override
  protected String getCommandName() {
    return "fcsh";
  }

}
