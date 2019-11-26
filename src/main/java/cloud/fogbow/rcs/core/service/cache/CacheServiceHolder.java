package cloud.fogbow.rcs.core.service.cache;

public class CacheServiceHolder {
    private static CacheService<String> cacheService;

    public static CacheService<String> getInstance() {
        if (cacheService == null) {
            cacheService = new MemoryBasedCache();
        }
        return cacheService;
    }
}
