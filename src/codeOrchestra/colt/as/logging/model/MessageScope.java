package codeOrchestra.colt.as.logging.model;

/**
 * @author Alexander Eliseyev
 */
public class MessageScope {

  private String scopeId;
  private String name;

  public MessageScope(String scopeId, String name) {
    this.scopeId = scopeId;
    this.name = name;
  }

  public MessageScope(String scopeId) {
    this.scopeId = scopeId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getScopeId() {
    return scopeId;
  }

}
