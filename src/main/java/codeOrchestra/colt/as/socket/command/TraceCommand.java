package codeOrchestra.colt.as.socket.command;

import codeOrchestra.colt.core.logging.model.LoggerMessage;
import codeOrchestra.colt.core.socket.ClientSocketHandler;

/**
 * @author Alexander Eliseyev
 */
public interface TraceCommand {
  boolean isApplicable(LoggerMessage message);
  void execute(LoggerMessage message, ClientSocketHandler clientSocketHandler);
}