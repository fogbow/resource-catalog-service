package cloud.fogbow.rcs.core.service.cache;

import cloud.fogbow.common.exceptions.FogbowException;

public interface CacheService<T> {
    void set(String key, T data) throws FogbowException;

    T get(String key) throws FogbowException;

    boolean has(String key) throws FogbowException;
}
