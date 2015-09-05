package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.FCSHManager;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.target.CompilerTarget;

/**
 * @author Alexander Eliseyev
 */
public class CompileTargetCommand extends AbstractCompileCommandCallback implements CompileCommandCallback {

  private FCSHManager fcshManager;
  private CompilerTarget target;

  public CompileTargetCommand(FCSHManager fcshManager, CompilerTarget target) {
    this.fcshManager = fcshManager;
    this.target = target;
  }

  @Override
  protected CompilationResult compile(CommandOutput response) {
    if (response.getErrorOutput() == null) {
      if (response.getNormalOutput().contains(getTargetNotFoundMessage())) {
        fcshManager.clearTargets();
        log.error("Compilation target specified not found, try rebuilding");
        return new CompilationResult(1, 0, false);
      }
    }

    return getCompilationResult(response);
  }

  private String getTargetNotFoundMessage() {
    return "Target " + target.getId() + " not found";
  }

  @Override
  public String getCommand() {
    return getExecutableName() + " " + target.getId();
  }

  @Override
  protected String getExecutableName() {
    return "compile";
  }

}
