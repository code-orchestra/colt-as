package codeOrchestra.colt.as.run;

import codeOrchestra.colt.as.air.util.DescriptorConverter;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.model.AsProjectBuildSettings;
import codeOrchestra.colt.as.model.AsProjectLiveSettings;
import codeOrchestra.colt.as.model.beans.air.AndroidAirModel;
import codeOrchestra.colt.as.model.beans.air.DesktopAirModel;
import codeOrchestra.colt.as.model.beans.air.IOSAirModel;
import codeOrchestra.colt.as.security.TrustedLocations;
import codeOrchestra.colt.core.execution.ExecutionException;
import codeOrchestra.colt.core.execution.ProcessHandlerWrapper;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.ui.ApplicationGUI;
import codeOrchestra.util.BrowserUtil;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.SystemInfo;
import codeOrchestra.util.process.ProcessHandlerBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveLauncher implements LiveLauncher<AsProject> {

    private List<ProcessHandlerWrapper> processHandlerWrappers = new ArrayList<>();

    public ProcessHandlerWrapper launch(AsProject project, boolean multiple, boolean production) throws ExecutionException {
        AsProjectLiveSettings liveCodingSettings = project.getProjectLiveSettings();
        AsProjectBuildSettings compilerSettings = project.getProjectBuildSettings();
        Target launchTarget = compilerSettings.getLaunchTarget();

        File swfFile = new File(production ? compilerSettings.productionBuildModel.getOutputPath() : project.getOutputDir().getPath(), compilerSettings.getOutputFilename());
        String swfPath = swfFile.getPath();
        File descriptionFile = new File(swfFile.getParent(), project.getName() + "-app.xml");

        if (launchTarget == Target.AIR_IOS) {
            IOSAirModel iosAirModel = compilerSettings.runTargetModel.getIosAirModel();
            if (iosAirModel.getUseCustomTemplate()) {
                DescriptorConverter.afterCompileReplace(new File(iosAirModel.getTemplatePath()), descriptionFile, swfFile.getName());
            } else {
                DescriptorConverter.afterCompileReplace(iosAirModel.getDescriptorModel(), iosAirModel.getAdditionalDescriptorModel(),descriptionFile, swfFile.getName());
            }
            String scriptPath = compilerSettings.getAirIosScript();
            if (StringUtils.isEmpty(scriptPath) || !new File(scriptPath).exists()) {
                throw new ExecutionException("Invalid iOS AIR run script path");
            }
            return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(protect(scriptPath)).build(project.getOutputDir()), true);
        }

        if (launchTarget == Target.AIR_ANDROID) {
            AndroidAirModel androidAirModel = compilerSettings.runTargetModel.getAndroidAirModel();
            if (androidAirModel.getUseCustomTemplate()) {
                DescriptorConverter.afterCompileReplace(new File(androidAirModel.getTemplatePath()), descriptionFile, swfFile.getName());
            } else {
                DescriptorConverter.afterCompileReplace(androidAirModel.getDescriptorModel(), androidAirModel.getAdditionalDescriptorModel(),descriptionFile, swfFile.getName());
            }
            String scriptPath = compilerSettings.getAirAndroidScript();
            if (StringUtils.isEmpty(scriptPath) || !new File(scriptPath).exists()) {
                throw new ExecutionException("Invalid Android AIR run script path");
            }
            return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(protect(scriptPath)).build(project.getOutputDir()), true);
        }

        if (launchTarget == Target.AIR_DESKTOP) {
            DesktopAirModel desktopAirModel = compilerSettings.runTargetModel.getDesktopAirModel();
            if (desktopAirModel.getUseCustomTemplate()) {
                DescriptorConverter.afterCompileReplace(new File(desktopAirModel.getTemplatePath()), descriptionFile, swfFile.getName());
            } else {
                DescriptorConverter.afterCompileReplace(desktopAirModel.getDescriptorModel(), descriptionFile, swfFile.getName());
            }
            //todo: implement run with ADL
            throw new ExecutionException("todo: implement run with ADL");
        }

        ApplicationGUI.CAN_SHOW_ADD = true;

        LauncherType launcherType = liveCodingSettings.getLauncherType();
        if (launchTarget == Target.SWF) {
            TrustedLocations.getInstance().addTrustedLocation(swfPath);
        }

        String target = launchTarget == Target.WEB_ADDRESS ? compilerSettings.getWebAddress() : swfPath;

        ProcessHandlerWrapper processHandlerWrapper;

        switch (launcherType) {
            case DEFAULT:
                processHandlerWrapper = new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(getCommand(BrowserUtil.launchBrowser(target, multiple))).build(), false);
                break;
            case FLASH_PLAYER:
                processHandlerWrapper = new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(completeFlashPlayerPath(liveCodingSettings.getFlashPlayerPath())).append(protect(target)).build(), false);
                break;
            default:
                throw new ExecutionException("Unsupported launcher type: " + launcherType);
        }

        processHandlerWrappers.add(processHandlerWrapper);

        return processHandlerWrapper;
    }

    private String getCommand(ProcessBuilder processBuilder) {
        StringBuilder res = new StringBuilder();
        for (String s : processBuilder.command()) {
            res.append(s).append(" ");
        }
        return res.toString();
    }

    private String completeFlashPlayerPath(String playerPath) throws ExecutionException {
        File playerFile = new File(playerPath);
        if (!(playerFile.exists())) {
            throw new ExecutionException("Can't locate Flash Player under " + playerPath);
        }

        String result = playerPath;
        if (SystemInfo.isMac) {
            if (playerFile.isDirectory()) {
                File executableDir = new File(playerFile, "Contents/MacOS");
                if (!(executableDir.exists())) {
                    throw new ExecutionException("Can't locate Flash Player under " + playerPath);
                }

                File[] files = executableDir.listFiles(new FilenameFilter() {
                    public boolean accept(File file, String fileName) {
                        return fileName.toLowerCase().contains("player");
                    }

                });
                if (files == null || files.length == 0) {
                    throw new ExecutionException("Can't locate Flash Player under " + playerPath);
                }

                result = files[0].getPath();
            }
        } else if (SystemInfo.isWindows) {
            // Do nothing
        } else {
            // Do nothing
        }

        return protect(result);
    }

    private String protect(String result) {
        if (result.contains(" ")) {
            return "\"" + result + "\"";
        }
        return result;
    }

    @Override
    public void dispose() {
        for (ProcessHandlerWrapper processHandlerWrapper : processHandlerWrappers) {
            processHandlerWrapper.getProcessHandler().detachProcess();
        }

        processHandlerWrappers.clear();
    }
}
