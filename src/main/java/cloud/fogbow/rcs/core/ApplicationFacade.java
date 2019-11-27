package cloud.fogbow.rcs.core;

import java.util.List;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.service.CatalogService;

public class ApplicationFacade {
    
    public static final String BUILD_NUMBER_FORMAT = "%s-%s";

    private static ApplicationFacade instance;
    private CatalogService catalogService;
    private String buildNumber;

    private ApplicationFacade() {
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                String.format(BUILD_NUMBER_FORMAT, SystemConstants.API_VERSION_NUMBER,
                        ConfigurationPropertyDefaults.BUILD_NUMBER));
    }

    public static ApplicationFacade getInstance() {
        synchronized (ApplicationFacade.class) {
            if (instance == null) {
                instance = new ApplicationFacade();
            }
            return instance;
        }
    }
    
    public List<String> getMembers() throws FogbowException {
        return this.catalogService.requestMembers();
    }
    
    public List<Service> getServicesFrom(String member) throws FogbowException {
        return this.catalogService.getMemberServices(member);
    }
    
    public String getService(String member, String service) throws FogbowException {
        return this.catalogService.getServiceCatalog(member, service);
    }
    
    // version request
    public String getVersionNumber() {
        return this.buildNumber;
    }

    public synchronized void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }
}