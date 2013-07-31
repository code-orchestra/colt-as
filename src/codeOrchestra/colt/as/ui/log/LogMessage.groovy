package codeOrchestra.colt.as.ui.log

/**
 * @author Dima Kruk
 */
class LogMessage {

    private long timestamp
    private String source
    private Level level
    private String message
    private String stackTrace

    public LogMessage(String source, Level level, String message, long timestamp, String stackTrace) {
        this.timestamp = timestamp
        this.source = source
        this.level = level
        this.message = message
        this.stackTrace = stackTrace
    }

    public String getMessageText(boolean showSource) {
        return message
    }

    Level getLevel() {
        return level
    }

    String getStackTrace() {
        return stackTrace
    }
}
