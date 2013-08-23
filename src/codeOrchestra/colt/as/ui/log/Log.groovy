package codeOrchestra.colt.as.ui.log

import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.logging.LoggerService
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.colt.core.ui.components.log.LogWebView

/**
 * @author Dima Kruk
 */
class Log implements LoggerService {
    LogWebView logWebView = new LogWebView()

    Log() {
//        logWebView.logMessages.addAll(TestLog.createTestLogList())
    }

    @Override
    synchronized void log(String source, String message, List<String> scopeIds, long timestamp, Level level, String stackTrace) {
        logWebView.logMessages.add(new LogMessage(source, level, message, timestamp, stackTrace))
    }

    @Override
    synchronized void clear(Level level) {
        def iterator = logWebView.logMessages.iterator()
        while (iterator.hasNext()) {
            def message = iterator.next()
            if (level == message.getLevel()) {
                iterator.remove();
            }
        }
    }
}
