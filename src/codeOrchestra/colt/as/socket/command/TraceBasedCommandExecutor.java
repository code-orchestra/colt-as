package codeOrchestra.colt.as.socket.command;

import codeOrchestra.colt.as.logging.model.LoggerMessageEncoder;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.socket.FlashClientSocketHandler;

import java.net.Socket;

/**
 * @author Alexander Eliseyev
 */
public class TraceBasedCommandExecutor extends FlashClientSocketHandler {

  public TraceBasedCommandExecutor(Socket clientSocket) {
    super(clientSocket, "*", String.valueOf(LoggerServerSocketThread.LOGGING_PORT));
  }

  @Override
  protected void handleMessage(String str) {
    boolean handled;
    try {
      handled = TraceCommandManager.getInstance().handleMessage(LoggerMessageEncoder.encode(str), this);
    } catch (Throwable t) {
      // TODO: improve logging
      // LOG.error("Error while handling trace message: " + str, t);
      return;
    }

    if (!handled) {
      // TODO: improve logging
      // LOG.error("No trace handler for command: " + str);
    }
  }

}
