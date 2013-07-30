package codeOrchestra.colt.as.view;

/**
 * @author Alexander Eliseyev
 */
public class FCSHConsoleView {

    private static FCSHConsoleView sharedInstance; // TODO: init this

    public static FCSHConsoleView get() {
        return sharedInstance;
    }

    public synchronized void write(String s) {
        // TODO: implement
    }

}
