package cloud.fogbow.rcs.core;

import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.rcs.constants.SystemConstants;

import java.util.Properties;

public class PropertiesHolder {
    private Properties properties;
    private static PropertiesHolder instance;

    private PropertiesHolder() throws FatalErrorException {
        String path = HomeDir.getPath();
        this.properties = PropertiesUtil.readProperties(path + SystemConstants.RCS_CONF_FILE);
    }

    public static synchronized PropertiesHolder getInstance() throws FatalErrorException {
        if (instance == null) {
            instance = new PropertiesHolder();
        }
        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getProperty(String propertyName, String defaultPropertyValue) {
        String propertyValue = this.properties.getProperty(propertyName, defaultPropertyValue);
        if (propertyValue.trim().isEmpty()) {
            propertyValue = defaultPropertyValue;
        }
        return propertyValue;
    }
}
