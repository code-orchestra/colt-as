package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class COMPCCommand extends AbstractCompileWithArgumentsCommandCallback {

  public static final String EXECUTABLE_NAME = "compc";
  
    public COMPCCommand(List<String> arguments) {
    super(arguments);
  }

  @Override
  protected String getExecutableName() {
    return EXECUTABLE_NAME;
  }
  
}
