/*
 * Copyright (c) 2015 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openbaton.sdk;

import org.openbaton.sdk.api.rest.*;
import org.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton api requestor. The Class is implemented in a static way to avoid any dependencies to
 * spring and to create a corresponding small lib size.
 */
public final class NFVORequestor {

  private static RequestFactory factory;

  /**
   * The public constructor for an NFVORequestor to a NFVO which runs on localhost port 8443 and
   * uses SSL
   *
   * @param username
   * @param password
   * @param projectId
   * @param version
   */
  public NFVORequestor(String username, String password, String projectId, String version) {
    factory =
        RequestFactory.getInstance(
            username, password, projectId, true, "localhost", "8443", version);
  }

  /**
   * The public constructor for an NFVORequestor to a NFVO which runs on localhost and depending
   * whether ssl is enabled or not the port will be chosen as 8443 or 8080
   *
   * @param username
   * @param password
   * @param projectId
   * @param sslEnabled
   * @param version
   */
  public NFVORequestor(
      String username, String password, String projectId, boolean sslEnabled, String version) {
    if (sslEnabled)
      factory =
          RequestFactory.getInstance(
              username, password, projectId, sslEnabled, "localhost", "8443", version);
    else
      factory =
          RequestFactory.getInstance(
              username, password, projectId, sslEnabled, "localhost", "8080", version);
  }

  /**
   * The public constructor for an NFVORequestor
   *
   * @param username
   * @param password
   * @param projectId
   * @param sslEnabled
   * @param nfvoIp
   * @param nfvoPort
   * @param version
   */
  public NFVORequestor(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    factory =
        RequestFactory.getInstance(
            username, password, projectId, sslEnabled, nfvoIp, nfvoPort, version);
  }

  /**
   * Gets the configuration requester
   *
   * @return configurationRequest: The (final) static configuration requester
   */
  public ConfigurationRestRequest getConfigurationAgent() {
    return factory.getConfigurationAgent();
  }

  /**
   * Gets the image requester
   *
   * @return image: The (final) static image requester
   */
  public ImageRestAgent getImageAgent() {
    return factory.getImageAgent();
  }

  /**
   * Gets the networkServiceDescriptor requester
   *
   * @return networkServiceDescriptorRequest: The (final) static networkServiceDescriptor requester
   */
  public NetworkServiceDescriptorRestAgent getNetworkServiceDescriptorAgent() {
    return factory.getNetworkServiceDescriptorAgent();
  }

  /**
   * Gets the networkServiceDescriptor requester
   *
   * @return networkServiceDescriptorRequest: The (final) static networkServiceDescriptor requester
   */
  public VirtualNetworkFunctionDescriptorRestAgent getVirtualNetworkFunctionDescriptorAgent() {
    return factory.getVirtualNetworkFunctionDescriptorAgent();
  }

  /**
   * Gets the networkServiceRecord requester
   *
   * @return networkServiceRecordRequest: The (final) static networkServiceRecord requester
   */
  public NetworkServiceRecordRestAgent getNetworkServiceRecordAgent() {
    return factory.getNetworkServiceRecordAgent();
  }

  /**
   * Gets the vimInstance requester
   *
   * @return vimInstanceRequest: The (final) static vimInstance requester
   */
  public VimInstanceRestAgent getVimInstanceAgent() {
    return factory.getVimInstanceAgent();
  }

  /**
   * Gets the virtualLink requester
   *
   * @return virtualLinkRequest: The (final) static virtualLink requester
   */
  public VirtualLinkRestAgent getVirtualLinkAgent() {
    return factory.getVirtualLinkAgent();
  }

  /**
   * Gets the VirtualNetworkFunctionDescriptor requester
   *
   * @return vnfdRequest; The (final) static VirtualNetworkFunctionDescriptor requester
   */
  public VirtualNetworkFunctionDescriptorRestAgent getVirtualNetworkFunctionDescriptorRestAgent() {
    return factory.getVirtualNetworkFunctionDescriptorAgent();
  }

  /**
   * Gets the VNFFG requester
   *
   * @return vNFFGRequest: The (final) static vNFFG requester
   */
  public VNFFGRestAgent getVNFFGAgent() {
    return factory.getVNFForwardingGraphAgent();
  }

  /**
   * Gets the Event requester
   *
   * @return eventRequest; The (final) static Event requester
   */
  public EventAgent getEventAgent() {
    return factory.getEventAgent();
  }

  /**
   * Gets the VNFPackage requester
   *
   * @return vnfPackageRequest; The (final) static VNFPackage requester
   */
  public VNFPackageAgent getVNFPackageAgent() {
    return factory.getVNFPackageAgent();
  }

  /**
   * Gets the Project requester
   *
   * @return projectRequest; The (final) static Project requester
   */
  public ProjectAgent getProjectAgent() {
    return factory.getProjectAgent();
  }

  /**
   * Gets the User requester
   *
   * @return userRequest; The (final) static User requester
   */
  public UserAgent getUserAgent() {
    return factory.getUserAgent();
  }

  /**
   * Gets the Key requester
   *
   * @return keyRequest; The (final) static Key requester
   */
  public KeyAgent getKeyAgent() {
    return factory.getKeyAgent();
  }

  public AbstractRestAgent abstractRestAgent(Class clazz, String path) {
    return factory.getAbstractAgent(clazz, path);
  }

  public void setProjectId(String projectId) {
    RequestFactory.setProjectId(projectId);
  }

  public String getProjectId() {
    return RequestFactory.getProjectId();
  }
}
