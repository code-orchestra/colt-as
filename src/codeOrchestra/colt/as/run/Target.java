package codeOrchestra.colt.as.run;

public enum Target {

  SWF,
  WEB_ADDRESS,
  AIR_IOS,
  AIR_ANDROID;
  
  public static Target parse(String str) {
    for (Target target : values()) {
      if (target.name().equals(str)) {
        return target;
      }
    }
    return SWF;
  }
  
}
