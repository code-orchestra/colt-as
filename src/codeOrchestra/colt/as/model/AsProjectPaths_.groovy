package codeOrchestra.colt.as.model

import codeOrchestra.colt.core.model.ProjectPaths
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
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
@Deprecated
class AsProjectPaths_ extends ProjectPaths<AsProject> {
    FXObservableList<String> sources = FXCollections.observableArrayList()
    FXObservableList<String> libraries = FXCollections.observableArrayList()
    FXObservableList<String> assets = FXCollections.observableArrayList()

    String htmlTemplatePath

    AsProjectPaths_() {
        clear()
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.add(htmlTemplatePath())
    }

    void clear() {
        sources.clear()
        libraries.clear()
        assets.clear()
        htmlTemplatePath = ""
    }

    public String getHTMLTemplatePath() {
        return htmlTemplatePath
    }

    public List<String> getSourcePaths() {
        return sources.toList()
    }

    public List<String> getLibraryPaths() {
        return libraries.toList()
    }

    public List<String> getAssetPaths() {
        return assets.toList()
    }

    @Override
    Closure buildXml() {
        return {
            'sources-list' {
                for (s in sources) {
                    item(PathUtils.makeRelative(s))
                }
            }
            'libraries-list' {
                for (s in libraries) {
                    item(PathUtils.makeRelative(s))
                }
            }
            'assets-list' {
                for (s in assets) {
                    item(PathUtils.makeRelative(s))
                }
            }
            'html-template'(PathUtils.makeRelative(htmlTemplatePath))
        }
    }

    @Override
    void buildModel(Object node) {
        sources.clear()
        libraries.clear()
        assets.clear()
        node.'sources-list'.item.each{it ->
            sources << PathUtils.makeAbsolute(it.toString())
        }
        node.'libraries-list'.item.each{it ->
            libraries << PathUtils.makeAbsolute(it.toString())
        }
        node.'assets-list'.item.each{it ->
            assets << PathUtils.makeAbsolute(it.toString())
        }
        htmlTemplatePath = PathUtils.makeAbsolute((node.'html-template')?.toString())
    }
}
