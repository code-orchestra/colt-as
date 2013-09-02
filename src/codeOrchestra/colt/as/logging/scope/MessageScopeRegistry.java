package codeOrchestra.colt.as.logging.scope;

import codeOrchestra.colt.core.logging.model.MessageScope;

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

  private Map<String, MessageScope> scopeMap = new HashMap<>();

  private MessageScopeRegistry() {
    scopeMap.put(MAIN_SCOPE_ID, new MessageScope(MAIN_SCOPE_ID, MAIN_SCOPE_NAME));
  }

  public void addOrUpdateScope(String scopeId, String scopeName) {
    synchronized (scopeMap) {
      if ("livecoding".equals(scopeName)) {
        return;
      }
      if ("Main".equals(scopeName)) {
        scopeName = "Log";
      }
      
      MessageScope scope = scopeMap.get(scopeId);
      if (scope == null) {
        scope = new MessageScope(scopeId, scopeName);
        scopeMap.put(scopeId, scope);

          // TODO: implement - add a messages manager
//          MessagesManager.getInstance().addTab(scopeName);
      } else {
        scope.setName(scopeName);
      }
    }
  }

  public MessageScope getScope(String scopeId) {
    synchronized (scopeMap) {
     return scopeMap.get(scopeId);
    }
  }

}
