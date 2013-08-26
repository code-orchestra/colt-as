package codeOrchestra.colt.as.air

import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.colt.as.model.beans.air.AIRModel

/**
 * @author Dima Kruk
 */
abstract class AirBuildScriptGenerator {
    protected AsProject project
    protected String appName

    AirBuildScriptGenerator(AsProject project) {
        this.project = project
        appName = project.name
    }

    File getScriptPath(AsProject project) {
        return new File(project.getOutputDir(), getScriptFileName())
    }

    File getDescScriptPath(AsProject project) {
        return new File(project.getOutputDir(), appName + "-app.xml")
    }

    protected abstract String getScriptFileName()

    abstract String generate(AIRModel aioParent, List<File> packagedFiles) throws IOException
}
