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

import org.openbaton.catalogue.security.Project;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.rest.*;

/**
 * This class serves as a creator of request agents for the NFVO. These agents can be obtained by
 * get methods and provide methods for sending requests to the NFVO API. The agents have the same
 * configuration as the NFVORequestor object from which they are obtained. In this way it is easier
 * to get the appropriate agents that are needed without calling the particular constructors each
 * time. The NFVORequestor class is thread safe.
 */
public final class NFVORequestor {

  private String serviceKey;
  private String username;
  private String password;
  private boolean isService;
  private String serviceName;
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
  // if a new agent is added please keep in mind to update the resetAgents method

  /**
   * Constructor for the NFVORequestor.
   *
   * @deprecated Please use NfvoRequestorBuilder
   * @see NfvoRequestorBuilder
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  @Deprecated
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
    this.isService = false;
  }

  /**
   * Constructor for the NFVORequestor, which takes the Project's name instead of the Project ID.
   * This constructor sends a request to the NFVO and checks if a Project with the given name
   * exists.
   *
   * @deprecated Please use NfvoRequestorBuilder
   * @see NfvoRequestorBuilder
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param sslEnabled true if the NFVO uses SSL
   * @param projectName the name of the NFVO Project that will be used in the requests to the NFVO
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @throws SDKException in case of exception
   */
  @Deprecated
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
    this.isService = false;
    try {
      this.projectId = getProjectIdForProjectName(projectName);
    } catch (SDKException e) {
      throw new SDKException(
          "Could not create the NFVORequestor", e.getStackTraceElements(), e.getReason());
    }
  }

  /**
   * Constructor for the NFVORequestor in service mode.
   *
   * @deprecated Please use NfvoRequestorBuilder
   * @see NfvoRequestorBuilder
   * @param serviceName the name of the service to use for requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @param serviceKey the key for authenticating the service
   * @throws SDKException in case of exception
   */
  @Deprecated
  public NFVORequestor(
      String serviceName,
      String projectId,
      String nfvoIp,
      String nfvoPort,
      String version,
      boolean sslEnabled,
      String serviceKey)
      throws SDKException {
    this.serviceName = serviceName;
    this.isService = true;
    this.projectId = projectId;
    this.sslEnabled = sslEnabled;
    this.nfvoIp = nfvoIp;
    this.nfvoPort = nfvoPort;
    this.version = version;
    this.serviceKey = serviceKey;
  }

  /**
   * Returns a ConfigurationAgent with which requests regarding Configurations can be sent to the
   * NFVO.
   *
   * @return a ConfigurationAgent
   */
  public synchronized ConfigurationAgent getConfigurationAgent() {
    if (this.configurationAgent == null) {
      if (isService) {
        this.configurationAgent =
            new ConfigurationAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.configurationAgent =
            new ConfigurationAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.configurationAgent;
  }

  /**
   * Returns a NetworkServiceDescriptorAgent with which requests regarding NetworkServiceDescriptors
   * can be sent to the NFVO.
   *
   * @return a NetworkServiceDescriptorAgent
   */
  public synchronized NetworkServiceDescriptorAgent getNetworkServiceDescriptorAgent() {
    if (this.networkServiceDescriptorAgent == null) {
      if (isService) {
        this.networkServiceDescriptorAgent =
            new NetworkServiceDescriptorAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.networkServiceDescriptorAgent =
            new NetworkServiceDescriptorAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.networkServiceDescriptorAgent;
  }

  /**
   * Returns a VirtualNetworkFunctionDescriptorAgent with which requests regarding
   * VirtualNetworkFunctionDescriptors can be sent to the NFVO.
   *
   * @return a VirtualNetworkFunctionDescriptorAgent
   */
  public synchronized VirtualNetworkFunctionDescriptorAgent
      getVirtualNetworkFunctionDescriptorAgent() {
    if (this.virtualNetworkFunctionDescriptorAgent == null) {
      if (isService) {
        this.virtualNetworkFunctionDescriptorAgent =
            new VirtualNetworkFunctionDescriptorAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.virtualNetworkFunctionDescriptorAgent =
            new VirtualNetworkFunctionDescriptorAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.virtualNetworkFunctionDescriptorAgent;
  }

  /**
   * Returns a NetworkServiceRecordAgent with which requests regarding NetworkServiceRecords can be
   * sent to the NFVO.
   *
   * @return a NetworkServiceRecordAgent
   */
  public synchronized NetworkServiceRecordAgent getNetworkServiceRecordAgent() {
    if (this.networkServiceRecordAgent == null) {
      if (isService) {
        this.networkServiceRecordAgent =
            new NetworkServiceRecordAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.networkServiceRecordAgent =
            new NetworkServiceRecordAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.networkServiceRecordAgent;
  }

  /**
   * Returns a VimInstanceAgent with which requests regarding VimInstances can be sent to the NFVO.
   *
   * @return a VimInstanceAgent
   */
  public synchronized VimInstanceAgent getVimInstanceAgent() {
    if (this.vimInstanceAgent == null) {
      if (isService) {
        this.vimInstanceAgent =
            new VimInstanceAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.vimInstanceAgent =
            new VimInstanceAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.vimInstanceAgent;
  }

  /**
   * Returns a VirtualLinkAgent with which requests regarding VirtualLinks can be sent to the NFVO.
   *
   * @return a VirtualLinkAgent
   */
  public synchronized VirtualLinkAgent getVirtualLinkAgent() {
    if (this.virtualLinkAgent == null) {
      if (isService) {
        this.virtualLinkAgent =
            new VirtualLinkAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.virtualLinkAgent =
            new VirtualLinkAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.virtualLinkAgent;
  }

  /**
   * Returns a VirtualNetworkFunctionDescriptorAgent with which requests regarding
   * VirtualNetworkFunctionDescriptors can be sent to the NFVO.
   *
   * @return a VirtualNetworkFunctionDescriptorAgent
   */
  public synchronized VirtualNetworkFunctionDescriptorAgent
      getVirtualNetworkFunctionDescriptorRestAgent() {
    if (this.virtualNetworkFunctionDescriptorAgent == null) {
      if (isService) {
        this.virtualNetworkFunctionDescriptorAgent =
            new VirtualNetworkFunctionDescriptorAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.virtualNetworkFunctionDescriptorAgent =
            new VirtualNetworkFunctionDescriptorAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.virtualNetworkFunctionDescriptorAgent;
  }

  /**
   * Returns a VNFFGAgent with which requests regarding VNFFGAgent can be sent to the NFVO.
   *
   * @return a VNFFGAgent
   */
  public synchronized VNFFGAgent getVNFFGAgent() {
    if (this.vnffgAgent == null) {
      if (isService) {
        this.vnffgAgent =
            new VNFFGAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.vnffgAgent =
            new VNFFGAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.vnffgAgent;
  }

  /**
   * Returns an EventAgent with which requests regarding Events can be sent to the NFVO.
   *
   * @return an EventAgent
   */
  public synchronized EventAgent getEventAgent() {
    if (this.eventAgent == null) {
      if (isService) {
        this.eventAgent =
            new EventAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.eventAgent =
            new EventAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.eventAgent;
  }

  /**
   * Returns a VNFPackageAgent with which requests regarding VNFPackages can be sent to the NFVO.
   *
   * @return a VNFPackageAgent
   */
  public synchronized VNFPackageAgent getVNFPackageAgent() {
    if (this.vnfPackageAgent == null) {
      if (isService) {
        this.vnfPackageAgent =
            new VNFPackageAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.vnfPackageAgent =
            new VNFPackageAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.vnfPackageAgent;
  }

  /**
   * Returns a ProjectAgent with which requests regarding Projects can be sent to the NFVO.
   *
   * @return a ProjectAgent
   */
  public synchronized ProjectAgent getProjectAgent() {
    if (this.projectAgent == null) {
      if (isService) {
        this.projectAgent =
            new ProjectAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.projectAgent =
            new ProjectAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.projectAgent;
  }

  /**
   * Returns a UserAgent with which requests regarding Users can be sent to the NFVO.
   *
   * @return a UserAgent
   */
  public synchronized UserAgent getUserAgent() {
    if (this.userAgent == null) {
      if (isService) {
        this.userAgent =
            new UserAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.userAgent =
            new UserAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.userAgent;
  }

  /**
   * Returns a KeyAgent with which requests regarding Keys can be sent to the NFVO.
   *
   * @return a KeyAgent
   */
  public synchronized KeyAgent getKeyAgent() {
    if (this.keyAgent == null) {
      if (isService) {
        this.keyAgent =
            new KeyAgent(
                this.serviceName,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version,
                this.serviceKey);
      } else {
        this.keyAgent =
            new KeyAgent(
                this.username,
                this.password,
                this.projectId,
                this.sslEnabled,
                this.nfvoIp,
                this.nfvoPort,
                this.version);
      }
    }
    return this.keyAgent;
  }

  /**
   * Set the NFVORequestor's project id. See the {@link #switchProject(String) switchProject} method
   * for a more convenient alternative.
   *
   * @param projectId the project id
   */
  public synchronized void setProjectId(String projectId) {
    // Set the agents to null so that no outdated agent is returned
    resetAgents();
    this.projectId = projectId;
  }

  /**
   * Get the NFVORequestor's project id.
   *
   * @return the current project id
   */
  public synchronized String getProjectId() {
    return this.projectId;
  }

  /**
   * Change the project related to this NFVORequestor. This is a convenient alternative for the
   * {@link #setProjectId(String) setProjectId} method. It throws an SDKException if no project
   * exists with the given projectName.
   *
   * @param projectName the name of the project to switch to
   * @throws SDKException in case of exception
   */
  public synchronized void switchProject(String projectName) throws SDKException {
    try {
      this.projectId = getProjectIdForProjectName(projectName);
      // Set the agents to null so that no outdated agent is returned
      resetAgents();
    } catch (SDKException e) {
      throw new SDKException(
          "Could not switch to project " + projectName, e.getStackTraceElements(), e.getReason());
    }
  }

  /**
   * Return the project id for a given project name.
   *
   * @param projectName the name of the project
   * @return the project id for the given project name
   * @throws SDKException in case of exception
   */
  private String getProjectIdForProjectName(String projectName) throws SDKException {
    for (Project project : this.getProjectAgent().findAll()) {
      if (project.getName().equals(projectName)) {
        return project.getId();
      }
    }
    throw new SDKException(
        "Did not find a Project named " + projectName,
        null,
        "Did not find a Project named " + projectName);
  }

  /** Set all the agent objects to null. */
  private void resetAgents() {
    this.configurationAgent = null;
    this.keyAgent = null;
    this.userAgent = null;
    this.vnfPackageAgent = null;
    this.projectAgent = null;
    this.eventAgent = null;
    this.vnffgAgent = null;
    this.virtualNetworkFunctionDescriptorAgent = null;
    this.virtualLinkAgent = null;
    this.vimInstanceAgent = null;
    this.networkServiceDescriptorAgent = null;
    this.networkServiceRecordAgent = null;
  }
}
