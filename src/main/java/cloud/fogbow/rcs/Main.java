package cloud.fogbow.rcs;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.core.intercomponent.xmpp.PacketSenderHolder;

@Component
public class Main implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(Main.class);
    
    @Override
    public void run(ApplicationArguments args) {
        // Starting PacketSender
        while (true) {
            try {
                PacketSenderHolder.init();
                break;
            } catch (IllegalStateException e) {
                LOGGER.error(Messages.Error.NO_PACKET_SENDER, e);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    LOGGER.error("", ex);
                }
            }
        }
    }

}