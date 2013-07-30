package codeOrchestra.colt.as.model;

import codeOrchestra.colt.as.compiler.fcsh.FSCHCompilerKind;
import codeOrchestra.colt.core.model.COLTProject;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistedAspect;
import codeOrchestra.util.ProjectHelper;
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

    public String getFlexConfigPath(FSCHCompilerKind compilerKind) {
        return new File(getBaseDir(), getName() + "_" + compilerKind.getCommandName() + "_flex_config.xml").getPath();
    }

    public File getLinkReportFile() {
        return new File(getOutputDir(), "link-report.xml");
    }

    public File getOrCreateIncrementalSourcesDir() {
        File incrementalSourcesDir = new File(getBaseDir(), "incremental");
        if (!incrementalSourcesDir.exists()) {
            incrementalSourcesDir.mkdir();
        }
        return incrementalSourcesDir;
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

    public static COLTAsProject getCurrentProject() {
        return ProjectHelper.<COLTAsProject>getCurrentProject();
    }
}
