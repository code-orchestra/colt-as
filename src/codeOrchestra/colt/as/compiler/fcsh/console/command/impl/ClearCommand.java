package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.actionScript.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.actionScript.compiler.fcsh.console.command.CommandOutput;

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
    // nothing here
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }
}
