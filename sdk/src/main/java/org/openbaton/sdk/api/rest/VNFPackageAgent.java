package org.openbaton.sdk.api.rest;

import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 * Created by ogo on 03.03.16.
 */
public class VNFPackageAgent extends AbstractRestAgent<VNFPackage> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VNFPackageAgent(String username, String password, String projectId, boolean sslEnabled, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, projectId, sslEnabled, nfvoIp, nfvoPort, path, version, VNFPackage.class);
    }

    @Override
    @Deprecated
    public VNFPackage create(VNFPackage vnfPackage) {
        log.warn("The creation of VNFPackages in this way is not supported. Use the create(String filePath) method instead to upload a tar file.");
        return null;
    }

    @Help(help = "Create a VNFPackage by uploading a tar file")
    public VNFPackage create(String filePath) throws SDKException {
        log.debug("Start uploading a VNFPackage using the tar at path " + filePath);
        File f = new File(filePath);
        if (f == null || !f.exists()) {
            log.error("No package: " + f.getName() + " found!");
            throw new SDKException("No package: " + f.getName() + " found!");
        }
        return requestPostPackage(f);
    }

}




