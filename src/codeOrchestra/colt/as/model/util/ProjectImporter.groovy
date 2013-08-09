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
                maxLoopIterations: {s ->
                    project.projectLiveSettings.liveSettingsModel.maxLoop = s
                },
                flexSDKPath: {s ->
                    project.projectBuildSettings.sdkModel.flexSDKPath = s
                },
                useDefaultSDKConfiguration: {s ->
                    project.projectBuildSettings.sdkModel.useFlexConfig = s == "true"
                },
                liveMethods: {s ->
                    project.projectLiveSettings.liveSettingsModel.liveType = s
                },
                target: {s ->
                    project.projectBuildSettings.runTargetModel.target = s
                },
                compilerOptions: {s ->
                    project.projectBuildSettings.buildModel.compilerOptions = s
                },
                sourcePaths: {String s ->
                    String[] paths = s.split(/\\:/)
                    //project.projectPaths.sourcePaths.clear()
                    project.projectPaths.sources.addAll(paths)
                    println "paths = $paths"
                    println "project.projectPaths.sources = $project.projectPaths.sources"
                },
                key: {s ->

                },
        ]

        file.eachLine { line ->
            String[] items = line.split(/=/, 2)
            String key = items[0]
            println "key = $key"
            if (map.containsKey(key)) {
                map[key](items[1])
            }
        }

        return project
    }

}
