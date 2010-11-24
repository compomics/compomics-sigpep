package com.compomics.sigpep;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Provides access to the SigPep application configuration in the config/sigpep-app.properties file.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 17:13:44<br/>
 */
public class Configuration extends PropertiesConfiguration {

    /** the singleton instance  */
    private static Configuration ourInstance;

    /**
     * Get the configuration instance.
     *
     * @return the configuration singleton instance
     */
    public static Configuration getInstance() {

        //create the singleton instance if it doesn't exist yet
        if(ourInstance==null){

            try {
                ourInstance=new Configuration("config/sigpep-app.properties");
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }

        }
        return ourInstance;
    }

    /**
     * Constructs the singleton instance providing access to the properties
     * in the specified properties file.
     *
     * @param propertiesFile the properties file
     * @throws ConfigurationException thrown if an exception occurs while accessing the properties file
     */
    private Configuration(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }
}
