package jk.wsocket.service;

import jk.krakenex.KrakenEnums;
import jk.rest.tools.LocalPropLoader;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static jk.krakenex.KrakenEnums.ConfigProperties.CONNECT_ON_START;

/**
 * Parses and validates provided properties from local config file
 */
@Service
public class LocalPropLoaderService {

    @Getter
    private LocalPropLoader propsLoader;

    @PostConstruct
    public void parseProperties () {
        this.propsLoader = new LocalPropLoader();
    }

    public Optional<String> getProperty(KrakenEnums.ConfigProperties property) {
        return Optional.ofNullable(this.propsLoader.getProps().getProperty(property.getConfigName()));
    }

    public Optional<String> getPublicAPI () {
        return Optional.ofNullable(
                this.propsLoader.getProps().getProperty(KrakenEnums.ConfigProperties.PUBLIC_ENDPOINT.getConfigName()));
    }

    public Optional<String> getPrivateAPI () {
        return Optional.ofNullable(
                this.propsLoader.getProps().getProperty(KrakenEnums.ConfigProperties.PRIVATE_ENDPOINT.getConfigName()));
    }

    public boolean connectOnStart () {
        val connect = this.propsLoader.getProps().getProperty(CONNECT_ON_START.getConfigName());
        return connect == null || Boolean.parseBoolean(connect);
    }
}
