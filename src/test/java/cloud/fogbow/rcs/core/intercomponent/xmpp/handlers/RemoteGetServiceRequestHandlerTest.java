package cloud.fogbow.rcs.core.intercomponent.xmpp.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.xmpp.packet.IQ;

import cloud.fogbow.rcs.core.BaseUnitTests;
import cloud.fogbow.rcs.core.TestUtils;
import cloud.fogbow.rcs.core.intercomponent.RemoteFacade;
import cloud.fogbow.rcs.core.intercomponent.xmpp.IqElement;

@PrepareForTest({ RemoteFacade.class })
public class RemoteGetServiceRequestHandlerTest extends BaseUnitTests {

    private RemoteGetServiceRequestHandler remoteHandler;
    
    @Before
    public void setUp() {
        this.remoteHandler = Mockito.spy(new RemoteGetServiceRequestHandler());
    }

    // test case: ...
    @Test
    public void testHandle() throws Exception {
        // set up
        IQ iq = this.testUtils.generateRemoteRequest("member1", "ms");
                
        String content = TestUtils.FAKE_CONTENT_JSON;
        
        RemoteFacade facade = Mockito.spy(RemoteFacade.getInstance());
        PowerMockito.mockStatic(RemoteFacade.class);
        BDDMockito.given(RemoteFacade.getInstance()).willReturn(facade);
        Mockito.doReturn(content).when(facade).requestService(Mockito.anyString(), Mockito.any());
        
        // exercise
        this.remoteHandler.handle(iq);
        
        // verify
        Mockito.verify(this.remoteHandler, Mockito.times(TestUtils.RUN_ONCE)).unmarshal(Mockito.eq(iq), Mockito.eq(IqElement.MEMBER));
        Mockito.verify(this.remoteHandler, Mockito.times(TestUtils.RUN_ONCE)).unmarshal(Mockito.eq(iq), Mockito.eq(IqElement.SERVICE));
        Mockito.verify(facade, Mockito.times(TestUtils.RUN_ONCE)).requestService(Mockito.anyString(), Mockito.any());
        Mockito.verify(this.remoteHandler, Mockito.times(TestUtils.RUN_ONCE)).marshal(Mockito.any(IQ.class), Mockito.anyString());
    }
    
}
