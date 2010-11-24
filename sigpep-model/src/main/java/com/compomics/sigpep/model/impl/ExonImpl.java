package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Feb-2008<br/>
 * Time: 14:21:11<br/>
 */
public class ExonImpl implements Exon, Persistable {

    private int id;    

    private Object sessionFactory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(Object sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * the source database cross reference
     */
    private DbXref primaryDbXref;


    public void setPrimaryDbXref(DbXref dbXref) {
        this.primaryDbXref =dbXref;
    }

    public DbXref getPrimaryDbXref() {
        return primaryDbXref;
    }



    /**
     * Checks for equality to another object. Two Genes are equal if their
     * primary <code>DbXref</coce>s are equal.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exon)) return false;

        Exon gene = (Exon) o;

        if (primaryDbXref != null ? !primaryDbXref.equals(gene.getPrimaryDbXref()) : gene.getPrimaryDbXref() != null)
            return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        return (primaryDbXref != null ? primaryDbXref.hashCode() : 0);
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "ExonImpl{" +
                "primaryDbXref=" + primaryDbXref +
                '}';
    }
}
