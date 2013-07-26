package codeOrchestra.colt.as.model;

import codeOrchestra.colt.core.model.COLTProjectBuildSettings;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsProjectBuildSettings extends COLTProjectBuildSettings<COLTAsProject> {

    public boolean allowComression() {
        // TODO: implement
        return false;
    }

    public boolean allowOptimization() {
        // TODO: implement
        return false;
    }

    public boolean excludeUnusedCode() {
        // TODO: implement
        return false;
    }

    public List<String> getExcludedClasses() {
        // TODO: implement
        return null;
    }

    public boolean interruptCompilationByTimeout() {
        // TODO: implement
        return false;
    }

    public int getCompilationTimeout() {
        // TODO: implement
        return 0;
    }

    public String getFlexSDKPath() {
        // TODO: implement
        return "";
    }

    public void setFlexSDKPath(String flexSDKPath) {
        // TODO: implement
    }

    public boolean useDefaultSDKConfiguration() {
        // TODO: implement
        return false;
    }

    public boolean useCustomSDKConfiguration() {
        // TODO: implement
        return false;
    }

    public String getCustomConfigPath() {
        // TODO: implement
        return "";
    }

    public void setCustomConfigPath(String customConfigPath) {
        // TODO: implement
    }

    public String getMainClass() {
        // TODO: implement
        return "";
    }

    public void setMainClass(String mainClass) {
        // TODO: implement
    }

    public String getAirIosScript() {
        // TODO: implement
        return "";
    }

    public String getAirAndroidScript() {
        // TODO: implement
        return "";
    }

    public String getOutputFilename() {
        // TODO: implement
        return "";
    }

    public void setOutputFilename(String outputFileName) {
        // TODO: implement
    }

    public String getOutputPath() {
        // TODO: implement
        return "";
    }

    public void setOutputPath(String outputPath) {
//        String previousOutputPath = getPreferenceStore().getString("outputPath");
//        getPreferenceStore().setValue("outputPath", outputPath);
//
//        if (!StringUtils.equals(outputPath, previousOutputPath)) {
//            LCSProject.getCurrentProject().updateExternalPaths();
//        }
        //TODO: implement
    }

    public String getTargetPlayerVersion() {
        // TODO: implement
        return "";
    }

    public void setTargetPlayerVersion(String targetPlayerVersion) {
//        getPreferenceStore().setValue("targetPlayerVersion", targetPlayerVersion);
        // TODO: implement
    }

    public boolean useFrameworkAsRSL() {
        // TODO: implement
        return false;
    }

    public boolean useNonDefaultLocale() {
        // TODO: implement
        return false;
    }

    public String getLocaleOptions() {
        // TODO: implement
        return "";
    }

    public String getCompilerOptions() {
        // TODO: implement
        return "";
    }

    public void setCompilerOptions(String compilerOptions) {
        // TODO: implement
    }

}
