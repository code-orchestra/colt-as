package codeOrchestra.colt.as.rpc;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.rpc.model.COLTCompilationResult;
import codeOrchestra.colt.as.rpc.model.COLTRemoteProject;
import codeOrchestra.colt.as.rpc.model.COLTState;
import codeOrchestra.colt.core.rpc.COLTRemoteService;
import codeOrchestra.colt.core.rpc.COLTRemoteTransferableException;

/**
 * @author Alexander Eliseyev
 */
public interface COLTAsRemoteService extends COLTRemoteService<COLTAsProject> {

    // Secured methods

    COLTState getState(String securityToken) throws COLTRemoteTransferableException;

    COLTCompilationResult runBaseCompilation(String securityToken) throws COLTRemoteTransferableException;

    COLTCompilationResult runBaseCompilation(String securityToken, boolean run) throws COLTRemoteTransferableException;

    COLTCompilationResult runProductionCompilation(String securityToken, boolean run) throws COLTRemoteTransferableException;

    void createProject(String securityToken, COLTRemoteProject project) throws COLTRemoteTransferableException;

    void loadProject(String securityToken, String path) throws COLTRemoteTransferableException;

}
