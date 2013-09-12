package codeOrchestra.colt.as.model.util

import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import codeOrchestra.util.PathUtils
import codeOrchestra.colt.as.model.ModelStorage

/**
 * @author Dima Kruk
 */
class ProjectImporter {

    static AsProject importProject(File file) {
        AsProject project = ModelStorage.instance.project

        project.path = file.path
        Map<String, Closure> map = [
                name: { s ->
                    project.name = s
                },
                outputPath: { String s ->
                    project.projectBuildSettings.buildModel.outputPath = PathUtils.makeAbsolute(s)
                },
                outputFileName: { String s ->
                    project.projectBuildSettings.buildModel.outputFileName = s
                },
                targetPlayerVersion: { String s ->
                    project.projectBuildSettings.buildModel.targetPlayerVersion = s
                },
                mainClass: { String s ->
                    project.projectBuildSettings.buildModel.mainClass = PathUtils.makeAbsolute(s)
                },
                compilationTimeoutValue: { String s ->
                    project.projectBuildSettings.buildModel.interruptValue = s
                },
                compilationTimeout: { String s ->
                    project.projectBuildSettings.buildModel.interrupt = s
                },
                excludeUnusedCode: { String s ->
                    project.projectBuildSettings.buildModel.excludeDeadCode = s
                },
                compilerOptions: { String s ->
                    project.projectBuildSettings.buildModel.compilerOptions = s
                },
                nonDefaultLocale: { String s ->
                    project.projectBuildSettings.buildModel.nonDefaultLocale = s
                },
                localeOptions: { String s ->
                    project.projectBuildSettings.buildModel.localeSettings = s
                },
                useFrameworkAsRSL: { String s ->
                    project.projectBuildSettings.buildModel.rsl = s
                },

                maxLoopIterations: { String s ->
                    project.projectLiveSettings.liveSettingsModel.maxLoop = s as Integer
                },
                liveMethods: { String s ->
                    project.projectLiveSettings.liveSettingsModel.liveType = s
                },
                makeGettersSettersLive: { String s ->
                    project.projectLiveSettings.liveSettingsModel.makeGSLive = s
                },

                flexSDKPath: { String s ->
                    project.projectBuildSettings.sdkModel.flexSDKPath = PathUtils.makeAbsolute(s)
                },
                useDefaultSDKConfiguration: { String s ->
                    project.projectBuildSettings.sdkModel.useFlexConfig = s
                },
                useCustomSDKConfiguration: { String s ->
                    project.projectBuildSettings.sdkModel.useCustomConfig = s
                },
                customConfigPath: { String s ->
                    project.projectBuildSettings.sdkModel.customConfigPath = PathUtils.makeAbsolute(s)
                },

                target: { String s ->
                    project.projectBuildSettings.runTargetModel.target = s
                },
                webAddress: { String s ->
                    project.projectBuildSettings.runTargetModel.httpIndex = s
                },
                'air-sdk': { String s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.airSDKPath = PathUtils.makeAbsolute(s)
                    project.projectBuildSettings.runTargetModel.iosAirModel.airSDKPath = PathUtils.makeAbsolute(s)
                },
                airAndroidScript: { String s ->
                    project.projectBuildSettings.runTargetModel.androidScript = PathUtils.makeAbsolute(s)
                },
                airScript: { String s ->
                    project.projectBuildSettings.runTargetModel.iosScript = PathUtils.makeAbsolute(s)
                },
                keystore: { String s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.keystorePath = PathUtils.makeAbsolute(s)
                    project.projectBuildSettings.runTargetModel.iosAirModel.keystorePath = PathUtils.makeAbsolute(s)
                },
                storepass: { String s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.storePass = s
                    project.projectBuildSettings.runTargetModel.iosAirModel.storePass = s
                },
                __SAVED_PACKAGED_FILES: { String s ->

                },
                'provisioning-profile': { String s ->
                    project.projectBuildSettings.runTargetModel.iosAirModel.provisionPath = PathUtils.makeAbsolute(s)
                },

                sourcePaths: { String s ->
                    List<File> paths = splitPaths(s)
                    project.projectPaths.sources = FilesetInput.createFilesetString(paths)
                },
                libraryPaths: { String s ->
                    List<File> paths = splitPaths(s)
                    project.projectPaths.libraries = FilesetInput.createFilesetString(paths)
                },
                asssetPaths: { String s ->
                    List<File> paths = splitPaths(s)
                    project.projectPaths.assets = FilesetInput.createFilesetString(paths)
                },
                htmlTemplatePath: { String s ->
                    project.projectPaths.htmlTemplatePath = PathUtils.makeAbsolute(s)
                },

                compression: { String s ->
                    project.projectBuildSettings.productionBuildModel.compression = s
                },
                optimization: { String s ->
                    project.projectBuildSettings.productionBuildModel.optimization = s
                },

                clearMessages: { String s ->
                    project.projectLiveSettings.settingsModel.clearLog = s
                },
                disconnectOnTimeout: { String s ->
                    project.projectLiveSettings.settingsModel.disconnectOnTimeout = s
                },

                flashPlayerPath: { String s ->
                    project.projectLiveSettings.launcherModel.flashPlayerPath = PathUtils.makeAbsolute(s)
                },
        ]

        Properties legacyProjectFile = new Properties()
        legacyProjectFile.load(new FileInputStream(file))
        legacyProjectFile.propertyNames().each {
            String key = (String) it
            if (map.containsKey(key)) {
                map[key](legacyProjectFile.getProperty(key))
            }
        }

        return project
    }

    private static List<File> splitPaths(String s) {
        if (s.contains(":\\")) {
            return s.split(";").collect { new File(PathUtils.makeAbsolute(it)) }
        }
        if (s.contains(":")) {
            return s.split(":").collect { new File(PathUtils.makeAbsolute(it)) }
        }
        if (s.contains(";")) {
            return s.split(";").collect { new File(PathUtils.makeAbsolute(it)) }
        }

        List<File> result = new ArrayList<>()
        result.add(new File(PathUtils.makeAbsolute(s)))
        return result
    }

}
