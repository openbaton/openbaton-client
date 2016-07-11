package org.openbaton.sdk.api.rest;

import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton VirtualNetworkFunctionDescriptor-related api requester.
 */
public class VirtualNetworkFunctionDescriptorRestAgent
    extends AbstractRestAgent<VirtualNetworkFunctionDescriptor> {

  /**
   * Create a VirtualNetworkFunctionDescriptor requester with a given url path
   *
   * @param nfvoIp the url path used for the api requests
   */
  public VirtualNetworkFunctionDescriptorRestAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String path,
      String version) {
    super(
        username,
        password,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        path,
        version,
        VirtualNetworkFunctionDescriptor.class);
  }
}
