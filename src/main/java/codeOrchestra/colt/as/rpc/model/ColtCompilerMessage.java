package codeOrchestra.colt.as.rpc.model;

import codeOrchestra.colt.as.compiler.fcsh.make.messages.CompilerMessage;

/**
 * @author Alexander Eliseyev
 */
public class ColtCompilerMessage {

  private String type;
  private String content;

  public ColtCompilerMessage(CompilerMessage compilerMessage) {
    this.type = compilerMessage.getType().getPresentation();
    this.content = compilerMessage.getContent();
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
}