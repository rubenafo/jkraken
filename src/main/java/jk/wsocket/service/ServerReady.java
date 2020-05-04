package jk.wsocket.service;

import jk.wsocket.data.Logs;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Group the functionality to be executed right after the JKraken server is ready
 */
@Component
public class ServerReady {

    private final KrakenWsService krakenService;

    public ServerReady (@NonNull KrakenWsService krakenService) {
        this.krakenService = krakenService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void whenReady () {
        Logs.info("ready!");
    }
}
