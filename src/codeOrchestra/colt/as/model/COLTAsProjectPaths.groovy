package codeOrchestra.colt.as.model

import codeOrchestra.colt.core.model.COLTProjectPaths
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical
import javafx.collections.FXCollections
import javafx.collections.ObservableList as FXObservableList

/**
 * @author Dima Kruk
 */
@Canonical
class COLTAsProjectPaths extends COLTProjectPaths<COLTAsProject> {
    FXObservableList<String> sources = FXCollections.observableArrayList()
    FXObservableList<String> libraries = FXCollections.observableArrayList()
    FXObservableList<String> assets = FXCollections.observableArrayList()

    @FXBindable String htmlTemplatePath

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
                    item(s)
                }
            }
            'libraries-list' {
                for (s in libraries) {
                    item(s)
                }
            }
            'assets-list' {
                for (s in assets) {
                    item(s)
                }
            }
            'html-template'(htmlTemplatePath)
        }
    }

    @Override
    void buildModel(Object node) {
        sources.clear()
        libraries.clear()
        assets.clear()
        node.'sources-list'.item.each{it ->
            sources << it.toString()
        }
        node.'libraries-list'.item.each{it ->
            libraries << it.toString()
        }
        node.'assets-list'.item.each{it ->
            assets << it.toString()
        }
        htmlTemplatePath = node.'html-template'
    }
}
