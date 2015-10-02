package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;

/**
 * @author Alexander Eliseyev
 */
public class ClearCommand extends AbstractCommandCallback {

  @Override
  public String getCommand() {
    return "clear";
  }

  @Override
  public void done(CommandOutput response) {
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }
}
