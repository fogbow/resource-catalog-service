package cloud.fogbow.rcs.core.intercomponent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.core.BaseUnitTests;
import cloud.fogbow.rcs.core.TestUtils;
import cloud.fogbow.rcs.core.models.ServiceType;

@PrepareForTest({ RemoteFacade.class })
public class RemoteFacadeTest extends BaseUnitTests {

    private RemoteFacade facade;
    
    @Before
    public void setUp() {
        this.facade = this.testUtils.mockRemoteFacade();
    }
    
    // test case: When invoking the requestService method, it must verify that the call
    // was successful.
    @Test
    public void testRequestService() throws FogbowException {
        // set up
        String senderId = Mockito.anyString();
        ServiceType serviceType = Mockito.any(ServiceType.class);

        // exercise
        this.facade.requestService(senderId, serviceType);

        // verify
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE))
                .requestService(Mockito.eq(senderId), Mockito.eq(serviceType));
    }
    
    // test case: When invoking the cacheSave method, it must verify that the call
    // was successful.
    @Test
    public void testCacheSave() {
        // set up
        String key = Mockito.anyString();
        String content = Mockito.anyString();

        // exercise
        this.facade.cacheSave(key, content);

        // verify
        Mockito.verify(this.facade, Mockito.times(TestUtils.RUN_ONCE))
                .cacheSave(Mockito.eq(key), Mockito.eq(content));
    }

}
