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

import java.io.FileNotFoundException;
import java.util.HashSet;
import org.junit.Ignore;
import org.junit.Test;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.PhysicalNetworkFunctionRecord;
import org.openbaton.catalogue.mano.record.VNFRecordDependency;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.viminstances.BaseVimInstance;
import org.openbaton.catalogue.nfvo.viminstances.OpenstackVimInstance;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.NfvoRequestorBuilder;
import org.openbaton.sdk.api.exception.SDKException;

public class SdkTest {

  @Test
  @Ignore
  public void createTest() throws FileNotFoundException, SDKException {

    /*
    This is how you create a NFVO requestor
     */
    NFVORequestor requestor =
        NfvoRequestorBuilder.create()
            .nfvoIp("localhost")
            .nfvoPort(8080)
            .username("admin")
            .password("openbaton")
            .projectName("default")
            .sslEnabled(false)
            .version("1")
            .build();

    /*
    The default values are the same so you can do something like this
     */
    requestor = NfvoRequestorBuilder.create().build();

    /*
    Or change only one field
     */
    requestor = NfvoRequestorBuilder.create().nfvoPort(8080).build();

    /*
    Use it the old way
     */

    requestor.getProjectAgent().findAll().forEach(System.out::println);
    requestor.getVimInstanceAgent().findAll().forEach(System.out::println);
    requestor
        .getVNFPackageAgent()
        .create("/Users/lorenzo/Projects/Work/OpenBaton/packages/iperf/server.tar");
    //
    //    /** VimInsance */
    //    vimInstance = createVimInstance();
    //
    //    //    System.out.println(gson.toJson(vimInstance));
    //    try {
    //      vimInstance = requestor.getVimInstanceAgent().create(vimInstance);
    //    } catch (SDKException e) {
    //      e.printStackTrace();
    //      System.err.println("Reason: " + e.getReason());
    //      System.exit(1);
    //    }
    //    log.debug("Result is: " + vimInstance);
    //
    //    /** Descriptors */
    //    NetworkServiceDescriptor networkServiceDescriptor =
    //        gson.fromJson(new FileReader(descriptorFileName), NetworkServiceDescriptor.class);
    //    log.debug("Sending: " + networkServiceDescriptor.getName());
    //    NetworkServiceDescriptor res2 = null;
    //    try {
    //      res2 = requestor.getNetworkServiceDescriptorAgent().create(networkServiceDescriptor);
    //    } catch (SDKException e) {
    //      e.printStackTrace();
    //      System.err.println("Reason: " + e.getReason());
    //      System.exit(2);
    //    }
    //    System.out.println("DESCRIPTOR: " + res2);
    //
    //    try {
    //      requestor.getNetworkServiceDescriptorAgent().delete(res2.getId());
    //    } catch (SDKException e) {
    //      e.printStackTrace();
    //      System.err.println("Reason: " + e.getReason());
    //      System.exit(2);
    //    }
    //
    //    try {
    //      requestor.getVimInstanceAgent().delete(vimInstance.getId());
    //    } catch (SDKException e) {
    //      e.printStackTrace();
    //      System.err.println("Reason: " + e.getReason());
    //      System.exit(2);
    //    }
    /* Event */

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

  private BaseVimInstance createVimInstance() {
    OpenstackVimInstance vimInstance = new OpenstackVimInstance();
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

  private NetworkServiceDescriptor createNetworkServiceDescriptor(String vimInstanceName) {
    NetworkServiceDescriptor networkServiceDescriptor = new NetworkServiceDescriptor();
    VirtualNetworkFunctionDescriptor vnfd = new VirtualNetworkFunctionDescriptor();

    vnfd.setName("" + Math.random());
    vnfd.setType("dummy");

    VirtualDeploymentUnit vdu = new VirtualDeploymentUnit();
    vdu.setVirtual_memory_resource_element("1024");
    vdu.setVirtual_network_bandwidth_resource("1000000");
    BaseVimInstance instance = createVimInstance();
    instance.setId(null);
    instance.setName(vimInstanceName);

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
    vnfd.setVdu(new HashSet<>());
    vnfd.getVdu().add(vdu);

    networkServiceDescriptor.setVnfd(new HashSet<>());
    networkServiceDescriptor.getVnfd().add(vnfd);

    networkServiceDescriptor.setVendor("fokus");
    networkServiceDescriptor.setVersion("1");
    return networkServiceDescriptor;
  }

  public static void main(String[] args) throws SDKException, FileNotFoundException {
    new SdkTest().createTest();
  }
}
