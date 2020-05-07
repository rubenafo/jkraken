package jk.wsocket.service;

import jk.krakenex.KrakenEnums;
import jk.rest.api.KrakenRestService;
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
    private final KrakenRestService krakenRestService;

    public OnceReadyService(@NonNull KrakenWsService krakenService,
                            @NonNull LocalPropLoaderService propsService,
                            @NonNull KrakenRestService krakenRestService) {
        this.krakenService = krakenService;
        this.propsService = propsService;
        this.krakenRestService = krakenRestService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void whenReady () {
        val keysProvided = propsService.getPropsLoader().keysFound();
        val disabledStart = !propsService.connectOnStart();
        if (!keysProvided) {
            Logs.error("Private keys not found in key file. Private methods won't work");
        }
        val subscribeTo = propsService.getProperty(KrakenEnums.ConfigProperties.SUBSCRIBE_TO);
        if (disabledStart) {
            Logs.info("Connection on start DISABLED. NOT connected to Kraken Exchange");
        }
        else {
            Logs.info("Connecting to Kraken Exchange...");
            this.krakenService.connect();
           // this.krakenService.subscribe(null, 0, 0, KrakenEnums.Channels.OWN_TRADES.getChannelName());
            if (keysProvided) {
                val balance = this.krakenRestService.getBalance();
                if (balance != null) {
                    Logs.info("Valid private keys provided");
                }
            }
        }
    }
}
