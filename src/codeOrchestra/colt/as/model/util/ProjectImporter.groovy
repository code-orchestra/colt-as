package codeOrchestra.colt.as.model.util

import codeOrchestra.colt.as.model.COLTAsProject

/**
 * @author Dima Kruk
 */
class ProjectImporter {

    static COLTAsProject importProject(File file) {
        COLTAsProject project = codeOrchestra.colt.as.model.ModelStorage.instance.project

        Map<String, Closure> map = [
                name: { s ->
                    project.name = s
                },
                outputPath: { s ->
                    project.projectBuildSettings.buildModel.outputPath = s
                },
                outputFileName: {s ->
                    project.projectBuildSettings.buildModel.outputFileName = s
                },
                targetPlayerVersion: {s ->
                    project.projectBuildSettings.buildModel.targetPlayerVersion = s
                },
                mainClass: {s ->
                    project.projectBuildSettings.buildModel.mainClass = s
                },
                compilationTimeoutValue: {s ->
                    project.projectBuildSettings.buildModel.interruptValue = s
                },
                compilationTimeout: {s ->
                    project.projectBuildSettings.buildModel.interrupt = s
                },
                excludeUnusedCode: {s ->
                    project.projectBuildSettings.buildModel.excludeDeadCode = s
                },
                compilerOptions: {s ->
                    project.projectBuildSettings.buildModel.compilerOptions = s
                },
                nonDefaultLocale: {s ->
                    project.projectBuildSettings.buildModel.nonDefaultLocale = s
                },
                localeOptions: {s ->
                    project.projectBuildSettings.buildModel.localeSettings = s
                },
                useFrameworkAsRSL: {s ->
                    project.projectBuildSettings.buildModel.rsl = s
                },

                maxLoopIterations: {s ->
                    project.projectLiveSettings.liveSettingsModel.maxLoop = s
                },
                liveMethods: {s ->
                    project.projectLiveSettings.liveSettingsModel.liveType = s
                },
                makeGettersSettersLive: {s ->
                    project.projectLiveSettings.liveSettingsModel.makeGSLive = s
                },

                flexSDKPath: {s ->
                    project.projectBuildSettings.sdkModel.flexSDKPath = s
                },
                useDefaultSDKConfiguration: {s ->
                    project.projectBuildSettings.sdkModel.useFlexConfig = s
                },
                useCustomSDKConfiguration: {s ->
                    project.projectBuildSettings.sdkModel.useCustomConfig = s
                },
                customConfigPath: {s ->
                    project.projectBuildSettings.sdkModel.customConfigPath = s
                },

                target: {s ->
                    project.projectBuildSettings.runTargetModel.target = s
                },
                webAddress: {s ->
                    project.projectBuildSettings.runTargetModel.httpIndex = s
                },
                'air-sdk': {s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.airSDKPath = s
                    project.projectBuildSettings.runTargetModel.iosAirModel.airSDKPath = s
                },
                airAndroidScript: {s ->
                    project.projectBuildSettings.runTargetModel.androidScript = s
                },
                airScript: {s ->
                    project.projectBuildSettings.runTargetModel.iosScript = s
                },
                keystore: {s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.keystorePath = s
                    project.projectBuildSettings.runTargetModel.iosAirModel.keystorePath = s
                },
                storepass: {s ->
                    project.projectBuildSettings.runTargetModel.androidAirModel.storePass = s
                    project.projectBuildSettings.runTargetModel.iosAirModel.storePass = s
                },
                __SAVED_PACKAGED_FILES: {s ->

                },
                'provisioning-profile': {s ->
                    project.projectBuildSettings.runTargetModel.iosAirModel.provisionPath = s
                },

                sourcePaths: {String s ->
                    String[] paths = s.split(/\\:/)
                    project.projectPaths.sources.clear()
                    project.projectPaths.sources.addAll(paths)
                },
                libraryPaths: {String s ->
                    String[] paths = s.split(/\\:/)
                    project.projectPaths.libraries.clear()
                    project.projectPaths.libraries.addAll(paths)
                },
                asssetPaths: {String s ->
                    String[] paths = s.split(/\\:/)
                    project.projectPaths.assets.clear()
                    project.projectPaths.assets.addAll(paths)
                },
                htmlTemplatePath: {s ->
                    project.projectPaths.htmlTemplatePath = s
                },

                compression: {s ->
                    project.projectBuildSettings.productionBuildModel.compression = s
                },
                optimization: {s ->
                    project.projectBuildSettings.productionBuildModel.optimization = s
                },

                clearMessages: {s ->
                    project.projectLiveSettings.settingsModel.clearLog = s
                },
                disconnectOnTimeout: {s ->
                    project.projectLiveSettings.settingsModel.disconnectOnTimeout = s
                },

                flashPlayerPath: {s ->
                    project.projectLiveSettings.launcherModel.flashPlayerPath = s
                },
        ]

        file.eachLine { line ->
            String[] items = line.split(/=/, 2)
            String key = items[0]
            if (map.containsKey(key)) {
                map[key](items[1])
            }
        }

        return project
    }

}
