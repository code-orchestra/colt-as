package codeOrchestra.colt.as.socket.command.impl;

import codeOrchestra.colt.as.logging.model.LoggerMessage;
import codeOrchestra.colt.as.logging.model.LoggerMessageEncoder;
import codeOrchestra.colt.as.logging.model.LoggerScopeWrapper;
import codeOrchestra.colt.as.logging.scope.MessageScopeRegistry;
import codeOrchestra.colt.as.socket.command.TraceCommand;
import codeOrchestra.colt.core.logging.Level;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.socket.ClientSocketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class LoggerTraceCommand implements TraceCommand {

  @Override
  public boolean isApplicable(LoggerMessage message) {
    return LoggerMessageEncoder.isLegitSeverityLevel(message.getCommand());
  }

  @Override
  public void execute(final LoggerMessage loggerMessage, ClientSocketHandler clientSocketHandler) {
    // Get the logger by the root name
    String rootSimpleName = loggerMessage.getRootSimpleName();
    Logger asLogger = Logger.getLogger(rootSimpleName != null ? rootSimpleName : "trace");

    // Severity
    Level severity = loggerMessage.getSeverity();

    // Scopes
    List<String> scopeIds = null;
    if (!loggerMessage.getScopes().isEmpty()) {
      scopeIds = new ArrayList<String>();
      for (LoggerScopeWrapper scopeWrapper : loggerMessage.getScopes()) {
        MessageScopeRegistry.getInstance().addOrUpdateScope(scopeWrapper.getId(), scopeWrapper.getName());

        scopeIds.add(scopeWrapper.getId());
      }
    }
    
    // Timestamp
    long timestamp = loggerMessage.getTimestamp();

    if (severity == Level.INFO || severity == Level.DEBUG) {
      asLogger.info(loggerMessage.getMessage(), scopeIds, timestamp);
    } else if (severity == Level.WARN) {
      asLogger.warning(loggerMessage.getMessage(), scopeIds, timestamp);
    } else if (severity == Level.ERROR || severity == Level.FATAL) {
      asLogger.error(loggerMessage.getMessage(), scopeIds, timestamp, loggerMessage.getStackTrace());
    }
  }

}
