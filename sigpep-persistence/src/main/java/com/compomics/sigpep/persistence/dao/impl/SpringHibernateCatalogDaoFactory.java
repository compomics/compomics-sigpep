package com.compomics.sigpep.persistence.dao.impl;

import org.hibernate.SessionFactory;
import com.compomics.sigpep.persistence.dao.CatalogDaoFactory;
import com.compomics.sigpep.persistence.dao.CatalogDao;
import com.compomics.sigpep.persistence.util.HibernateUtil;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 18-Jul-2008<br/>
 * Time: 10:28:12<br/>
 */
public class SpringHibernateCatalogDaoFactory extends CatalogDaoFactory {

    /**
     * @TODO: JavaDoc missing
     */
    public SpringHibernateCatalogDaoFactory() {
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @return
     */
    public CatalogDao createCatalogDao() {
        SpringHibernateCatalogDao retVal = new SpringHibernateCatalogDao();
        SessionFactory sessionFactory = HibernateUtil.getCatalogSessionFactory();
        retVal.setSessionFactory(sessionFactory);
        return retVal;
    }
}
