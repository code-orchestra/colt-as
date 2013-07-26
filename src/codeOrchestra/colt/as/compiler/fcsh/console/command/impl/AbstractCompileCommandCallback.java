package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.FCSHException;
import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandOutput;
import codeOrchestra.colt.core.logging.Logger;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public abstract class AbstractCompileCommandCallback extends AbstractCommandCallback {

  private static final String COMMAND_LINE_ERROR_TOKEN = "command line: ";
  private static final String COMPILE_ERRORS_LOG_FILE_NAME = "compile_errors.log";
  private static final String DELIVERY_MESSAGE_MARKER = "Delivery Message:";
  
  public static final CompilationResult OK_COMPILATION_RESULT = new CompilationResult(0, 0, false);

  protected abstract CompilationResult compile(CommandOutput response);

  private CompilationResult compilationResult;
  
  protected Logger log = Logger.getLogger(getExecutableName());

  @Override
  public final void done(CommandOutput response) {
    this.compilationResult = compile(response);
  }

  public CompilationResult getCompileResult() throws FCSHException {
    if (hasFailed()) {
      throw new FCSHException(getFailCause());
    }
    return compilationResult;
  }

  protected void processResponseAdditionally(String[] responseLines) {    
    for (String responseLine : responseLines) {
      if (responseLine.contains(DELIVERY_MESSAGE_MARKER)) {
        LiveCodingManager.instance().addDeliveryMessage(responseLine.substring(responseLine.indexOf("[") + 1, responseLine.indexOf("]")));
      }
    }
  }
  
  protected abstract String getExecutableName();

  protected CompilationResult getCompilationResult(CommandOutput response) {
    // Parse/save the compiler output (errors/warnings)
    String errorOutput = response.getErrorOutput();
    if (errorOutput == null) {
      return OK_COMPILATION_RESULT;
    }
    FileUtils.write(new File(getErrorLogFilePath()), errorOutput);

    // RE-658: Command line error handling
    if (errorOutput.startsWith(COMMAND_LINE_ERROR_TOKEN)) {
      log.error(errorOutput);
      return new CompilationResult(1, 0, false);
    }

    // Report compiler messages
    CompilerMessagesWrapper messagesWrapper = CompilerMessage.extract(errorOutput);
    for (CompilerMessage compilerMessage : messagesWrapper.getMessagesSmart()) {
      String reportMessage = compilerMessage.getReportMessage();
      switch (compilerMessage.getType()) {
        case ERROR:
          getLogger().error(reportMessage);
          break;
        case WARNING:
          getLogger().warning(reportMessage);
          break;
      }
    }

    // Double-check
    if (messagesWrapper.isEmpty()) {
      String trimmedErrorOutput = errorOutput.trim();
      if (trimmedErrorOutput.contains("Error:")) {
        getLogger().error(errorOutput);
        return new CompilationResult(1, 0, false);
      }
    }

    return new CompilationResult(messagesWrapper);
  }

  private String getErrorLogFilePath() {
    return new File(LCSProject.getCurrentProject().getBaseDir(), COMPILE_ERRORS_LOG_FILE_NAME).getPath();
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }

  public Logger getLogger() {
    return log;
  }

}
