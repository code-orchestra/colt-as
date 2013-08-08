package codeOrchestra.colt.as.air.ui

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