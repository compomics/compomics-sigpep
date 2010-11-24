package com.compomics.sigpep.model;


/**
 * Implemented by object that can be persisted to a database.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 08-Feb-2008<br/>
 * Time: 16:11:06<br/>
 */
public interface Persistable {

    /**
     * Returns the database ID of a persistable object.
     *
     * @return the database ID
     */
    int getId();

    /**
     * Sets the database ID of a persistable object.
     *
     * @param id the database ID
     */
    void setId(int id);

    Object getSessionFactory();

    void setSessionFactory(Object sessionFactory);

}
