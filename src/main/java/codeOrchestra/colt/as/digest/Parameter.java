package codeOrchestra.colt.as.digest;

/**
 * @author Alexander Eliseyev
 */
public class Parameter {

  private String name;
  private String type;
  
  public Parameter(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }
  
}
