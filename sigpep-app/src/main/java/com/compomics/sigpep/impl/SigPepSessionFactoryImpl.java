package com.compomics.sigpep.impl;

import com.opensymphony.oscache.base.Cache;
import com.compomics.sigpep.CacheFactory;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.persistence.dao.*;

import java.util.Set;


/**
 * Creates SigPepSessions for a specified organism.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 15:10:29<br/>
 */
public class SigPepSessionFactoryImpl implements SigPepSessionFactory {

    /**
     * Field cacheFactory
     */
    private CacheFactory cacheFactory;
    /**
     * Field catalogDaoFactory
     */
    private CatalogDaoFactory catalogDaoFactory;
    /**
     * Field objectDaoFactory
     */
    private ObjectDaoFactory objectDaoFactory;
    /**
     * Field simpleQueryDaoFactory
     */
    private SimpleQueryDaoFactory simpleQueryDaoFactory;

    /**
     * Constructs a SigPepSessionFactory with the DAO factory implementations
     * defined in the sigpep-persistence.properties file
     */
    public SigPepSessionFactoryImpl() {
        this(SimpleQueryDaoFactory.getInstance(),
                ObjectDaoFactory.getInstance(),
                CatalogDaoFactory.getInstance());
    }

    /**
     * Constructs a SigPepSessionFactory with the DAO factory implementations
     * passed as arguments and a cache factory specified in the
     * sigpep-persistence.properties file.
     *
     * @param simpleQueryDaoFactory the factory for the simple SQL query DAO
     * @param objectDaoFactory      the factory for the object-relational DAO
     * @param catalogDaoFactory     the factory for the SigPep catalog DAO
     */
    public SigPepSessionFactoryImpl(SimpleQueryDaoFactory simpleQueryDaoFactory,
                                    ObjectDaoFactory objectDaoFactory,
                                    CatalogDaoFactory catalogDaoFactory) {
        this.objectDaoFactory = objectDaoFactory;
        this.simpleQueryDaoFactory = simpleQueryDaoFactory;
        this.catalogDaoFactory = catalogDaoFactory;
        this.cacheFactory = CacheFactory.getInstance();
    }

    /**
     * Returns a SigPep session for an organism specified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return the SigPep session
     */
    public SigPepSession createSigPepSession(int taxonId) {

        //create DAOs
        ObjectDao objectDao = objectDaoFactory.createObjectDao(taxonId);
        SimpleQueryDao simpleQueryDao = simpleQueryDaoFactory.createSimpleQueryDao(taxonId);
        //Cache sessionCache = cacheFactory.createCache();

        //return session
        return new SigPepSessionNoCacheImpl(simpleQueryDao, objectDao, null);
    }

    /**
     * Creates a SigPep session for an organism.
     *
     * @param organism the organism
     * @return a SigPep session
     */
    public SigPepSession createSigPepSession(Organism organism) {
        return this.createSigPepSession(organism.getTaxonId());
    }

    /**
     * Returns the organisms available in the SigPep database.
     *
     * @return a set of organisms
     */
    public Set<Organism> getOrganisms() {
        return catalogDaoFactory.createCatalogDao().getOrganisms();
    }

    /**
     * Returns the organisms specified by the taxon ID.
     *
     * @param taxonId a taxon ID
     * @return an organism
     */
    public Organism getOrganism(int taxonId) {
        return catalogDaoFactory.createCatalogDao().getOrganismByTaxonId(taxonId);
    }

    /**
     * Returns the object DAO factory of the SigPep session factory.
     *
     * @return the object DAO
     */
    public ObjectDaoFactory getObjectDaoFactory() {
        return objectDaoFactory;
    }

    /**
     * Sets the objcet DAO factory of the SigPep session factory.
     *
     * @param objectDaoFactory the object DAO factory
     */
    public void setObjectDaoFactory(ObjectDaoFactory objectDaoFactory) {
        this.objectDaoFactory = objectDaoFactory;
    }

    /**
     * Returns the simple query DAO factory of the SigPep session factory.
     *
     * @return the simple query DAO factory
     */
    public SimpleQueryDaoFactory getSimpleQueryDaoFactory() {
        return simpleQueryDaoFactory;
    }

    /**
     * Sets the simple query DAO factory of the SigPep session factory.
     *
     * @param simpleQueryDaoFactory the simple query DAO factory
     */
    public void setSimpleQueryDaoFactory(SimpleQueryDaoFactory simpleQueryDaoFactory) {
        this.simpleQueryDaoFactory = simpleQueryDaoFactory;
    }

    /**
     * Returns the cache factory of the SigPep session factory.
     *
     * @return the cache factory
     */
    public CacheFactory getCacheFactory() {
        return cacheFactory;
    }

    /**
     * Sets the cache factory of the SigPep sesssion factory.
     *
     * @param cacheFactory the cache factory
     */
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    /**
     * Returns the catalog DAO factory of the SigPep session factory.
     *
     * @return the catalog DAO factory
     */
    public CatalogDaoFactory getCatalogDaoFactory() {
        return catalogDaoFactory;
    }

    /**
     * Sets the catalog DAO factory of the SigPep session factory.
     *
     * @param catalogDaoFactory the catalog DAO factory
     */
    public void setCatalogDaoFactory(CatalogDaoFactory catalogDaoFactory) {
        this.catalogDaoFactory = catalogDaoFactory;
    }
}
