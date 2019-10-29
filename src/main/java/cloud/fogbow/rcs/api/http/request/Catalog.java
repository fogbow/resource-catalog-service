package cloud.fogbow.rcs.api.http.request;

import cloud.fogbow.rcs.constants.ApiDocumentation;
import cloud.fogbow.rcs.constants.SystemConstants;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class Catalog {
    public static final String VERSION_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "version";

    private final Logger LOGGER = Logger.getLogger(Catalog.class);

    @ApiOperation(value = ApiDocumentation.Catalog.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)

}

