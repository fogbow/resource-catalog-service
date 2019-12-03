package cloud.fogbow.rcs.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;
import cloud.fogbow.rcs.core.service.CatalogService;

public class ApplicationFacadeTest extends BaseUnitTests {

    private ApplicationFacade facade;
    private CatalogService catalogService;
    
    @Before
    public void setUp() {
        this.catalogService = Mockito.mock(CatalogService.class);
        this.facade = Mockito.spy(ApplicationFacade.getInstance());
        this.facade.setCatalogService(this.catalogService);
    }
    
    // test case: When calling the getMembers method, it must verify that the
    // requestMembers method in CatalogService has been called.
    @Test
    public void testGetMembers() throws Exception {
        // exercise
        this.facade.getMembers();

        // verify
        Mockito.verify(this.catalogService, Mockito.times(TestUtils.RUN_ONCE)).requestMembers();
    }
    
    // test case: When calling the getServicesFrom method, it must verify that the
    // getMemberServices method in CatalogService has been called.
    @Test
    public void testGetServicesFromMember() throws FogbowException {
        // exercise
        List<Service> services = new ArrayList<>();
        services.add(this.testUtils.createLocalService());

        String member = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        Mockito.when(this.catalogService.getMemberServices(Mockito.eq(member))).thenReturn(services);

        // exercise
        this.facade.getServicesFrom(member);

        // verify
        Mockito.verify(this.catalogService, Mockito.times(TestUtils.RUN_ONCE)).getMemberServices(Mockito.eq(member));
    }
    
    // test case: When calling the getService method, it must verify that the
    // getServiceCatalog method in CatalogService has been called.
    @Test
    public void testGetService() throws FogbowException, IOException {
        String member = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        String service = ServiceType.MS.getName();

        // set up
        CatalogFactory factory = Mockito.mock(CatalogFactory.class);
        CatalogService service = Mockito.mock(CatalogService.class);
        String fakeSpec = this.testUtils.getCatalogSpec();
        String fakeMember = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        String fakeService = TestUtils.SERVICES[TestUtils.LOCAL_MEMBER_INDEX];

        Mockito.when(factory.makeCatalogService()).thenReturn(service);
        Mockito.when(service.getServiceCatalog(Mockito.anyString(), Mockito.anyString())).thenReturn(fakeSpec);

        this.facade.setCatalogFactory(factory);

        // exercise
        this.facade.getService(fakeMember, fakeService);

        // verify
        Mockito.verify(factory, Mockito.times(TestUtils.RUN_ONCE)).makeCatalogService();
        Mockito.verify(service, Mockito.times(TestUtils.RUN_ONCE)).getServiceCatalog(Mockito.eq(fakeMember), Mockito.eq(fakeService));
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE)).getService(Mockito.eq(fakeMember), Mockito.eq(fakeService));
    }
    
    // test case: When calling the getVersionNumber method, it must verify that the
    // expected value has been returned.
    @Test
    public void testGetVersionNumber() throws FogbowException {
        // set up
        String expected = String.format(ApplicationFacade.BUILD_NUMBER_FORMAT, SystemConstants.API_VERSION_NUMBER,
                ConfigurationPropertyDefaults.BUILD_NUMBER);

        // exercise
        String build = this.facade.getVersionNumber();

        // verify
        Assert.assertEquals(expected, build);
    }

}
