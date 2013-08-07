package codeOrchestra.colt.as.ui.air

/**
 * @author Dima Kruk
 */
public enum AirOptionType {
    STRING,
    PASSWORD,
    FILE,
    DIRECTORY

    public boolean isFileType() {
        return this == FILE || this == DIRECTORY
    }
}