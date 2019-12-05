package cloud.fogbow.rcs.core;

import java.nio.file.Paths;
import java.util.List;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.BashScriptRunner;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.service.cache.CacheServiceHolder;
import cloud.fogbow.rcs.core.service.cache.MemoryBasedCache;

public class ApplicationFacade {
    
    private static final String SEPARATOR = "-";

    private static ApplicationFacade instance;
    private CatalogFactory factory;
    private String buildNumber;

    private final String UPDATE_PROPERTIES_SCRIPT_PATH = "/bin/update-properties.sh";
    private final String UPDATE_PROPERTIES_SCRIPT_WHOLE_PATH = Paths.get("").toAbsolutePath().toString() + UPDATE_PROPERTIES_SCRIPT_PATH;

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

    public String getService(String member, String service) throws FogbowException {
        return this.factory.makeCatalogService().getServiceCatalog(member, service);
    }

    public void updateCacheExpiration(String newExpiration) throws Exception{
        BashScriptRunner runner = new BashScriptRunner();

        String[] command = {"bash", UPDATE_PROPERTIES_SCRIPT_WHOLE_PATH, ConfigurationPropertyKeys.CACHE_EXPIRATION_TIME_KEY, newExpiration};
        BashScriptRunner.Output result = runner.runtimeRun(command);

        if(result.getExitCode() != 0) {
            throw new UnexpectedException("Unable to update property");
        }

        PropertiesHolder.getInstance().refreshProperties();

        int newCacheExpiration = Integer.parseInt(PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.CACHE_EXPIRATION_TIME_KEY));
        ((MemoryBasedCache) CacheServiceHolder.getInstance()).setCacheExpiration(newCacheExpiration);
    }
}