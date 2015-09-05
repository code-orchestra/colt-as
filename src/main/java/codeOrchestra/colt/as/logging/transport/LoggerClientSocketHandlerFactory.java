package codeOrchestra.colt.as.logging.transport;

import codeOrchestra.colt.as.socket.command.TraceBasedCommandExecutor;
import codeOrchestra.colt.core.socket.ClientSocketHandler;
import codeOrchestra.colt.core.socket.ClientSocketHandlerFactory;

import java.net.Socket;

/**
 * @author Alexander Eliseyev
 */
public final class LoggerClientSocketHandlerFactory implements ClientSocketHandlerFactory {

  private final static ClientSocketHandlerFactory INSTANCE = new LoggerClientSocketHandlerFactory();

  public static ClientSocketHandlerFactory getInstance() {
    return INSTANCE;
  }

  private LoggerClientSocketHandlerFactory() {
  }

  public ClientSocketHandler createHandler(Socket socket) {
    return new TraceBasedCommandExecutor(socket);
  }
}
