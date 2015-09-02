package codeOrchestra.colt.as.compiler.fcsh;

import codeOrchestra.colt.as.compiler.fcsh.console.command.AbstractCommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.output.ProcessOutputTypes;
import codeOrchestra.colt.as.view.FCSHConsoleView;
import codeOrchestra.colt.core.execution.OSProcessHandler;
import codeOrchestra.colt.core.execution.ProcessEvent;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.util.ExceptionUtils;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.process.ProcessAdapter;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Alexander Eliseyev
 */
public class FCSHProcessHandler extends OSProcessHandler {
  
  private static final Logger LOG = Logger.getLogger("FCSHProcessHandler");

  public OutputStreamWriter myOutputStreamWriter;

  private boolean initialized;

  public FCSHProcessHandler(Process process, String params) {
    super(process, params);

    this.addProcessListener(new ProcessAdapter() {
      public void onTextAvailable(ProcessEvent event, String k) {
        String text = event.getText();
        if (!initialized && text != null && text.contains(AbstractCommandCallback.FCSH_COMMAND_PROMPT)) {
          initialized = true;
        }
        append(text, k);
      }
    });
  }

  private synchronized void append(String s, String key) {
    if (StringUtils.isNotEmpty(s)) {
      FCSHConsoleView.get().write(s);
    }
  }

  public boolean isInitialized() {
    return initialized;
  }
  
  public void input(String s) {
    try {
      getProcessInputWriter().append(s);
    } catch (IOException ex) {
      LOG.error(ex);
    }
  }

  public boolean inputWithFlush(String s) {
    try {
      getProcessInputWriter().append(s);
      getProcessInputWriter().flush();

      append("> " + s, ProcessOutputTypes.STDOUT);
      return true;
    } catch (IOException ex) {
      if (ExceptionUtils.isBrokenPipe(ex)) {
        return false;
      } else {
        LOG.error(ex);
        return true;
      }
    }
  }

  private OutputStreamWriter getProcessInputWriter() {
    if (myOutputStreamWriter == null) {
      myOutputStreamWriter = new OutputStreamWriter(getProcessInput());
    }
    return myOutputStreamWriter;
  }

  public void flush() {
    try {
      getProcessInputWriter().flush();
    } catch (IOException ex) {
      LOG.error(ex);
    }
  }
}
