package com.compomics.sigpep.persistence.dao;

import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.persistence.config.Configuration;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 18-Jul-2008<br/>
 * Time: 10:24:17<br/>
 */
public abstract class CatalogDaoFactory {

    /**
     * provides access to the persistence layer configuration
     */
    private static Configuration config = Configuration.getInstance();

    /**
     * the name of the catalog DAO implementation class
     */
    private static String catalogDaoClass = config.getString("sigpep.db.catalog.dao.factory.class");

    /**
     * the singleton instance of the catalog DAO factory
     */
    private static CatalogDaoFactory ourInstance;

    /**
     * Returns the singleton instance of the catalog DAO factory.
     *
     * @return the catalog DAO factory
     */
    public static CatalogDaoFactory getInstance() {

        //create singleton instance if it does not exist yet
        if (ourInstance == null) {

            try {
                ourInstance = (CatalogDaoFactory) Class.forName(catalogDaoClass).newInstance();
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
     * Returns a catalog DAO
     *
     * @return the catalog DAO
     */
    public abstract CatalogDao createCatalogDao();


    /**
     * The main method. Used for testing purposes.
     *
     * @param args
     */
    public static void main(String[] args) {
        CatalogDao dao = CatalogDaoFactory.getInstance().createCatalogDao();
        for (Organism o : dao.getOrganisms()) {
            System.out.println(o);
        }
    }
}
