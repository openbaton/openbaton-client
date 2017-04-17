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

package org.openbaton.sdk.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Ignore;
import org.junit.Test;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.PhysicalNetworkFunctionRecord;
import org.openbaton.catalogue.mano.record.VNFRecordDependency;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.catalogue.security.Project;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Created by lto on 03/07/15.
 */
public class SdkTest {

  private Logger log = LoggerFactory.getLogger(this.getClass());
  private VimInstance vimInstance;
  private VimInstance res;
  private final static String descriptorFileName =
      "/opt/fokus-repo/descriptors/network_service_descriptors/NetworkServiceDescriptor-iperf-single.json";

  @Test
  @Ignore
  public void createTest() throws FileNotFoundException {
    GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    Gson gson = gsonBuilder.create();
    NFVORequestor requestor =
        new NFVORequestor("admin", "openbaton", "", false, "localhost", "8080", "1");

    String projectId = null;
    try {
      for (Project project : requestor.getProjectAgent().findAll()) {
        if (project.getName().equals("default")) {
          projectId = project.getId();
        }
      }
    } catch (SDKException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    requestor = new NFVORequestor("admin", "openbaton", projectId, false, "localhost", "8080", "1");

    /**
     * VimInsance
     */
    vimInstance = createVimInstance();

    //    System.out.println(gson.toJson(vimInstance));
    try {
      vimInstance = requestor.getVimInstanceAgent().create(vimInstance);
    } catch (SDKException e) {
      e.printStackTrace();
      System.err.println("Reason: " + e.getReason());
      System.exit(1);
    }
    log.debug("Result is: " + vimInstance);

    /**
     * Descriptors
     */
    NetworkServiceDescriptor networkServiceDescriptor =
        gson.fromJson(new FileReader(descriptorFileName), NetworkServiceDescriptor.class);
    log.debug("Sending: " + networkServiceDescriptor.getName());
    NetworkServiceDescriptor res2 = null;
    try {
      res2 = requestor.getNetworkServiceDescriptorAgent().create(networkServiceDescriptor);
    } catch (SDKException e) {
      e.printStackTrace();
      System.err.println("Reason: " + e.getReason());
      System.exit(2);
    }
    System.out.println("DESCRIPTOR: " + res2);

    try {
      requestor.getNetworkServiceDescriptorAgent().delete(res2.getId());
    } catch (SDKException e) {
      e.printStackTrace();
      System.err.println("Reason: " + e.getReason());
      System.exit(2);
    }

    try {
      requestor.getVimInstanceAgent().delete(vimInstance.getId());
    } catch (SDKException e) {
      e.printStackTrace();
      System.err.println("Reason: " + e.getReason());
      System.exit(2);
    }
    /**
     * Event
     */

    //    EventAgent eventAgent = requestor.getEventAgent();
    //    EventEndpoint eventEndpoint = new EventEndpoint();
    //    eventEndpoint.setType(EndpointType.REST);
    //    eventEndpoint.setEndpoint("http://localhost:8082/callme");
    //    eventEndpoint.setEvent(Action.INSTANTIATE_FINISH);
    //    eventEndpoint.setNetworkServiceId("idhere");
    //    EventEndpoint endpoint = eventAgent.create(eventEndpoint);
    //    eventAgent.delete(endpoint.getName());

    //SECURITY//

    //        Security  security = new Security();
    //        Security res = requestor.getNetworkServiceDescriptorAgent().createSecurity(res2.getId(),security);
    //        Security  security2 = new Security();
    //        Security res3 = requestor.getNetworkServiceDescriptorAgent().updateSecurity(res2.getId(),res.getId(),security2);
    //
    //        log.debug("Received: " + res3.toString());

    //PHYSICALNETWORKFUNCTIONDESCRIPTOR//

    //        PhysicalNetworkFunctionDescriptor origin = new  PhysicalNetworkFunctionDescriptor();
    //        PhysicalNetworkFunctionDescriptor source = requestor.getNetworkServiceDescriptorAgent().createPhysicalNetworkFunctionDescriptor(res2.getId(),origin);
    //
    //        PhysicalNetworkFunctionDescriptor response = requestor.getNetworkServiceDescriptorAgent().getPhysicalNetworkFunctionDescriptor(res2.getId(),source.getId());
    //        log.debug("Received: " + response.toString());

    //VNFDependency//

    //        VNFDependency  vnfDependency = new VNFDependency();
    //        VNFDependency res = requestor.getNetworkServiceDescriptorAgent().createVNFDependency(res2.getId(),vnfDependency);
    //        VNFDependency  vnfDependency2 = new VNFDependency();
    //        VNFDependency res3 = requestor.getNetworkServiceDescriptorAgent().updateVNFD(res2.getId(),res.getId(),vnfDependency2);
    //        log.debug("Received update: " + res3.toString());

    //VIRTUAL_NETWORK_FUNCTION_DESCRIPTOR

    //CREATE//
    //        VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor = createVNFDescriptor();
    //        VirtualNetworkFunctionDescriptor res = requestor.getNetworkServiceDescriptorAgent().createVNFD(res2.getId(), virtualNetworkFunctionDescriptor);
    //        log.debug("POST_VNFDescriptor: " + res.toString());

    //UPDATE//
    //        VirtualNetworkFunctionDescriptor res = requestor.getNetworkServiceDescriptorAgent().updateVNFD(res2.getId(), res2.getVnfd().iterator().next().getId(),response);
    //        log.debug("UPDATE_VNFDescriptor: " + res.toString());

    //NETWORK_SERVICE_RECORD

    //       NetworkServiceRecord networkServiceRecord = requestor.getNetworkServiceRecordAgent().create(res2.getId());
    //        log.debug("RECORD: "+networkServiceRecord);
    //
    //        VirtualNetworkFunctionRecord[] response = requestor.getNetworkServiceRecordAgent().getVirtualNetworkFunctionRecords(networkServiceRecord.getId());
    //
    //        for (VirtualNetworkFunctionRecord virtualNetworkFunctionRecord : response)
    //            log.debug("Received: " + virtualNetworkFunctionRecord.toString());

    //       requestor.getNetworkServiceRecordAgent().deleteVirtualNetworkFunctionRecord(networkServiceRecord.getId(), networkServiceRecord.getVnfr().iterator().next().getId());

    //VIRTUAL_NETWORK_FUNCTION_RECORD

    //-CREATE VNFR//
    //        VirtualNetworkFunctionRecord virtualNetworkFunctionRecord = new VirtualNetworkFunctionRecord();
    //        VirtualNetworkFunctionRecord response = requestor.getNetworkServiceRecordAgent().createVNFR(networkServiceRecord.getId(), virtualNetworkFunctionRecord);
    //        log.debug("Received: " + response.toString());
    //
    //        //-UPDATE VNFR//
    //        VirtualNetworkFunctionRecord update = new VirtualNetworkFunctionRecord();
    //        requestor.getNetworkServiceRecordAgent().updateVNFR(networkServiceRecord.getId(), networkServiceRecord.getVnfr().iterator().next().getId(), update);

    //VIRTUAL_NETWORK_RECORD_DEPENDENCY

    //requestor.getNetworkServiceRecordAgent().deleteVNFDependency(networkServiceRecord.getId(), networkServiceRecord.getVnf_dependency().iterator().next().getId());
    //        VNFRecordDependency vnfDependency = createVNFDependency();
    //
    //        VNFRecordDependency res = requestor.getNetworkServiceRecordAgent().postVNFDependency(networkServiceRecord.getId(), vnfDependency);
    //        log.debug("POST_VNFD: " + res.toString());

    //         VNFRecordDependency res = requestor.getNetworkServiceRecordAgent().updateVNFDependency(networkServiceRecord.getId(), networkServiceRecord.getVnf_dependency().iterator().next().getId(), vnfDependency);
    //         log.debug("UPDAPTE_VNFD: " + res.toString());

  }

  private VirtualNetworkFunctionDescriptor createVNFDescriptor() {

    VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor =
        new VirtualNetworkFunctionDescriptor();
    virtualNetworkFunctionDescriptor.setName("fokusland");
    virtualNetworkFunctionDescriptor.setVendor("fokus");

    return virtualNetworkFunctionDescriptor;
  }

  private PhysicalNetworkFunctionRecord createPNFR() {
    PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord =
        new PhysicalNetworkFunctionRecord();

    physicalNetworkFunctionRecord.setDescription("Warp 2000");
    physicalNetworkFunctionRecord.setVendor("fokus");

    return physicalNetworkFunctionRecord;
  }

  private VNFRecordDependency createVNFDependency() {
    VirtualNetworkFunctionRecord source = new VirtualNetworkFunctionRecord();
    source.setName("vnf-dummy-2");

    VirtualNetworkFunctionRecord target = new VirtualNetworkFunctionRecord();
    target.setName("vnf-dummy-1");

    return new VNFRecordDependency();
  }

  private VimInstance createVimInstance() {
    VimInstance vimInstance = new VimInstance();
    vimInstance.setName("vim-instance-test");
    vimInstance.setType("test");
    vimInstance.setAuthUrl("test.de");
    vimInstance.setPassword("password");
    vimInstance.setUsername("username");
    vimInstance.setActive(true);
    Location location = new Location();
    location.setName("location");
    location.setLatitude("0");
    location.setLongitude("0");
    vimInstance.setLocation(location);

    return vimInstance;
  }

  private NetworkServiceDescriptor createNetworkServiceDescriptor() {
    NetworkServiceDescriptor networkServiceDescriptor = new NetworkServiceDescriptor();
    VirtualNetworkFunctionDescriptor vnfd = new VirtualNetworkFunctionDescriptor();

    vnfd.setName("" + Math.random());
    vnfd.setType("dummy");

    VirtualDeploymentUnit vdu = new VirtualDeploymentUnit();
    vdu.setVirtual_memory_resource_element("1024");
    vdu.setVirtual_network_bandwidth_resource("1000000");
    VimInstance instance = new VimInstance();
    instance.setId(null);
    instance.setName(vimInstance.getName());
    //vdu.setVimInstance(instance);

    vdu.setVm_image(
        new HashSet<String>() {
          {
            add("image_name_1");
          }
        });
    vdu.setScale_in_out(3);
    vdu.setMonitoring_parameter(
        new HashSet<String>() {
          {
            add("cpu_utilization");
          }
        });
    vnfd.setVdu(new HashSet<VirtualDeploymentUnit>());
    vnfd.getVdu().add(vdu);

    networkServiceDescriptor.setVnfd(new HashSet<VirtualNetworkFunctionDescriptor>());
    networkServiceDescriptor.getVnfd().add(vnfd);

    networkServiceDescriptor.setVendor("fokus");
    networkServiceDescriptor.setVersion("1");
    return networkServiceDescriptor;
  }

  public static void main(String[] args) throws SDKException, FileNotFoundException {
    new SdkTest().createTest();
  }
}
