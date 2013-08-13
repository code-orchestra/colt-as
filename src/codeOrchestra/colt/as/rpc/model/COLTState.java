package codeOrchestra.colt.as.rpc.model;

/**
 * @author Alexander Eliseyev
 */
public class COLTState {
  
  private COLTConnection[] activeConnections;
  
  public COLTConnection[] getActiveConnections() {
    return activeConnections;
  }
  
  public void setActiveConnections(COLTConnection[] activeConnections) {
    this.activeConnections = activeConnections;
  }

}
