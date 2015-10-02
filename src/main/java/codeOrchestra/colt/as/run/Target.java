package codeOrchestra.colt.as.run;

/**
 * @author Alexander Eliseyev
 */
public enum Target {

    SWF("Local SWF"),
    WEB_ADDRESS("via HTTP"),
    AIR_IOS("AIR for iOS"),
    AIR_ANDROID("AIR for Android"),
    AIR_DESKTOP("AIR for desktop");

    private String description;

    Target(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Target parse(String str) {
        for (Target target : values()) {
            if (target.name().equals(str)) {
                return target;
            }
        }
        return SWF;
    }

}
