package codeOrchestra.colt.as.ui.log

import codeOrchestra.colt.core.logging.Level
import codeOrchestra.colt.core.logging.LoggerService
import codeOrchestra.colt.core.ui.components.log.LogMessage
import codeOrchestra.colt.core.ui.components.log.LogWebView
import javafx.collections.FXCollections
import javafx.collections.ObservableList as FXObservableList
import javafx.scene.Node as FXNode
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.util.Callback

/**
 * @author Dima Kruk
 */
class Log implements LoggerService {
    LogWebView logWebView

    public FXNode getPane() {
        if (!logWebView) {
            logWebView = new LogWebView()
            logWebView.logMessages.addAll(TestLog.createTestLogList())//todo: remove this
        }
        return logWebView
    }

    @Override
    void log(String source, String message, List<String> scopeIds, long timestamp, Level level, String stackTrace) {
        if (logWebView) {
            logWebView.logMessages.add(new LogMessage(source, level, message, timestamp, stackTrace))
        }
    }
}
