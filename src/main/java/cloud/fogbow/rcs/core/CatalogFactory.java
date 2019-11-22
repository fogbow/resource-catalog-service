package cloud.fogbow.rcs.core;

import cloud.fogbow.rcs.core.service.CatalogService;

public class CatalogFactory {

    public CatalogService makeCatalogService() {
        return new CatalogService();
    }
}
