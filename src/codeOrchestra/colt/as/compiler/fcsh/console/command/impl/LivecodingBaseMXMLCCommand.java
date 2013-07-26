package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class LivecodingBaseMXMLCCommand extends AbstractCompileWithArgumentsCommandCallback {

  public static final String EXECUTABLE_NAME = "lcmxmlc";

  public LivecodingBaseMXMLCCommand(List<String> arguments) {
    super(arguments);
  }

  @Override
  protected String getExecutableName() {
    return EXECUTABLE_NAME;
  }

}
