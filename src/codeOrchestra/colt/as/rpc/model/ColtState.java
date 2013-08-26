package codeOrchestra.colt.as.rpc.model;

/**
 * @author Alexander Eliseyev
 */
public class ColtState {
  
  private ColtConnection[] activeConnections;
  
  public ColtConnection[] getActiveConnections() {
    return activeConnections;
  }
  
  public void setActiveConnections(ColtConnection[] activeConnections) {
    this.activeConnections = activeConnections;
  }

}
