package com.compomics.sigpep.model.constants;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Singelton to access to the mono-isotopic amino acid masses in a properties file.
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 09-Oct-2007<br>
 * Time: 13:20:34<br>
 */
public class MonoAminoAcidMasses extends PropertiesConfiguration {

    /**
     * the singleton instance
     */
    private static MonoAminoAcidMasses ourInstance;

    /**
     * the logger
     */
    private static Logger logger = Logger.getLogger(MonoAminoAcidMasses.class);

    /**
     * Returns a singleton instance to access mono-isotopic amino acid masses.
     *
     * @return the singleton
     */
    public static MonoAminoAcidMasses getInstance() {
        if (ourInstance == null) {
            try {
                ourInstance = new MonoAminoAcidMasses("MonoAAMasses.properties");
            } catch (ConfigurationException e) {
                logger.error(e);
            }
        }
        return ourInstance;
    }

    /**
     * Constructs a new instance based on a properities file
     *
     * @param propertiesFile the properties file
     * @throws ConfigurationException if an exception occurs while accessing the properties file
     */
    private MonoAminoAcidMasses(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }
}
