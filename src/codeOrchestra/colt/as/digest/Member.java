package codeOrchestra.colt.as.digest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class Member {

  private String type;
  private String name;
  private boolean isStatic;
  private MemberKind kind;
  private Visibility visibility;
  
  private List<Parameter> parameters;
  
  public Member(String name, String type, boolean isStatic, MemberKind kind, Visibility visibility) {
    this.type = type;
    this.name = name;
    this.isStatic = isStatic;
    this.kind = kind;
    this.visibility = visibility;
  }
  
  public void addParameter(String name, String type) {
    if (parameters == null) {
      parameters = new ArrayList<Parameter>();
    }
    parameters.add(new Parameter(name, type));
  }
  
  public List<Parameter> getParameters() {
    if (parameters == null) {
      return Collections.emptyList();
    }
    return parameters;
  }
  
  public String getType() {
    return type;
  }

  public Visibility getVisibility() {
    return visibility;
  }

  public String getName() {
    return name;
  }

  public boolean isStatic() {
    return isStatic;
  }

  public MemberKind getKind() {
    return kind;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (isStatic ? 1231 : 1237);
    result = prime * result + ((kind == null) ? 0 : kind.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Member other = (Member) obj;
    if (isStatic != other.isStatic)
      return false;
    if (kind != other.kind)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
  
}
