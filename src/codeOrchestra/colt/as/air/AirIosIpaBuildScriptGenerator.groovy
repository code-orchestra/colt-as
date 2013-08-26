package codeOrchestra.colt.as.air

import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.as.model.beans.air.AIRModel
import codeOrchestra.util.PathUtils
import codeOrchestra.util.SystemInfo
import codeOrchestra.util.templates.TemplateCopyUtil

/**
 * @author Dima Kruk
 */
class AirIosIpaBuildScriptGenerator extends AirBuildScriptGenerator {

    AirIosIpaBuildScriptGenerator(AsProject project) {
        super(project)
    }

    @Override
    protected String getScriptFileName() {
        return SystemInfo.isWindows ? "airIosIpaBuild.bat" : "airIosIpaBuild.sh"
    }

    @Override
    String generate(AIRModel aioParent, List<File> packagedFiles) throws IOException {
        File targetScriptFile = getScriptPath(project)
        File templateFile = new File(PathUtils.getTemplatesDir(), getScriptFileName())
        //for test
        //File templateFile = new File("/Users/dimakruk/IdeaProjects/colt-as/templates", getScriptFileName())

        File targetDescScriptFile = getDescScriptPath(project)
        File templateDescFile = new File(PathUtils.getTemplatesDir(), "AppName-app.xml")
        //for test
        //File templateDescFile = new File("/Users/dimakruk/IdeaProjects/colt-as/templates", "AppName-app.xml")

        Map<String, String> replacements = new HashMap<>()
        replacements.put("{AIR_SDK}", aioParent.airSDKPath)
        replacements.put("{APPNAME}", appName)
        replacements.put("{PROJECT_DIR}", project.getBaseDir().getAbsolutePath())
        //for test
        //replacements.put("{PROJECT_DIR}", project.getOutputDir().parentFile.getAbsolutePath())
        String outputDirPath = project.getOutputDir().getAbsolutePath()
        if (SystemInfo.isWindows && !outputDirPath.endsWith(File.separator)) {
            outputDirPath += File.separator
        }
        replacements.put("{OUTPUT_DIR}", outputDirPath)

        File outputDir = project.getOutputDir()
        replacements.put("{IPA_FILE}", appName + ".ipa")
        replacements.put("{DESCRIPTOR_FILE}", appName + "-app.xml")

        replacements.put("{provisioning-profile}", aioParent.provisionPath)
        replacements.put("{keystore}", aioParent.keystorePath)
        replacements.put("{storepass}", aioParent.storePass)

        String packaged = ""

        for (File currFile : packagedFiles) {
            String relative = new File(outputDir.getAbsolutePath()).toURI()
                    .relativize(new File(currFile.getAbsolutePath()).toURI()).getPath()
            packaged += "\"" + relative + "\" "
        }

        replacements.put("{PACKAGED_FILES}", packaged)

        TemplateCopyUtil.copy(templateFile, targetScriptFile, replacements)
        targetScriptFile.setExecutable(true)

        Map<String, String> descReplacements = new HashMap<>()
        descReplacements.put("{APPNAME}", appName)

        descReplacements.put("{OUTPUT_FILE}", project.getProjectBuildSettings().outputFilename)

        TemplateCopyUtil.copy(templateDescFile, targetDescScriptFile, descReplacements)

        return targetScriptFile.getPath()
    }
}
