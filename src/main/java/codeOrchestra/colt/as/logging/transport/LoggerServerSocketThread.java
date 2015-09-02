package codeOrchestra.colt.as.logging.transport;

import codeOrchestra.colt.core.socket.ServerSocketThread;
import codeOrchestra.util.SocketUtil;

/**
 * @author Alexander Eliseyev
 */
public class LoggerServerSocketThread extends ServerSocketThread {

  public static final int LOGGING_PORT = SocketUtil.findAvailablePortStartingFrom(6126);

  public LoggerServerSocketThread() {
    super(LOGGING_PORT, LoggerClientSocketHandlerFactory.getInstance());
  }

  protected boolean allowMultipleConnections() {
    return true;
  }
}
