package codeOrchestra.colt.as;

import codeOrchestra.colt.as.controller.COLTAsController;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.model.ModelStorage;
import codeOrchestra.colt.as.model.util.ProjectImporter;
import codeOrchestra.colt.as.rpc.impl.COLTAsRemoteServiceImpl;
import codeOrchestra.colt.as.run.ASLiveLauncher;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.as.ui.TestMainApp;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.colt.core.AbstractLiveCodingLanguageHandler;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.controller.COLTController;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.logging.LoggerService;
import codeOrchestra.colt.core.rpc.COLTRemoteService;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;
import codeOrchestra.colt.core.ui.components.COLTProgressIndicatorController;
import codeOrchestra.colt.core.ui.components.ICOLTProgressIndicator;
import codeOrchestra.util.StringUtils;
import groovy.util.slurpersupport.GPathResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.File;

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
    public COLTAsProject parseProject(GPathResult gPathResult, String projectPath) {
        COLTAsProject project = ModelStorage.getInstance().getProject();
        project.clear();

        project.setPath(projectPath);

        project.buildModel(gPathResult);

        // Default settings
        if (StringUtils.isEmpty(project.getProjectBuildSettings().sdkModel.getFlexSDKPath())) {
            project.getProjectBuildSettings().sdkModel.setFlexSDKPath(ASPathUtils.getFlexSDKPath());
        }

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public COLTAsProject createProject(String pName, File pFile) {
        COLTAsProject project = ModelStorage.getInstance().getProject();
        project.clear();
        project.setName(pName);
        project.setPath(pFile.getPath());

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public COLTAsProject importProject(File file) {
        COLTAsProject project = ProjectImporter.importProject(file);

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public COLTAsProject getCurrentProject() {
        return ModelStorage.getInstance().getProject();
    }

    @Override
    public void initHandler() {
        loggerServerSocketThread.openSocket();
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
    public ICOLTProgressIndicator getProgressIndicator() {
        return COLTProgressIndicatorController.getInstance();
    }

    @Override
    public COLTController<COLTAsProject> createCOLTController() {
        return new COLTAsController();
    }

    @Override
    public COLTRemoteService<COLTAsProject> createRPCService() {
        return new COLTAsRemoteServiceImpl();
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
