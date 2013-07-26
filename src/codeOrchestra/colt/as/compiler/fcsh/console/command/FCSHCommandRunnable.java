package codeOrchestra.colt.as.compiler.fcsh.console.command;

import codeOrchestra.actionScript.compiler.fcsh.FCSHManager;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.lcs.fcsh.FCSHProcessHandler;
import codeOrchestra.lcs.project.CompilerSettings;
import codeOrchestra.lcs.project.LCSProject;
import codeOrchestra.util.ThreadUtils;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.process.ProcessOutputTypes;

/**
 * @author Alexander Eliseyev
 */
public class FCSHCommandRunnable implements Runnable  {

  private static final Logger LOG = Logger.getLogger("FCSHProcessHandler");
  
  private static final int TEXT_RECIEVE_SLEEP_TIMEOUT = 50;

  private FCSHManager fcshManager;
  private CommandCallback commandCallback;

  public FCSHCommandRunnable(FCSHManager fcshManager, CommandCallback commandCallback) {
    this.fcshManager = fcshManager;
    this.commandCallback = commandCallback;
  }

  @Override
  public final void run() {
    synchronized (FCSHCommandExecuteThread.class) {
      try {
        FCSHProcessHandler fcshProcessHandler = fcshManager.getProcessHandler();

        final CommandOutput commandOutput = new CommandOutput();
        ProcessListener processListener = new ProcessAdapter() {
          public void onTextAvailable(ProcessEvent event, String outputType) {
            commandOutput.addOutput(outputType, event.getText());
            commandCallback.textAvailable(event.getText(), outputType);
          }
        };

        fcshProcessHandler.addProcessListener(processListener);

        // RF-1246
        if (!fcshProcessHandler.inputWithFlush(commandCallback.getCommand() + '\n')) {
          // FCSH was down, got to restart
          fcshManager.restart();
          fcshManager.submitCommand(commandCallback);
          return;
        }

        CompilerSettings compilerSettings = LCSProject.getCurrentProject().getCompilerSettings();        
        long timeout = compilerSettings.interruptCompilationByTimeout() ? compilerSettings.getCompilationTimeout() * 1000 : Long.MAX_VALUE;
        while (true) {
          // Sleep a bit
          ThreadUtils.sleep(TEXT_RECIEVE_SLEEP_TIMEOUT);
          if (commandCallback.isDone()) {
            break;
          }

          timeout -= TEXT_RECIEVE_SLEEP_TIMEOUT;
          if (timeout <= 0) {
            LOG.error("Command execute timed out: " + commandCallback.getCommand());
            LOG.error(ProcessOutputTypes.STDOUT + ": " + commandOutput.getNormalOutput());
            LOG.error(ProcessOutputTypes.STDERR + ": " + commandOutput.getErrorOutput());
            
            fcshManager.destroyProcess();
            return;
          }
        }

        // In case we've missed some output
        ThreadUtils.sleep(TEXT_RECIEVE_SLEEP_TIMEOUT);

        commandCallback.done(commandOutput);
        fcshProcessHandler.removeProcessListener(processListener);
      } catch (Throwable t) {
        commandCallback.failed(t);
      }
    }
  }

}
