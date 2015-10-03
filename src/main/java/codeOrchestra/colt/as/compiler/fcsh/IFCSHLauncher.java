package codeOrchestra.colt.as.compiler.fcsh;

/**
 * @author Alexander Eliseyev
 */
public interface IFCSHLauncher {
	ProcessBuilder createProcessBuilder();
	void runBeforeStart();
}