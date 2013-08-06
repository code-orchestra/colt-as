package codeOrchestra.colt.as.ui.log

import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
class LogMessage {

    long timestamp
    String source
    Level level
    String message
    String stackTrace

    public LogMessage(String source, Level level, String message, long timestamp, String stackTrace) {
        this.timestamp = timestamp
        this.source = source
        this.level = level
        this.message = message
        this.stackTrace = stackTrace
    }
}
