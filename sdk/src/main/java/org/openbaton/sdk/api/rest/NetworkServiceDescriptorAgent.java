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

package org.openbaton.sdk.api.rest;

import org.openbaton.catalogue.mano.common.Security;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.PhysicalNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.descriptor.VNFDependency;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;

import java.util.Arrays;
import java.util.List;

/**
 * This class is a Rest Request Agent for sending requests regarding NetworkServiceDescriptor
 * objects to the NFVO API.
 */
public class NetworkServiceDescriptorAgent extends AbstractRestAgent<NetworkServiceDescriptor> {

  /**
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  public NetworkServiceDescriptorAgent(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    super(
        username,
        password,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        version,
        NetworkServiceDescriptor.class);
  }

  /**
   * Get all VirtualNetworkFunctionDescriptors contained in a NetworkServiceDescriptor specified by
   * its ID.
   *
   * @param idNSD the NetworkServiceDescriptor's id
   * @return a List of all VirtualNetworkServiceDescriptors contained in the
   *     NetworkServiceDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Get all the VirtualNetworkFunctionDescriptors of a NetworkServiceDescriptor with specific id"
  )
  @Deprecated
  public List<VirtualNetworkFunctionDescriptor> getVirtualNetworkFunctionDescriptors(
      final String idNSD) throws SDKException {
    String url = idNSD + "/vnfdescriptors";
    return Arrays.asList(
        (VirtualNetworkFunctionDescriptor[])
            requestGetAll(url, VirtualNetworkFunctionDescriptor.class));
  }

  /**
   * Return a VirtualNetworkFunctionDescriptor that is contained in a particular
   * NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @param idVfn the id of the VirtualNetworkFunctionDescriptor
   * @return the VirtualNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Get a specific VirtualNetworkFunctionDescriptor of a particular NetworkServiceDescriptor specified by their IDs"
  )
  @Deprecated
  public VirtualNetworkFunctionDescriptor getVirtualNetworkFunctionDescriptor(
      final String idNSD, final String idVfn) throws SDKException {
    String url = idNSD + "/vnfdescriptors" + "/" + idVfn;
    return (VirtualNetworkFunctionDescriptor)
        requestGet(url, VirtualNetworkFunctionDescriptor.class);
  }

  /**
   * Delete a specific VirtualNetworkFunctionDescriptor that is contained in a particular
   * NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @param idVnf the id of the VirtualNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Delete the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  @Deprecated
  public void deleteVirtualNetworkFunctionDescriptors(final String idNSD, final String idVnf)
      throws SDKException {
    String url = idNSD + "/vnfdescriptors" + "/" + idVnf;
    requestDelete(url);
  }

  /**
   * Create a VirtualNetworkFunctionDescriptor in a specific NetworkServiceDescriptor.
   *
   * @param idNSD : The id of the networkServiceDescriptor the vnfd shall be created at
   * @param virtualNetworkFunctionDescriptor : : the Network Service Descriptor to be updated
   * @throws SDKException
   */
  @Help(
    help =
        "create the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  @Deprecated
  public VirtualNetworkFunctionDescriptor createVNFD(
      final String idNSD, final VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor)
      throws SDKException {
    String url = idNSD + "/vnfdescriptors" + "/";
    return (VirtualNetworkFunctionDescriptor) requestPost(url, virtualNetworkFunctionDescriptor);
  }

  /**
   * Update a specific VirtualNetworkFunctionDescriptor that is contained in a particular
   * NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceRecord containing the VirtualNetworkFunctionDescriptor
   * @param idVfn the ID of the VNF Descriptor that shall be updated
   * @param virtualNetworkFunctionDescriptor the updated version of the
   *     VirtualNetworkFunctionDescriptor
   * @return the updated VirtualNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Update the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  @Deprecated
  public VirtualNetworkFunctionDescriptor updateVNFD(
      final String idNSD,
      final String idVfn,
      final VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor)
      throws SDKException {
    String url = idNSD + "/vnfdescriptors" + "/" + idVfn;
    return (VirtualNetworkFunctionDescriptor) requestPut(url, virtualNetworkFunctionDescriptor);
  }

  /**
   * Return a List with all the VNFDependencies that are contained in a specific
   * NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @return the List of VNFDependencies
   * @throws SDKException
   */
  @Help(
    help =
        "Get all the VirtualNetworkFunctionDescriptor Dependency of a NetworkServiceDescriptor with specific id"
  )
  public List<VNFDependency> getVNFDependencies(final String idNSD) throws SDKException {
    String url = idNSD + "/vnfdependencies";
    return Arrays.asList((VNFDependency[]) requestGetAll(url, VNFDependency.class));
  }

  /**
   * Return a specific VNFDependency that is contained in a particular NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @param idVnfd the VNFDependencies' ID
   * @return the VNFDependency
   * @throws SDKException
   */
  @Help(
    help =
        "get the VirtualNetworkFunctionDescriptor dependency with specific id of a NetworkServiceDescriptor with specific id"
  )
  public VNFDependency getVNFDependency(final String idNSD, final String idVnfd)
      throws SDKException {
    String url = idNSD + "/vnfdependencies" + "/" + idVnfd;
    return (VNFDependency) requestGet(url, VNFDependency.class);
  }

  /**
   * Delete a VNFDependency.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor which contains the VNFDependency
   * @param idVnfd the ID of the VNFDependency that shall be deleted
   * @throws SDKException
   */
  @Help(
    help =
        "Delete the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id"
  )
  public void deleteVNFDependency(final String idNSD, final String idVnfd) throws SDKException {
    String url = idNSD + "/vnfdependencies" + "/" + idVnfd;
    requestDelete(url);
  }

  /**
   * Add a new VNFDependency to a specific NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @param vnfDependency the new VNFDependency
   * @return the new VNFDependency
   * @throws SDKException
   */
  @Help(
    help =
        "Create the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id"
  )
  public VNFDependency createVNFDependency(final String idNSD, final VNFDependency vnfDependency)
      throws SDKException {
    String url = idNSD + "/vnfdependencies" + "/";
    return (VNFDependency) requestPost(url, vnfDependency);
  }

  /**
   * Update a specific VNFDependency which is contained in a particular NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor containing the VNFDependency
   * @param idVnfDep the ID of the VNFDependency which shall be updated
   * @param vnfDependency the updated version of the VNFDependency
   * @return the updated VNFDependency
   * @throws SDKException
   */
  @Help(
    help =
        "Update the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id"
  )
  public VNFDependency updateVNFD(
      final String idNSD, final String idVnfDep, final VNFDependency vnfDependency)
      throws SDKException {
    String url = idNSD + "/vnfdependencies" + "/" + idVnfDep;
    return (VNFDependency) requestPut(url, vnfDependency);
  }

  /**
   * Returns the List of PhysicalNetworkFunctionDescriptors that are contained in a specific
   * NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @return the List of PhysicalNetworkFunctionDescriptors
   * @throws SDKException
   */
  @Help(
    help =
        "Get all the PhysicalNetworkFunctionDescriptors of a NetworkServiceDescriptor with specific id"
  )
  public List<PhysicalNetworkFunctionDescriptor> getPhysicalNetworkFunctionDescriptors(
      final String idNSD) throws SDKException {
    String url = idNSD + "/pnfdescriptors";
    return Arrays.asList(
        (PhysicalNetworkFunctionDescriptor[])
            requestGetAll(url, PhysicalNetworkFunctionDescriptor.class));
  }

  /**
   * Returns a specific PhysicalNetworkFunctionDescriptor that is contained in a particular
   * NetworkServiceDescriptor.
   *
   * @param idNsd the NetworkServiceDescriptr's ID
   * @param idPnf the PhysicalNetworkFunctionDescriptor's ID
   * @return the PhysicalNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Get the PhysicalNetworkFunctionDescriptor with specific id of a NetworkServiceDescriptor with specific id"
  )
  public PhysicalNetworkFunctionDescriptor getPhysicalNetworkFunctionDescriptor(
      final String idNsd, final String idPnf) throws SDKException {
    String url = idNsd + "/pnfdescriptors" + "/" + idPnf;
    return (PhysicalNetworkFunctionDescriptor)
        requestGetWithStatusAccepted(url, PhysicalNetworkFunctionDescriptor.class);
  }

  /**
   * Delete a specific PhysicalNetworkFunctionDescriptor which is contained in a particular
   * NetworkServiceDescriptor.
   *
   * @param idNsd the NetworkServiceDescriptor's ID
   * @param idPnf :the PhysicalNetworkFunctionDescriptor's ID
   * @throws SDKException
   */
  @Help(
    help =
        "Delete the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  public void deletePhysicalNetworkFunctionDescriptor(final String idNsd, final String idPnf)
      throws SDKException {
    String url = idNsd + "/pnfdescriptors" + "/" + idPnf;
    requestDelete(url);
  }

  /**
   * Create a new PhysicalNetworkFunctionDescriptor in a NetworkServiceDescriptor
   *
   * @param idNsd the NetworkServiceDescriptor's ID
   * @param physicalNetworkFunctionDescriptor the new PhysicalNetworkFunctionDescriptor
   * @return the created PhysicalNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Create the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  public PhysicalNetworkFunctionDescriptor createPhysicalNetworkFunctionDescriptor(
      final String idNsd, final PhysicalNetworkFunctionDescriptor physicalNetworkFunctionDescriptor)
      throws SDKException {
    String url = idNsd + "/pnfdescriptors";
    return (PhysicalNetworkFunctionDescriptor) requestPost(url, physicalNetworkFunctionDescriptor);
  }

  /**
   * Update a PhysicalNetworkFunctionDescriptor.
   *
   * @param idNsd the NetworkFunctionDescriptor's ID
   * @param idPnf the PhysicalNetworkFunctionDescriptor's ID
   * @param physicalNetworkFunctionDescriptor the updated version of the
   *     PhysicalNetworkFunctionDescriptor
   * @return the updated PhysicalNetworkFunctionDescriptor
   * @throws SDKException
   */
  @Help(
    help =
        "Update the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id"
  )
  public PhysicalNetworkFunctionDescriptor updatePNFD(
      final String idNsd,
      final String idPnf,
      final PhysicalNetworkFunctionDescriptor physicalNetworkFunctionDescriptor)
      throws SDKException {
    String url = idNsd + "/pnfdescriptors" + "/" + idPnf;
    return (PhysicalNetworkFunctionDescriptor) requestPut(url, physicalNetworkFunctionDescriptor);
  }

  /**
   * Returns a List of all Security objects that are contained in a specific
   * NetworkServiceDescriptor.
   *
   * @param idNsd the ID of the NetworkServiceDescriptor
   * @return the List of Security objects
   * @throws SDKException
   */
  @Help(help = "Get all the Security of a NetworkServiceDescriptor with specific id")
  public Security getSecurities(final String idNsd) throws SDKException {
    String url = idNsd + "/security";
    return ((Security) requestGet(url, Security.class));
  }

  /**
   * Delete a Security object.
   *
   * @param idNsd the NetworkServiceDescriptor's ID
   * @param idSecurity the Security object's ID
   * @throws SDKException
   */
  @Help(help = "Delete the Security of a NetworkServiceDescriptor with specific id")
  public void deleteSecurity(final String idNsd, final String idSecurity) throws SDKException {
    String url = idNsd + "/security" + "/" + idSecurity;
    requestDelete(url);
  }

  /**
   * Add a new Security object to a NetworkServiceDescriptor.
   *
   * @param idNSD the ID of the NetworkServiceDescriptor
   * @param security the Security object to add
   * @return the new Security object
   * @throws SDKException
   */
  @Help(help = "Create the Security of a NetworkServiceDescriptor with specific id")
  public Security createSecurity(final String idNSD, final Security security) throws SDKException {
    String url = idNSD + "/security" + "/";
    return (Security) requestPost(url, security);
  }

  /**
   * Update a Security object of a specific NetworkServiceDescriptor.
   *
   * @param idNSD the id of the NetworkServiceDescriptor
   * @param idSecurity the ID of the Security object which shall be updated
   * @param updatedSecurity the updated version of the Security object
   * @return the updated Security object
   * @throws SDKException
   */
  @Help(help = "Update the Security of a NetworkServiceDescriptor with specific id")
  public Security updateSecurity(
      final String idNSD, final String idSecurity, final Security updatedSecurity)
      throws SDKException {
    String url = idNSD + "/security" + "/" + idSecurity;
    return (Security) requestPut(url, updatedSecurity);
  }
}
