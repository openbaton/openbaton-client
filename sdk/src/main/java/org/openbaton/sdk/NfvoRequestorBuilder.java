package org.openbaton.sdk;

import org.openbaton.sdk.api.exception.SDKException;

public class NfvoRequestorBuilder {

  private String serviceKey;
  private String username;
  private String password;
  private String serviceName;
  private String projectId;
  private String projectName;
  private boolean sslEnabled;
  private String nfvoIp;
  private int nfvoPort;
  private String version;

  private NfvoRequestorBuilder() {}

  public static NfvoRequestorBuilder create() {
    NfvoRequestorBuilder builder = new NfvoRequestorBuilder();
    builder.version = "1";
    builder.username = "admin";
    builder.projectName = "default";
    builder.password = "openbaton";
    builder.nfvoIp = "localhost";
    builder.nfvoPort = 8080;
    builder.sslEnabled = false;
    return builder;
  }

  public NfvoRequestorBuilder serviceKey(String serviceKey) {
    this.serviceKey = serviceKey;
    return this;
  }

  public NfvoRequestorBuilder username(String username) {
    this.username = username;
    return this;
  }

  public NfvoRequestorBuilder password(String password) {
    this.password = password;
    return this;
  }

  public NfvoRequestorBuilder serviceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  public NfvoRequestorBuilder projectId(String projectId) {
    this.projectId = projectId;
    return this;
  }

  public NfvoRequestorBuilder projectName(String projectName) {
    this.projectName = projectName;
    return this;
  }

  public NfvoRequestorBuilder version(String version) {
    this.version = version;
    return this;
  }

  public NfvoRequestorBuilder nfvoIp(String nfvoIp) {
    this.nfvoIp = nfvoIp;
    return this;
  }

  public NfvoRequestorBuilder nfvoPort(int nfvoPort) {
    this.nfvoPort = nfvoPort;
    return this;
  }

  public NfvoRequestorBuilder sslEnabled(boolean sslEnabled) {
    this.sslEnabled = sslEnabled;
    return this;
  }

  public NFVORequestor build() throws SDKException {
    // Is service
    if (serviceKey != null
        && !serviceKey.equalsIgnoreCase("")
        && serviceName != null
        && !serviceName.equalsIgnoreCase("")) {
      if (projectName != null && !projectName.equals("")) {
        return new NFVORequestor(
            serviceName,
            nfvoIp,
            String.format("%d", nfvoPort),
            version,
            sslEnabled,
            projectName,
            serviceKey);
      } else {
        return new NFVORequestor(
            serviceName,
            projectId,
            nfvoIp,
            String.format("%d", nfvoPort),
            version,
            sslEnabled,
            serviceKey);
      }
    } else {
      if (projectName != null && !projectName.equalsIgnoreCase("")) {
        return new NFVORequestor(
            username,
            password,
            sslEnabled,
            projectName,
            nfvoIp,
            String.format("%d", nfvoPort),
            version);
      } else {
        return new NFVORequestor(
            username,
            password,
            projectId,
            sslEnabled,
            nfvoIp,
            String.format("%d", nfvoPort),
            version);
      }
    }
  }
}
