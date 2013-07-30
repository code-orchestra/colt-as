package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import java.util.List;

import codeOrchestra.actionScript.compiler.fcsh.FCSHManager;
import codeOrchestra.actionScript.compiler.fcsh.console.command.CommandOutput;
import codeOrchestra.actionScript.modulemaker.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.util.StringUtils;
import codeOrchestra.utils.StringUtils;

/**
 * @author Alexander Eliseyev
 */
public abstract class AbstractCompileWithArgumentsCommandCallback extends AbstractCompileCommandCallback implements CompileCommandCallback {

  public static final String  ASSIGNED_TOKEN = " Assigned ";

  private List<String> arguments;

  public AbstractCompileWithArgumentsCommandCallback(List<String> arguments) {
    this.arguments = arguments;
  }

  @Override
  public String getCommand() {
    return getExecutableName() + " " + (isBenchmarkEnabled() ? "-benchmark=true " : "") + getArgumentsSeparated();
  }

  private boolean isBenchmarkEnabled() {
    return false;
  }

  private String getArgumentsSeparated() {
    return StringUtils.join(arguments, " ");
  }
  
  protected CompilationResult compile(CommandOutput response) {
    String normalOutput = response.getNormalOutput();
    Integer targetId = null;

    // Parse the compiler target
    String[] responseLines = normalOutput.split("\\r?\\n");
    for (String responseLine : responseLines) {
      if (responseLine.contains(ASSIGNED_TOKEN)) {
        String temp = responseLine.substring(responseLine.indexOf(ASSIGNED_TOKEN) + ASSIGNED_TOKEN.length());
        String numberStr = temp.substring(0, temp.indexOf(" "));

        targetId = Integer.parseInt(numberStr);
        break;
      }
    }
    if (targetId == null) {
      throw new RuntimeException("Can't retrieve the compile target ID assigned from the output:\n" + normalOutput);
    }
    
    processResponseAdditionally(responseLines);

    // Get the compilation result
    CompilationResult compilationResult = getCompilationResult(response);
    
    // Register the compiler target if the compilation went ok
    if (compilationResult.isOk()) {
      FCSHManager.instance().registerCompileTarget(arguments, targetId);
    }

    return compilationResult;
  }

}

