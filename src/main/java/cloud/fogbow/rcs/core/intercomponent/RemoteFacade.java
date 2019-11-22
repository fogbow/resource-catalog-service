package cloud.fogbow.rcs.core.intercomponent;

import cloud.fogbow.rcs.core.models.ServiceType;

public class RemoteFacade {

    private static RemoteFacade instance;
    
    private RemoteFacade() {}

    public static RemoteFacade getInstance() {
        synchronized (RemoteFacade.class) {
            if (instance == null) {
                instance = new RemoteFacade();
            }
            return instance;
        }
    }
    
    public void getMessage(String content) {
        System.out.println(content);
    }

    public String requestService(String senderId, ServiceType serviceType) {
        // TODO call the requestService in the CatalogService
        return null;
    }
    
}
