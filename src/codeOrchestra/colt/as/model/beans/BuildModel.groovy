package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.groovyfx.FXBindable
import codeOrchestra.util.PathUtils
import groovy.transform.Canonical
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

/**
 * Created with IntelliJ IDEA.
 * User: dimakruk
 * Date: 7/25/13
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Canonical
@FXBindable
class BuildModel implements IModelElement {
    String mainClass = ""
    String outputFileName = ""
    String outputPath = ""
    boolean useMaxVersion = true
    String targetPlayerVersion = ""
    boolean rsl = false
    boolean nonDefaultLocale = false
    String localeSettings = ""
    boolean excludeDeadCode = false
    boolean interrupt = false
    int interruptValue = 30
    String compilerOptions = ""

    BuildModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                mainClass(),
                outputFileName(),
                outputPath(),
                useMaxVersion(),
//                targetPlayerVersion(),
                rsl(),
                nonDefaultLocale(),
                localeSettings(),
                excludeDeadCode(),
                interrupt(),
                interruptValue(),
                compilerOptions()
        )
        useMaxVersion().addListener({ ObservableValue<? extends Boolean> observableValue, Boolean t, Boolean t1 ->
            t1 ? monitor.remove(targetPlayerVersion()) : monitor.add(targetPlayerVersion())
        } as ChangeListener)
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'main-class'(PathUtils.makeRelative(mainClass, project))
            'output-name'(outputFileName)
            'output-path'(PathUtils.makeRelative(outputPath, project))
            'use-max-version'(useMaxVersion)
            'player-version'(targetPlayerVersion)
            'is-rsl'(rsl)

            'custom-locale'(nonDefaultLocale)
            'locale'(localeSettings)

            'is-exclude'(excludeDeadCode)

            'is-interrupt'(interrupt)
            'interrupt-value'(interruptValue)

            'compiler-options'(compilerOptions)
        }
    }

    @Override
    void buildModel(Object node) {
        mainClass = PathUtils.makeAbsolute((node.'main-class'?.toString()))
        outputFileName = node.'output-name'
        outputPath = PathUtils.makeAbsolute((node.'output-path'?.toString()))
        useMaxVersion = node.'use-max-version' == "true"
        targetPlayerVersion = node.'player-version'
        rsl = node.'is-rsl' == "true"

        nonDefaultLocale = node.'custom-locale' == "true"
        localeSettings = node.'locale'

        excludeDeadCode = node.'is-exclude' == "true"

        interrupt = node.'is-interrupt' == "true"
        interruptValue = "" + node.'interrupt-value' as Integer

        compilerOptions = node.'compiler-options'
    }
}
