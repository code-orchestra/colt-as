package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.model.beans.ProjectModel

/**
 * @author Dima Kruk
 */
class ModelStorage {
    private static ModelStorage ourInstance = new ModelStorage()

    public static ModelStorage getInstance() {
        return ourInstance
    }

    public ProjectModel project

    private ModelStorage() {
        project = new ProjectModel()
    }

    public String save() {
        return project.toXmlString()
    }

    public void load(String xmlStr) {
        project.fromXmlString(xmlStr)
    }
}
