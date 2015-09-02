package codeOrchestra.colt.as.compiler.fcsh.console.command;

/**
 * @author Alexander Eliseyev
 */
public interface CommandCallback {

  String getCommand();

  void textAvailable(String text, String key);

  boolean isDone();

  void done(CommandOutput response);

  boolean isSynchronous();

  void failed(Throwable t);

}