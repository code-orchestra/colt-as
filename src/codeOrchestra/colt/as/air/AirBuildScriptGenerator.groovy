package codeOrchestra.colt.as.air

import codeOrchestra.colt.as.model.COLTAsProject
import codeOrchestra.colt.as.model.beans.air.AIRModel

/**
 * @author Dima Kruk
 */
abstract class AirBuildScriptGenerator {
    protected COLTAsProject project
    protected String appName

    AirBuildScriptGenerator(COLTAsProject project) {
        this.project = project
        appName = project.name
    }

    File getScriptPath(COLTAsProject project) {
        return new File(project.getOutputDir(), getScriptFileName())
    }

    File getDescScriptPath(COLTAsProject project) {
        return new File(project.getOutputDir(), appName + "-app.xml")
    }

    protected abstract String getScriptFileName()

    abstract String generate(AIRModel aioParent, List<File> packagedFiles) throws IOException
}
