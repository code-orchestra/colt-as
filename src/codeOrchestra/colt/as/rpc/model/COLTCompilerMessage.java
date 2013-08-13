package codeOrchestra.colt.as.rpc.model;

import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessage;

/**
 * @author Alexander Eliseyev
 */
public class COLTCompilerMessage {

  private String sourcePath;
  private int lineNumber;
  private int columnNumber;
  private String type;
  private String content;
  private String fullMessage;

  public COLTCompilerMessage() {    
  }
  
  public COLTCompilerMessage(CompilerMessage compilerMessage) {
    this.sourcePath = compilerMessage.getSourcePath();
    this.lineNumber = compilerMessage.getLineNumber();
    this.columnNumber = compilerMessage.getColumnNumber();
    this.type = compilerMessage.getType().getPresentation();
    this.content = compilerMessage.getContent();
    this.fullMessage = compilerMessage.getReportMessage();
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public void setColumnNumber(int columnNumber) {
    this.columnNumber = columnNumber;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFullMessage() {
    return fullMessage;
  }
  
}
