package jk.rest.tools;

import jk.krakenex.KrakenEnums;
import jk.wsocket.data.Logs;
import lombok.Getter;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static jk.krakenex.KrakenEnums.ConfigProperties.PAPI;

public class LocalPropLoader {

    @Getter
    private Properties props;

    public LocalPropLoader() {
        this.props = new Properties();
        val propertiesFile = System.getProperty("krakenkeys");
        if (propertiesFile == null) {
            Logs.error("Missing jk.props file. Private methods won't work");
        }
        try {
            val inputStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(propertiesFile)));
            props.load(inputStream);
            Logs.info("Config variables sucessfully read from file: " + propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException("Invalid path for Kraken keys file: " + propertiesFile);
        }
    }

    public String getApi() {
        return this.props.getProperty(KrakenEnums.ConfigProperties.API.getConfigName());
    }

    public String getApiSecret() {
        return this.props.getProperty(PAPI.getConfigName());
    }

    public boolean keysFound() {
        return this.getApi() != null && this.getApiSecret() != null;
    }
}
