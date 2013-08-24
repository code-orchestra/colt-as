package codeOrchestra.colt.as.compiler.fcsh;

import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.console.command.FCSHCommandExecuteThread;
import codeOrchestra.colt.as.compiler.fcsh.console.command.FCSHCommandRunnable;
import codeOrchestra.colt.as.compiler.fcsh.console.command.impl.*;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;
import codeOrchestra.colt.as.compiler.fcsh.target.CompilerTarget;
import codeOrchestra.colt.core.license.DemoHelper;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.lcs.license.COLTRunningKey;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.SystemInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class FCSHManager {

  private static FCSHManager instance = new FCSHManager();

  public static FCSHManager instance() {
    return instance;
  }

  public static final Logger LOG = Logger.getLogger("fcsh");

  private static final int FCSH_INIT_CHECK_INTERVAL = 100;
  public static final long FCSH_INIT_TIMEOUT = 3000;

  private FCSHProcessHandler fcshProcessHandler;

  private final Map<List<String>, CompilerTarget> compilerTargets = Collections.synchronizedMap(new HashMap<>());

  public void restart() throws FCSHException, MaximumCompilationsCountReachedException {
    if (DemoHelper.get().maxCompilationsCountReached()) {
      COLTRunningKey.setRunning(false);
      throw new MaximumCompilationsCountReachedException(); 
    } else {
      COLTRunningKey.setRunning(true);
    }
    
    destroyProcess();
    assureFCSHIsActive();
  }

  public void destroyProcess() {
    try {
      if (fcshProcessHandler != null && !fcshProcessHandler.isProcessTerminated()) {
        fcshProcessHandler.destroyProcess();
      }
    } catch (Throwable t) {
      // ignore
    }
  }

  public CompilerTarget registerCompileTarget(List<String> arguments, int id) {
    synchronized (compilerTargets) {
      CompilerTarget compilerTarget = compilerTargets.get(arguments);
      if (compilerTarget == null) {
        compilerTarget = new CompilerTarget(id);
        compilerTargets.put(arguments, compilerTarget);
      }

      return compilerTarget;
    }
  }

  public CompilationResult compile(CompilerTarget target) throws FCSHException, MaximumCompilationsCountReachedException {
    incrementCompilationCount();
    
    assureFCSHIsActive();

    CompileTargetCommand compileCommand = new CompileTargetCommand(this, target);
    LOG.compile("Compiling the target #" + target.getId());

    submitCommand(compileCommand);

    return compileCommand.getCompileResult();
  }

  private void assureFCSHIsActive() throws FCSHException {
    if (fcshProcessHandler != null && !fcshProcessHandler.isProcessTerminated()) {
      // No need to reactivate, the process is still running
      return;
    }

    clearTargets();
    
    IFCSHLauncher fcshLauncher;
    ProcessBuilder processBuilder;
    if (FCSHLauncher.NATIVE_FCSH && SystemInfo.isWindows) {
      fcshLauncher = new FCSHNativeLauncher();
    } else {
      fcshLauncher = new FCSHLauncher();
    }
    processBuilder = fcshLauncher.createProcessBuilder();

    Process fcshProcess;
    try {
      fcshLauncher.runBeforeStart();
      fcshProcess = processBuilder.start();
    } catch (IOException e) {
      throw new FCSHException("Error while trying to start the fcsh process", e);
    }

    String commandString = StringUtils.join(processBuilder.command(), ", ");
    LOG.compile(commandString);

    fcshProcessHandler = new FCSHProcessHandler(fcshProcess, commandString);
    fcshProcessHandler.startNotify();

    // Give fcsh some time to start up
    long timeout = FCSH_INIT_TIMEOUT;
    while (!fcshProcessHandler.isInitialized()) {
      if (timeout < 0) {
        return;
      }

      try {
        Thread.sleep(FCSH_INIT_CHECK_INTERVAL);
        timeout -= FCSH_INIT_CHECK_INTERVAL;
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }

  public void submitCommand(CommandCallback commandCallback) throws FCSHException {
    assureFCSHIsActive();

    FCSHCommandRunnable fcshCommandRunnable = new FCSHCommandRunnable(this, commandCallback);
    if (commandCallback.isSynchronous()) {
      fcshCommandRunnable.run();
    } else {
      new FCSHCommandExecuteThread(fcshCommandRunnable).start();
    }
  }

  public void clear() throws FCSHException {
    assureFCSHIsActive();

    submitCommand(new ClearCommand());
  }

  public CompilationResult baseMXMLC(List<String> arguments) throws FCSHException, MaximumCompilationsCountReachedException {
    incrementCompilationCount();
    
    assureFCSHIsActive();

    LivecodingBaseMXMLCCommand mxmlcCommand = new LivecodingBaseMXMLCCommand(arguments);
    LOG.compile("Compiling: " + mxmlcCommand.getCommand());

    submitCommand(mxmlcCommand);

    return mxmlcCommand.getCompileResult();
  }

  public CompilationResult baseCOMPC(List<String> arguments) throws FCSHException {
    assureFCSHIsActive();

    LivecodingBaseCOMPCCommand compcCommand = new LivecodingBaseCOMPCCommand(arguments);
    LOG.compile("Compiling: " + compcCommand.getCommand());

    submitCommand(compcCommand);

    return compcCommand.getCompileResult();
  }

  public CompilationResult incrementalCOMPC(List<String> arguments) throws FCSHException, MaximumCompilationsCountReachedException {
    incrementCompilationCount();
    
    assureFCSHIsActive();

    LivecodingIncrementalCOMPCCommand compcCommand = new LivecodingIncrementalCOMPCCommand(arguments);
    LOG.compile("Compiling: " + compcCommand.getCommand());

    submitCommand(compcCommand);

    return compcCommand.getCompileResult();
  }

  public CompilationResult compc(List<String> commandArguments) throws FCSHException, MaximumCompilationsCountReachedException {
    incrementCompilationCount();
    
    assureFCSHIsActive();

    synchronized (compilerTargets) {
      CompilerTarget compilerTarget = compilerTargets.get(commandArguments);
      if (compilerTarget != null) {
        return compile(compilerTarget);
      }
    }
    
    COMPCCommand compcCommand = new COMPCCommand(commandArguments);
    LOG.compile("Compiling: " + compcCommand.getCommand());

    submitCommand(compcCommand);

    return compcCommand.getCompileResult();
  }

  public CompilationResult mxmlc(List<String> commandArguments) throws FCSHException, MaximumCompilationsCountReachedException {
    incrementCompilationCount();
    
    assureFCSHIsActive();

    synchronized (compilerTargets) {
      CompilerTarget compilerTarget = compilerTargets.get(commandArguments);
      if (compilerTarget != null) {
        return compile(compilerTarget);
      }
    }
    
    MXMLCCommand mxmlcCommand = new MXMLCCommand(commandArguments);
    LOG.compile("Compiling: " + mxmlcCommand.getCommand());

    submitCommand(mxmlcCommand);

    return mxmlcCommand.getCompileResult();
  }
  
  private void incrementCompilationCount() throws MaximumCompilationsCountReachedException {
    if (DemoHelper.get().maxCompilationsCountReached()) {
      COLTRunningKey.setRunning(false);
      throw new MaximumCompilationsCountReachedException(); 
    } else {
      COLTRunningKey.setRunning(true);
    }
    
    DemoHelper.get().incrementCompilationsCount();
  }  

  public void deleteLivecodingCaches() throws FCSHException {
    assureFCSHIsActive();

    LivecodingCachesDeleteCommand deleteCachesCommand = new LivecodingCachesDeleteCommand();
    submitCommand(deleteCachesCommand);
  }

  public void startCPUProfiling() throws FCSHException {
    if (!FCSHLauncher.PROFILING_ON) {
      return;
    }
    assureFCSHIsActive();
    submitCommand(new CPUProfilingStartCommand());
  }

  public void stopCPUProfiling() throws FCSHException {
    if (!FCSHLauncher.PROFILING_ON) {
      return;
    }
    assureFCSHIsActive();
    submitCommand(new CPUProfilingStopCommand());
  }

  public void clearTargets() {
    this.compilerTargets.clear();
  }

  public FCSHProcessHandler getProcessHandler() {
    return fcshProcessHandler;
  }

}
