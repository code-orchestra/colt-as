package codeOrchestra.colt.as.compiler.fcsh.make.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class CompilerMessagesWrapper {

  private static final int MAX_SMART_MESSAGES_COUNT = 10;
  
  private List<CompilerMessage> messages;
  private int errorCount;
  private int warningsCount;

  public CompilerMessagesWrapper(List<CompilerMessage> messages) {
    this.messages = messages;
    for (CompilerMessage compilerMessage : messages) {
      switch (compilerMessage.getType()) {
        case ERROR: errorCount++; break;
        case WARNING: warningsCount++; break;
      }
    }
  }

  public List<CompilerMessage> getMessages() {
    return messages;
  }
  
  public List<CompilerMessage> getMessagesSmart() {
    List<CompilerMessage> smartMessages = new ArrayList<CompilerMessage>();
    
    if (errorCount > 0) {
      Iterator<CompilerMessage> messagesIterator = messages.iterator();
      while (smartMessages.size() < MAX_SMART_MESSAGES_COUNT && messagesIterator.hasNext()) {
        CompilerMessage message = messagesIterator.next();
        if (message.getType() == MessageType.ERROR) {
          smartMessages.add(message);
        }
      }
    } else {
      for (CompilerMessage compilerMessage : messages) {
        if (smartMessages.size() < MAX_SMART_MESSAGES_COUNT) {
          smartMessages.add(compilerMessage);
        }
      }
    }
    
    return smartMessages;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public int getWarningsCount() {
    return warningsCount;
  }

  public boolean isEmpty() {
    return getErrorCount() == 0 && getWarningsCount() == 0;
  }
}
