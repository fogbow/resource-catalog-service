package cloud.fogbow.rcs.core.service;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.annotations.VisibleForTesting;

import cloud.fogbow.common.constants.HttpMethod;
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

public class CatalogService {

    private static final String URL_PREFFIX_ADDRESS = "https://";
    private static final String DOC_ENDPOINT = "/doc";
    private static final int FIRST_POSITION = 0;
    
    private Properties properties;

    public CatalogService() {
        String confFilePath = HomeDir.getPath() + SystemConstants.RCS_CONF_FILE;
        this.properties = PropertiesUtil.readProperties(confFilePath);
    }

    public List<String> requestMembers() throws UnexpectedException {
        String endpoint = getServiceEndpoint();
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpRequestClient.doGenericRequest(HttpMethod.GET, endpoint, headers, body);
            MembershipServiceResponse response = MembershipServiceResponse.fromJson(httpResponse.getContent());
            return listProviderMembers(response);
        } catch (Exception e) {
            String message = String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage());
            throw new UnexpectedException(message, e);
        }
    }
    
    public List<Service> getMemberServices(String member) throws UnexpectedException {
        List<Service> services = new ArrayList<>();
        if (member.equals(getLocalMember())) {
            services.add(getLocalCatalog());
        }
        return services;
    }
    
    @VisibleForTesting
    Service getLocalCatalog() throws UnexpectedException {
        String providerURL = URL_PREFFIX_ADDRESS + getLocalMember();
        try {
            InetAddress address = InetAddress.getByName(new URL(providerURL).getHost());
            return new Service(ServiceType.LOCAL, URL_PREFFIX_ADDRESS + address.getCanonicalHostName() + DOC_ENDPOINT);
        } catch (Exception e) {
            String message = String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage());
            throw new UnexpectedException(message, e);
        }
    }
    
    @VisibleForTesting
    List<String> listProviderMembers(MembershipServiceResponse response) {
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
    String getServiceEndpoint() {
        return this.properties.getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_URL_KEY);
    }

    @VisibleForTesting
    String getLocalMember() {
        return this.properties.getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
    }

}
