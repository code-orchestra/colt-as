package codeOrchestra.colt.as.view;

import codeOrchestra.colt.core.logging.Logger;

/**
 * @author Alexander Eliseyev
 */
public class FCSHConsoleView {

    private static Logger logger = Logger.getLogger("FCSH");

    private static FCSHConsoleView sharedInstance; // TODO: init this

    public static FCSHConsoleView get() {
        return sharedInstance;
    }

    public synchronized void write(String s) {
        logger.compile(s);
    }

}
