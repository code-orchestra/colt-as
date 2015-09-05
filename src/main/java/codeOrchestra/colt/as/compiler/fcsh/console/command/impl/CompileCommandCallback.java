package codeOrchestra.colt.as.compiler.fcsh.console.command.impl;

import codeOrchestra.colt.as.compiler.fcsh.FCSHException;
import codeOrchestra.colt.as.compiler.fcsh.console.command.CommandCallback;
import codeOrchestra.colt.as.compiler.fcsh.make.CompilationResult;

/**
 * @author Alexander Eliseyev
 */
public interface CompileCommandCallback extends CommandCallback {

  CompilationResult getCompileResult() throws FCSHException;

}
