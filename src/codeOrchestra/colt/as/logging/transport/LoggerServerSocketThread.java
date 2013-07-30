package codeOrchestra.colt.as.logging.transport;

import codeOrchestra.colt.core.socket.ServerSocketThread;

/**
 * @author Alexander Eliseyev
 */
public class LoggerServerSocketThread extends ServerSocketThread {

  public static final int LOGGING_PORT = 6126;

  public LoggerServerSocketThread() {
    super(LOGGING_PORT, LoggerClientSocketHandlerFactory.getInstance());
  }

  protected boolean allowMultipleConnections() {
    return true;
  }
}
