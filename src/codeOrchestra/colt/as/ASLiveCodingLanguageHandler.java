package codeOrchestra.colt.as;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.run.ASLiveLauncher;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.core.AbstractLiveCodingLanguageHandler;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.model.persistence.COLTProjectPersistence;
import codeOrchestra.colt.core.rpc.COLTRemoteService;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingLanguageHandler extends AbstractLiveCodingLanguageHandler<COLTAsProject> {

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
        return new COLTProjectPersistence[0];
    }

    @Override
    public COLTAsProject getCurrentProject() {
        // TODO: implement
        return null;
    }

    @Override
    public void initHandler() {
        // TODO: implement
    }

    @Override
    public void disposeHandler() {
        // TODO: implement
    }

    @Override
    public Logger getLogger(String source) {
        return null;  // TODO: implement
    }

    @Override
    public void loadUI(Stage primaryStage, Parent root) throws Exception {
//        root.getChildrenUnmodifiable().re

        primaryStage.setTitle("COLT 1.1 ActionScript");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
    }

    @Override
    public COLTRemoteService createRPCService() {
        return null;  // TODO: implement
    }

    @Override
    public LiveLauncher<COLTAsProject> createLauncher() {
        return new ASLiveLauncher();
    }

    @Override
    public LiveCodingManager<COLTAsProject> createLiveCodingManager() {
        return new ASLiveCodingManager();
    }

    @Override
    public SourceFileFactory createSourceFileFactory() {
        return new ASSourceFileFactory();
    }

}
