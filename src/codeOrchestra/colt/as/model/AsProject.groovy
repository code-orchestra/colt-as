package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.compiler.fcsh.FSCHCompilerKind
import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.core.ColtProjectManager
import codeOrchestra.colt.core.model.Project
import codeOrchestra.util.StringUtils

/**
 * @author Dima Kruk
 */
class AsProject extends Project {

    private final AsProjectPaths projectPaths = new AsProjectPaths()
    private final AsProjectBuildSettings buildSettings = new AsProjectBuildSettings()
    private final AsProjectLiveSettings liveSettings = new AsProjectLiveSettings()

    @Override
    AsProjectPaths getProjectPaths() {
        return projectPaths
    }

    @Override
    AsProjectLiveSettings getProjectLiveSettings() {
        return liveSettings
    }

    @Override
    AsProjectBuildSettings getProjectBuildSettings() {
        return buildSettings
    }

    @Override
    String getProjectType() {
        return "AS";
    }

    @Override
    Closure buildXml() {
        return {
            paths(projectPaths.buildXml())
            build(buildSettings.buildXml())
            live(liveSettings.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        super.buildModel(node)

        projectPaths.buildModel(node.paths)
        buildSettings.buildModel(node.build)
        liveSettings.buildModel(node.live)
    }

    static AsProject getCurrentProject() {
        return (AsProject) ColtProjectManager.instance.currentProject
    }

    public File getOrCreateIncrementalSourcesDir() {
        File incrementalSourcesDir = new File(getBaseDir(), "incremental")
        if (!incrementalSourcesDir.exists()) {
            incrementalSourcesDir.mkdir()
        }
        return incrementalSourcesDir
    }

    public File getLinkReportFile() {
        return new File(getOutputDir(), "link-report.xml")
    }

    public File getOutputDir() {
        String outputPath = buildSettings.getOutputPath()
        if (StringUtils.isEmpty(outputPath)) {
            return getDefaultOutputDir()
        }
        return new File(outputPath);
    }

    public File getDefaultOutputDir() {
        return new File(getBaseDir(), "colt_output")
    }

    public File getDigestsDir() {
        return new File(getBaseDir(), "digests")
    }

    public File getIncrementalOutputDir() {
        new File(getOutputDir(), "livecoding")
    }

    public void initPaths() {
        File outputDir = getOutputDir();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputIncrementalDir = getIncrementalOutputDir();
        if (!outputIncrementalDir.exists()) {
            outputIncrementalDir.mkdirs();
        }

        File digestsDir = getDigestsDir();
        if (!digestsDir.exists()) {
            digestsDir.mkdirs();
        }
    }

    public String getFlexConfigPath(FSCHCompilerKind compilerKind) {
        return new File(getBaseDir(), getName() + "_" + compilerKind.getCommandName() + "_flex_config.xml").getPath()
    }


    public void initDefaultValues() {
        buildSettings.flexSDKPath = codeOrchestra.colt.as.util.ASPathUtils.flexSDKPath
        buildSettings.setUseDefaultSDKConfiguration(true);
        FlexSDKManager manager = FlexSDKManager.instance
        List<String> versions = manager.getAvailablePlayerVersions(new File(buildSettings.flexSDKPath))
        buildSettings.buildModel.targetPlayerVersion = versions.first()
        buildSettings.sdkModel.isValidFlexSDK = true

        liveSettings.launcherType = codeOrchestra.colt.as.run.LauncherType.DEFAULT;
        liveSettings.liveMethods = codeOrchestra.colt.as.run.LiveMethods.ANNOTATED;

        buildSettings.buildModel.outputPath = getDefaultOutputDir().path
    }
}
