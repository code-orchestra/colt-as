package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.ProjectBuildSettings

/**
 * @author Dima Kruk
 */
class AsProjectBuildSettings extends ProjectBuildSettings<AsProject> {

    public final SDKModel sdkModel = new SDKModel()
    public final BuildModel buildModel = new BuildModel()
    public final ProductionBuildModel productionBuildModel = new ProductionBuildModel()
    public final RunTargetModel runTargetModel = new RunTargetModel()

    String getWebAddress() {
        return runTargetModel.httpIndex
    }

    void setWebAddress(String webAddress) {
        runTargetModel.httpIndex = webAddress
    }

    public boolean allowCompression() {
        return productionBuildModel.compression
    }

    public boolean allowOptimization() {
        return productionBuildModel.optimization
    }

    public boolean excludeUnusedCode() {
        return buildModel.excludeDeadCode
    }

    public List<String> getExcludedClasses() {
        // TODO: implement
        return Collections.emptyList();
    }

    public boolean interruptCompilationByTimeout() {
        return buildModel.interrupt;
    }

    public int getCompilationTimeout() {
        return buildModel.interruptValue
    }

    public String getFlexSDKPath() {
        return sdkModel.flexSDKPath
    }

    public void setFlexSDKPath(String flexSDKPath) {
        sdkModel.flexSDKPath = flexSDKPath
    }

    public boolean useDefaultSDKConfiguration() {
        return sdkModel.useFlexConfig
    }

    public void setUseDefaultSDKConfiguration(boolean use) {
        sdkModel.useFlexConfig = use;
    }

    public boolean useCustomSDKConfiguration() {
        return sdkModel.useCustomConfig
    }

    public String getCustomConfigPath() {
        return sdkModel.customConfigPath
    }

    public String getMainClass() {
        return buildModel.mainClass
    }

    public String getAirIosScript() {
        return runTargetModel.iosScript
    }

    public String getAirAndroidScript() {
        return runTargetModel.androidScript
    }

    public String getOutputFilename() {
        return buildModel.outputFileName
    }

    public String getOutputPath() {
        return buildModel.outputPath
    }

    public String getTargetPlayerVersion() {
        return buildModel.targetPlayerVersion
    }

    public boolean useFrameworkAsRSL() {
        return buildModel.rsl
    }

    public boolean useNonDefaultLocale() {
        return buildModel.nonDefaultLocale
    }

    public String getLocaleOptions() {
        return buildModel.localeSettings
    }

    public String getCompilerOptions() {
        return buildModel.compilerOptions
    }

    @Override
    Closure buildXml(Project project) {
        return {
            sdk(sdkModel.buildXml(project))
            build(buildModel.buildXml(project))
            production(productionBuildModel.buildXml(project))
            'run-target'(runTargetModel.buildXml(project))
        }
    }

    @Override
    void buildModel(Object node) {
        sdkModel.buildModel(node.sdk)
        buildModel.buildModel(node.build)
        productionBuildModel.buildModel(node.production)
        runTargetModel.buildModel(node.'run-target')
    }

    public Target getLaunchTarget() {
        return Target.parse(runTargetModel.target);
    }
}
