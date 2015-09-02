package codeOrchestra.colt.as.socket;

import codeOrchestra.colt.core.socket.ClientSocketHandler;

import java.net.Socket;

/**
 * @author Alexander Eliseyev
 */
public abstract class FlashClientSocketHandler extends ClientSocketHandler {

  public static final String POLICY_REQUEST = "<policy-file-request/>";

  private String policyHost;
  private String policyPorts;

  public FlashClientSocketHandler(Socket clientSocket, String policyHost, String policyPorts) {
    super(clientSocket);
    this.policyHost = policyHost;
    this.policyPorts = policyPorts;
  }

  protected final void handle(String str) {
    if (POLICY_REQUEST.equals(str)) {
      socketWrite(buildPolicy());
      return;
    }
    handleMessage(str);
  }

  private String buildPolicy()  {
    StringBuilder policyBuffer = new StringBuilder();

    policyBuffer.append("<?xml version=\"1.0\"?>");
    policyBuffer.append("<cross-domain-policy>");
    policyBuffer.append("<allow-access-from domain=\"").append(policyHost)
      .append("\" to-ports=\"").append(policyPorts).append("\" secure=\"false\" />");
    policyBuffer.append("</cross-domain-policy>");

    return policyBuffer.toString();
  }

  protected abstract void handleMessage(String str);

}
