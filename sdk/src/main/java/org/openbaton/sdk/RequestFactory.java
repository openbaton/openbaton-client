package org.openbaton.sdk;

import org.openbaton.sdk.api.rest.*;
import org.openbaton.sdk.api.util.PropertyReader;
import org.openbaton.sdk.api.util.AbstractRestAgent;

public class RequestFactory {

  private static final String SDK_PROPERTIES_FILE = "sdk.api.properties";
  private static final PropertyReader propertyReader = new PropertyReader(SDK_PROPERTIES_FILE);
  private static RequestFactory instance;

  // create the requester here, maybe shift this to a manager
  private static ConfigurationRestRequest configurationRequest = null;
  private static ImageRestAgent imageRequest = null;
  private static NetworkServiceDescriptorRestAgent networkServiceDescriptorRequest = null;
  private static NetworkServiceRecordRestAgent networkServiceRecordRequest = null;
  private static VimInstanceRestAgent vimInstanceRequest = null;
  private static VirtualLinkRestAgent virtualLinkRequest = null;
  private static VirtualNetworkFunctionDescriptorRestAgent virtualNetworkFunctionDescriptorRequest =
      null;
  private static VNFFGRestAgent vNFFGRequest = null;
  private static EventAgent eventAgent = null;
  private static VNFPackageAgent vnfPackageAgent = null;
  private static ProjectAgent projectAgent = null;
  private static UserAgent userAgent = null;

  private static String username;
  private static String password;
  private static String projectId;
  private static boolean sslEnabled;
  private final String nfvoPort;
  private final String nfvoIp;
  private final String version;

  static public void setProjectId(String projectId) {
    RequestFactory.projectId = projectId;
    if (configurationRequest != null) configurationRequest.setProjectId(projectId);
    if (imageRequest != null) imageRequest.setProjectId(projectId);
    if (networkServiceDescriptorRequest != null)
      networkServiceDescriptorRequest.setProjectId(projectId);
    if (networkServiceRecordRequest != null) networkServiceRecordRequest.setProjectId(projectId);
    if (vimInstanceRequest != null) vimInstanceRequest.setProjectId(projectId);
    if (vnfPackageAgent != null) vnfPackageAgent.setProjectId(projectId);
    if (virtualNetworkFunctionDescriptorRequest != null)
      virtualNetworkFunctionDescriptorRequest.setProjectId(projectId);
    if (projectAgent != null) projectAgent.setProjectId(projectId);
    if (userAgent != null) userAgent.setProjectId(projectId);
    if (eventAgent != null) eventAgent.setProjectId(projectId);
    if (virtualLinkRequest != null) virtualLinkRequest.setProjectId(projectId);
    if (vNFFGRequest != null) vNFFGRequest.setProjectId(projectId);
  }

  public static String getProjectId() {
    return projectId;
  }

  private RequestFactory(
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
    this.nfvoPort = nfvoPort;
    this.nfvoIp = nfvoIp;
    this.version = version;
  }

  public static RequestFactory getInstance(
      String username,
      String password,
      String projectId,
      boolean sslEnabled,
      String nfvoIp,
      String nfvoPort,
      String version) {
    if (instance == null) {
      return new RequestFactory(
          username, password, projectId, sslEnabled, nfvoIp, nfvoPort, version);
    } else return instance;
  }

  public ConfigurationRestRequest getConfigurationAgent() {
    if (configurationRequest == null) {
      configurationRequest =
          new ConfigurationRestRequest(
              username,
              password,
              projectId,
              sslEnabled,
              propertyReader.getRestConfigurationUrl(),
              nfvoIp,
              nfvoPort,
              version);
    }
    return configurationRequest;
  }

  public ImageRestAgent getImageAgent() {
    if (imageRequest == null) {
      imageRequest =
          new ImageRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestImageUrl(),
              version);
    }
    return imageRequest;
  }

  public NetworkServiceDescriptorRestAgent getNetworkServiceDescriptorAgent() {
    if (networkServiceDescriptorRequest == null) {
      networkServiceDescriptorRequest =
          new NetworkServiceDescriptorRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestNetworkServiceDescriptorUrl(),
              version);
    }
    return networkServiceDescriptorRequest;
  }

  public NetworkServiceRecordRestAgent getNetworkServiceRecordAgent() {
    if (networkServiceRecordRequest == null) {
      networkServiceRecordRequest =
          new NetworkServiceRecordRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestNetworkServiceRecordUrl(),
              version);
    }
    return networkServiceRecordRequest;
  }

  public VimInstanceRestAgent getVimInstanceAgent() {
    if (vimInstanceRequest == null) {
      vimInstanceRequest =
          new VimInstanceRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestVimInstanceUrl(),
              version);
    }
    return vimInstanceRequest;
  }

  public VirtualLinkRestAgent getVirtualLinkAgent() {
    if (virtualLinkRequest == null) {
      virtualLinkRequest =
          new VirtualLinkRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestVirtualLinkUrl(),
              version);
    }
    return virtualLinkRequest;
  }

  public VirtualNetworkFunctionDescriptorRestAgent getVirtualNetworkFunctionDescriptorAgent() {
    if (virtualNetworkFunctionDescriptorRequest == null) {
      virtualNetworkFunctionDescriptorRequest =
          new VirtualNetworkFunctionDescriptorRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestVirtualNetworkFunctionDescriptorUrl(),
              version);
    }
    return virtualNetworkFunctionDescriptorRequest;
  }

  public VNFFGRestAgent getVNFForwardingGraphAgent() {
    if (vNFFGRequest == null) {
      vNFFGRequest =
          new VNFFGRestAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getRestVNFFGUrl(),
              version);
    }
    return vNFFGRequest;
  }

  public EventAgent getEventAgent() {
    if (eventAgent == null) {
      eventAgent =
          new EventAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getEventUrl(),
              version);
    }
    return eventAgent;
  }

  public VNFPackageAgent getVNFPackageAgent() {
    if (vnfPackageAgent == null) {
      vnfPackageAgent =
          new VNFPackageAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getVNFPackageUrl(),
              version);
    }
    return vnfPackageAgent;
  }

  public ProjectAgent getProjectAgent() {
    if (projectAgent == null) {
      projectAgent =
          new ProjectAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getProjectUrl(),
              version);
    }
    return projectAgent;
  }

  public UserAgent getUserAgent() {
    if (userAgent == null) {
      userAgent =
          new UserAgent(
              username,
              password,
              projectId,
              sslEnabled,
              nfvoIp,
              nfvoPort,
              propertyReader.getUserUrl(),
              version);
    }
    return userAgent;
  }

  public AbstractRestAgent getAbstractAgent(Class clazz, String path) {
    return new AbstractRestAgent(
        username, password, projectId, sslEnabled, nfvoIp, nfvoPort, path, version, clazz);
  }
}
