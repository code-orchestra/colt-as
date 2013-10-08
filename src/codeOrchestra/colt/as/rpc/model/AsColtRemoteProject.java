package codeOrchestra.colt.as.rpc.model;

import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.run.LauncherType;
import codeOrchestra.colt.core.model.Project;
import codeOrchestra.colt.core.rpc.model.ColtRemoteProject;

/**
 * @author Alexander Eliseyev
 */
public class AsColtRemoteProject implements ColtRemoteProject {

    private String parentIDEProjectPath;

    private String path;
    private String name;

    private String[] sources;
    private String[] libraries;
    private String[] assets;

    private String htmlTemplateDir;

    private String flashPlayerPath;
    private String flexSDKPath;
    private String customConfigPath;

    private String mainClass;

    private String outputFileName;
    private String outputPath;
    private String targetPlayerVersion;
    private String compilerOptions;

    private String launchTarget; // corresponds to codeOrchestra.colt.as.run.Target

    @Override
    public void copyToProject(Project project) {
        // TODO: implement
    }

    public void copyTo(AsProject coltProject) {
        if (sources != null) {
            coltProject.getProjectPaths().addSources(sources);
        }
        if (libraries != null) {
            coltProject.getProjectPaths().addLibraries(libraries);
        }
        if (assets != null) {
            coltProject.getProjectPaths().addAssets(libraries);
        }
        if (htmlTemplateDir != null) {
            coltProject.getProjectPaths().setHtmlTemplatePath(htmlTemplateDir);
        }
        if (flashPlayerPath != null) {
            coltProject.getProjectLiveSettings().launcherModel.setFlashPlayerPath(flashPlayerPath);
            coltProject.getProjectLiveSettings().launcherModel.setLauncherType(LauncherType.FLASH_PLAYER.name());
        }
        if (flexSDKPath != null) {
            coltProject.getProjectBuildSettings().sdkModel.setFlexSDKPath(flexSDKPath);
        }
        if (customConfigPath != null) {
            coltProject.getProjectBuildSettings().sdkModel.setCustomConfigPath(customConfigPath);
        }
        if (mainClass != null) {
            coltProject.getProjectBuildSettings().buildModel.setMainClass(mainClass);
        }
        if (outputFileName != null) {
            coltProject.getProjectBuildSettings().buildModel.setOutputFileName(outputFileName);
        }
        if (outputPath != null) {
            coltProject.getProjectBuildSettings().buildModel.setOutputPath(outputPath);
        }
        if (targetPlayerVersion != null) {
            coltProject.getProjectBuildSettings().buildModel.setTargetPlayerVersion(targetPlayerVersion);
        }
        if (compilerOptions != null) {
            coltProject.getProjectBuildSettings().buildModel.setCompilerOptions(compilerOptions);
        }
    }

    public String getParentIDEProjectPath() {
        return parentIDEProjectPath;
    }

    public void setParentIDEProjectPath(String parentIDEProjectPath) {
        this.parentIDEProjectPath = parentIDEProjectPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }

    public String[] getLibraries() {
        return libraries;
    }

    public void setLibraries(String[] libraries) {
        this.libraries = libraries;
    }

    public String[] getAssets() {
        return assets;
    }

    public void setAssets(String[] assets) {
        this.assets = assets;
    }

    public String getHtmlTemplateDir() {
        return htmlTemplateDir;
    }

    public void setHtmlTemplateDir(String htmlTemplateDir) {
        this.htmlTemplateDir = htmlTemplateDir;
    }

    public String getFlashPlayerPath() {
        return flashPlayerPath;
    }

    public void setFlashPlayerPath(String flashPlayerPath) {
        this.flashPlayerPath = flashPlayerPath;
    }

    public String getFlexSDKPath() {
        return flexSDKPath;
    }

    public void setFlexSDKPath(String flexSDKPath) {
        this.flexSDKPath = flexSDKPath;
    }

    public String getCustomConfigPath() {
        return customConfigPath;
    }

    public void setCustomConfigPath(String customConfigPath) {
        this.customConfigPath = customConfigPath;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getTargetPlayerVersion() {
        return targetPlayerVersion;
    }

    public void setTargetPlayerVersion(String targetPlayerVersion) {
        this.targetPlayerVersion = targetPlayerVersion;
    }

    public String getCompilerOptions() {
        return compilerOptions;
    }

    public void setCompilerOptions(String compilerOptions) {
        this.compilerOptions = compilerOptions;
    }

    @Override
    public String getType() {
        return "AS";
    }
}
