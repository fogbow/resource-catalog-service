package cloud.fogbow.rcs.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.log4j.Logger;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.models.Service;
import cloud.fogbow.rcs.core.service.CatalogService;

public class ApplicationFacade {
    
    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;
    private String buildNumber;

    private ApplicationFacade() {
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
    
    public List<String> getMembers() throws UnexpectedException {
        return new CatalogService().requestMembers();
    }
    
    public List<Service> getServicesFrom(String member) throws UnexpectedException {
        return new CatalogService().getMemberServices(member);
    }
    
    // version request
    public String getVersionNumber() {
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
    }

    // public key request
    public String getPublicKey() throws UnexpectedException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (IOException | GeneralSecurityException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    public String getService(String member, String service) throws FileNotFoundException {
        return new CatalogService().getServiceCatalog(member, service);
    }
}