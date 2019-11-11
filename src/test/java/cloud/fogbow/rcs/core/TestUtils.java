package cloud.fogbow.rcs.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class TestUtils {
    
    public static final String BASE_URL = "/";
    public static final String EMPTY_STRING = "";
    public static final String FAKE_LOCAL_MEMBER_URL = "https://member1.org/doc";
    public static final String MEMBER_ONE = "member1";
    public static final String MEMBER_TWO = "member2";
    public static final String MEMBER_THREE = "member3";
    public static final String URL_MEMBER_ID_ENDPOINT = BASE_URL.concat(MEMBER_ONE);
    
    private static final String RESOURCES_API_HTTP_RESPONSE_PATH = "cloud/fogbow/rcs/api/http/response/";
    private static final String MEMBERS_LIST_JSON_FILE_NAME = "membersList.json";
    private static final String SERVICES_LIST_JSON_FILE_NAME = "localServicesList.json";

    public static ApplicationFacade mockApplicationFacade() {
        ApplicationFacade facade = Mockito.mock(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(facade);
        return facade;
    }
    
    public static String getLocalServicesListResponseJson(String type, String url) throws IOException {
        String pathFile = getPathFile(RESOURCES_API_HTTP_RESPONSE_PATH.concat(SERVICES_LIST_JSON_FILE_NAME));
        String rawJson = readFileAsString(pathFile);
        return String.format(rawJson, type, url);
    }
    
    public static String getMembersListResponseJson(String[] members) throws IOException {
        String pathFile = getPathFile(RESOURCES_API_HTTP_RESPONSE_PATH.concat(MEMBERS_LIST_JSON_FILE_NAME));
        String rawJson = readFileAsString(pathFile);
        return String.format(rawJson, members);
    }
    
    private static String getPathFile(String path) {
        String rootPath = Thread.currentThread().getContextClassLoader()
                .getResource(EMPTY_STRING)
                .getPath();
        
        return rootPath.concat(path);
    }
    
    private static String readFileAsString(final String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
