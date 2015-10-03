package codeOrchestra.colt.as.logging.scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public final class MessageScopeRegistry {

  private static final MessageScopeRegistry INSTANCE = new MessageScopeRegistry();

  public static final String MAIN_SCOPE_ID = "0";
  private static final String MAIN_SCOPE_NAME = "Log";

  public static MessageScopeRegistry getInstance() {
    return INSTANCE;
  }

  private final Map<String, String> scopeMap = new HashMap<>();

  private MessageScopeRegistry() {
    scopeMap.put(MAIN_SCOPE_ID, MAIN_SCOPE_NAME);
  }

  public void addOrUpdateScope(String scopeId, String scopeName) {
    synchronized (scopeMap) {
      if ("livecoding".equals(scopeName)) {
        return;
      }
      if ("Main".equals(scopeName)) {
        scopeName = MAIN_SCOPE_NAME;
      }
      scopeMap.put(scopeId, scopeName);
      // TODO: implement - add a messages manager
      // MessagesManager.getInstance().addTab(scopeName);
    }
  }
}