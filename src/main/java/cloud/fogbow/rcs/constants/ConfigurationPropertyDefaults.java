package cloud.fogbow.rcs.constants;

import java.util.concurrent.TimeUnit;

public class ConfigurationPropertyDefaults {
    // RCS CONF DEFAULTS
    public static final String BUILD_NUMBER = "[testing mode]";
    public static final String CACHE_EXPIRATION_TIME_DEFAULT = "3600";
    public static final String DEFAULT_CATALOG_VIEW = "catalog";
    public static final String DEFAULT_SERVICE_CATALOG_SPEC_ATTRIBUTE = "spec";
    
    // INTERCOMPONENT CONF DEFAULT
    public static final String XMPP_CSC_PORT = Integer.toString(5347);
    public static final String XMPP_TIMEOUT = Long.toString(TimeUnit.SECONDS.toMillis(5)); // the reference value is 5 seconds
}
