package codeOrchestra.colt.as.compiler.fcsh;

/**
 * @author Alexander Eliseyev
 */
public class FCSHException extends Exception {
  public FCSHException() {
  }

  public FCSHException(String s) {
    super(s);
  }

  public FCSHException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public FCSHException(Throwable throwable) {
    super(throwable);
  }
}
