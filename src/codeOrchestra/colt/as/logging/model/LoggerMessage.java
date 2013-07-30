package codeOrchestra.colt.as.logging.model;

import codeOrchestra.colt.core.logging.Level;
import codeOrchestra.util.NameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class LoggerMessage {

  private Level severity;
  private String message;
  private List<LoggerScopeWrapper> scopes = new ArrayList<LoggerScopeWrapper>();
  private String rootFQName;
  private long timestamp;
  private String command;
  private String stackTrace;

  private LoggerMessage(Builder builder) {
    this.message = builder.message;
    this.severity = builder.severity;
    this.scopes = builder.scopes;
    this.rootFQName = builder.rootFQName;
    this.timestamp = builder.timestamp;
    this.command = builder.command;
    this.stackTrace = builder.stackTrace;
  }

  public boolean hasStackTrace() {
    return stackTrace != null;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public List<LoggerScopeWrapper> getScopes() {
    return scopes;
  }

  public String getRootSimpleName() {
    if (rootFQName != null) {
      return NameUtil.shortNameFromLongName(rootFQName);
    }
    return "";
  }

  public String getMessage() {
    return message;
  }

  public Level getSeverity() {
    return severity;
  }

  public String getCommand() {
    return command;
  }

  // Freakish builder pattern!
  public static class Builder {
    private Level severity;
    private String message;
    private List<LoggerScopeWrapper> scopes = new ArrayList<LoggerScopeWrapper>();
    private String rootFQName;
    private long timestamp;
    public String command;
    private String stackTrace;

    public Builder() {
    }

    public Builder command(String command) {
      this.command = command;
      return this;
    }
    public Builder stackTrace(String stackTrace) {
      this.stackTrace = stackTrace;
      return this;
    }
    public Builder timestamp(long timestamp) {
      this.timestamp = timestamp;
      return this;
    }
    public Builder severity(Level severity) {
      this.severity = severity;
      return this;
    }
    public Builder message(String message) {
      this.message = message;
      return this;
    }
    public Builder scopes(List<LoggerScopeWrapper> scopes) {
      this.scopes = scopes;
      return this;
    }
    public Builder rootFQName(String rootFQName) {
      this.rootFQName = rootFQName;
      return this;
    }
    public LoggerMessage build() {
      return new LoggerMessage(this);
    }
  }

}
