package com.jkraken.utils;

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
        var inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("not found in the classpath");
        }
    }

    public String getPrivAPI () {
        return this.props.getProperty("papi");
    }
}