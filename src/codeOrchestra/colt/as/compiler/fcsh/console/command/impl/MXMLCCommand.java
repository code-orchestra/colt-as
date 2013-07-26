package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class MXMLCCommand extends AbstractCompileWithArgumentsCommandCallback {

  public static final String EXECUTABLE_NAME = "mxmlc";

  public MXMLCCommand(List<String> arguments) {
    super(arguments);
  }

  @Override
  protected String getExecutableName() {
    return EXECUTABLE_NAME;
  }

}
