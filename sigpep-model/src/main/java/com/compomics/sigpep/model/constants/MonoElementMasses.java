package com.compomics.sigpep.model.constants;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 09-Oct-2007<br>
 * Time: 13:20:34<br>
 */
public class MonoElementMasses extends PropertiesConfiguration {

    /**
     * the singleton instance
     */
    private static MonoElementMasses ourInstance;

    /**
     * the logger
     */
    private static Logger logger = Logger.getLogger(MonoElementMasses.class);

    /**
     * Returns a singleton instance to access mono-isotopic element masses.
     *
     * @return the singleton
     */
    public static MonoElementMasses getInstance() {
        if (ourInstance == null) {
            try {
                ourInstance = new MonoElementMasses("MonoElementMasses.properties");

            } catch (ConfigurationException e) {
                logger.error(e);
            }
        }
        return ourInstance;
    }

    /**
     * Constructs a new instance based on a properities file.
     *
     * @param propertiesFile the properties file
     * @throws ConfigurationException if an exception occurs while accessing the properties file
     */
    private MonoElementMasses(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }
}
