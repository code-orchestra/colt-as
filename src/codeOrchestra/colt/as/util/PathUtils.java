package codeOrchestra.colt.as.util;

import codeOrchestra.colt.as.model.COLTAsProject;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class PathUtils {

    public static String getFlexSDKPath() {
        File productDir = getApplicationBaseDir();
        return new File(productDir, "flex_sdk").getPath();
    }

    public static String getTemplatesDir() {
        File productDir = getApplicationBaseDir();
        return new File(productDir, "templates").getPath();
    }

    public static String getColtSWCPath() {
        File productDir = getApplicationBaseDir();
        return new File(new File(productDir, "lib"), "colt.swc").getPath();
    }

    public static String getIncrementalSWFPath(COLTAsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swf";
    }

    public static String getIncrementalOutputDir(COLTAsProject project) {
        return project.getOutputDir().getPath() + File.separator + "livecoding";
    }

    public static String getSourceIncrementalSWCPath(COLTAsProject project) {
        return project.getOutputDir().getPath() + File.separator + project.getName() + "_liveCoding.swc";
    }

    public static String getTargetIncrementalSWCPath(COLTAsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swc";
    }

    public static File getApplicationBaseDir() {
        // TODO: implement!
        return new File("");
    }

}
