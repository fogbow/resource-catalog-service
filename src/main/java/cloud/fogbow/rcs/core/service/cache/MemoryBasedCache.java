package cloud.fogbow.rcs.core.service.cache;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.core.PropertiesHolder;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults.CACHE_EXPIRATION_TIME_DEFAULT;
import static cloud.fogbow.rcs.constants.ConfigurationPropertyKeys.CACHE_EXPIRATION_TIME_KEY;

public class MemoryBasedCache<T> implements CacheService<T> {
    private final int CACHE_VALIDITY;
    private Map<String, CacheEntry> cacheMap;

    public MemoryBasedCache() {
        Properties properties = PropertiesHolder.getInstance().getProperties();
        this.CACHE_VALIDITY = Integer.parseInt(properties.getProperty(CACHE_EXPIRATION_TIME_KEY, CACHE_EXPIRATION_TIME_DEFAULT));

        this.cacheMap = new HashMap<>();
    }

    @Override
    public void set(String key, T data) throws FogbowException {
        CacheEntry cacheEntry = new CacheEntry(data);
        this.cacheMap.put(key, cacheEntry);
    }

    @Override
    public T get(String key) throws FogbowException {
        return this.cacheMap.get(key).getData();
    }

    @Override
    public boolean has(String key) throws FogbowException {
        if (!this.cacheMap.containsKey(key)) {
            return false;
        }

        CacheEntry cacheEntry = this.cacheMap.get(key);

        if (cacheEntry.expired()) {
            this.cacheMap.remove(key);
            return false;
        }

        return true;
    }

    private class CacheEntry {
        private T data;
        private Date expiration;

        public CacheEntry(T data) {
            this.data = data;
            Date now = new Date();
            this.expiration = DateUtils.addSeconds(now, CACHE_VALIDITY);
        }

        public boolean expired() {
            Date now = new Date();
            return now.getTime() >= this.expiration.getTime();
        }

        public T getData() {
            return data;
        }
    }
}
