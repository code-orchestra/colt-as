package codeOrchestra.colt.as.logging.model;

/**
 * @author Alexander Eliseyev
 */
public class LoggerScopeWrapper {

  private String id;
  private String name;

  public LoggerScopeWrapper(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
