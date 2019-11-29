package cloud.fogbow.rcs.core.intercomponent.xmpp.handlers;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jamppa.component.handler.AbstractQueryHandler;
import org.xmpp.packet.IQ;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;

import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.core.intercomponent.RemoteFacade;
import cloud.fogbow.rcs.core.intercomponent.xmpp.IqElement;
import cloud.fogbow.rcs.core.intercomponent.xmpp.RemoteMethod;
import cloud.fogbow.rcs.core.models.ServiceType;

public class RemoteGetServiceRequestHandler extends AbstractQueryHandler {

    private static final Logger LOGGER = Logger.getLogger(RemoteGetServiceRequestHandler.class);
    private static final String REMOTE_GET_SERVICE = RemoteMethod.REMOTE_GET_SERVICE.toString();
    
    public RemoteGetServiceRequestHandler() {
        super(REMOTE_GET_SERVICE);
    }

    @Override
    public IQ handle(IQ iq) {
        LOGGER.info(String.format(Messages.Info.RECEIVING_REMOTE_REQUEST_FROM_S, iq.getFrom()));
        IQ response = IQ.createResultIQ(iq);
        
        String member = unmarshal(iq, IqElement.MEMBER);
        String service = unmarshal(iq, IqElement.SERVICE).toUpperCase();        
        String content = RemoteFacade.getInstance().requestService(member, ServiceType.valueOf(service));
        
        marshal(response, content);
        return response;
    }

    @VisibleForTesting
    void marshal(IQ response, String content) {
        Element queryElement = response.getElement().addElement(IqElement.QUERY.toString());
        Element contentElement = queryElement.addElement(IqElement.CONTENT.toString());
        contentElement.setText(content);
    }

    @VisibleForTesting
    String unmarshal(IQ iq, IqElement iqElement) {
        Element queryElement = iq.getElement().element(IqElement.QUERY.toString());
        Element contentElement = queryElement.element(iqElement.toString());
        return new Gson().fromJson(contentElement.getText(), String.class);
    }

}
