package codeOrchestra.colt.as.compiler.fcsh.make;

import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessage;
import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessagesWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class CompilationResult implements Serializable {

  public static final CompilationResult OK = new CompilationResult(0, 0, false);
  
  public static final CompilationResult ABORTED = new CompilationResult(0, 0, true);

  private int myErrors;
  private int myWarnings;
  private boolean myAborted;

  private List<CompilerMessage> messages = new ArrayList<>();

  public CompilationResult(int errors, int warnings, boolean aborted) {
    myErrors = errors;
    myWarnings = warnings;
    myAborted = aborted;
  }

  public CompilationResult(CompilerMessagesWrapper messagesWrapper) {
    this(messagesWrapper.getErrorCount(), messagesWrapper.getWarningsCount(), false);
    messages.addAll(messagesWrapper.getMessages());
  }

  public List<CompilerMessage> getMessages() {
    return messages;
  }

  public int getErrors() {
    return myErrors;
  }

  public int getWarnings() {
    return myWarnings;
  }

  public boolean isAborted() {
    return myAborted;
  }

  public boolean isOk() {
    return (getErrors() == 0) && !isAborted();
  }

  public String toString() {
    if (!isAborted()) {
      return "compilation finished : errors: " + getErrors() + " warnings: " + getWarnings();
    } else {
      return "compilation aborted";
    }
  }
}
