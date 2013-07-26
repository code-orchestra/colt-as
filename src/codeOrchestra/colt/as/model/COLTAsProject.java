package codeOrchestra.colt.as.model;

import codeOrchestra.colt.core.model.COLTProject;
import codeOrchestra.colt.core.model.COLTProjectBuildSettings;
import codeOrchestra.colt.core.model.COLTProjectLiveSettings;
import codeOrchestra.colt.core.model.COLTProjectPaths;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistedAspect;

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
    public COLTProjectPaths getProjectPaths() {
        return projectPaths;
    }

    @Override
    public COLTProjectLiveSettings getProjectLiveSettings() {
        return liveSettings;
    }

    @Override
    public COLTProjectBuildSettings getProjectBuildSettings() {
        return buildSettings;
    }

    @Override
    protected List<COLTProjectPersistedAspect> getLanguageSpecificAspects() {
        return Collections.EMPTY_LIST;
    }
}
