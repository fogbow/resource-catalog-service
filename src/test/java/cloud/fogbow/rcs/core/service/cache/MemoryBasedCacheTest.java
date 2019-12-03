package cloud.fogbow.rcs.core.service.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.core.BaseUnitTests;
import cloud.fogbow.rcs.core.PropertiesHolder;
import cloud.fogbow.rcs.core.TestUtils;

@PrepareForTest({PropertiesHolder.class})
class MemoryBasedCacheTest extends BaseUnitTests {

    private static final String ANY_KEY = "foo";
    private static final String ANY_DATA = "bar";

    private PropertiesHolder propertiesHolder;
    
    public MemoryBasedCacheTest() {}

    @Before
    public void setup() {
        this.propertiesHolder = Mockito.spy(PropertiesHolder.getInstance());
        PowerMockito.mockStatic(PropertiesHolder.class);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(this.propertiesHolder);
    }

    // test case: Add value to cache service and make sure it is valid.
    @Test
    public void testSetAndRetrieveCacheWithinTime() throws FogbowException {
        // setup
        Mockito.when(this.propertiesHolder.getProperty(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(TestUtils.HANG_ON);
        
        CacheService<String> cacheService = new MemoryBasedCache<String>();

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertTrue(cacheService.has(ANY_KEY));
    }

    // test case: Creates an entry and expects it to be expired.
    @Test
    public void testOldCachedIsCleared() throws FogbowException {
        // setup
        Mockito.when(this.propertiesHolder.getProperty(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(TestUtils.NOT_WAIT);
        
        CacheService<String> cacheService = new MemoryBasedCache<String>();

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertFalse(cacheService.has(ANY_KEY));
    }
}