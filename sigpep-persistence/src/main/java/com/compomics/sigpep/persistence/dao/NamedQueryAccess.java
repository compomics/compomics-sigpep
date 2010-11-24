package com.compomics.sigpep.persistence.dao;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;


/**
 * Singelton to access named SQL queries.
 *
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 25-Sep-2007<br>
 * Time: 09:44:45<br>
 */
public class NamedQueryAccess extends XMLConfiguration {

    private static NamedQueryAccess ourInstance;
    private static Logger logger = Logger.getLogger(NamedQueryAccess.class);

    public static NamedQueryAccess getInstance() {
        if(ourInstance==null){
            try {
                ourInstance=new NamedQueryAccess("namedSqlQueries.xml");
            } catch (ConfigurationException e) {
                logger.error(e);
            }
        }
        return ourInstance;
    }

    private NamedQueryAccess(String propertiesFile) throws ConfigurationException {
        super(propertiesFile);
    }

}
