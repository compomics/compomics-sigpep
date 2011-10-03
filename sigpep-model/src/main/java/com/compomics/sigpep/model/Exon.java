package com.compomics.sigpep.model;

/**
 * An exon.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Feb-2008<br/>
 * Time: 14:19:57<br/>
 */
public interface Exon {

    /**
     * Sets the primary database cross-reference of the exon.
     *
     * @param dbXref database cross-reference
     */
    void setPrimaryDbXref(DbXref dbXref);

    /**
     * Returns the primary database cross-reference of the exon.
     *
     * @return a database cross-reference
     */
    DbXref getPrimaryDbXref();

}
