package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.actionScript.compiler.fcsh.FCSHException;
import codeOrchestra.actionScript.compiler.fcsh.console.command.CommandCallback;
import codeOrchestra.actionScript.modulemaker.CompilationResult;

/**
 * @author Alexander Eliseyev
 */
public interface CompileCommandCallback extends CommandCallback {

  CompilationResult getCompileResult() throws FCSHException;

}
