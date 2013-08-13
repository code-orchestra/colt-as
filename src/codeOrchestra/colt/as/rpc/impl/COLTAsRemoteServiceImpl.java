package codeOrchestra.colt.as.rpc.impl;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.rpc.COLTAsRemoteService;
import codeOrchestra.colt.as.rpc.model.COLTCompilationResult;
import codeOrchestra.colt.as.rpc.model.COLTRemoteProject;
import codeOrchestra.colt.as.rpc.model.COLTState;
import codeOrchestra.colt.core.rpc.AbstractCOLTRemoteService;
import codeOrchestra.colt.core.rpc.COLTRemoteTransferableException;

/**
 * @author Alexander Eliseyev
 */
public class COLTAsRemoteServiceImpl extends AbstractCOLTRemoteService<COLTAsProject> implements COLTAsRemoteService {

    @Override
    public void dispose() {
    }

    @Override
    public COLTState getState(String securityToken) throws COLTRemoteTransferableException {
        return null;  // TODO: implement
    }

    @Override
    public COLTCompilationResult runBaseCompilation(String securityToken) throws COLTRemoteTransferableException {
        return null;  // TODO: implement
    }

    @Override
    public COLTCompilationResult runBaseCompilation(String securityToken, boolean run) throws COLTRemoteTransferableException {
        return null;  // TODO: implement
    }

    @Override
    public COLTCompilationResult runProductionCompilation(String securityToken, boolean run) throws COLTRemoteTransferableException {
        return null;  // TODO: implement
    }

    @Override
    public void createProject(String securityToken, COLTRemoteProject project) throws COLTRemoteTransferableException {
        // TODO: implement
    }

    @Override
    public void loadProject(String securityToken, String path) throws COLTRemoteTransferableException {
        // TODO: implement
    }

}
