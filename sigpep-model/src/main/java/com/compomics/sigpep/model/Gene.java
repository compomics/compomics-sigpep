package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A gene.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 17:51:02<br/>
 */
public interface Gene {

    /**
     * The organism whose genome contains the gene.
     *
     * @return the organism
     */
    Organism getOrganism();

    /**
     * Sets the organism whose genome contains the gene.
     *
     * @param organism the organism
     */
    void setOrganism(Organism organism);

    /**
     * Returns the primary database cross-reference of the gene.
     *
     * @return a database cross-reference
     */
    DbXref getPrimaryDbXref();


    /**
     * The the primary database cross-reference of the gene.
     *
     * @param primaryDbXref the database cross-reference
     */
    void setPrimaryDbXref(DbXref primaryDbXref);

    /**
     * Returns the proteins encoded by the gene.
     *
     * @return the proteins
     */
    Set<Protein> getProteins();

    /**
     * Sets the proteins encoded by the gene.
     *
     * @param proteins the proteins
     */
    void setProteins(Set<Protein> proteins);
}
