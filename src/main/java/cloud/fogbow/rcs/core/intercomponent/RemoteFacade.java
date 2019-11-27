package cloud.fogbow.rcs.core.intercomponent;

import cloud.fogbow.rcs.core.CatalogFactory;
import cloud.fogbow.rcs.core.PropertiesHolder;
import cloud.fogbow.rcs.core.models.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class RemoteFacade {

    private final String SERVICE_PROPERTY_SEPARATOR = "_";

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
                add("fns_ip");
                add("ras_ip");
                add("as_ip");
                add("ms_ip");
            }
        };

        List<ServiceType> services = new ArrayList<>();

        for(String serviceProperty : possibleServicesProperties) {
            String propertyValue = PropertiesHolder.getInstance().getProperty(serviceProperty);
            if(propertyValue != null && !propertyValue.trim().isEmpty()) {
                ServiceType currentServiceType = ServiceType.valueOf(serviceProperty.split(SERVICE_PROPERTY_SEPARATOR)[0]);
                services.add(currentServiceType);
            }
        }

        return services;
    }
    
}
