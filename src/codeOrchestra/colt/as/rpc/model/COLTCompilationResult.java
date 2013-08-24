package codeOrchestra.colt.as.rpc.model;

import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class COLTCompilationResult {
  
  private boolean successful;
  private COLTCompilerMessage[] errorMessages;
  private COLTCompilerMessage[] warningMessages;
  
  public COLTCompilationResult() {    
  }
  
  public COLTCompilationResult(CompilationResult compilationResult) {
    this.successful = compilationResult.isOk();
    
    List<COLTCompilerMessage> errorMessagesList = new ArrayList<>();
    List<COLTCompilerMessage> warningMessagesList = new ArrayList<>();
    
    List<CompilerMessage> messages = compilationResult.getMessages();
    for (CompilerMessage compilerMessage : messages) {
      switch (compilerMessage.getType()) {
      case ERROR:
        errorMessagesList.add(new COLTCompilerMessage(compilerMessage));
        break;
      case WARNING:
        warningMessagesList.add(new COLTCompilerMessage(compilerMessage));
        break;
      default:
        break;
      }
    }
    
    errorMessages = errorMessagesList.toArray(new COLTCompilerMessage[errorMessagesList.size()]);
    warningMessages = warningMessagesList.toArray(new COLTCompilerMessage[warningMessagesList.size()]);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public COLTCompilerMessage[] getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(COLTCompilerMessage[] errorMessages) {
    this.errorMessages = errorMessages;
  }

  public COLTCompilerMessage[] getWarningMessages() {
    return warningMessages;
  }

  public void setWarningMessages(COLTCompilerMessage[] warningMessages) {
    this.warningMessages = warningMessages;
  }

  
}
