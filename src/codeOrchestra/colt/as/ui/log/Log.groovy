package codeOrchestra.colt.as.ui.log

import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.logging.LoggerService
import codeOrchestra.colt.core.tracker.GAController
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.colt.core.ui.components.log.LogWebView

/**
 * @author Dima Kruk
 */
class Log implements LoggerService {

    @Lazy static Log instance = new Log()
    @Lazy LogWebView logWebView = new LogWebView()

    private Log() {
        GAController.instance.registerPage(logWebView, "/as/asLog.html", "asLog")
    }

    @Override
    synchronized void log(String source, String message, List<String> scopeIds, long timestamp, Level level, String stackTrace) {
        logWebView.logMessages.add(new LogMessage(source, level, message, timestamp, stackTrace))
    }

    @Override
    synchronized void clear(Level level) {
        ArrayList<LogMessage> newMessages = logWebView.logMessages.asList().grep{LogMessage m ->  m.level == level }
        logWebView.logMessages.clear()
        logWebView.logMessages.addAll(newMessages)

        //todo: нужно clear чтобы убрать? почему тогда фильтр по уровня соообщения?
        //todo спросить у Саши что он имел ввиду, полагаю это загадочный Log.ALL нужен чтобы работала очистка
        //todo: полагаю потом нужно убрать аргумент и оставить только строчку logWebView.logMessages.clear()
//        def iterator = logWebView.logMessages.iterator()
//        while (iterator.hasNext()) {
//            def message = iterator.next()
//            if (level == message.getLevel()) {
//                iterator.remove();
//            }
//        }
    }
}
