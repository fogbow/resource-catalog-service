package cloud.fogbow.rcs;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.rcs.constants.Messages;
import cloud.fogbow.rcs.core.intercomponent.xmpp.PacketSenderHolder;

@Component
public class Main implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(Main.class);
    
    @Override
    public void run(ApplicationArguments args) {
        try {
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
        } catch (FatalErrorException e) {
            LOGGER.fatal(e.getMessage(), e);
            tryExit();
        }
    }
    
    private void tryExit() {
        if (!Boolean.parseBoolean(System.getenv("SKIP_TEST_ON_TRAVIS")))
            System.exit(1);
    }
}