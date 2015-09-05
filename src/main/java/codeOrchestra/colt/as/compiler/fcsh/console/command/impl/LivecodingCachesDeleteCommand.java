package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;

/**
 * @author Alexander Eliseyev
 */
public class LivecodingCachesDeleteCommand extends AbstractCommandCallback {

  @Override
  public String getCommand() {
    return "livecoding.caches.delete";
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
