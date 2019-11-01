package cloud.fogbow.rcs.core.service;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import cloud.fogbow.rcs.core.models.MembershipServiceResponse;
import cloud.fogbow.rcs.core.models.ProviderMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        HttpRequestClient.class,
        MembershipServiceResponse.class
})
public class CatalogServiceTest {

    private static final String ANY_STRING = "any-string";
    private static final String FAKE_MEMBER_TEMPLATE = "fake-member-%d";
    private static final String FAKE_LOCAL_MEMBER = "fake-local-member";
    private CatalogService catalogService;

    @Before
    public void setup() {
        this.catalogService = Mockito.spy(new CatalogService());
    }

    // test case: the requestMembers should return a list of ProviderMember
    @Test
    public void testRequestMembersSuccessful() throws Exception {
        // setup
        PowerMockito.mockStatic(HttpRequestClient.class);
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.getContent()).thenReturn(ANY_STRING);

        PowerMockito.mockStatic(MembershipServiceResponse.class);
        PowerMockito.when(MembershipServiceResponse.fromJson(Mockito.anyString())).thenReturn(Mockito.mock(MembershipServiceResponse.class));

        BDDMockito.given(HttpRequestClient.doGenericRequest(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(HashMap.class))).willReturn(httpResponse);

        Mockito.doReturn(new ArrayList<>()).when(catalogService).listProviderMembers(Mockito.any());

        // exercise
        catalogService.requestMembers();

        // verify
        Mockito.verify(catalogService).listProviderMembers(Mockito.any());
    }

    // test case: in case something wrong happen's it should throw an UnexpectedException
    @Test(expected = UnexpectedException.class)
    public void testRequestMembersFail() throws Exception {
        // setup
        PowerMockito.mockStatic(HttpRequestClient.class);
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.getContent()).thenReturn(ANY_STRING);

        PowerMockito.mockStatic(MembershipServiceResponse.class);
        PowerMockito.when(MembershipServiceResponse.fromJson(Mockito.anyString())).thenReturn(Mockito.mock(MembershipServiceResponse.class));

        BDDMockito.given(HttpRequestClient.doGenericRequest(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(HashMap.class))).willThrow(Exception.class);

        Mockito.doReturn(new ArrayList<>()).when(catalogService).listProviderMembers(Mockito.any());

        // exercise
        catalogService.requestMembers();
        Assert.fail();
    }

    // test case: listProviderMembers should iterate over the response and return a list with
    // the ProviderMembers
    @Test
    public void testListProviderMembers() {
        // setup
        MembershipServiceResponse response = createMockedMembershipServiceResponse();
        Mockito.doReturn(FAKE_LOCAL_MEMBER).when(catalogService).getLocalMember();

        // exercise
        List<ProviderMember> providerMembers = catalogService.listProviderMembers(response);

        // verify
        Mockito.verify(response).getMembers();
        Assert.assertTrue(providerMembers.stream().anyMatch(o -> o.isLocal()));
        Assert.assertTrue(providerMembers.stream().anyMatch(o -> !o.isLocal()));
    }

    private MembershipServiceResponse createMockedMembershipServiceResponse() {
        int memberCount = 3;
        MembershipServiceResponse membershipServiceResponse = Mockito.mock(MembershipServiceResponse.class);

        ArrayList<String> members = new ArrayList();

        for (int i = 0; i < memberCount; ++i) {
            String fakeMember = String.format(FAKE_MEMBER_TEMPLATE, i);
            members.add(fakeMember);
        }

        members.add(FAKE_LOCAL_MEMBER);

        Mockito.when(membershipServiceResponse.getMembers()).thenReturn(members.toArray(new String[memberCount]));

        return membershipServiceResponse;
    }
}