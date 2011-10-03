package com.compomics.sigpep.persistence.dao.impl;

import com.compomics.sigpep.persistence.dao.SimpleQueryDaoFactory;
import com.compomics.sigpep.persistence.dao.SimpleQueryDao;
import com.compomics.sigpep.persistence.rdbms.DataSourceFactory;

import javax.sql.DataSource;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jun-2008<br/>
 * Time: 19:34:52<br/>
 */
public class SpringJdbcSimpleQueryDaoFactory extends SimpleQueryDaoFactory {

    /**
     * Creates a new SpringJdbcSimpleQueryDaoFactory.
     */
    public SpringJdbcSimpleQueryDaoFactory() {
    }

    /**
     * Creates a new SimpleQueryDao from the given taxonomy id.
     *
     * @param taxonId the taxonomy id to use.
     * @return the SpringJdbcSimpleQueryDao
     */
    public SimpleQueryDao createSimpleQueryDao(int taxonId) {
        DataSource dataSource = DataSourceFactory.getInstance().createDataSource(taxonId);
        return new SpringJdbcSimpleQueryDao(dataSource);
    }
}
