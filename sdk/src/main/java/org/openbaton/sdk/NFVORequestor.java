/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.sdk;

import org.apache.http.annotation.ThreadSafe;
import org.openbaton.catalogue.security.Project;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.rest.*;

/**
 * This class serves as a creator of request agents for the NFVO. These agents can be obtained by
 * get methods and provide methods for sending requests to the NFVO API. The agents have the same
 * configuration as the NFVORequestor object from which they are obtained. In this way it is easier
 * to get the appropriate agents that are needed without calling the particular constructors each
 * time.
 */
@ThreadSafe
public final class NFVORequestor {

  private String username;
  private String password;
  private String projectId;
  private boolean sslEnabled;
  private String nfvoIp;
  private String nfvoPort;
  private String version;

  private ConfigurationAgent configurationAgent;
  private NetworkServiceDescriptorAgent networkServiceDescriptorAgent;
  private NetworkServiceRecordAgent networkServiceRecordAgent;
  private VimInstanceAgent vimInstanceAgent;
  private VirtualLinkAgent virtualLinkAgent;
  private VirtualNetworkFunctionDescriptorAgent virtualNetworkFunctionDescriptorAgent;
  private VNFFGAgent vnffgAgent;
  private EventAgent eventAgent;
  private VNFPackageAgent vnfPackageAgent;
  private ProjectAgent projectAgent;
  private UserAgent userAgent;
  private KeyAgent keyAgent;

  /**
   * Constructor for the NFVORequestor.
   *
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  public NFVORequestor(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    this.username = username;
    this.password = password;
    this.projectId = projectId;
    this.sslEnabled = sslEnabled;
    this.nfvoIp = nfvoIp;
    this.nfvoPort = nfvoPort;
    this.version = version;
  }

  /**
   * Constructor for the NFVORequestor, which takes the Project's name instead of the Project ID.
   * This constructor sends a request to the NFVO and checks if a Project with the given name
   * exists.
   *
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param sslEnabled true if the NFVO uses SSL
   * @param projectName the name of the NFVO Project that will be used in the requests to the NFVO
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @throws SDKException
   */
  public NFVORequestor(
      String username,
      String password,
      boolean sslEnabled,
      String projectName,
      String nfvoIp,
      String nfvoPort,
      String version)
      throws SDKException {
    this.username = username;
    this.password = password;
    this.sslEnabled = sslEnabled;
    this.nfvoIp = nfvoIp;
    this.nfvoPort = nfvoPort;
    this.version = version;

    this.projectId = "";
    try {
      for (Project project : this.getProjectAgent().findAll()) {
        if (project.getName().equals(projectName)) {
          this.projectId = project.getId();
          break;
        }
      }
    } catch (ClassNotFoundException e) {
      throw new SDKException(e.getCause());
    }
    if (this.projectId.equals(""))
      throw new SDKException(
          "Could not create the NFVORequestor",
          null,
          "Did not find a Project named " + projectName);
  }

  /**
   * Returns a ConfigurationAgent with which requests regarding Configurations can be sent to the
   * NFVO.
   *
   * @return a ConfigurationAgent
   */
  public ConfigurationAgent getConfigurationAgent() {
    if (this.configurationAgent == null)
      this.configurationAgent =
          new ConfigurationAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.configurationAgent;
  }

  /**
   * Returns a NetworkServiceDescriptorAgent with which requests regarding NetworkServiceDescriptors
   * can be sent to the NFVO.
   *
   * @return a NetworkServiceDescriptorAgent
   */
  public NetworkServiceDescriptorAgent getNetworkServiceDescriptorAgent() {
    if (this.networkServiceDescriptorAgent == null)
      this.networkServiceDescriptorAgent =
          new NetworkServiceDescriptorAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.networkServiceDescriptorAgent;
  }

  /**
   * Returns a VirtualNetworkFunctionDescriptorAgent with which requests regarding
   * VirtualNetworkFunctionDescriptors can be sent to the NFVO.
   *
   * @return a VirtualNetworkFunctionDescriptorAgent
   */
  public VirtualNetworkFunctionDescriptorAgent getVirtualNetworkFunctionDescriptorAgent() {
    if (this.virtualNetworkFunctionDescriptorAgent == null)
      this.virtualNetworkFunctionDescriptorAgent =
          new VirtualNetworkFunctionDescriptorAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.virtualNetworkFunctionDescriptorAgent;
  }

