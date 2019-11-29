package cloud.fogbow.rcs.core;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.service.CatalogService;

import java.util.List;

public class ApplicationFacade {
    
    private static final String SEPARATOR = "-";

    private static ApplicationFacade instance;
    private CatalogFactory factory;
    private String buildNumber;

    private ApplicationFacade() {
        this.factory = new CatalogFactory();
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY, ConfigurationPropertyDefaults.BUILD_NUMBER);
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
        return this.factory.makeCatalogService().requestMembers();
    }
    
    public List<Service> getServicesFrom(String member) throws FogbowException {
        return this.factory.makeCatalogService().getMemberServices(member);
    }
    
    // version request
    public String getVersionNumber() {
        return SystemConstants.API_VERSION_NUMBER.concat(SEPARATOR).concat(this.buildNumber);
    }

    public String getService(String member, String service) {
        return new CatalogService().getServiceCatalog(member, service);
    }
}