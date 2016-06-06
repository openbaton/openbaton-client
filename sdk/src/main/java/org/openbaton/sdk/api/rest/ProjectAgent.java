package org.openbaton.sdk.api.rest;

import org.openbaton.catalogue.security.Project;
import org.openbaton.sdk.api.util.AbstractRestAgent;

public class ProjectAgent extends AbstractRestAgent<Project>{

    public ProjectAgent(String username, String password, String projectId, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, projectId, nfvoIp, nfvoPort, path, version, Project.class);
    }
}
