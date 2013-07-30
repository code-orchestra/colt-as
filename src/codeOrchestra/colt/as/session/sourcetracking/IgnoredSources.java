package codeOrchestra.colt.as.session.sourcetracking;

import java.util.HashSet;
import java.util.Set;

public class IgnoredSources {

  private static Set<String> ignoredClassesSet = new HashSet<String>() {{
    String[] ignoredClasses = new String[] { "codeOrchestra.actionScript.util.CustomPackageName", "codeOrchestra.actionScript.util.ReplaceCallsWith",
        "codeOrchestra.actionScript.util.NeverUsed", "codeOrchestra.actionScript.util.CustomFileName", "codeOrchestra.actionScript.liveCoding.util.CodeUpdateMethod",
        "codeOrchestra.actionScript.util.Live", "codeOrchestra.actionScript.util.Remove", "codeOrchestra.actionScript.util.LiveAssetUpdateListener",
        "codeOrchestra.actionScript.enums.util.CompactPresentation", "codeOrchestra.actionScript.util.Timer", "codeOrchestra.actionScript.util.LiveCodeUpdateListener",
        "codeOrchestra.actionScript.util.Inline", "codeOrchestra.actionScript.util.EnterFrameListener", "codeOrchestra.actionScript.util.AlwaysUsed",
        "codeOrchestra.actionScript.util.RemoveFromStageListener", "codeOrchestra.actionScript.liveCoding.util.LiveCodeDisable", "codeOrchestra.actionScript.util.AddedToStageListener",
        "codeOrchestra.actionScript.util.MixinReplacement", "codeOrchestra.actionScript.util.LiveConsole", "codeOrchestra.actionScript.extensionMethods.methods.InheritExtensionMethod" };
    
    for (int i = 0; i < ignoredClasses.length; i++) {
      add(ignoredClasses[i]);
    }
  }};
  
  public static boolean isIgnoredTrait(String fqName) {
    return ignoredClassesSet.contains(fqName);
  }
  
}
