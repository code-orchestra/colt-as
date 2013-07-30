package codeOrchestra.colt.as.digest;

/**
 * @author Alexander Eliseyev
 */
public class EmbedDigest {

  private String source;
  private String mimeType;
  private String fullPath;
  
  public EmbedDigest(String source, String mimeType, String fullPath) {
    this.source = source;
    this.mimeType = mimeType;
    this.fullPath = fullPath;
  }

  public String getSource() {
    return source;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getFullPath() {
    return fullPath;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      EmbedDigest that = (EmbedDigest) o;

      if (fullPath != null ? !fullPath.equals(that.fullPath) : that.fullPath != null) return false;
      if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
      if (source != null ? !source.equals(that.source) : that.source != null) return false;

      return true;
  }

  @Override
  public int hashCode() {
      int result = source != null ? source.hashCode() : 0;
      result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
      result = 31 * result + (fullPath != null ? fullPath.hashCode() : 0);
      return result;
  }
  
}
