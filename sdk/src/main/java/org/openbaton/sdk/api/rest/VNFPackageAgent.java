package org.openbaton.sdk.api.rest;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;


/**
 * Created by ogo on 03.03.16.
 */
public class VNFPackageAgent extends AbstractRestAgent<VNFPackage> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VNFPackageAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, nfvoIp, nfvoPort, path, version, VNFPackage.class);
    }

    @Override
    @Deprecated
    public VNFPackage create(VNFPackage vnfPackage) {
        log.warn("The creation of VNFPackages in this way is not supported. Use the create(String filePath) method instead to upload a tar file.");
        return null;
    }

    @Help(help = "Create a VNFPackage by uploading a tar file")
    public VNFPackage create(String filePath) throws Exception {
        log.debug("Start uploading a VNFPackage using the tar at path " + filePath);
        com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = null;

        File f = new File(filePath);
        if (f == null || !f.exists()) {
            log.error("No package: " + f.getName() + " found!");
            throw new Exception("No package: " + f.getName() + " found!");
        }
        try {
            log.debug("Executing post on " + baseUrl);
            jsonResponse = Unirest.post(this.baseUrl)
                    .header("accept", "multipart/form-data")
                    .field("file", f)
                    .asJson();
        } catch (UnirestException e) {
            log.error("Could not create VNFPackage from file " + f.getName());
            throw e;
        }
        // check status
        if (jsonResponse.getStatus() != HttpStatus.SC_OK) {
            log.error("Could not create VNFPackage from file " + f.getName());
            log.error("Http status expected: " + HttpStatus.SC_OK + " obtained: " + jsonResponse.getStatus());
            throw new SDKException("Received wrong API HTTPStatus");
        }
        log.debug("Uploaded the VNFPackage from tar at path " + filePath);
        return mapper.fromJson(jsonResponse.getBody().toString(), VNFPackage.class);
    }

}




