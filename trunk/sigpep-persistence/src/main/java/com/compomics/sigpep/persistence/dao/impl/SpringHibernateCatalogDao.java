package com.compomics.sigpep.persistence.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.persistence.dao.CatalogDao;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 18-Jul-2008<br/>
 * Time: 10:20:57<br/>
 */
public class SpringHibernateCatalogDao extends HibernateDaoSupport implements CatalogDao {

    /**
     * Constructs a Catalog DAO (for use with the Spring Bean Factory).
     * The Hibernate session factory must be set via the respective setter.
     */
    public SpringHibernateCatalogDao() {
    }

    /**
     * Constructs a CatalogDao that uses the session factory passed as a parameter
     * to open Hibernate sessions.
     *
     * @param sessionFactory the Hibernate session factory
     */
    public SpringHibernateCatalogDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }


    /**
     * Returns the organisms available in the SigPep database.
     *
     * @return a set of organisms
     */
    public Set<Organism> getOrganisms() {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("organism");
        Set<Organism> result = new HashSet<Organism>();
        result.addAll(query.list());
        session.getTransaction().commit();
        session.close();        
        return result;

    }

    /**
     * Returns the organism identified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return an organism object, null if the organism is not available in the SigPep database
     */
    public Organism getOrganismByTaxonId(int taxonId) {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("organismByTaxonId").setParameter("taxonId", taxonId);
        Organism retVal = (Organism)query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retVal;

    }
    
}
