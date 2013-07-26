package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class LivecodingBaseCOMPCCommand extends AbstractCompileWithArgumentsCommandCallback {

  public static final String EXECUTABLE_NAME = "lccompc";

  public LivecodingBaseCOMPCCommand(List<String> arguments) {
    super(arguments);
  }

  @Override
  protected String getExecutableName() {
    return EXECUTABLE_NAME;
  }

}
