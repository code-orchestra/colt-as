package codeOrchestra.colt.as.model;

import codeOrchestra.colt.core.model.COLTProject;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistedAspect;
import codeOrchestra.util.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsProject extends COLTProject {

    private COLTAsProjectBuildSettings buildSettings;
    private COLTAsProjectLiveSettings liveSettings;
    private COLTAsProjectPaths projectPaths;

    @Override
    public COLTAsProjectPaths getProjectPaths() {
        return projectPaths;
    }

    @Override
    public COLTAsProjectLiveSettings getProjectLiveSettings() {
        return liveSettings;
    }

    @Override
    public COLTAsProjectBuildSettings getProjectBuildSettings() {
        return buildSettings;
    }

    @Override
    protected List<COLTProjectPersistedAspect> getLanguageSpecificAspects() {
        return Collections.EMPTY_LIST;
    }

    public File getOutputDir() {
        String outputPath = getProjectBuildSettings().getOutputPath();
        if (StringUtils.isEmpty(outputPath)) {
            return getDefaultOutputDir();
        }
        return new File(outputPath);
    }

    public File getDefaultOutputDir() {
        return new File(getBaseDir(), "colt_output");
    }

    public File getDigestsDir() {
        return new File(getBaseDir(), "digests");
    }

    public File getBaseDir() {
        return new File(getPath()).getParentFile();
    }

}
