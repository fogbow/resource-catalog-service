package cloud.fogbow.rcs.constants;

import java.util.concurrent.TimeUnit;

public class ConfigurationPropertyDefaults {
    // RCS CONF DEFAULTS
    public static final String BUILD_NUMBER = "[testing mode]";
    public static final String CACHE_EXPIRATION_TIME_DEFAULT = Long.toString(TimeUnit.HOURS.toMinutes(24)); // One day
}
