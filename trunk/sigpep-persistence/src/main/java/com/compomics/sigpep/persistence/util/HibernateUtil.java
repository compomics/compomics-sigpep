package com.compomics.sigpep.persistence.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import com.compomics.sigpep.persistence.config.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 17-Jan-2008<br/>
 * Time: 10:42:30<br/>
 */
public class HibernateUtil {


    private static Map<Integer, SessionFactory> sessionFactoryCache = new HashMap<Integer, SessionFactory>();

    private static JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();

    protected static final Logger logger = Logger.getLogger(HibernateUtil.class);

    static {
        //initialise catalog session factory when class is instantiated
        getCatalogSessionFactory();
    }


    /**
     * Returns a Hibernate session factory for the species specified by the taxon ID.
     *
     * @param taxonId the taxon ID
     * @return a Hibernate session factory
     */
    public static SessionFactory getSessionFactory(int taxonId) {

        Configuration config = Configuration.getInstance();

        SessionFactory retVal = null;

        //check if session factory for taxon ID is in cache
        if (sessionFactoryCache.containsKey(taxonId)) {
            retVal = sessionFactoryCache.get(taxonId);
        } else {

            //if not create a new one
            try {

                // Create the SessionFactory from hibernate.cfg.xml
                org.hibernate.cfg.Configuration hibernateConfig = new org.hibernate.cfg.Configuration();

                //check if a JNDI datasource is configured
                DataSource jndiDataSource;
                try {
                    jndiDataSource = jndiDataSourceLookup.getDataSource("" + taxonId);
                } catch (DataSourceLookupFailureException e) {
                    jndiDataSource = null;
                }

                if (jndiDataSource != null) {

                    logger.info("JNDI datasource configured: " + jndiDataSource);
                    hibernateConfig.setProperty("hibernate.connection.datasource", "java:comp/env/" + taxonId);

                    //if not use connection pool configured in Hibernate configuration
                } else {

                    logger.info("No JNDI datasource configured. Using connection properties in sigpep-persistence.properties file.");

                    hibernateConfig.setProperty("hibernate.connection.url", config.getString("sigpep.db.url") + "/" + config.getString("sigpep.db.schema." + taxonId));

                    hibernateConfig.setProperty("hibernate.connection.username", config.getString("sigpep.db.username"));
                    hibernateConfig.setProperty("hibernate.connection.password", config.getString("sigpep.db.password"));
                    hibernateConfig.setProperty("hibernate.connection.driver_class", config.getString("sigpep.db.driverClassName"));
                }

                //set Hibernate dialect
                hibernateConfig.setProperty("hibernate.dialect", config.getString("sigpep.db.hibernate.dialect"));
                hibernateConfig.configure();

                retVal = hibernateConfig.buildSessionFactory();
                sessionFactoryCache.put(taxonId, retVal);

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                logger.error("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        return retVal;
    }

    /**
     * Returns a Hibernate session factory for the catalog schema.
     *
     * @return a Hibernate session factory
     */
    public static SessionFactory getCatalogSessionFactory() {

        Configuration config = Configuration.getInstance();

        SessionFactory retVal = null;

        //check if session factory for taxon ID is in cache
        if (sessionFactoryCache.containsKey(0)) {
            retVal = sessionFactoryCache.get(0);
        } else {

            //check if a JNDI datasource is configured
            DataSource jndiDataSource;
            try {
                jndiDataSource = jndiDataSourceLookup.getDataSource("catalog");
                logger.info("JNDI datasource configured: " + jndiDataSource);
            } catch (DataSourceLookupFailureException e) {
                logger.info("No JNDI datasource configured. Using connection properties in sigpep-persistence.properties file.");
                jndiDataSource = null;
            }

            //if not create a new one
            try {

                // Create the SessionFactory from hibernate.cfg.xml
                org.hibernate.cfg.Configuration hibernateConfig = new org.hibernate.cfg.Configuration().configure("/catalog-hibernate.cfg.xml");

                if (jndiDataSource != null) {

                    hibernateConfig.setProperty("hibernate.connection.datasource", "java:comp/env/catalog");
                    //if not use connection pool configured in Hibernate configuration
                } else {

                    hibernateConfig.setProperty("hibernate.connection.url", config.getString("sigpep.db.url") + "/" + config.getString("sigpep.db.schema.catalog"));
                    hibernateConfig.setProperty("hibernate.connection.username", config.getString("sigpep.db.username"));
                    hibernateConfig.setProperty("hibernate.connection.password", config.getString("sigpep.db.password"));
                    hibernateConfig.setProperty("hibernate.connection.driver_class", config.getString("sigpep.db.driverClassName"));
                }

                //set Hibernate dialect
                hibernateConfig.setProperty("hibernate.dialect", config.getString("sigpep.db.hibernate.dialect"));

                retVal = hibernateConfig.buildSessionFactory();

                sessionFactoryCache.put(0, retVal);

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                logger.error("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        return retVal;
    }
}
