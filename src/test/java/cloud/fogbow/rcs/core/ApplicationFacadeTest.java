package cloud.fogbow.rcs.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.core.models.Service;

@PrepareForTest({ ApplicationFacade.class })
public class ApplicationFacadeTest extends BaseUnitTests {

    private ApplicationFacade facade;
    
    @Before
    public void setUp() {
        this.facade = this.testUtils.mockApplicationFacade();
    }
    
    // test case: When invoking the getMembers method, it must verify that the call
    // was successful.
    @Test
    public void testGetMembers() throws FogbowException {
        // exercise
        this.facade.getMembers();
        
        // verify
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE)).getMembers();
    }
    
    // test case: When invoking the getServicesFrom method, it must verify that the
    // call was successful.
    @Test
    public void testGetServicesFromMember() throws FogbowException {
        // exercise
        List<Service> services = new ArrayList<>();
        services.add(this.testUtils.createLocalService());
        
        String member = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        Mockito.doReturn(services).when(this.facade).getServicesFrom(Mockito.eq(member));

        // exercise
        this.facade.getServicesFrom(member);

        // verify
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE)).getServicesFrom(Mockito.eq(member));
    }
    
    // test case: When invoking the getVersionNumber method, it must verify that the
    // call was successful.
    @Test
    public void testGetVersionNumber() throws FogbowException {
        // exercise
        this.facade.getVersionNumber();

        // verify
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE)).getVersionNumber();
    }
    
}
