package com.compomics.sigpep.persistence.rdbms.impl;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import com.compomics.sigpep.persistence.config.Configuration;
import com.compomics.sigpep.persistence.rdbms.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Provides Apache commons DBCP connection pool data sources.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 15:32:22<br/>
 */
public class DbcpDataSourceFactory extends DataSourceFactory {

    /** provides access to the persistence layer configuration in sigpep-persistence.properties */
    private static Configuration config = Configuration.getInstance();

    /**
     * Constructs a data source factory provides ODCP data sources.
     */
    public DbcpDataSourceFactory()  {
    }

    /**
     * Creates a JDBC data source for the organism identified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return a data source
     */
    public DataSource createDataSource(int taxonId){

        Properties properties = new Properties();
        properties.setProperty("username", config.getString("sigpep.db.username"));
        properties.setProperty("password", config.getString("sigpep.db.password"));
        properties.setProperty("url", config.getString("sigpep.db.url") + "/" + config.getString("sigpep.db.schema." + taxonId));
        properties.setProperty("driverClassName", config.getString("sigpep.db.driverClassName"));

        try {
            return BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("Exception while creating data source.", e);
        }
    }

    public DataSource createCatalogDataSource(){

        Properties properties = new Properties();
        properties.setProperty("username", config.getString("sigpep.db.username"));
        properties.setProperty("password", config.getString("sigpep.db.password"));
        properties.setProperty("url", config.getString("sigpep.db.url") + "/" + config.getString("sigpep.db.schema.catalog"));
        properties.setProperty("driverClassName", config.getString("sigpep.db.driverClassName"));

        try {
            return BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("Exception while creating data source.", e);
        }
    }
}
