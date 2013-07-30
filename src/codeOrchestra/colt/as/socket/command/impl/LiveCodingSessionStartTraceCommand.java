package codeOrchestra.colt.as.socket.command.impl;

import codeOrchestra.colt.as.ASLiveCodingManager;
import codeOrchestra.colt.as.logging.model.LoggerMessage;
import codeOrchestra.colt.as.socket.command.TraceCommand;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.socket.ClientSocketHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class LiveCodingSessionStartTraceCommand implements TraceCommand {

  private static final String START_SESSION_COMMAND = "start-live-coding-session";

  @Override
  public boolean isApplicable(LoggerMessage message) {
    return START_SESSION_COMMAND.equals(message.getCommand());
  }

  @Override
  public void execute(LoggerMessage message, ClientSocketHandler clientSocketHandler) {
    ASLiveCodingManager liveCodingManager = (ASLiveCodingManager) ServiceProvider.get(LiveCodingManager.class);

    // RF-1307 - we do this check in case no project is open, but a live-coding-compiled swf is run
    if (liveCodingManager != null) {
      String sessionId = message.getMessage();
      String[] sessionIdSplit = sessionId.split(":");
      
      String broadcastId = sessionIdSplit[0];
      String clientId = sessionIdSplit[1];
      String clientInfo = sessionIdSplit[2];
      
      String decoded = null;
      try {      
        decoded = URLDecoder.decode(clientInfo, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        // can't happen
      }
      
      Map<String, String> clientInfoDictionary = new HashMap<String, String>();
      String[] decodedSplit = decoded.split("&");
      for (int i = 0; i < decodedSplit.length; i++) {
        String keyValue = decodedSplit[i];
        String[] keyValueSplit = keyValue.split("=");      
        clientInfoDictionary.put(keyValueSplit[0], keyValueSplit[1]);
      }
      
      liveCodingManager.startSession(broadcastId, clientId, clientInfoDictionary, clientSocketHandler);
    }
  }
   
}