  /**
   * Returns a NetworkServiceRecordAgent with which requests regarding NetworkServiceRecords can be
   * sent to the NFVO.
   *
   * @return a NetworkServiceRecordAgent
   */
  public NetworkServiceRecordAgent getNetworkServiceRecordAgent() {
    if (this.networkServiceRecordAgent == null)
      this.networkServiceRecordAgent =
          new NetworkServiceRecordAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.networkServiceRecordAgent;
  }

  /**
   * Returns a VimInstanceAgent with which requests regarding VimInstances can be sent to the NFVO.
   *
   * @return a VimInstanceAgent
   */
  public VimInstanceAgent getVimInstanceAgent() {
    if (this.vimInstanceAgent == null)
      this.vimInstanceAgent =
          new VimInstanceAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.vimInstanceAgent;
  }

  /**
   * Returns a VirtualLinkAgent with which requests regarding VirtualLinks can be sent to the NFVO.
   *
   * @return a VirtualLinkAgent
   */
  public VirtualLinkAgent getVirtualLinkAgent() {
    if (this.virtualLinkAgent == null)
      this.virtualLinkAgent =
          new VirtualLinkAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.virtualLinkAgent;
  }

  /**
   * Returns a VirtualNetworkFunctionDescriptorAgent with which requests regarding
   * VirtualNetworkFunctionDescriptors can be sent to the NFVO.
   *
   * @return a VirtualNetworkFunctionDescriptorAgent
   */
  public VirtualNetworkFunctionDescriptorAgent getVirtualNetworkFunctionDescriptorRestAgent() {
    if (this.virtualNetworkFunctionDescriptorAgent == null)
      this.virtualNetworkFunctionDescriptorAgent =
          new VirtualNetworkFunctionDescriptorAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.virtualNetworkFunctionDescriptorAgent;
  }

  /**
   * Returns a VNFFGAgent with which requests regarding VNFFGAgent can be sent to the NFVO.
   *
   * @return a VNFFGAgent
   */
  public VNFFGAgent getVNFFGAgent() {
    if (this.vnffgAgent == null)
      this.vnffgAgent =
          new VNFFGAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.vnffgAgent;
  }

  /**
   * Returns an EventAgent with which requests regarding Events can be sent to the NFVO.
   *
   * @return an EventAgent
   */
  public EventAgent getEventAgent() {
    if (this.eventAgent == null)
      this.eventAgent =
          new EventAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.eventAgent;
  }

  /**
   * Returns a VNFPackageAgent with which requests regarding VNFPackages can be sent to the NFVO.
   *
   * @return a VNFPackageAgent
   */
  public VNFPackageAgent getVNFPackageAgent() {
    if (this.vnfPackageAgent == null)
      this.vnfPackageAgent =
          new VNFPackageAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.vnfPackageAgent;
  }

  /**
   * Returns a ProjectAgent with which requests regarding Projects can be sent to the NFVO.
   *
   * @return a ProjectAgent
   */
  public ProjectAgent getProjectAgent() {
    if (this.projectAgent == null)
      this.projectAgent =
          new ProjectAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.projectAgent;
  }

  /**
   * Returns a UserAgent with which requests regarding Users can be sent to the NFVO.
   *
   * @return a UserAgent
   */
  public UserAgent getUserAgent() {
    if (this.userAgent == null)
      this.userAgent =
          new UserAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.userAgent;
  }

  /**
   * Returns a KeyAgent with which requests regarding Keys can be sent to the NFVO.
   *
   * @return a KeyAgent
   */
  public KeyAgent getKeyAgent() {
    if (this.keyAgent == null)
      this.keyAgent =
          new KeyAgent(
              this.username,
              this.password,
              this.projectId,
              this.sslEnabled,
              this.nfvoIp,
              this.nfvoPort,
              this.version);
    return this.keyAgent;
  }
}
