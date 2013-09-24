package codeOrchestra.colt.as.compiler.fcsh.make;

import codeOrchestra.colt.as.compiler.fcsh.*;
import codeOrchestra.colt.as.compiler.fcsh.console.command.impl.LivecodingStartCommand;
import codeOrchestra.colt.as.compiler.fcsh.console.command.impl.LivecodingStopCommand;
import codeOrchestra.colt.as.flex.config.FlexConfig;
import codeOrchestra.colt.as.flex.config.FlexConfigBuilder;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.model.AsProjectBuildSettings;
import codeOrchestra.colt.as.run.Target;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFile;
import codeOrchestra.colt.core.build.BuildException;
import codeOrchestra.colt.core.logging.Logger;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class AsMaker {

    private static final Logger LOG = Logger.getLogger(AsMaker.class.getSimpleName());

    private static boolean sentLiveCodingCommand;
    private boolean isIncremental;
    private List<ASSourceFile> changedFiles;
    private boolean assetMode;

    private boolean skipSecondPhase;
    private boolean productionMode;

    public AsMaker(boolean isIncremental) {
        this.isIncremental = isIncremental;
    }

    public AsMaker(List<ASSourceFile> changedFilesSnapshot) {
        this(changedFilesSnapshot, false);
    }

    public AsMaker(List<ASSourceFile> changedFilesSnapshot, boolean assetMode) {
        this(true);
        this.changedFiles = changedFilesSnapshot;
        this.assetMode = assetMode;
    }

    public void setProductionMode(boolean productionMode) {
        if (productionMode) {
            setSkipSecondPhase(true);
        }
        this.productionMode = productionMode;
    }

    public void setSkipSecondPhase(boolean skipSecondPhase) {
        this.skipSecondPhase = skipSecondPhase;
    }

    public CompilationResult make() throws MakeException, MaximumCompilationsCountReachedException {
        FSCHCompilerKind compilerKind = productionMode ? FSCHCompilerKind.MXMLC : (
                assetMode ? FSCHCompilerKind.COMPC : (
                        isIncremental ? FSCHCompilerKind.INCREMENTAL_COMPC : FSCHCompilerKind.BASE_MXMLC
                )
        );

        AsProject currentProject = AsProject.getCurrentProject();
        AsProjectBuildSettings compilerSettings = currentProject.getProjectBuildSettings();

        boolean airCompile = EnumSet.of(Target.AIR_ANDROID, Target.AIR_IOS, Target.AIR_DESKTOP).contains(compilerSettings.runTargetModel.getRunTarget());

        // Generate & save Flex config
        FlexConfigBuilder flexConfigBuilder = new FlexConfigBuilder(currentProject, isIncremental, changedFiles, assetMode);
        FlexConfig flexConfig = null;
        File flexConfigFile = null;
        try {
            flexConfig = flexConfigBuilder.build();
        } catch (BuildException e) {
            throw new MakeException("Can't build a flex config", e);
        }

        // Production-only options
        if (productionMode) {
            flexConfig.setAllowCompression(compilerSettings.allowCompression());
            flexConfig.setAllowOptimization(compilerSettings.allowOptimization());

            String swfFileName = new File(flexConfig.getOutputPath()).getName();
            flexConfig.setOutputPath(new File(currentProject.getProjectBuildSettings().productionBuildModel.getOutputPath(), swfFileName).getPath());
        }

        try {
            flexConfigFile = flexConfig.saveToFile(currentProject.getFlexConfigPath(compilerKind));
        } catch (BuildException e) {
            throw new MakeException("Can't save a flex config", e);
        }

        // Toggle livecoding mode in fcsh
        if (!productionMode && !assetMode) {
            if (isIncremental) {
                if (!sentLiveCodingCommand) {
                    try {
                        FCSHManager fcshManager = FCSHManager.instance();
                        fcshManager.submitCommand(new LivecodingStartCommand());
                    } catch (FCSHException e) {
                        throw new MakeException("Unable to start livecoding mode in FCSH", e);
                    }
                    sentLiveCodingCommand = true;
                }
            } else {
                if (sentLiveCodingCommand) {
                    try {
                        FCSHManager fcshManager = FCSHManager.instance();
                        fcshManager.submitCommand(new LivecodingStopCommand());
                    } catch (FCSHException e) {
                        throw new MakeException("Unable to stop livecoding mode in FCSH", e);
                    }
                    sentLiveCodingCommand = false;
                }
            }
        }

        // Custom SDK config file
        if (compilerSettings.useCustomSDKConfiguration()) {
            File customConfigFile = new File(compilerSettings.getCustomConfigPath());
            if (!customConfigFile.exists()) {
                throw new MakeException("Custom compile configuration file [" + compilerSettings.getCustomConfigPath() + "] doesn't exist");
            }
        }

        // Base/incremental compilation first phase
        FCSHFlexSDKRunner flexSDKRunner = new FCSHFlexSDKRunner(flexConfigFile, compilerKind, airCompile);
        CompilationResult compilationResult = doCompile(flexSDKRunner, compilerKind != FSCHCompilerKind.BASE_MXMLC);
        if (!compilationResult.isOk()) {
            return compilationResult;
        }

        // Base compilation second phase
        if (!isIncremental && !skipSecondPhase) {
            compilerKind = FSCHCompilerKind.BASE_COMPC;

            flexConfig.setOutputPath(flexConfig.getOutputPath().replaceFirst("\\.swf$", ".swc"));
            flexConfig.setLinkReportFilePath(null);
            flexConfig.setLibrary(true);

            try {
                FlexConfigBuilder.addLibraryClasses(flexConfig, currentProject.getProjectPaths().getSourcePaths());
            } catch (BuildException e) {
                throw new MakeException("Can't save a flex config", e);
            }

            flexConfigFile.delete();
            try {
                flexConfigFile = flexConfig.saveToFile(currentProject.getFlexConfigPath(compilerKind));
            } catch (BuildException e) {
                throw new MakeException("Can't save a flex config", e);
            }

            flexSDKRunner = new FCSHFlexSDKRunner(flexConfigFile, compilerKind, airCompile);
            compilationResult = doCompile(flexSDKRunner, true);
            if (!compilationResult.isOk()) {
                return compilationResult;
            }
        }

        return compilationResult;
    }

    private CompilationResult doCompile(FCSHFlexSDKRunner flexSDKRunner, boolean reportSuccess) throws MakeException, MaximumCompilationsCountReachedException {
        CompilationResult compilationResult = flexSDKRunner.run();

        if (compilationResult == null) {
            String errorMessage = String.format("Compilation timed out");
            LOG.error(errorMessage);
            return CompilationResult.ABORTED;
        }

        if (compilationResult.getErrors() > 0) {
            final String outputFile = flexSDKRunner.getErrorLogFilePath();
            String errorMessage = String.format("Compilation failed with (%d) error(s): %s", compilationResult.getErrors(), outputFile);

            LOG.error(errorMessage);
            return compilationResult;
        }

        if (reportSuccess) {
            LOG.info("Compilation is completed successfully");
        }
        return compilationResult;
    }

}
