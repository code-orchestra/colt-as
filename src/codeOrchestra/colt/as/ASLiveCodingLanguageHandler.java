package codeOrchestra.colt.as;

import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.ModelStorage;
import codeOrchestra.colt.as.run.ASLiveLauncher;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.as.ui.TestMainApp;
import codeOrchestra.colt.core.AbstractLiveCodingLanguageHandler;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.colt.core.logging.LoggerService;
import codeOrchestra.colt.core.rpc.COLTRemoteService;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;
import groovy.util.slurpersupport.GPathResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingLanguageHandler extends AbstractLiveCodingLanguageHandler<COLTAsProject> {

    private LoggerServerSocketThread loggerServerSocketThread = new LoggerServerSocketThread();

    private LoggerService loggerService;

    @Override
    public String getId() {
        return "AS";
    }

    @Override
    public String getName() {
        return "ActionScript";
    }

    @Override
    public COLTAsProject parseProject(GPathResult gPathResult) {
        ModelStorage.getInstance().getProject().buildModel(gPathResult);
        return ModelStorage.getInstance().getProject();
    }

    @Override
    public COLTAsProject createProject(String pName) {
        COLTAsProject project = ModelStorage.getInstance().getProject();
        project.setName(pName);
        return project;
    }

    @Override
    public COLTAsProject getCurrentProject() {
        return ModelStorage.getInstance().getProject();
    }

    @Override
    public void initHandler() {
        loggerServerSocketThread.openSocket();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                Logger.getLogger("my source").info("my message");
            }
        }.start();
    }

    @Override
    public void disposeHandler() {
        loggerServerSocketThread.closeSocket();
    }

    @Override
    public LoggerService getLoggerService() {
        return loggerService;
    }

    public void setLoggerService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public Node getPane() throws Exception {
        return FXMLLoader.load(TestMainApp.class.getResource("main_app.fxml"));
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
