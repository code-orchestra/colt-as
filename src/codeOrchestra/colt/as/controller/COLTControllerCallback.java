package codeOrchestra.colt.as.controller;

/**
 * @author Alexander Eliseyev
 */
public interface COLTControllerCallback<S, E> {
  
  void onComplete(S successResult);
  
  void onError(Throwable t, E errorResult);

}
