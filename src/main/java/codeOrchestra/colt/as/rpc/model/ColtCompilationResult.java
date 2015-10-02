package codeOrchestra.colt.as.rpc.model;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ColtCompilationResult {
  
  private boolean successful;
  private ColtCompilerMessage[] errorMessages;
  private ColtCompilerMessage[] warningMessages;

  public ColtCompilationResult(CompilationResult compilationResult) {
    this.successful = compilationResult.isOk();
    
    List<ColtCompilerMessage> errorMessagesList = new ArrayList<>();
    List<ColtCompilerMessage> warningMessagesList = new ArrayList<>();
    
    List<CompilerMessage> messages = compilationResult.getMessages();
    for (CompilerMessage compilerMessage : messages) {
      switch (compilerMessage.getType()) {
      case ERROR:
        errorMessagesList.add(new ColtCompilerMessage(compilerMessage));
        break;
      case WARNING:
        warningMessagesList.add(new ColtCompilerMessage(compilerMessage));
        break;
      default:
        break;
      }
    }
    
    errorMessages = errorMessagesList.toArray(new ColtCompilerMessage[errorMessagesList.size()]);
    warningMessages = warningMessagesList.toArray(new ColtCompilerMessage[warningMessagesList.size()]);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public ColtCompilerMessage[] getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(ColtCompilerMessage[] errorMessages) {
    this.errorMessages = errorMessages;
  }

  public ColtCompilerMessage[] getWarningMessages() {
    return warningMessages;
  }

  public void setWarningMessages(ColtCompilerMessage[] warningMessages) {
    this.warningMessages = warningMessages;
  }

  
}
