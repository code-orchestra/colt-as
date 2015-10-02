package codeOrchestra.colt.as.model

import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.ProjectPaths
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AsProjectPaths extends ProjectPaths<AsProject> {
    String sources = "src/"
    String libraries = "lib/"
    String assets = "assets/"

    String htmlTemplatePath = ""

    AsProjectPaths() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(sources(),
                libraries(),
                assets(),
                htmlTemplatePath())
    }

    public String getHTMLTemplatePath() {
        return htmlTemplatePath
    }

    public List<String> getSourcePaths() {
        return FilesetInput.getDirectoriesFromString(sources).collect{it.path}
    }

    public List<String> getLibraryPaths() {
        return FilesetInput.getFilesFromString(libraries).collect{it.path}.findAll {it.matches(/(?i).*\.(swc|ane)$/)}
    }

    public List<String> getAssetPaths() {
        return FilesetInput.getDirectoriesFromString(assets).collect{it.path}
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'sources-set' (sources)
            'libraries-set' (libraries)
            'assets-set' (assets)
            'html-template'(PathUtils.makeRelative(htmlTemplatePath, project))
        }
    }

    @Override
    void buildModel(Object node) {
        sources = node.'sources-set'
        libraries = node.'libraries-set'
        assets = node.'assets-set'
        htmlTemplatePath = PathUtils.makeAbsolute((node.'html-template')?.toString())
    }
}
