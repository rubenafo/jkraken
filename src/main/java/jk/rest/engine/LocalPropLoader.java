package jk.rest.engine;

import lombok.Data;
import lombok.var;

import java.io.IOException;
import java.util.Properties;

@Data
public class LocalPropLoader
{
    private Properties props;

    public LocalPropLoader () {
        this.props = new Properties();
        var inputStream = getClass().getClassLoader().getResourceAsStream("jk.properties");
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Key file not found in the classpath");
        }
        if (!keysFound()) {
            System.out.println("Keys couldn't be found in key file. Private methods won't work");
        }
    }

    public String getApi () { return this.props.getProperty("api");}
    public String getApiSecret () {
        return this.props.getProperty("papi");
    }

    public boolean keysFound () {
        return this.getApi() != null && this.getApiSecret() != null;
    }
}
