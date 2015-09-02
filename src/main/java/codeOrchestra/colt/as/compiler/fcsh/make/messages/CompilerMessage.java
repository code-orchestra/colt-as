package codeOrchestra.colt.as.compiler.fcsh.make.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alexander Eliseyev
 */
@SuppressWarnings("serial")
public class CompilerMessage implements Serializable {

  private final static String MESSAGE_PATTERN = "^\\s*((?:\\w:)?[^:]+)\\((\\d+)\\):\\s+col:\\s+(\\d+):?\\s+(Error|Warning):\\s+(.+)\\s*$";

  public static final String TRACE_INFO = ".trace.info";

  // Match group numbers:

  // 1 - source path
  // 2 - line number
  // 3 - column number
  // 4 - message type
  // 5 - message contents

  private String sourcePath;
  private int lineNumber;
  private int columnNumber;
  private MessageType type;
  private String content;

  public CompilerMessage(MessageType type, String content) {
    this.type = type;
    this.content = content;
  }

  private CompilerMessage(Matcher messageMatcher) {
    String sourcePathRaw = messageMatcher.group(1);
    this.sourcePath = sourcePathRaw.contains("^") ? sourcePathRaw.substring(sourcePathRaw.indexOf("^") + 1, sourcePathRaw.length()).trim() : sourcePathRaw;

    this.lineNumber = Integer.parseInt(messageMatcher.group(2));
    this.columnNumber = Integer.parseInt(messageMatcher.group(3));
    this.type = MessageType.parse(messageMatcher.group(4));
    this.content = messageMatcher.group(5);
  }

  public static CompilerMessagesWrapper extract(String compilerOutput) {
    List<CompilerMessage> compilerMessages = new ArrayList<>();

    Pattern pattern = Pattern.compile(MESSAGE_PATTERN, Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(compilerOutput);

    while (matcher.find()) {
      compilerMessages.add(new CompilerMessage(matcher));
    }

    return new CompilerMessagesWrapper(compilerMessages);
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public MessageType getType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CompilerMessage that = (CompilerMessage) o;

    if (columnNumber != that.columnNumber) return false;
    if (lineNumber != that.lineNumber) return false;
    if (content != null ? !content.equals(that.content) : that.content != null) return false;
    if (sourcePath != null ? !sourcePath.equals(that.sourcePath) : that.sourcePath != null) return false;
    if (type != that.type) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = sourcePath != null ? sourcePath.hashCode() : 0;
    result = 31 * result + lineNumber;
    result = 31 * result + columnNumber;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CompilerMessage{" +
      "sourcePath='" + sourcePath + '\'' +
      ", lineNumber=" + lineNumber +
      ", columnNumber=" + columnNumber +
      ", type=" + type +
      ", content='" + content + '\'' +
      '}';
  }

  public String getReportMessage() {
    return getContent() + " (line: " + lineNumber + ", source: " + sourcePath + ")";
  }
}
