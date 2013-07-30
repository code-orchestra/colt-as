package codeOrchestra.colt.as.run;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.COLTAsProjectBuildSettings;
import codeOrchestra.colt.as.model.COLTAsProjectLiveSettings;
import codeOrchestra.colt.as.security.TrustedLocations;
import codeOrchestra.colt.core.execution.ExecutionException;
import codeOrchestra.colt.core.execution.ProcessHandlerWrapper;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.util.BrowserUtil;
import codeOrchestra.util.SystemInfo;
import codeOrchestra.util.process.ProcessHandlerBuilder;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveLauncher implements LiveLauncher<COLTAsProject> {

    public ProcessHandlerWrapper launch(COLTAsProject project) throws ExecutionException {
        COLTAsProjectLiveSettings liveCodingSettings = project.getProjectLiveSettings();
        COLTAsProjectBuildSettings compilerSettings = project.getProjectBuildSettings();
        Target launchTarget = liveCodingSettings.getLaunchTarget();

        if (launchTarget == Target.AIR_IOS) {
            String scriptPath = compilerSettings.getAirIosScript();
            return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(protect(scriptPath)).build(project.getOutputDir()), true);
        }

        if (launchTarget == Target.AIR_ANDROID) {
            String scriptPath = compilerSettings.getAirAndroidScript();
            return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(protect(scriptPath)).build(project.getOutputDir()), true);
        }

        LauncherType launcherType = liveCodingSettings.getLauncherType();
        String swfPath = project.getOutputDir().getPath() + File.separator + compilerSettings.getOutputFilename();
        if (launchTarget == Target.SWF) {
            TrustedLocations.getInstance().addTrustedLocation(swfPath);
        }

        String target = launchTarget == Target.WEB_ADDRESS ? liveCodingSettings.getWebAddress() : swfPath;

        switch (launcherType) {
            case DEFAULT:
                return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(getCommand(BrowserUtil.launchBrowser(target, null))).build(), false);
            case FLASH_PLAYER:
                return new ProcessHandlerWrapper(new ProcessHandlerBuilder().append(completeFlashPlayerPath(liveCodingSettings.getFlashPlayerPath()))
                        .append(protect(target)).build(), false);
            default:
                throw new ExecutionException("Unsupported launcher type: " + launcherType);
        }
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

}
