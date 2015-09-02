package codeOrchestra.colt.as.digest;

/**
 * @author Alexander Eliseyev
 */
public class DigestException extends Exception {

    public DigestException() {
    }

    public DigestException(String message) {
        super(message);
    }

    public DigestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigestException(Throwable cause) {
        super(cause);
    }

    public DigestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
