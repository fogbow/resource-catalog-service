package cloud.fogbow.rcs.api.http.request;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.constants.SystemConstants;
import cloud.fogbow.rcs.core.ApplicationFacade;
import cloud.fogbow.rcs.core.models.ProviderMember;

@CrossOrigin
@RestController
@RequestMapping(value = LocalProvider.ENDPOINT)
//@Api(description = ApiDocumentation.Catalog.API) // FIXME
public class LocalProvider {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "local";

    private static final Logger LOGGER = Logger.getLogger(LocalProvider.class);
    
    //@ApiOperation(value = ApiDocumentation.Catalog.GET_OPERATION) // FIXME
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ProviderMember> getLocalProvider() throws FogbowException {
        try {
            //LOGGER.debug(String.format(Messages.Info.GETTING_ALL_MEMBERS)); // FIXME
            ProviderMember provider = ApplicationFacade.getInstance().getLocalProvider();
            return new ResponseEntity<>(provider, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.debug(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
}
