package cloud.fogbow.rcs.core.service;

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
import cloud.fogbow.rcs.core.models.ProviderMember;

public class CatalogService {
    public static final String MEMBERSHIP_SERVICE_ENDPOINT = "/ms/members";
    public static final String PORT_SEPARATOR = ":";

    private Properties properties;

    public CatalogService() {
        String confFilePath = HomeDir.getPath() + SystemConstants.RCS_CONF_FILE;
        this.properties = PropertiesUtil.readProperties(confFilePath);
    }

    public List<ProviderMember> requestMembers() throws UnexpectedException {
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

    @VisibleForTesting
    List<ProviderMember> listProviderMembers(MembershipServiceResponse response) {
        List<ProviderMember> members = new ArrayList<ProviderMember>();
        String localMember = getLocalMember();
        for (String member : response.getMembers()) {
            if (member.equals(localMember)) {
                members.add(new ProviderMember(member, true));
            } else {
                members.add(new ProviderMember(member, false));
            }
        }
        return members;
    }

    @VisibleForTesting
    String getServiceEndpoint() {
        String msUrl = this.properties.getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_URL_KEY);
        String msPort = this.properties.getProperty(ConfigurationPropertyKeys.MEMBERSHIP_SERVICE_PORT_KEY);

        return msUrl + PORT_SEPARATOR + msPort + MEMBERSHIP_SERVICE_ENDPOINT;
    }

    @VisibleForTesting
    String getLocalMember() {
        return this.properties.getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
    }

}
