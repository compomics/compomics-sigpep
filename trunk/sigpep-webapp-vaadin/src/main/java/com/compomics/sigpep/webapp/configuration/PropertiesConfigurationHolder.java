package com.compomics.sigpep.webapp.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 30/09/11
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesConfigurationHolder extends PropertiesConfiguration {

    private static PropertiesConfigurationHolder ourInstance;

    static {
        try {
            ourInstance = new PropertiesConfigurationHolder("config/sigPepWebApp.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Configuration instance.
     *
     * @return
     */
    public static PropertiesConfigurationHolder getInstance() {
        return ourInstance;
    }

    /**
     * Reads the configuration file and creates a new Configuration instance.
     *
     * @param propertiesFile the propeties files to use
     * @throws ConfigurationException
     */
    private PropertiesConfigurationHolder(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }
}