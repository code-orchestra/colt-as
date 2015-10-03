package codeOrchestra.colt.as.compiler.fcsh.make;

/**
 * @author Alexander Eliseyev
 */
public class MakeException extends Exception {
  public MakeException(String message) {
    super(message);
  }
  public MakeException(String message, Throwable cause) {
    super(message, cause);
  }
}