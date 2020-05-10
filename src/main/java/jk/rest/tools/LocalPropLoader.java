package jk.rest.tools;

import jk.krakenex.KrakenEnums;
import jk.wsocket.data.Logs;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

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
        val propertiesFile = System.getProperty("configFile");
        if (propertiesFile == null) {
            throw new RuntimeException("Missing configFile parameter (-DconfigFile=<path_to_config_file>");
        }
        try {
            val inputStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(propertiesFile)));
            props.load(inputStream);
            Logs.info("Config variables successfully read from file: " + propertiesFile);
            if (StringUtils.isEmpty(this.getPublicAPI())) {
                throw new RuntimeException("Error: public_ws is missing from config file");
            }
            if (StringUtils.isEmpty(this.getPrivateAPI())) {
                throw new RuntimeException("Error: private_ws is missing from config file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Missing config file: " + propertiesFile);
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

    public String getPublicAPI () {
        return this.props.getProperty(KrakenEnums.ConfigProperties.PUBLIC_ENDPOINT.getConfigName());
    }

    public String getPrivateAPI () {
        return this.props.getProperty(KrakenEnums.ConfigProperties.PRIVATE_ENDPOINT.getConfigName());
    }
}
