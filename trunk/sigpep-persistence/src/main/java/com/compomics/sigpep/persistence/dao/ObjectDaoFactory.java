package com.compomics.sigpep.persistence.dao;

import com.compomics.sigpep.persistence.config.Configuration;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 03-Jun-2008<br/>
 * Time: 09:37:11<br/>
 */
public abstract class ObjectDaoFactory {

    private static Configuration config = Configuration.getInstance();
    private static String objectDaoClass = config.getString("sigpep.db.object.dao.factory.class");
    private static ObjectDaoFactory ourInstance;

    public static ObjectDaoFactory getInstance() {

        if (ourInstance == null) {

            try {
                ourInstance = (ObjectDaoFactory)Class.forName(objectDaoClass).newInstance();
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

    public abstract ObjectDao createObjectDao(int taxonId);


}
