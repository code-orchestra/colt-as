package codeOrchestra.colt.as.compiler.fcsh.console.command;

/**
 * @author Alexander Eliseyev
 */
public class FCSHCommandExecuteThread extends Thread {

  private Runnable fcshCommandRunnable;

  public FCSHCommandExecuteThread(Runnable fcshCommandRunnable) {
    this.fcshCommandRunnable = fcshCommandRunnable;
  }

  @Override
  public void run() {
    synchronized (FCSHCommandExecuteThread.class) {
      fcshCommandRunnable.run();
    }
  }
}
