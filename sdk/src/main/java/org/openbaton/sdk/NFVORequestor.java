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

import org.openbaton.sdk.api.rest.*;
import org.openbaton.sdk.api.util.AbstractRestAgent;

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
    this.sslEnabled = true;
    this.nfvoIp = nfvoIp;
    this.nfvoPort = nfvoPort;
    this.version = version;
  }

  /**
   * Gets the configuration requester
   *
   * @return configurationRequest: The (final) static configuration requester
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
   * Gets the networkServiceDescriptor requester
   *
   * @return networkServiceDescriptorRequest: The (final) static networkServiceDescriptor requester
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
   * Gets the networkServiceDescriptor requester
   *
   * @return networkServiceDescriptorRequest: The (final) static networkServiceDescriptor requester
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
   * Gets the networkServiceRecord requester
   *
   * @return networkServiceRecordRequest: The (final) static networkServiceRecord requester
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
   * Gets the vimInstance requester
   *
   * @return vimInstanceRequest: The (final) static vimInstance requester
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
   * Gets the virtualLink requester
   *
   * @return virtualLinkRequest: The (final) static virtualLink requester
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
   * Gets the VirtualNetworkFunctionDescriptor requester
   *
   * @return vnfdRequest; The (final) static VirtualNetworkFunctionDescriptor requester
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
   * Gets the VNFFG requester
   *
   * @return vNFFGRequest: The (final) static vNFFG requester
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
   * Gets the Event requester
   *
   * @return eventRequest; The (final) static Event requester
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
   * Gets the VNFPackage requester
   *
   * @return vnfPackageRequest; The (final) static VNFPackage requester
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
   * Gets the Project requester
   *
   * @return projectRequest; The (final) static Project requester
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
   * Gets the User requester
   *
   * @return userRequest; The (final) static User requester
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
   * Gets the Key requester
   *
   * @return keyRequest; The (final) static Key requester
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
