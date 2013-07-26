package codeOrchestra.colt.as;

import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.core.LiveCodingLanguageHandler;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistence;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingLanguageHandler implements LiveCodingLanguageHandler {

    private SourceFileFactory sourceFileFactory = new ASSourceFileFactory();

    @Override
    public String getId() {
        return "AS";
    }

    @Override
    public String getName() {
        return "ActionScript";
    }

    @Override
    public COLTProjectPersistence[] getAvailablePersistenceHandlers() {
        return new COLTProjectPersistence[0]; // TODO: implement
    }

    @Override
    public SourceFileFactory getSourceFileFactory() {
        return sourceFileFactory;
    }

}
