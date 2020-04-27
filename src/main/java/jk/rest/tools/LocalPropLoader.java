package jk.rest.tools;

import lombok.Data;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Data
public class LocalPropLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalPropLoader.class);

    private Properties props;

    public LocalPropLoader() {
        this.props = new Properties();
        val propertiesFile = System.getProperty("krakenkeys");
        if (propertiesFile == null) {
            LOGGER.error("Missing jk.props file. Private methods won't work");
        }
        try {
            val inputStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(propertiesFile)));
            props.load(inputStream);
            LOGGER.info("Kraken keys successfully read from file: " + propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException("Invalid path for Kraken keys file: " + propertiesFile);
        }
        if (!keysFound()) {
            LOGGER.error("Keys couldn't be found in key file. Private methods won't work");
        }
    }

    public String getApi() {
        return this.props.getProperty("api");
    }

    public String getApiSecret() {
        return this.props.getProperty("papi");
    }

    public boolean keysFound() {
        return this.getApi() != null && this.getApiSecret() != null;
    }
}
