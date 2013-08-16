package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.ProductionBuildModel
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.model.COLTProjectBuildSettings

/**
 * @author Dima Kruk
 */
class COLTAsProjectBuildSettings extends COLTProjectBuildSettings<COLTAsProject> {

    public final SDKModel sdkModel = new SDKModel()
    public final BuildModel buildModel = new BuildModel()
    public final ProductionBuildModel productionBuildModel = new ProductionBuildModel()
    public final RunTargetModel runTargetModel = new RunTargetModel()

    void clear() {
        sdkModel.clear()
        buildModel.clear()
        productionBuildModel.clear()
        runTargetModel.clear()
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

    public boolean useDefaultSDKConfiguration() {
        return sdkModel.useFlexConfig
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
    Closure buildXml() {
        return {
            sdk(sdkModel.buildXml())
            build(buildModel.buildXml())
            production(productionBuildModel.buildXml())
            'run-target'(runTargetModel.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        sdkModel.buildModel(node.sdk)
        buildModel.buildModel(node.build)
        productionBuildModel.buildModel(node.production)
        runTargetModel.buildModel(node.'run-target')
    }
}
