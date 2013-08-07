package codeOrchestra.colt.as.ui.air

import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
class FileCellBean {
    File file
    @FXBindable boolean checked
    boolean required

    FileCellBean(File file, boolean required = false) {
        this.file = file
        this.required = required
        checked = required
    }
}
