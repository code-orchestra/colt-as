package codeOrchestra.colt.as.session.sourcetracking;

import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class ASSourceFileFactory implements SourceFileFactory<ASSourceFile> {

    @Override
    public ASSourceFile createSourceFile(File file, File baseDir) {
        return new ASSourceFile(file, baseDir.getPath());
    }

}
