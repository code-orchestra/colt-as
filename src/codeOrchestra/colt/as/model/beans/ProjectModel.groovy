package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical
import groovy.xml.MarkupBuilder
import javafx.collections.FXCollections
import javafx.collections.ObservableList as FXObservableList

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class ProjectModel implements IModelElement {
    FXObservableList<String> sources = FXCollections.observableArrayList()
    FXObservableList<String> libraries = FXCollections.observableArrayList()
    FXObservableList<String> assets = FXCollections.observableArrayList()

    String htmlTemplatePath;

    public final SDKModel sdkModel = new SDKModel()
    public final BuildModel buildModel = new BuildModel()
    public final ProductionBuildModel productionBuildModel = new ProductionBuildModel()
    public final SettingsModel settingsModel = new SettingsModel()
    public final RunTargetModel runTargetModel = new RunTargetModel()
    public final LauncherModel launcherModel = new LauncherModel()
    public final LiveSettingsModel liveSettingsModel = new LiveSettingsModel()

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
            sdk(sdkModel.buildXml())
            build(buildModel.buildXml())
            production(productionBuildModel.buildXml())
            settings(settingsModel.buildXml())
            'run-target'(runTargetModel.buildXml())
            launch(launcherModel.buildXml())
            live(liveSettingsModel.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        sources.clear()
        libraries.clear()
        assets.clear()
        node.'sources-list'.item.each{it ->
            sources << it
        }
        node.'libraries-list'.item.each{it ->
            libraries << it
        }
        node.'assets-list'.item.each{it ->
            assets << it
        }
        htmlTemplatePath = node.'html-template'
        sdkModel.buildModel(node.sdk)
        buildModel.buildModel(node.build)
        productionBuildModel.buildModel(node.production)
        settingsModel.buildModel(node.settings)
        runTargetModel.buildModel(node.'run-target')
        launcherModel.buildModel(node.launch)
        liveSettingsModel.buildModel(node.live)
    }

    String toXmlString() {
        StringWriter writer = new StringWriter()
        new MarkupBuilder(writer).xml(buildXml())
        writer.toString()
    }

    void fromXmlString(String source) {
        buildModel(new XmlSlurper().parseText(source))

    }
}
