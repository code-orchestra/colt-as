package codeOrchestra.colt.as.air

import codeOrchestra.colt.as.model.COLTAsProject
import codeOrchestra.colt.as.model.beans.air.AIRModel

/**
 * @author Dima Kruk
 */
class AirAndroidApkBuildScriptGenerator extends AirBuildScriptGenerator {
    AirAndroidApkBuildScriptGenerator(COLTAsProject project) {
        super(project)
    }

    @Override
    protected String getScriptFileName() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    String generate(AIRModel aioParent, List<File> packagedFiles) throws IOException {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }
}
