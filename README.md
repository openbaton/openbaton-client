  <img src="https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/openBaton.png" width="250"/>
  
  Copyright © 2015-2016 [Open Baton](http://openbaton.org). 
  Licensed under [Apache v2 License](http://www.apache.org/licenses/LICENSE-2.0).

# Open Baton Client

Open Baton Client contains the Open Baton SDK and the Open Baton command-line interface. The SDK provides methods to access the RESTful NFVO API. The command-line interface enables you to use the OpenBaton SDK via the command-line. 

## Project Structure
The project contains the two modules *cli* and *sdk*. 

## How to install the Open Baton Client

Git clone this project. Navigate into the root directory of the project and execute *./gradlew build*.

## How to use the command-line interface

  
## Documentation
 * [Command-line interface][cli-documentation]
 * [SDK][sdk-documentation]

### Set the required environment variables
Navigate into the project's root directory and execute *source nfvo.properties*. A dialog appears and will ask you for some properties. 

### Openbaton command-line usage
    
    run a command: openbaton.sh command-name [arg-1] [arg-2] [arg-3]
    show the configuration: openbaton.sh -c
    activate debug mode: openbaton.sh -d command-name [arg-1] [arg-2] [arg-3]
    print help: openbaton.sh -h
    print help for a command: openbaton.sh command-name help
    list the available commands: openbaton.sh -l
    

### For help on a specific openbaton command, enter:
```sh
 $ openbaton.sh COMMAND help
```

### **Vim Instance Subcommands**
* **create**
  * Create the object of type Vim Instance
  ```sh
   $ openbaton.sh VimInstance-create file.json
  ```

* **delete**
  * Delete the object of type Vim Instance passing the id
  ```sh
   $ openbaton.sh VimInstance-delete id-vim-instance
  ```

* **update**
  * Update the object of type Vim nstance passing the new object and the id of the old object
  ```sh
   $ openbaton.sh VimInstance-update file.json id-vim-instance
  ```

* **findAll**
  * Find all the objects of type Vim Instance
  ```sh
   $ openbaton.sh VimInstance-findAll
  ```

* **findById**
  * Find the object of type Vim Instance through the id
  ```sh
   $ openbaton.sh VimInstance-findById id-vim-instance
  ```

### **Network Service Descriptor Subcommands**
* **create**
  * Create the object of type Network Service Descriptor
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-create file.json
  ```

* **delete**
  * Delete the object of type Network Service Descriptor passing the id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-delete id-network-service-descriptor
  ```

* **findAll**
  * Find all the objects of type Network Service Descriptor
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-findAll
  ```

* **findById**
  * Find the object of type Network Service Descriptor through the id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-findById id-network-service-descriptor
  ```

* **createVNFDependency**                                   
  * Create the Virtual Network Function Descriptor dependency for a Network Service Descriptor with a specific id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-createVNFDependency id-network-service-descriptor file.json
  ```
  The file should look similar to this:
  ```json
  {"parameters":["theParameter"], "version":1, 
  "source":{"id":"950811b6-ebb6-4a17-bf4e-ab61974acbc8"}, 
  "target": {"id":"9873ad54-2963-424d-ab5d-39403a5dd544"}
  }
  ```
  The ids belong to the particular VirtualNettworkFunctionDescriptor.

* **deleteVNFDependency**                                   
  * Delete the Virtual Network Function Descriptor dependency of a Network Service Descriptor with specific id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-deleteVNFDependency id-network-service-descriptor id-vnfdependency
  ```

* **getVNFDependencies**                                  
  * Get all the Virtual Network Function Descriptor Dependency of a Network Service Descriptor with specific id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-getVNFDependencies id-network-service-descriptor
  ```

* **getVNFDependency**                                      
  * Get the VirtualNetwork Function Descriptor dependency with specific id of a Network Service Descriptor with specific id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-getVNFDependency id-network-service-descriptor id-vnfdependency
  ```
  
* **getVirtualNetworkFunctionDescriptors**
  * Find all the objects of type VirtualNetworkFunctionDescriptor
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-getVirtualNetworkFunctionDescriptors id-network-service-descriptor
  ```
* **getVirtualNetworkFunctionDescriptor**
  * Find the object of type VirtualNetworkFunctionDescriptor through the id
  ```sh
   $ openbaton.sh NetworkServiceDescriptor-getVirtualNetworkFunctionDescriptor id-network-service-descriptor id-vnfd
  ```
      
### **Virtual Network Function Descriptor Subcommands**
* **create**
  * Create the object of type Virtual Network Function Descriptor
  ```sh
   $ openbaton.sh VirtualNetworkFunctionDescriptor-create file.json
  ```

* **delete**
  * Delete the object of type Virtual Network Function Descriptor passing the id
  ```sh
   $ openbaton.sh VirtualNetworkFunctionDescriptor-delete id-virtual-network-function-descriptor
  ```

* **findAll**
  * Find all the objects of type Virtual Network Function Descriptor
  ```sh
   $ openbaton.sh VirtualNetworkFunctionDescriptor-findAll
  ```

* **findById**
  * Find the object of type Virtual Network Function Descriptor through the id
  ```sh
   $ openbaton.sh VirtualNetworkFunctionDescriptor-findById id-virtual-network-function-descriptor
  ```

### **Network Service Record Subcommands**
*  **create**
 
  * Create a Network Service Record from a Network Service Descriptor stored in the orchestrator
  ```sh
   $ openbaton.sh NetworkServiceRecord-create id-network-service-descriptor {} []
  ```

The two arguments after the NSD id can be used to specify the VIM on which a VDU should be deployed and the keypairs that shall be used to deploy the NSR.  
If you want to specify the VIM to use for a particular VDU you can pass a map like this:
  ```sh
   $ openbaton.sh NetworkServiceRecord-create id-network-service-descriptor {"vdu1Name":[vim1,vim2,vim3], "vdu2Name":[vim1]} []
  ```

In this case the VDU named vdu2Name would be deployed on vim1 and the VDU named vdu1Name randomly on one of the VIMs vim1, vim2 or vim3.  

The last command argument describes which keypairs shall be used to deploy the NSR. Here is an example: 
  ```sh
   $ openbaton.sh NetworkServiceRecord-create id-network-service-descriptor {"vdu1Name":[vim1,vim2,vim3], "vdu2Name":[vim1]} ["key1", "key2", "key3"]
  ```

Of course you do not have to specify VIMs and keys. If you do not want to specify them just pass empty braces. 

* **delete**
  * Delete the object of type Network Service Record passing the id
  ```sh
   $ openbaton.sh NetworkServiceRecord-delete id-network-service-record
  ```

* **update**
  * Update the Network Service Record
  ```sh
   $ openbaton.sh NetworkServiceRecord-update file.json id-network-service-record 
  ```

* **findAll**
  * Find all the objects of type Network Service Record
  ```sh
   $ openbaton.sh NetworkServiceRecord-findAll 
  ```

* **findById**
  * Find the object of type Network Service Record through the id
  ```sh
   $ openbaton.sh NetworkServiceRecord-findById id-network-service-record
  ```

* **getVirtualNetworkFunctionRecords**                         
  * Get all the Virtual Network Function Records of Network Service Record with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-getVirtualNetworkFunctionRecords id-network-service-record
  ```
  
* **getVirtualNetworkFunctionRecord**                           
  * Get the Virtual Network Function Record with specific id of Network Service Record with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-getVirtualNetworkFunctionRecord id-network-service-record id-vnfr
  ```
    
* **deleteVirtualNetworkFunctionRecord**                        
  * Delete the Virtual Network Function Record of NetworkS ervice Record with specific id
  ```sh
     $ openbaton.sh NetworkServiceRecord-deleteVirtualNetworkFunctionRecord id-network-service-record id-vnfr
  ```
  
* **createVNFDependency**                                      
  * Create a VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-createVNFDependency id-network-service-record file.json
  ```
  
* **deleteVNFDependency**                                      
  * Delete the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-deleteVNFDependency id-network-service-record id-vnfdependency
  ```
  
* **getVNFDependencies**                                      
  * Get all the VirtualNetworkFunctionRecord Dependencies of a NetworkServiceRecord with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-getVNFDependencies id-network-service-record
  ```
  
* **getVNFDependency**                                      
  * Get the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id
  ```sh
   $ openbaton.sh NetworkServiceRecord-getVNFDependency id-network-service-record id-vnfdependency
  ```
  
*  **createVNFCInstance**                                      
  * Add a VNFCInstance to a VNF by performing a SCALE_OUT operation on the VNF
  ```sh
  $ openbaton.sh NetworkServiceRecord-createVNFCInstance id-network-service-record id-virtual-network-function-record file.json
  ```
  
*  **deleteVNFCInstance**
  
  * Perform a SCALE_IN operation on a Virtual Network Function by deleting a VNFCInstance from the Virtual Network Function Record
  ```sh
  $ openbaton.sh NetworkServiceRecord-deleteVNFCInstance id-network-service-record id-virtual-network-function-record
  ```
  
  
  ### **User Subcommands**
*  **create**
  
  * Create a new User
  ```sh
  $ openbaton.sh User-create file.json
  ```
  
*  **delete**
  
  * Delete a user passing his id
  ```
  $ openbaton.sh User-delete user-id
  ```
  
*  **update**
  
  * Update a User
  ```sh
  $ openbaton.sh User-update file.json user-id
  ```
  
*  **findAll**
  
  * Find all Users
  ```sh
  $ openbaton.sh User-findAll
  ```
  
*  **findByName**
  
  * Find a User by his username
  ```sh
  $ openbaton.sh User-findByName username
  ```
  
*  **changePassword**
  
  * Change the password of the current user
  ```sh
  $ openbaton.sh User-changePassword oldPassword newPassword
  ```
  
Remember to source nfvo.properties afterwards and set the new password if you want to continue working as this User. 


### **Project Subcommands**
*  **create**

  * Create a new Project
  ```sh
  $ openbaton.sh Project-create file.json
  ```

*  **delete**

  * Delete a Project passing its id
  ```sh
  $ openbaton.sh Project-delete id-project
  ```

*  **update**

  * Delete a Project passing its id
  ```sh
  $ openbaton.sh Project-delete id-project
  ```

*  **findAll**

  * Find all Projects
  ```sh
  $ openbaton.sh Project-findAll
  ```

*  **findById**

  * Find a Project by passing its id
  ```sh
  $ openbaton.sh Project-findById id-project
  ```
  
  
### **Key Subcommands**
*  **generateKey**
  
  * Generate a new Key in the NFVO
  ```sh
  $ openbaton.sh Key-generateKey keyname
  ```
  
  You will get back the private key.
  
*  **importKey**
  
  * Import a Key into the NFVO
  ```sh
  $ openbaton.sh Key-importKey keyname publicKey
  ```
  
*  **delete**
  
  * Delete a Key passing its id
  ```sh
  $ openbaton.sh Key-delete id-key
  ```
  
*  **findAll**
  
  * Find all Keys
  ```sh
  $ openbaton.sh Key-findAll
  ```
  
*  **findById**
  
  * Find a Key by passing its id
  ```sh
  $ openbaton.sh Key-findById id-key
  ```



### **Event Subcommands**
* **create**
  * Create the object of type Event
  ```sh
  $ openbaton.sh Event-create file.json
  ```
  
* **delete**
  * Delete the object of type Event passing the id
  ```sh
  $ openbaton.sh Event-delete id-event
  ```
  
* **findAll**
  * Find all the objects of type Event
  ```sh
  $ openbaton.sh Event-findAll
  ```
* **findById**
  * Find the object of type Event through the id
  ```sh
   $ openbaton.sh Event-findById id-event
  ```

### **Configuration Subcommands**
* **create**
  * Create the object of type Configuration
  ```sh
   $ openbaton.sh Configuration-create file.json
  ```
  
* **delete**
  * Delete the object of type Configuration passing the id
  ```sh
   $ openbaton.sh Configuration-delete id-configuration
  ```
  
* **findAll**
  * Find all the objects of type Configuration
  ```sh
   $ openbaton.sh Configuration-findAll
  ```

* **findById**
  * Find the object of type Configuration through the id
  ```sh
   $ openbaton.sh Configuration-findById id-configuration
  ```

### **Image Subcommands**
* **create**
  * Create the object of type Image
    ```sh
     $ openbaton.sh Image-create file.json
    ```

* **delete**
  * Delete the object of type Image passing the id
  ```sh
   $ openbaton.sh Image-delete id-image
  ```
  
* **findAll**
  * Find all the objects of type Image
  ```sh
   $ openbaton.sh Image-findAll
  ```

* **findById**
  * Find the object of type Image through the id
  ```sh
   $ openbaton.sh Image-findById id-image 
  ```

### **VirtualLink Subcommands**
* **create**
  * Create the object of type VirtualLink
  ```sh
   $ openbaton.sh VirtualLink-create file.json 
  ```
  
* **delete**
  * Delete the object of type VirtualLink passing the id
  ```sh
   $ openbaton.sh VirtualLink-delete id-virtual-link 
  ```
  
* **update**
  * Update the object of type VirtualLink passing the new object and the id of the old object
  ```sh
   $ openbaton.sh VirtualLink-update file.json id-virtual-link
  ```
  
* **findAll**
  * Find all the objects of type VirtualLink
  ```sh
   $ openbaton.sh VirtualLink-findAll
  ```
  
* **findById**
  * Find the object of type VirtualLink through the id
  ```sh
   $ openbaton.sh VirtualLink-findById id-virtual-link
  ```
    
### **VNFPackage Subcommands**
* **create**
  * Create a VNFPackage by uploading a tar file to the NFVO
  ```sh
   $ openbaton.sh VNFPackage-upload file.tar 
  ```
  
* **delete**
  * Delete the object of type VNFPackage passing the id
  ```sh
   $ openbaton.sh VNFPackage-delete id-vnfPackage 
  ```
  
* **update**
  * Update the object of type VNFPackage passing the new object and the id of the old object
  ```sh
   $ openbaton.sh VNFPackage-update file.json id-vnfPackage
  ```

* **findAll**
  * Find all the objects of type VNFPackage
  ```sh
   $ openbaton.sh VNFPackage-findAll
  ```

* **findById**
  * Find the object of type VNFPackage through the id
  ```sh
   $ openbaton.sh VNFPackage-findById id-vnfPackage
  ```

## Issue tracker

Issues and bug reports should be posted to the GitHub Issue Tracker of this project

# What is Open Baton?

OpenBaton is an open source project providing a comprehensive implementation of the ETSI Management and Orchestration (MANO) specification.

Open Baton is a ETSI NFV MANO compliant framework. Open Baton was part of the OpenSDNCore (www.opensdncore.org) project started almost three years ago by Fraunhofer FOKUS with the objective of providing a compliant implementation of the ETSI NFV specification. 

Open Baton is easily extensible. It integrates with OpenStack, and provides a plugin mechanism for supporting additional VIM types. It supports Network Service management either using a generic VNFM or interoperating with VNF-specific VNFM. It uses different mechanisms (REST or PUB/SUB) for interoperating with the VNFMs. It integrates with additional components for the runtime management of a Network Service. For instance, it provides autoscaling and fault management based on monitoring information coming from the the monitoring system available at the NFVI level.

## Source Code and documentation

The Source Code of the other Open Baton projects can be found [here][openbaton-github] and the documentation can be found [here][openbaton-doc] .

## News and Website

Check the [Open Baton Website][openbaton]
Follow us on Twitter @[openbaton][openbaton-twitter].

## Licensing and distribution
Copyright [2015-2016] Open Baton project

Licensed under the Apache License, Version 2.0 (the "License");

you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Support
The Open Baton project provides community support through the Open Baton Public Mailing List and through StackOverflow using the tags openbaton.

## Supported by
  <img src="https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/fokus.png" width="250"/><img src="https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/tu.png" width="150"/>

[fokus-logo]: https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/fokus.png
[cli-documentation]:http://openbaton.github.io/documentation/nfvo-how-to-use-cli/
[sdk-documentation]:http://openbaton.github.io/documentation/nfvo-sdk/
[openbaton]: http://openbaton.org
[openbaton-doc]: http://openbaton.org/documentation
[openbaton-github]: http://github.org/openbaton
[openbaton-logo]: https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/openBaton.png
[openbaton-mail]: mailto:users@openbaton.org
[openbaton-twitter]: https://twitter.com/openbaton
[tub-logo]: https://raw.githubusercontent.com/openbaton/openbaton.github.io/master/images/tu.png
