package com.compomics.sigpep.persistence.rdbms.impl;

import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import javax.sql.DataSource;
import com.compomics.sigpep.persistence.rdbms.DataSourceFactory;

/**
 * Provides JNDI data sources. Uses the Spring JNDI data source lookup
 * to look up data sources defined in the web application context.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 16:52:41<br/>
 */
public class SpringContextJndiDataSourceFactory extends DataSourceFactory {

    /** the Spring JNDI data source lookup  */
    JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();

    /**
     * Constructs a JNDI data source factory
     */
    public SpringContextJndiDataSourceFactory(){
    }

    /**
     * Creates a JDBC data source for the organism identified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return a data source
     */
    public DataSource createDataSource(int taxonId) {
        return jndiDataSourceLookup.getDataSource("jdbc/" + taxonId);
    }

    /**
     * Creates a JDBC data source for the catalog schema.
     *
     * @return a data source
     */
    public DataSource createCatalogDataSource() {
        return jndiDataSourceLookup.getDataSource("jdbc/catalog");
    }
}
