package cloud.fogbow.rcs.api.http.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import cloud.fogbow.rcs.core.TestUtils;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ApplicationFacade.class })
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(Catalog.class)
public class CatalogTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private ApplicationFacade facade;
    
    // test case: When executing a GET request for the "/rcs/members" endpoint, it
    // must invoke the getAllMembers method by returning the Status OK and the
    // content of MembersList class in JSON format.
    @Test
    public void testGetAllMembers() throws Exception {
        // set up
        String[] array = { TestUtils.MEMBER_ONE, TestUtils.MEMBER_TWO, TestUtils.MEMBER_THREE };
        List<String> members = Arrays.asList(array);
        
        this.facade = TestUtils.mockApplicationFacade();
        Mockito.doReturn(members).when(this.facade).getMembers();
        
        String expected = TestUtils.getMembersListResponseJson(array);

        // exercise
        this.mockMvc.perform(MockMvcRequestBuilders.get(TestUtils.BASE_URL.concat(Catalog.ENDPOINT)))
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
        ServiceType type = ServiceType.LOCAL;
        String url = TestUtils.FAKE_LOCAL_MEMBER_URL;
        Service service = new Service(type, url);
        
        List<Service> services = new ArrayList<>();
        services.add(service);
        
        this.facade = TestUtils.mockApplicationFacade();
        Mockito.doReturn(services).when(this.facade).getServicesFrom(Mockito.eq(TestUtils.MEMBER_ONE));

        String expected = TestUtils.getLocalServicesListResponseJson(type.getName(), url);

        // exercise
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(TestUtils.BASE_URL.concat(Catalog.ENDPOINT).concat(TestUtils.URL_MEMBER_ID_ENDPOINT)))
                // verify
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

}
