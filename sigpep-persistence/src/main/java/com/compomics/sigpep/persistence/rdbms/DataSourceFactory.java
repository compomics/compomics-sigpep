package com.compomics.sigpep.persistence.rdbms;

import com.compomics.sigpep.persistence.config.Configuration;

import javax.sql.DataSource;

/**
 * Provides JDBC data sources.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 16:25:17<br/>
 */
public abstract class DataSourceFactory {

    /**
     * provides access to the persistence layer configuration in sigpep-persistence.properties
     */
    private static Configuration config = Configuration.getInstance();

    /**
     * the name of the data source factory implementation class
     */
    private static String dataSourceFactoryClass = config.getString("sigpep.db.datasource.factory.class");

    /**
     * the singleton instance
     */
    private static DataSourceFactory ourInstance;

    /**
     * Returns the data source factory singleton instance.
     *
     * @return the data source factory instance
     */
    public static DataSourceFactory getInstance() {

        //create a singleton instance if it hasn't been created yet
        if (ourInstance == null) {

            try {
                ourInstance = (DataSourceFactory) Class.forName(dataSourceFactoryClass).newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return ourInstance;
    }

    /**
     * Creates a JDBC data source for the organism identified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return a data source
     */
    public abstract DataSource createDataSource(int taxonId);

    /**
     * Creates a JDBC data source for the catalog schema.
     *
     * @return a data source
     */
    public abstract DataSource createCatalogDataSource();
}
