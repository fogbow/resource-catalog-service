package cloud.fogbow.rcs.core.intercomponent;

import cloud.fogbow.rcs.core.CatalogFactory;
import cloud.fogbow.rcs.core.models.ServiceType;

public class RemoteFacade {

    private static RemoteFacade instance;
    private CatalogFactory factory;
    
    private RemoteFacade() {
        this.factory = new CatalogFactory();
    }

    public static RemoteFacade getInstance() {
        synchronized (RemoteFacade.class) {
            if (instance == null) {
                instance = new RemoteFacade();
            }
            return instance;
        }
    }
    
    public String requestService(String senderId, ServiceType serviceType) {
        return this.factory.makeCatalogService().requestService(senderId, serviceType);
    }

    public void cacheSave(String key, String content) {
        this.factory.makeCatalogService().cacheSave(key, content);
    }
    
}
