package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;

/**
 * @author Alexander Eliseyev
 */
public class CPUProfilingStopCommand extends AbstractCommandCallback {
  @Override
  public String getCommand() {
    return "profiling.cpu.end";
  }

  @Override
  public void done(CommandOutput response) {
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }
}
