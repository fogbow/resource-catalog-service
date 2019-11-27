package cloud.fogbow.rcs.core.intercomponent.xmpp.handlers;

import cloud.fogbow.common.util.GsonHolder;
import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.core.intercomponent.RemoteFacade;
import cloud.fogbow.rcs.core.intercomponent.xmpp.IqElement;
import cloud.fogbow.rcs.core.intercomponent.xmpp.RemoteMethod;
import cloud.fogbow.rcs.core.intercomponent.xmpp.requesters.RemoteGetServiceRequest;
import cloud.fogbow.rcs.core.intercomponent.xmpp.requesters.RemoteGetServicesRequest;
import cloud.fogbow.rcs.core.models.ServiceType;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jamppa.component.handler.AbstractQueryHandler;
import org.xmpp.packet.IQ;

import java.util.List;

public class RemoteGetServicesRequestHandler extends AbstractQueryHandler {

    private static final Logger LOGGER = Logger.getLogger(RemoteGetServiceRequestHandler.class);
    private static final String REMOTE_GET_SERVICES = RemoteMethod.REMOTE_GET_SERVICES.toString();

    public RemoteGetServicesRequestHandler() {
        super(REMOTE_GET_SERVICES);
    }

    @Override
    public IQ handle(IQ iq) {
        LOGGER.info(String.format(Messages.Info.RECEIVING_REMOTE_REQUEST_FROM_S, iq.getFrom()));
        IQ response = IQ.createResultIQ(iq);

        String member = unmarshal(iq, IqElement.MEMBER);
        List<ServiceType> services = RemoteFacade.getInstance().getServices();

        marshal(response, services);
        return response;
    }

    @VisibleForTesting
    void marshal(IQ response, List<ServiceType> content) {
        Element queryElement = response.getElement().addElement(IqElement.QUERY.toString());
        Element contentElement = queryElement.addElement(IqElement.CONTENT.toString());
        contentElement.setText(GsonHolder.getInstance().toJson(content));
    }

    @VisibleForTesting
    String unmarshal(IQ iq, IqElement iqElement) {
        Element queryElement = iq.getElement().element(IqElement.QUERY.toString());
        Element contentElement = queryElement.element(iqElement.toString());
        return new Gson().fromJson(contentElement.getText(), String.class);
    }


}
