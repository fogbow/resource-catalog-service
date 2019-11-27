package cloud.fogbow.rcs.core.intercomponent;

import cloud.fogbow.rcs.core.models.ServiceType;
import cloud.fogbow.rcs.core.service.CatalogService;

public class RemoteFacade {

    private static RemoteFacade instance;
    private CatalogService catalogService;
    
    public static RemoteFacade getInstance() {
        synchronized (RemoteFacade.class) {
            if (instance == null) {
                instance = new RemoteFacade();
            }
            return instance;
        }
    }
    
    public String requestService(String senderId, ServiceType serviceType) {
        return this.catalogService.requestService(senderId, serviceType);
    }

    public void cacheSave(String key, String content) {
        this.catalogService.cacheSave(key, content);
    }

    public synchronized void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }
}
