package cloud.fogbow.rcs.core.service.cache;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.core.PropertiesHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

@PrepareForTest({PropertiesHolder.class})
@RunWith(PowerMockRunner.class)
class MemoryBasedCacheTest {

    private String ANY_KEY = "foo";
    private String ANY_DATA = "bar";

    private Properties properties;

    public MemoryBasedCacheTest() {}

    @Before
    public void setup() {
        this.properties = Mockito.mock(Properties.class);
        PropertiesHolder propertiesHolderMock = Mockito.mock(PropertiesHolder.class);

        Mockito.when(propertiesHolderMock.getProperties()).thenReturn(this.properties);

        PowerMockito.mockStatic(PropertiesHolder.class);
        BDDMockito.given(PropertiesHolder.getInstance()).willReturn(propertiesHolderMock);
    }

    @Test
    public void testSetAndRetrieveCacheWithinTime() throws FogbowException {
        // setup
        Mockito.when(this.properties.getProperty(Mockito.anyString(),
                Mockito.anyString())).thenReturn("1");
        CacheService cacheService = new MemoryBasedCache();

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertTrue(cacheService.has(ANY_KEY));
    }

    @Test
    public void testOldCachedIsCleared() throws FogbowException {
        // setup
        Mockito.when(this.properties.getProperty(Mockito.anyString(),
                Mockito.anyString())).thenReturn("0");
        CacheService cacheService = new MemoryBasedCache();

        // exercise
        cacheService.set(ANY_KEY, ANY_DATA);

        // verify
        Assert.assertFalse(cacheService.has(ANY_KEY));
    }
}