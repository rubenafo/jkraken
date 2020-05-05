package jk.wsocket.service;

import jk.krakenex.KrakenEnums;
import jk.wsocket.data.Logs;
import lombok.NonNull;
import lombok.val;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Group the functionality to be executed right after the JKraken server is ready
 */
@Service
public class OnceReadyService {

    private final KrakenWsService krakenService;
    private final LocalPropLoaderService propsService;

    public OnceReadyService(@NonNull KrakenWsService krakenService,
                            @NonNull LocalPropLoaderService propsService) {
        this.krakenService = krakenService;
        this.propsService = propsService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void whenReady () {
        if (!propsService.getPropsLoader().keysFound()) {
            Logs.error("Keys couldn't be found in key file. Private methods won't work");
        }
        val subscribeTo = propsService.getProperty(KrakenEnums.ConfigProperties.SUBSCRIBE_TO);
        if (propsService.connectOnStart()) {
            Logs.info("Connecting to Kraken Exchange...");
            this.krakenService.connect();
        }
    }
}
