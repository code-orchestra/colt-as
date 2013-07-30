package codeOrchestra.colt.as;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.core.LiveCodingLanguageHandler;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.model.listener.ProjectListener;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistence;
import codeOrchestra.colt.core.rpc.COLTRemoteService;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;
import codeOrchestra.util.ProjectHelper;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingLanguageHandler implements LiveCodingLanguageHandler<COLTAsProject> {

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

    @Override
    public COLTAsProject getCurrentProject() {
        // TODO: implement
        return null;
    }

    @Override
    public void initHandler() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void disposeHandler() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addProjectListener(ProjectListener<COLTAsProject> projectListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeProjectListener(ProjectListener<COLTAsProject> projectListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Logger getLogger(String source) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public COLTRemoteService getRPCService() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LiveLauncher<COLTAsProject> getLauncher() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LiveCodingManager<COLTAsProject> getLiveCodingManager() {
        return new ASLiveCodingManager();  //To change body of implemented methods use File | Settings | File Templates.
    }

}
