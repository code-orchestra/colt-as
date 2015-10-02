package codeOrchestra.colt.as.compiler.fcsh.make.messages;

/**
 * @author Alexander Eliseyev
 */
public enum MessageType {

  ERROR("Error"),
  WARNING("Warning");

  private String presentation;

  MessageType(String presentation) {
    this.presentation = presentation;
  }

  public String getPresentation() {
    return presentation;
  }

  public static MessageType parse(String value) {
    for (MessageType type : MessageType.values()) {
      if (type.getPresentation().equals(value)) {
        return type;
      }
    }
    throw new RuntimeException("Unknown message type: " + value);
  }
}
