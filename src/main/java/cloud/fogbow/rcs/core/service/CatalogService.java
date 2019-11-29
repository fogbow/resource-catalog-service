package cloud.fogbow.rcs.core.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.common.annotations.VisibleForTesting;

import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.MembershipServiceResponse;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;
import com.google.gson.Gson;

public class CatalogService {
    
    private static final Logger LOGGER = Logger.getLogger(CatalogService.class);
    
    public static final String MEMBERSHIP_SERVICE_ENDPOINT = "/ms/members";
    public static final String PORT_SEPARATOR = ":";
    public static final String URL_PREFFIX_ADDRESS = "https://";
    public static final String DOC_ENDPOINT = "/doc";
    
    private static final int FIRST_POSITION = 0;
    
    private Properties properties;

    public CatalogService() {
        String confFilePath = HomeDir.getPath().concat(SystemConstants.RCS_CONF_FILE);
        this.properties = PropertiesUtil.readProperties(confFilePath);
    }

    public List<String> requestMembers() throws FogbowException {
        String endpoint = getServiceEndpoint();
        HttpResponse content = doRequestMembers(endpoint);
        MembershipServiceResponse response = getResponseFrom(content); 
        return listMembersFrom(response);
    }
    
    public List<Service> getMemberServices(String member) throws FogbowException {
        List<Service> services = new ArrayList<>();
        if (member.equals(getLocalMember())) {
            services.add(getLocalCatalog());
        } else {
            LOGGER.info("Remote member service not yet implemented");
        }
        return services;
    }

    public String getServiceCatalog(String member, String service) {
        return null;
    }

    @VisibleForTesting
    Service getLocalCatalog() throws FogbowException {
        try {
            String localHost = getLocalHostProvider();
            return new Service(ServiceType.LOCAL, getHostAddress(localHost));
        } catch (Exception e) {
            String message = String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage());
            throw new UnexpectedException(message, e);
        }
    }
    
    @VisibleForTesting
    String getHostAddress(String host) throws Exception {
        return URL_PREFFIX_ADDRESS.concat(InetAddress.getByName(host).getCanonicalHostName()).concat(DOC_ENDPOINT);
    }
    
    @VisibleForTesting
    String getLocalHostProvider() throws Exception {
        return new URL(URL_PREFFIX_ADDRESS.concat(getLocalMember())).getHost();
    }
    
    @VisibleForTesting
    List<String> listMembersFrom(MembershipServiceResponse response) {
        List<String> members = new ArrayList<>();
        String localMember = getLocalMember();
        for (String member : response.getMembers()) {
            if (member.equals(localMember)) {
                // add local member always at beginning of the list
                members.add(FIRST_POSITION, member);
            } else {
                members.add(member);
            }
        }
        return members;
    }
    
    @VisibleForTesting
    MembershipServiceResponse getResponseFrom(HttpResponse httpResponse) {
        String content = httpResponse.getContent();
        return MembershipServiceResponse.fromJson(content);
    }
    
    @VisibleForTesting
    HttpResponse doRequestMembers(String endpoint) throws UnexpectedException {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        try {
            return HttpRequestClient.doGenericRequest(HttpMethod.GET, endpoint, headers, body);
        } catch (Exception e) {
            String message = String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage());
            throw new UnexpectedException(message, e);
        }
    }

    @VisibleForTesting
    String getServiceEndpoint() {
        String msUrl = this.properties.getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_URL_KEY);
        String msPort = this.properties.getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_PORT_KEY);

        return msUrl.concat(PORT_SEPARATOR).concat(msPort).concat(MEMBERSHIP_SERVICE_ENDPOINT);
    }

    @VisibleForTesting
    String getLocalMember() {
        return this.properties.getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
    }
}
