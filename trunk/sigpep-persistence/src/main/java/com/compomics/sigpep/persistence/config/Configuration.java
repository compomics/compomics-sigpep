package com.compomics.sigpep.persistence.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Provides access to configuration properties.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Sep-2007<br/>
 * Time: 12:20:29<br/>
 */
public class Configuration extends PropertiesConfiguration {

    private static Configuration ourInstance;

    static {
        try {
            ourInstance = new Configuration("config/sigpep-persistence.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Configuration instance.
     *
     * @return
     */
    public static Configuration getInstance() {
        return ourInstance;
    }

    /**
     * Reads the configuration file and creates a new Configuration instance.
     *
     * @param propertiesFile the propeties files to use
     * @throws ConfigurationException
     */
    private Configuration(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }
}
