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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openbaton.catalogue.mano.descriptor.VNFComponent;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.PhysicalNetworkFunctionRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFRecordDependency;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Configuration;
import org.openbaton.catalogue.nfvo.messages.Interfaces.NFVMessage;
import org.openbaton.sdk.api.annotations.Help;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * This class is a Rest Request Agent for sending requests regarding NetworkServiceRecord objects to
 * the NFVO API. It is thread safe.
 */
public class NetworkServiceRecordAgent extends AbstractRestAgent<NetworkServiceRecord> {

  /**
   * @param username the username used for sending requests
   * @param password the password used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   */
  public NetworkServiceRecordAgent(
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
        NetworkServiceRecord.class);
  }

  /**
   * @param serviceName the service name used for sending requests
   * @param projectId the NFVO Project's ID that will be used in the requests to the NFVO
   * @param sslEnabled true if the NFVO uses SSL
   * @param nfvoIp the IP address of the NFVO to which the requests are sent
   * @param nfvoPort the port on which the NFVO runs
   * @param version the API version
   * @param serviceKey the key for authenticating the service
   * @throws IllegalArgumentException if the service key is null
   */
  public NetworkServiceRecordAgent(
      String serviceName,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version,
      String serviceKey)
      throws IllegalArgumentException {
    super(
        serviceName,
        projectId,
        sslEnabled,
        nfvoIp,
        nfvoPort,
        version,
        serviceKey,
        NetworkServiceRecord.class);
  }

  /**
   * Create a new NetworkServiceRecord from a NetworkServiceDescriptor.
   *
   * @param id ID of the NetworkServiceDescriptor
   * @param vduVimInstances a HashMap assigning VimInstance names to VirtualDeploymentUnits
   * @param keys an ArrayList of Key names that shall be passed to the NetworkServiceRecord
   * @param configurations a HashMap assigning Configuration objects to VirtualNetworkServiceRecord
   * @param monitoringIp the IP of the monitoring system
   * @return the created NetworkServiceRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "Create NetworkServiceRecord from NetworkServiceDescriptor id")
  public NetworkServiceRecord create(
      final String id,
      HashMap<String, ArrayList<String>> vduVimInstances,
      ArrayList<String> keys,
      HashMap<String, Configuration> configurations,
      String monitoringIp)
      throws SDKException {
    HashMap<String, Serializable> jsonBody = new HashMap<>();
    jsonBody.put("keys", keys);
    jsonBody.put("vduVimInstances", vduVimInstances);
    jsonBody.put("configurations", configurations);
    jsonBody.put("monitoringIp", monitoringIp);
    return (NetworkServiceRecord) this.requestPost(id, jsonBody, NetworkServiceRecord.class);
  }

  /**
   * Returns a List of all the VirtualNetworkFunctionRecords that are contained in a
   * NetworkServiceRecord.
   *
   * @param id ID of the NetworkServiceRecord
   * @return the List of VirtualNetworkFunctionRecords
   * @throws SDKException if the request fails
   */
  @Help(help = "Get all the VirtualNetworkFunctionRecords of NetworkServiceRecord with specific id")
  public List<VirtualNetworkFunctionRecord> getVirtualNetworkFunctionRecords(final String id)
      throws SDKException {
    String url = id + "/vnfrecords";
    return Arrays.asList(
        (VirtualNetworkFunctionRecord[]) requestGetAll(url, VirtualNetworkFunctionRecord.class));
  }

  /**
   * Returns a specific VirtualNetworkFunctionRecord which is contained in a NetworkServiceRecord.
   *
   * @param id the ID of the NetworkServiceRecord
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord
   * @return the VirtualNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "Get the VirtualNetworkFunctionRecord of NetworkServiceRecord with specific id")
  public VirtualNetworkFunctionRecord getVirtualNetworkFunctionRecord(
      final String id, final String idVnfr) throws SDKException {
    String url = id + "/vnfrecords" + "/" + idVnfr;
    return (VirtualNetworkFunctionRecord) requestGet(url, VirtualNetworkFunctionRecord.class);
  }

  /**
   * Deletes a specific VirtualNetworkFunctionRecord.
   *
   * @param id the ID of the NetworkServiceRecord containing the VirtualNetworkFunctionRecord
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord to delete
   * @throws SDKException if the request fails
   */
  @Help(help = "Delete the VirtualNetworkFunctionRecord of NetworkServiceRecord with specific id")
  public void deleteVirtualNetworkFunctionRecord(final String id, final String idVnfr)
      throws SDKException {
    String url = id + "/vnfrecords" + "/" + idVnfr;
    requestDelete(url);
  }

  /**
   * Create a new VNFCInstance in standby mode.
   *
   * @param idNsr the ID of the NetworkServiceRecord to which the VNFCInstance shall be added
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord to which the VNFCInstance shall be
   *     added
   * @param idVdu the ID of the VirtualDeploymentUnit to which the VNFCInstance shall be added
   * @param vnfComponent the VNFComponent from which the VNFCInstance shall be created
   * @param vimInstanceNames the list of vimInstanceNames where you want to deploy the new vnf
   *     component
   * @throws SDKException if the request fails
   */
  @Help(help = "Create VNFCInstance in standby")
  public void createVNFCInstanceInStandby(
      final String idNsr,
      final String idVnfr,
      final String idVdu,
      final VNFComponent vnfComponent,
      ArrayList<String> vimInstanceNames)
      throws SDKException {
    String url = idNsr + "/vnfrecords/" + idVnfr + "/vdunits/" + idVdu + "/vnfcinstances/standby";
    HashMap<String, Serializable> body = new HashMap<>();
    body.put("vnfComponent", vnfComponent);
    body.put("vimInstanceNames", vimInstanceNames);
    requestPost(url, body);
  }

  /**
   * Make a VNFCInstance switch into standby mode.
   *
   * @param idNsr the ID of the NetworkServiceRecord containing the VNFCInstance
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord containing the VNFCInstance
   * @param idVdu the ID of the VirtualDeploymentUnit containing the VNFCInstance
   * @param idVnfc the ID on the VNFCInstance that shall switch into standby mode
   * @param failedVnfcInstance the failed VNFCInstance
   * @throws SDKException if the request fails
   */
  @Help(help = "Switch to standby")
  public void switchToStandby(
      final String idNsr,
      final String idVnfr,
      final String idVdu,
      final String idVnfc,
      final VNFCInstance failedVnfcInstance)
      throws SDKException {
    String url =
        idNsr
            + "/vnfrecords/"
            + idVnfr
            + "/vdunits/"
            + idVdu
            + "/vnfcinstances/"
            + idVnfc
            + "/switchtostandby";
    requestPost(url, failedVnfcInstance);
  }

  /**
   * Trigger the execution of a specific LifecycleEvent on a VNFCInstance. Currently only the HEAL
   * LifecycleEvent is supported.
   *
   * @param idNsr the ID of the NetworkServiceRecord containing the VNFCInstance
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord containing the VNFCInstance
   * @param idVdu the ID of the VirtualDeploymentUnit containing the VNFCInstance
   * @param idVnfc the ID on the VNFCInstance on which a LifecycleEvent shall be executed
   * @param nfvMessage the NFVMessage describing the LifecyceEvent to execute
   * @throws SDKException if the request fails
   */
  @Help(help = "Execute a specific action specified in the nfvMessage")
  public void postAction(
      final String idNsr,
      final String idVnfr,
      final String idVdu,
      final String idVnfc,
      final NFVMessage nfvMessage)
      throws SDKException {
    String url =
        idNsr
            + "/vnfrecords/"
            + idVnfr
            + "/vdunits/"
            + idVdu
            + "/vnfcinstances/"
            + idVnfc
            + "/actions";
    requestPost(url, nfvMessage);
  }

  /** TODO (check the orchestrator) */
  /**
   * Create a new VirtualNetworkFunctionRecord and add it to a NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord to which the VirtualNetworkFunctionRecord shall
   *     be added
   * @param virtualNetworkFunctionRecord the new VirtualNetworkFunctionRecord
   * @return the VirtualNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "create VirtualNetworkFunctionRecord")
  public VirtualNetworkFunctionRecord createVNFR(
      final String idNsr, final VirtualNetworkFunctionRecord virtualNetworkFunctionRecord)
      throws SDKException {
    String url = idNsr + "/vnfrecords";
    return (VirtualNetworkFunctionRecord) requestPost(url, virtualNetworkFunctionRecord);
  }

  /**
   * Create a new VNFCInstance from a VNFComponent without specifying the VirtualDeploymentUnit to
   * which the VNFCInstance shall be added. This is also called a scale out operation.
   *
   * @param idNsr the ID of the NetworkServiceRecord to which the new VNFCInstance shall be added
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord to which the new VNFCInstance shall be
   *     added
   * @param vnfComponent the VNFComponent from which the new VNFCInstance shall be created
   * @param vimInstanceNames the list of vimInstanceNames where you want to deploy the new vnf
   *     component
   * @throws SDKException if the request fails
   */
  @Help(help = "create VNFCInstance. Aka SCALE OUT")
  public void createVNFCInstance(
      final String idNsr,
      final String idVnfr,
      final VNFComponent vnfComponent,
      ArrayList<String> vimInstanceNames)
      throws SDKException {
    String url = idNsr + "/vnfrecords/" + idVnfr + "/vdunits/vnfcinstances";
    HashMap<String, Serializable> body = new HashMap<>();
    body.put("vnfComponent", vnfComponent);
    body.put("vimInstanceNames", vimInstanceNames);
    requestPost(url, body);
  }

  /**
   * Create a new VNFCInstance from a VNFComponent and specify the VirtualDeploymentUnit to which
   * the VNFCInstance shall be added. This is also called a scale out operation.
   *
   * @param idNsr the ID of the NetworkServiceRecord to which the new VNFCInstance shall be added
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord to which the new VNFCInstance shall be
   *     added
   * @param idVdu the VirtualDeploymentUnit to which the new VNFCInstance shall be added
   * @param vnfComponent the VNFComponent from which the new VNFCInstance shall be created
   * @param vimInstanceNames the list of vimInstanceNames where you want to deploy the new vnf
   *     component
   * @throws SDKException if the request fails
   */
  @Help(help = "create VNFCInstance. Aka SCALE OUT")
  public void createVNFCInstance(
      final String idNsr,
      final String idVnfr,
      final String idVdu,
      final VNFComponent vnfComponent,
      ArrayList<String> vimInstanceNames)
      throws SDKException {
    String url = idNsr + "/vnfrecords/" + idVnfr + "/vdunits/" + idVdu + "/vnfcinstances";
    HashMap<String, Serializable> body = new HashMap<>();
    body.put("vnfComponent", vnfComponent);
    body.put("vimInstanceNames", vimInstanceNames);
    requestPost(url, body);
  }

  /**
   * Delete a VNFCInstance from a VirtualNetworkFunctionRecord. This operation is also called
   * scaling in. This method does not require you to specify the VirtualDeploymentUnit from which a
   * VNFCInstance shall be deleted.
   *
   * @param idNsr the ID of the NetworkServiceRecord from which a VNFCInstance shall be deleted
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord from which a VNFCInstance shall be
   *     deleted
   * @throws SDKException if the request fails
   */
  @Help(help = "remove VNFCInstance. Aka SCALE IN")
  public void deleteVNFCInstance(final String idNsr, final String idVnfr) throws SDKException {
    String url = idNsr + "/vnfrecords/" + idVnfr + "/vdunits/vnfcinstances";
    requestDelete(url);
  }

  /**
   * Delete a VNFCInstance from a VirtualDeploymentUnit. This operation is also called scaling in.
   * You have to specify the VirtualDeploymentUnit from which a VNFCInstance shall be deleted but
   * you cannot specify exactly which VNFCInstance is removed.
   *
   * @param idNsr the ID of the NetworkServiceRecord from which a VNFCInstance shall be deleted
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord from which a VNFCInstance shall be
   *     deleted
   * @param idVdu the ID of the VirtualDeploymentUnit from which a VNFCInstance shall be deleted
   * @throws SDKException if the request fails
   */
  @Help(help = "remove VNFCInstance. Aka SCALE IN")
  public void deleteVNFCInstance(final String idNsr, final String idVnfr, final String idVdu)
      throws SDKException {
    String url = idNsr + "/vnfrecords/" + idVnfr + "/vdunits/" + idVdu + "/vnfcinstances";
    requestDelete(url);
  }

  /**
   * Delete a VNFCInstance from a VirtualDeploymentUnit. This operation is also called scaling in.
   * This method lets you specify exactly which VNFCInstance from which VirtualDeploymentUnit shall
   * be deleted.
   *
   * @param idNsr the ID of the NetworkServiceRecord from which the VNFCInstance shall be deleted
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord from which the VNFCInstance shall be
   *     deleted
   * @param idVdu the ID of the VirtualDeploymentUnit from which the VNFCInstance shall be deleted
   * @param idVnfcInstance the ID of the VNFCInstance that shall be deleted
   * @throws SDKException if the request fails
   */
  @Help(help = "remove VNFCInstance. Aka SCALE IN")
  public void deleteVNFCInstance(
      final String idNsr, final String idVnfr, final String idVdu, final String idVnfcInstance)
      throws SDKException {
    String url =
        idNsr + "/vnfrecords/" + idVnfr + "/vdunits/" + idVdu + "/vnfcinstances/" + idVnfcInstance;
    requestDelete(url);
  }

  /** TODO (check the orchestrator) */
  /**
   * Updates a VirtualNetworkFunctionRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord containing the VirtualNetworkFunctionRecord
   * @param idVnfr the ID of the VirtualNetworkFunctionRecord to update
   * @param virtualNetworkFunctionRecord the updated version of the VirtualNetworkFunctionRecord
   * @return the updated VirtualNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "update VirtualNetworkFunctionRecord")
  public String updateVNFR(
      final String idNsr,
      final String idVnfr,
      final VirtualNetworkFunctionRecord virtualNetworkFunctionRecord)
      throws SDKException {
    String url = idNsr + "/vnfrecords" + "/" + idVnfr;
    return requestPut(url, virtualNetworkFunctionRecord).toString();
  }

  /**
   * Returns a List of all the VNFRecordDependencies contained in a particular NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @return a List of VNFRecordDependencies
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Get all the VirtualNetworkFunctionRecord dependencies of NetworkServiceRecord with specific id"
  )
  public List<VNFRecordDependency> getVNFDependencies(final String idNsr) throws SDKException {
    String url = idNsr + "/vnfdependencies";
    return Arrays.asList((VNFRecordDependency[]) requestGetAll(url, VNFRecordDependency.class));
  }

  /**
   * Returns a specific VNFRecordDependency from a particular NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfrDep the ID of the requested VNFRecordDependency
   * @return the VNFRecordDependency
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Get the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id"
  )
  public VNFRecordDependency getVNFDependency(final String idNsr, final String idVnfrDep)
      throws SDKException {
    String url = idNsr + "/vnfdependencies" + "/" + idVnfrDep;
    return (VNFRecordDependency) requestGet(url, VNFRecordDependency.class);
  }

  /**
   * Deletes a specific VNFRecordDependency from a NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfrDep the ID of the VNFRecordDependency to delete
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Delete the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id"
  )
  public void deleteVNFDependency(final String idNsr, final String idVnfrDep) throws SDKException {
    String url = idNsr + "/vnfdependencies" + "/" + idVnfrDep;
    requestDelete(url);
  }

  /** TODO (check the orchestrator) */
  /**
   * Add a new VNFRecordDependency to a NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param vnfRecordDependency the new VNFRecordDependency
   * @return the created VNFRecordDependency
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Create the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id"
  )
  public VNFRecordDependency postVNFDependency(
      final String idNsr, final VNFRecordDependency vnfRecordDependency) throws SDKException {
    String url = idNsr + "/vnfdependencies" + "/";
    return (VNFRecordDependency) requestPost(url, vnfRecordDependency);
  }

  /**
   * Update a VNFRecordDependency.
   *
   * @param idNsr the ID of the NetworkServiceRecord containing the VNFRecordDependency
   * @param idVnfrDep the ID of the VNFRecordDependency to update
   * @param vnfRecordDependency the updated version of the VNFRecordDependency
   * @return the updated VNFRecordDependency
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Update the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id"
  )
  public VNFRecordDependency updateVNFDependency(
      final String idNsr, final String idVnfrDep, final VNFRecordDependency vnfRecordDependency)
      throws SDKException {
    String url = idNsr + "/vnfdependencies" + "/" + idVnfrDep;
    return (VNFRecordDependency) requestPut(url, vnfRecordDependency);
  }

  /**
   * Start a specific VNFCInstance.
   *
   * @param nsrId the ID of the NetworkServiceRecord containing the VNFCInstance
   * @param vnfrId the ID of the VirtualNetworkFunctionRecord containing the VNFCInstance
   * @param vduId the ID of the VirtualDeploymentUnit containing the VNFCInstance
   * @param vnfcInstanceId the ID on the VNFCInstance that shall be started
   * @throws SDKException if the request fails
   */
  @Help(help = "Start the specified VNFC Instance")
  public void startVNFCInstance(
      final String nsrId, final String vnfrId, final String vduId, final String vnfcInstanceId)
      throws SDKException {
    String url =
        nsrId
            + "/vnfrecords/"
            + vnfrId
            + "/vdunits/"
            + vduId
            + "/vnfcinstances/"
            + vnfcInstanceId
            + "/start";
    requestPost(url);
  }

  /**
   * Stops a specific VNFCInstance.
   *
   * @param nsrId the ID of the NetworkServiceRecord containing the VNFCInstance
   * @param vnfrId the ID of the VirtualNetworkFunctionRecord containing the VNFCInstance
   * @param vduId the ID of the VirtualDeploymentUnit containing the VNFCInstance
   * @param vnfcInstanceId the ID on the VNFCInstance that shall be stopped
   * @throws SDKException if the request fails
   */
  @Help(help = "Stop the specified VNFC Instance")
  public void stopVNFCInstance(
      final String nsrId, final String vnfrId, final String vduId, final String vnfcInstanceId)
      throws SDKException {
    String url =
        nsrId
            + "/vnfrecords/"
            + vnfrId
            + "/vdunits/"
            + vduId
            + "/vnfcinstances/"
            + vnfcInstanceId
            + "/stop";
    requestPost(url);
  }

  /**
   * Returns a List of all the PhysicalNetworkFunctionRecords that are contained in a particular
   * NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @return a List of PhysicalNetworkFunctionRecords
   * @throws SDKException if the request fails
   */
  @Help(
    help = "Get all the PhysicalNetworkFunctionRecords of a specific NetworkServiceRecord with id"
  )
  public List<PhysicalNetworkFunctionRecord> getPhysicalNetworkFunctionRecords(final String idNsr)
      throws SDKException {
    String url = idNsr + "/pnfrecords";
    return Arrays.asList(
        (PhysicalNetworkFunctionRecord[]) requestGetAll(url, PhysicalNetworkFunctionRecord.class));
  }

  /**
   * Returns a specific PhysicalNetworkFunctionRecord.
   *
   * @param idNsr the ID of the NetworkFunctionRecord containing the PhysicalNetworkFunctionRecord
   * @param idPnfr the ID of the requested PhysicalNetworkFunctionRecord
   * @return the PhysicalNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "Get the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
  public PhysicalNetworkFunctionRecord getPhysicalNetworkFunctionRecord(
      final String idNsr, final String idPnfr) throws SDKException {
    String url = idNsr + "/pnfrecords" + "/" + idPnfr;
    return (PhysicalNetworkFunctionRecord)
        requestGetWithStatusAccepted(url, PhysicalNetworkFunctionRecord.class);
  }

  /**
   * Deletes a specific PhysicalNetworkFunctionRecord.
   *
   * @param idNsr the ID of the NetworkFunctionRecord containing the PhysicalNetworkFunctionRecord
   * @param idPnfr the ID of the PhysicalNetworkFunctionRecord to delete
   * @throws SDKException if the request fails
   */
  @Help(
    help = "Delete the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id"
  )
  public void deletePhysicalNetworkFunctionRecord(final String idNsr, final String idPnfr)
      throws SDKException {
    String url = idNsr + "/pnfrecords" + "/" + idPnfr;
    requestDelete(url);
  }

  /**
   * Create a new PhysicalnetworkFunctionRecord and add it ot a NetworkServiceRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param physicalNetworkFunctionRecord the new PhysicalNetworkFunctionRecord
   * @return the new PhysicalNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(
    help = "Create the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id"
  )
  public PhysicalNetworkFunctionRecord postPhysicalNetworkFunctionRecord(
      final String idNsr, final PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord)
      throws SDKException {
    String url = idNsr + "/pnfrecords" + "/";
    return (PhysicalNetworkFunctionRecord) requestPost(url, physicalNetworkFunctionRecord);
  }

  /**
   * Updates a specific PhysicalNetworkFunctionRecord.
   *
   * @param idNsr the ID of the NetworkServiceRecord containing the PhysicalNetworkFunctionRecord
   * @param idPnfr the ID of the PhysicalNetworkFunctionRecord to update
   * @param physicalNetworkFunctionRecord the updated version of the PhysicalNetworkFunctionRecord
   * @return the updated PhysicalNetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(
    help = "Update the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id"
  )
  public PhysicalNetworkFunctionRecord updatePNFD(
      final String idNsr,
      final String idPnfr,
      final PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord)
      throws SDKException {
    String url = idNsr + "/pnfrecords" + "/" + idPnfr;
    return (PhysicalNetworkFunctionRecord) requestPut(url, physicalNetworkFunctionRecord);
  }

  /**
   * Scales out/add a VNF to a running NSR.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfd the ID of the VNFD to add
   * @param vduVimInstances a HashMap assigning VimInstance names to VirtualDeploymentUnits
   * @param keys an ArrayList of Key names that shall be passed to the NetworkServiceRecord
   * @param configurations a HashMap assigning Configuration objects to VirtualNetworkServiceRecords
   * @param monitoringIp the IP of the monitoring system
   * @return the updated NetworkFunctionRecord
   * @throws SDKException if the request fails
   */
  @Help(help = "Scales out/add a VNF to a running NetworkServiceRecord with specific id")
  public NetworkServiceRecord scaleOut(
      final String idNsr,
      final String idVnfd,
      HashMap<String, ArrayList<String>> vduVimInstances,
      ArrayList<String> keys,
      HashMap<String, Configuration> configurations,
      String monitoringIp)
      throws SDKException {
    HashMap<String, Serializable> jsonBody = new HashMap<>();
    jsonBody.put("keys", keys);
    jsonBody.put("vduVimInstances", vduVimInstances);
    jsonBody.put("configurations", configurations);
    jsonBody.put("monitoringIp", monitoringIp);
    String url = idNsr + "/vnfd" + "/" + idVnfd;
    return (NetworkServiceRecord) requestPut(url, jsonBody);
  }

  /**
   * Restarts a VNFR in a running NSR.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfr the ID of the VNFR to restart
   * @param imageName rebuilding the VNFR with a different image if defined
   * @throws SDKException if the request fails
   */
  @Help(help = "Scales out/add a VNF to a running NetworkServiceRecord with specific id")
  public void restartVnfr(final String idNsr, final String idVnfr, String imageName)
      throws SDKException {
    HashMap<String, Serializable> jsonBody = new HashMap<>();
    jsonBody.put("imageName", imageName);
    String url = idNsr + "/vnfrecords" + "/" + idVnfr + "/restart";
    requestPost(url, jsonBody);
  }

  /**
   * Upgrades a VNFR of a defined VNFD in a running NSR.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfr the ID of the VNFR to be upgraded
   * @param idVnfd the VNFD ID to which the VNFR shall be upgraded
   * @throws SDKException if the request fails
   */
  @Help(help = "Upgrades a VNFR to a defined VNFD in a running NSR with specific id")
  public void upgradeVnfr(final String idNsr, final String idVnfr, final String idVnfd)
      throws SDKException {
    HashMap<String, Serializable> jsonBody = new HashMap<>();
    jsonBody.put("vnfdId", idVnfd);
    String url = idNsr + "/vnfrecords" + "/" + idVnfr + "/upgrade";
    requestPost(url, jsonBody);
  }

  /**
   * Updates a VNFR of a defined VNFD in a running NSR.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfr the ID of the VNFR to be upgraded
   * @throws SDKException if the request fails
   */
  @Help(help = "Updates a VNFR to a defined VNFD in a running NSR with specific id")
  public void updateVnfr(final String idNsr, final String idVnfr) throws SDKException {
    String url = idNsr + "/vnfrecords" + "/" + idVnfr + "/update";
    requestPost(url);
  }

  /**
   * Executes a script at runtime for a VNFR of a defined VNFD in a running NSR.
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @param idVnfr the ID of the VNFR to be upgraded
   * @param script the script to execute
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Executes a script at runtime for a VNFR of a defined VNFD in a running NSR with specific id"
  )
  public void executeScript(final String idNsr, final String idVnfr, String script)
      throws SDKException {
    String url = idNsr + "/vnfrecords" + "/" + idVnfr + "/execute-script";
    requestPost(url, script);
  }

  /**
   * Resumes a NSR that failed while executing a script in a VNFR. The id in the URL specifies the
   * Network Service Record that will be resumed..
   *
   * @param idNsr the ID of the NetworkServiceRecord
   * @throws SDKException if the request fails
   */
  @Help(
    help =
        "Resumes a NSR that failed while executing a script in a VNFR. The id in the URL specifies the Network Service Record that will be resumed."
  )
  public void resume(final String idNsr) throws SDKException {
    String url = idNsr + "/resume";
    requestPost(url);
  }
}
