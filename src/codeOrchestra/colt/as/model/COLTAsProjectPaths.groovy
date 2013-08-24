package codeOrchestra.colt.as.model

import codeOrchestra.colt.core.model.COLTProjectPaths
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.ui.components.fileset.FilesetInput
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical
import javafx.collections.FXCollections
import javafx.collections.ObservableList as FXObservableList

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class COLTAsProjectPaths extends COLTProjectPaths<COLTAsProject> {
    String sources = "src/"
    String libraries = "lib/"
    String assets = "assets/"

    String htmlTemplatePath = ""

    COLTAsProjectPaths() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.add(htmlTemplatePath())
    }

    public String getHTMLTemplatePath() {
        return htmlTemplatePath
    }

    public addSources(String[] value) {
        sources = FilesetInput.createFilesetString(value.collect{new File(it)})
    }

    public addLibraries(String[] value) {
        libraries = FilesetInput.createFilesetString(value.collect{new File(it)})
    }

    public addAssets(String[] value) {
        assets = FilesetInput.createFilesetString(value.collect{new File(it)})
    }

    public List<String> getSourcePaths() {
        return FilesetInput.getDirectoriesFromString(sources).collect{it.path}
    }

    public List<String> getLibraryPaths() {
        return FilesetInput.getFilesFromString(libraries).collect{it.path}
    }

    public List<String> getAssetPaths() {
        return FilesetInput.getDirectoriesFromString(assets).collect{it.path}
    }

    @Override
    Closure buildXml() {
        return {
            'sources-set' (sources)
            'libraries-set' (libraries)
            'assets-set' (assets)
            'html-template'(PathUtils.makeRelative(htmlTemplatePath))
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
