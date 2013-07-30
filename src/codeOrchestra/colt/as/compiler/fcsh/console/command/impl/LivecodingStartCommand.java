package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;

/**
 * @author Anton.I.Neverov
 */
public class LivecodingStartCommand extends AbstractCommandCallback {
  @Override
  public String getCommand() {
    return "livecoding.start";
  }

  @Override
  public void done(CommandOutput response) {
    // do nothing
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }
}
