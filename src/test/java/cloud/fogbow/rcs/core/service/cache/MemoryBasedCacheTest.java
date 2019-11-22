package cloud.fogbow.rcs.core.service.cache;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.core.PropertiesHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest({PropertiesHolder.class})
@RunWith(PowerMockRunner.class)
class MemoryBasedCacheTest {

    private String ANY_KEY = "foo";
    private String ANY_DATA = "bar";

    public MemoryBasedCacheTest() {}

    @Test
    public void testSetAndRetrieveCacheWithinTime() throws FogbowException {
        // setup
        CacheService cacheService = new MemoryBasedCache();
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.when(propertiesHolder.getProperty(ConfigurationPropertyKeys.CACHE_EXPIRATION_TIME_KEY,
                Mockito.anyString())).thenReturn("1");

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertTrue(cacheService.has(ANY_KEY));
    }

    @Test
    public void testOldCachedIsCleared() throws FogbowException {
        // setup
        CacheService cacheService = new MemoryBasedCache();
        PropertiesHolder propertiesHolder = Mockito.mock(PropertiesHolder.class);
        Mockito.when(propertiesHolder.getProperty(ConfigurationPropertyKeys.CACHE_EXPIRATION_TIME_KEY,
                Mockito.anyString())).thenReturn("0");

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertFalse(cacheService.has(ANY_KEY));
    }
}