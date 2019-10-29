package cloud.fogbow.rcs.core;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.rcs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.rcs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.rcs.constants.SystemConstants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ApplicationFacade {
    private final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);
    private final String FAILED_REQUEST_BODY = "{\"message\":\"" + "%s" + "\"}";

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
}