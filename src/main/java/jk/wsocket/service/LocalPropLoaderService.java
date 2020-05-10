package jk.wsocket.service;

import jk.krakenex.KrakenEnums;
import jk.rest.tools.LocalPropLoader;
import jk.wsocket.data.Logs;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static jk.krakenex.KrakenEnums.ConfigProperties.CONNECT_ON_START;
import static jk.krakenex.KrakenEnums.ConfigProperties.DEBUG_MESSAGES;

/**
 * Parses and validates provided properties from local config file
 */
@Service
public class LocalPropLoaderService {

    @Getter
    private LocalPropLoader propsLoader;

    public LocalPropLoaderService () {
        try {
            this.propsLoader = new LocalPropLoader();
        }
        catch (Exception ex) {
            Logs.error(ex.getMessage());
            System.exit(-1);
        }
    }

    public Optional<String> getProperty(KrakenEnums.ConfigProperties property) {
        return Optional.ofNullable(this.propsLoader.getProps().getProperty(property.getConfigName()));
    }

    public Optional<String> getPublicAPI () {
        return Optional.ofNullable(this.propsLoader.getPublicAPI());
    }

    public Optional<String> getPrivateAPI () {
        return Optional.ofNullable(this.propsLoader.getPrivateAPI());
    }

    public boolean connectOnStart () {
        val connect = this.propsLoader.getProps().getProperty(CONNECT_ON_START.getConfigName());
        return connect == null || Boolean.parseBoolean(connect);
    }

    public boolean debugMessages () {
        val printHeartbeat = this.propsLoader.getProps().getProperty(DEBUG_MESSAGES.getConfigName());
        return printHeartbeat != null && Boolean.parseBoolean(printHeartbeat);
    }
}
