package cloud.fogbow.rcs.core.service;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import cloud.fogbow.rcs.core.models.ProviderMember;
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
@PrepareForTest(HttpRequestClient.class)
public class CatalogServiceTest {

    private static final String ANY_STRING = "any-string";
    private static final String FAKE_MEMBER_TEMPLATE = "member-%s";
    private CatalogService catalogService;
    private List<ProviderMember> mockedProviderMemberList;

    @Before
    public void setup() {
        this.catalogService = Mockito.spy(new CatalogService());
        this.mockedProviderMemberList = createMockedProviderMemberList();
    }

    // test case: the requestMembers should return a list of ProviderMember

    @Test
    public void testRequestMembersSuccessful() throws FogbowException {
        // setup
        PowerMockito.mockStatic(HttpRequestClient.class);
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.getContent()).thenReturn(ANY_STRING)
        BDDMockito.given(HttpRequestClient.doGenericRequest(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(HashMap.class))).willReturn(httpResponse);


    }

    private List<ProviderMember> createMockedProviderMemberList() {
        List<ProviderMember> list = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            ProviderMember providerMember = new ProviderMember(String.format(FAKE_MEMBER_TEMPLATE, i), i == 0);
            list.add(providerMember);
        }

        return list;
    }
}