package codeOrchestra.colt.as.compiler.fcsh.console.command;

import codeOrchestra.colt.as.compiler.fcsh.console.command.output.ProcessOutputTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class CommandOutput {

  private Map<String, StringBuilder> outputMap = new HashMap<String, StringBuilder>();

  public void addOutput(String key, String text) {
    StringBuilder stringBuilder = outputMap.get(key);
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
      outputMap.put(key, stringBuilder);
    }

    stringBuilder.append(text);
  }

  public String getNormalOutput() {
    StringBuilder stringBuilder = outputMap.get(ProcessOutputTypes.STDOUT);
    if (stringBuilder == null) {
      return null;
    }

    return stringBuilder.toString();
  }

  public String getErrorOutput() {
    StringBuilder stringBuilder = outputMap.get(ProcessOutputTypes.STDERR);
    if (stringBuilder == null) {
      return null;
    }

    return stringBuilder.toString();
  }

}
