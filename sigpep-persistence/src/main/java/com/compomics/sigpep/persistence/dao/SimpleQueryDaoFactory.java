package com.compomics.sigpep.persistence.dao;

import com.compomics.sigpep.persistence.config.Configuration;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 03-Jun-2008<br/>
 * Time: 09:36:30<br/>
 */
public abstract class SimpleQueryDaoFactory {

    private static Configuration config = Configuration.getInstance();
    private static String simpleQueryDaoClass = config.getString("sigpep.db.simple.query.dao.factory.class");
    private static SimpleQueryDaoFactory ourInstance;

    /**
     * Creates a SimpleQueryDaoFactory instance.
     *
     * @return the SimpleQueryDaoFactory instance
     */
    public static SimpleQueryDaoFactory getInstance() {

        if (ourInstance == null) {

            try {
                ourInstance = (SimpleQueryDaoFactory)Class.forName(simpleQueryDaoClass).newInstance();
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
     * Creates a SimpleQueryDao from a taxonomy id.
     *
     * @param taxonId the taxonomy id to create the SimpleQueryDao from.
     * @return the SimpleQueryDao
     */
    public abstract SimpleQueryDao createSimpleQueryDao(int taxonId);
}
