package cloud.fogbow.rcs.core.intercomponent;

import cloud.fogbow.rcs.core.CatalogFactory;
import cloud.fogbow.rcs.core.PropertiesHolder;
import cloud.fogbow.rcs.core.models.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class RemoteFacade {

    private final String SERVICE_PROPERTY_SEPARATOR = "_";
    private final String FNS_SERVICE_PROPERTY = "fns_url";
    private final String RAS_SERVICE_PROPERTY = "ras_url";
    private final String MS_SERVICE_PROPERTY = "ms_url";
    private final String AS_SERVICE_PROPERTY = "as_url";

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

    public List<ServiceType> getServices() {
        List<String> possibleServicesProperties = new ArrayList<String>(){
            {
                add(FNS_SERVICE_PROPERTY);
                add(RAS_SERVICE_PROPERTY);
                add(AS_SERVICE_PROPERTY);
                add(MS_SERVICE_PROPERTY);
            }
        };

        List<ServiceType> services = new ArrayList<>();

        for(String serviceProperty : possibleServicesProperties) {
            String propertyValue = PropertiesHolder.getInstance().getProperty(serviceProperty);
            if(propertyValue != null && !propertyValue.trim().isEmpty()) {
                ServiceType currentServiceType = ServiceType.valueOf(serviceProperty.split(SERVICE_PROPERTY_SEPARATOR)[0].toUpperCase());
                services.add(currentServiceType);
            }
        }

        return services;
    }

    public void cacheSave(String key, String content) {
        this.factory.makeCatalogService().cacheSave(key, content);
    }
    
}
