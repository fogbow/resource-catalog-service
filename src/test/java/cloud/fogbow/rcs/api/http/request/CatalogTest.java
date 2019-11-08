package cloud.fogbow.rcs.api.http.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cloud.fogbow.rcs.core.ApplicationFacade;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ApplicationFacade.class })
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(Catalog.class)
public class CatalogTest {

    private static final String BASE_URL = "/";
    private static final String MEMBER_ONE = "member1";
    private static final String MEMBER_TWO = "member2";
    private static final String MEMBER_THREE = "member3";
    private static final String FAKE_LOCAL_MEMBER_URL = "https://member1.org/doc";
    private static final String FAKE_MEMBER_ID_ENDPOINT = BASE_URL.concat(MEMBER_ONE);
    
    @Autowired
    private MockMvc mockMvc;
    
    private ApplicationFacade facade;
    
    @Before
    public void setup() {
        this.facade = Mockito.mock(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }
    
    // test case: When executing a GET request for the "/rcs/members" endpoint, it
    // must invoke the getAllMembers method by returning the Status OK and the
    // content of MembersList class in JSON format.
    @Test
    public void testGetAllMembers() throws Exception {
        // set up
        String[] array = { MEMBER_ONE, MEMBER_TWO, MEMBER_THREE };
        List<String> members = Arrays.asList(array);
        Mockito.doReturn(members).when(this.facade).getMembers();
        
        String expected = getAllMembersResponseContent();

        // exercise
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL.concat(Catalog.ENDPOINT)))
                // verify
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }
    
    // test case: When executing a GET request to the "/rcs/members" endpoint,
    // passing a member ID, it must call the getAllServices method by returning 
    // the OK Status and the contents of the ServicesList class in JSON format.
    @Test
    public void testGetAllServices() throws Exception {
        // set up
        List<Service> services = new ArrayList<>();
        services.add(new Service(ServiceType.LOCAL, FAKE_LOCAL_MEMBER_URL));
        Mockito.doReturn(services).when(this.facade).getServicesFrom(Mockito.eq(MEMBER_ONE));

        String expected = getAllServicesResponseContent();

        // exercise
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(BASE_URL.concat(Catalog.ENDPOINT).concat(FAKE_MEMBER_ID_ENDPOINT)))
                // verify
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }
    
    private String getAllServicesResponseContent() {
        return "{\n"
               + "    \"services\": [\n"
               + "        {\n"
               + "            \"serviceType\": \"local\",\n"
               + "            \"location\": \"https://member1.org/doc\"\n"
               + "        }\n"
               + "    ]\n"
               + "}";
    }
    
    private String getAllMembersResponseContent() {
        return "{\n"
               + "    \"members\": [\n" 
               + "        \"member1\",\n"
               + "        \"member2\",\n"
               + "        \"member3\"\n"
               + "    ]\n"
               + "}";
    }

}
