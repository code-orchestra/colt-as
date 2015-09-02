package codeOrchestra.colt.as.util;

import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.util.PathUtils;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class ASPathUtils {

    public static boolean checkFlexSDK() {
        File flex_sdk_folder = new File(PathUtils.getApplicationBaseDir(), "flex_sdk");
        File libc = new File(flex_sdk_folder, "liblc");
        return libc.exists();
    }

    public static String getFlexSDKPath() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(productDir, "flex_sdk").getPath();
    }

    public static String getTemplatesDir() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(productDir, "templates").getPath();
    }

    public static String getColtSWCPath() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(new File(productDir, "lib"), "colt.swc").getPath();
    }

    public static String getIncrementalSWFPath(AsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swf";
    }

    public static String getIncrementalOutputDir(AsProject project) {
        return project.getOutputDir().getPath() + File.separator + "livecoding";
    }

    public static String getSourceIncrementalSWCPath(AsProject project) {
        return project.getOutputDir().getPath() + File.separator + project.getName() + "_liveCoding.swc";
    }

    public static String getTargetIncrementalSWCPath(AsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swc";
    }

}
