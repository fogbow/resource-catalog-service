package cloud.fogbow.rcs.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;

public class TestUtils {
    
    private static final String EMPTY_STRING = "";
    private static final String FAKE_LOCAL_MEMBER_URL = "https://member1.org/doc";
    private static final String MEMBERS_LIST_JSON_FILE_NAME = "membersList.json";
    private static final String RESOURCES_API_HTTP_RESPONSE_PATH = "cloud/fogbow/rcs/api/http/response/";
    private static final String SERVICES_LIST_JSON_FILE_NAME = "localServicesList.json";
    private static final String VERSION_NUMBER_JSON_FILE_NAME = "versionNumber.json";
    
    public static final String[] MEMBERS = { "member1", "member2", "member3" }; 
    public static final String BASE_URL = "/";
    public static final int LOCAL_MEMBER_INDEX = 0;
    public static final int RUN_ONCE = 1;

    public ApplicationFacade mockApplicationFacade() {
        ApplicationFacade facade = Mockito.mock(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(facade);
        return facade;
    }
    
    public Service createLocalService() {
        return new Service(ServiceType.LOCAL, TestUtils.FAKE_LOCAL_MEMBER_URL);
    }
    
    public String getLocalServicesListResponseContent() throws IOException {
        String pathFile = getPathFile(RESOURCES_API_HTTP_RESPONSE_PATH.concat(SERVICES_LIST_JSON_FILE_NAME));
        String rawJson = readFileAsString(pathFile);
        return String.format(rawJson, ServiceType.LOCAL.getName(), FAKE_LOCAL_MEMBER_URL);
    }
    
    public String getMembersListResponseContent() throws IOException {
        String pathFile = getPathFile(RESOURCES_API_HTTP_RESPONSE_PATH.concat(MEMBERS_LIST_JSON_FILE_NAME));
        String rawJson = readFileAsString(pathFile);
        return String.format(rawJson, MEMBERS);
    }
    
    public String getVersionNumberResponseContent() throws IOException {
        String pathFile = getPathFile(RESOURCES_API_HTTP_RESPONSE_PATH.concat(VERSION_NUMBER_JSON_FILE_NAME));
        String rawJson = readFileAsString(pathFile);
        return String.format(rawJson, ApplicationFacade.getInstance().getVersionNumber());
    }
    
    private String getPathFile(String path) {
        String rootPath = Thread.currentThread().getContextClassLoader()
                .getResource(EMPTY_STRING)
                .getPath();
        
        return rootPath.concat(path);
    }
    
    private String readFileAsString(final String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
