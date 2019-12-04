package cloud.fogbow.rcs.core.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cloud.fogbow.rcs.core.exceptions.NoSuchMemberException;
import cloud.fogbow.rcs.core.intercomponent.xmpp.requesters.RemoteGetAllServicesRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InvalidParameterException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.core.BaseUnitTests;
import cloud.fogbow.rcs.core.TestUtils;
import cloud.fogbow.rcs.core.models.MembershipServiceResponse;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.models.ServiceType;

@PrepareForTest({ HttpRequestClient.class, InetAddress.class, MembershipServiceResponse.class, RemoteGetAllServicesRequest.class })
public class CatalogServiceTest extends BaseUnitTests {

    private CatalogService service;

    private static final String RAS_LOCATION = "/rcs/service/member1-ms/ras";
    private static final String AS_LOCATION = "/rcs/service/member1-ms/as";
    
    @Before
    public void setUp() {
        this.service = Mockito.spy(new CatalogService());
    }
    
    // test case: When invoking the requestMembers method, it must verify that the
    // call was successful.
    @Test
    public void testRequestMembers() throws FogbowException {
        // set up
        String endpoint = TestUtils.MEMBERSHIP_SERVICE_ENDPOINT;
        Mockito.doReturn(endpoint).when(this.service).getServiceEndpoint();

        HttpResponse content = Mockito.mock(HttpResponse.class);
        Mockito.doReturn(content).when(this.service).doGetRequest(Mockito.eq(endpoint));

        MembershipServiceResponse response = Mockito.mock(MembershipServiceResponse.class);
        Mockito.doReturn(response).when(this.service).getResponseFrom(Mockito.eq(content));

        List<String> members = Arrays.asList(TestUtils.MEMBERS);
        Mockito.doReturn(members).when(this.service).listMembersFrom(response);

        // exercise
        this.service.requestMembers();

        // verify
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getServiceEndpoint();
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).doGetRequest(Mockito.eq(endpoint));
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getResponseFrom(Mockito.eq(content));
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).listMembersFrom(response);
    }
    
    // test case: When invoking the getMemberServices method from local member, it
    // must verify that the call was successful.
    @Test
    public void testGetMemberServicesLocal() throws FogbowException {
        // set up
        String member = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        Mockito.doReturn(member).when(this.service).getLocalMember();

        Service service = new Service(ServiceType.LOCAL, TestUtils.FAKE_LOCAL_MEMBER_URL);
        Mockito.doReturn(service).when(this.service).getLocalCatalog();

        // exercise
        this.service.getMemberServices(member);

        // verify
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getLocalMember();
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getLocalCatalog();
    }
    
    // test case: When invoking the getLocalCatalog method, it must verify that the
    // call was successful.
    @Test
    public void testGetLocalCatalog() throws Exception {
        // set up
        String localHost = CatalogService.URL_PREFFIX_ADDRESS.concat(TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX]);
        Mockito.doReturn(localHost).when(this.service).getLocalHostProvider();

        Mockito.doReturn(TestUtils.FAKE_LOCAL_MEMBER_URL).when(this.service).getHostAddress(Mockito.eq(localHost));

        // exercise
        this.service.getLocalCatalog();

        // verify
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getLocalHostProvider();
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getHostAddress(Mockito.eq(localHost));
    }
    
    // test case: When invoking the getLocalCatalog method, and a problem occurs, it
    // must verify that an UnexpectedException was thrown.
    @Test
    public void testGetLocalCatalogFail() throws Exception {
        // set up
        Exception exception = new UnknownHostException();
        Mockito.doThrow(exception).when(this.service).getLocalHostProvider();

        String expected = String.format(Messages.Exception.GENERIC_EXCEPTION, exception.getMessage());

        try {
            // exercise
            this.service.getLocalCatalog();
            Assert.fail();
        } catch (UnexpectedException e) {
            // verify
            Assert.assertEquals(expected, e.getMessage());
        }
    }
    
    // test case: When invoking the listMembersFrom method, it must verify that the
    // call was successful and returned the specific list with a local member in the
    // first position.
    @Test
    public void testListMembersFromResponse() throws Exception {
        // set up
        String localMember = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        Mockito.doReturn(localMember).when(this.service).getLocalMember();

        MembershipServiceResponse response = MembershipServiceResponse
                .fromJson(this.testUtils.getMembersListResponseContent());

        List<String> expected = Arrays.asList(TestUtils.MEMBERS);

        // exercise
        List<String> members = this.service.listMembersFrom(response);

        // verify
        Mockito.verify(this.service, Mockito.times(TestUtils.RUN_ONCE)).getLocalMember();

        Assert.assertEquals(expected, members);
        Assert.assertEquals(localMember, members.listIterator().next());
    }
    
    // test case: When invoking the getResponseFrom method, it must verify that the
    // call was successful.
    @Test
    public void testGetResponseFromContent() {
        // set up
        MembershipServiceResponse response = Mockito.mock(MembershipServiceResponse.class);
        PowerMockito.mockStatic(MembershipServiceResponse.class);
        BDDMockito.given(MembershipServiceResponse.fromJson(Mockito.anyString())).willReturn(response);
        HttpResponse content = Mockito.mock(HttpResponse.class);
        Mockito.when(content.getContent()).thenReturn(Mockito.anyString());
        
        // exercise
        this.service.getResponseFrom(content);

        // verify
        PowerMockito.verifyStatic(MembershipServiceResponse.class, Mockito.times(TestUtils.RUN_ONCE));
        MembershipServiceResponse.fromJson(Mockito.anyString());
    }
    
    // test case: When invoking the doRequestMembers method, it must verify that the
    // call was successful.
    @Test
    public void testDoRequestMembers() throws FogbowException {
        // set up
        String endpoint = TestUtils.MEMBERSHIP_SERVICE_ENDPOINT;

        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        PowerMockito.mockStatic(HttpRequestClient.class);
        BDDMockito.given(HttpRequestClient.doGenericRequest(Mockito.eq(HttpMethod.GET), Mockito.eq(endpoint),
                Mockito.any(), Mockito.any())).willReturn(httpResponse);

        // exercise
        this.service.doGetRequest(endpoint);

        // verify
        PowerMockito.verifyStatic(HttpRequestClient.class, Mockito.times(TestUtils.RUN_ONCE));
        HttpRequestClient.doGenericRequest(Mockito.eq(HttpMethod.GET), Mockito.eq(endpoint), Mockito.any(),
                Mockito.any());
    }

    // test case: When invoking the doRequestMembers method, and a problem occurs, it
    // must verify that an UnexpectedException was thrown.
    @Test
    public void testDoRequestMembersFail() throws FogbowException {
        // set up
        String endpoint = TestUtils.MEMBERSHIP_SERVICE_ENDPOINT;

        Exception exception = Mockito.mock(InvalidParameterException.class);
        PowerMockito.mockStatic(HttpRequestClient.class);
        BDDMockito.given(HttpRequestClient.doGenericRequest(Mockito.eq(HttpMethod.GET), Mockito.eq(endpoint),
                Mockito.any(), Mockito.any())).willThrow(exception);

        String expected = String.format(Messages.Exception.GENERIC_EXCEPTION, exception.getMessage());

        try {
            // exercise
            this.service.requestMembers();
            Assert.fail();
        } catch (UnexpectedException e) {
            // verify
            Assert.assertEquals(expected, e.getMessage());
        }
    }
    
    // test case: When invoking the getServiceEndpoint method, it must verify that the expected endpoint has been returned.
    @Test
    public void testGetServiceEndpoint() {
        // set up
        String expected = TestUtils.MEMBERSHIP_SERVICE_ENDPOINT;
        
        // exercise
        String endpoint = this.service.getServiceEndpoint();
        
        // verify
        Assert.assertEquals(expected, endpoint);
    }
    
    // test case: When invoking the getLocalMember method, it must verify that the expected member has been returned.
    @Test
    public void testGetLocalMember() {
        // set up
        String expected = TestUtils.MEMBERS[TestUtils.LOCAL_MEMBER_INDEX];
        
        // exercise
        String localMember = this.service.getLocalMember();
        
        // verify
        Assert.assertEquals(expected, localMember);
    }

    //test case: check if the expected services are returned
    @Test
    public void testGetServices() {
        //exercise
        List<ServiceType> services = this.service.getServices();
        //verify
        Assert.assertEquals(4, services.size());
    }

    //test case: check if an exception is thrown when an invalid member is passed
    @Test(expected = NoSuchMemberException.class)//verify
    public void testGetRemoteCatalogFromInvalidMember() throws FogbowException {
        //setup
        Mockito.doReturn(new ArrayList<>()).when(service).requestMembers();
        //exercise
        service.getRemoteCatalogFrom(TestUtils.FAKE_MEMBER_SERVICE_KEY);
    }

    //test case: check if the expected calls are done in the successful case
    @Test
    public void testGetRemoteCatalogFrom() throws Exception {
        //setup
        List<String> members = new ArrayList<>();
        members.add(TestUtils.FAKE_MEMBER_SERVICE_KEY);
        Mockito.doReturn(members).when(service).requestMembers();

        RemoteGetAllServicesRequest.Builder requestBuilder = Mockito.spy(RemoteGetAllServicesRequest.builder());
        PowerMockito.mockStatic(RemoteGetAllServicesRequest.class);
        PowerMockito.doReturn(requestBuilder).when(RemoteGetAllServicesRequest.class, "builder");

        RemoteGetAllServicesRequest request = Mockito.spy(new RemoteGetAllServicesRequest(requestBuilder));
        Mockito.doReturn(request).when(requestBuilder).build();

        List<ServiceType> servicesTypes = new ArrayList<>();
        servicesTypes.add(ServiceType.RAS);
        servicesTypes.add(ServiceType.AS);

        Mockito.doReturn(servicesTypes).when(request).send();

        //exercise
        List<Service> services = service.getRemoteCatalogFrom(TestUtils.FAKE_MEMBER_SERVICE_KEY);
        //verify
        Assert.assertEquals(2, services.size());
        Assert.assertEquals(RAS_LOCATION, services.get(TestUtils.FIRST_ARRAY_POSITION).getLocation());
        Assert.assertEquals(AS_LOCATION, services.get(TestUtils.SECOND_ARRAY_POSITION).getLocation());
        Mockito.verify(request, Mockito.times(TestUtils.RUN_ONCE)).send();
        Mockito.verify(service, Mockito.times(TestUtils.RUN_ONCE)).requestMembers();


    }

}
