package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A protein is associated with exactly one gene and has exactly one sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 17:50:30<br/>
 */
public interface Protein {

    /**
     * Returns the gene the protein is encoded by.
     *
     * @return the gene
     */
    Gene getGene();

    /**
     * Sets the gene the protein is encoded by.
     *
     * @param gene the gene
     */
    void setGene(Gene gene);

    /**
     * Returns the primary database cross-reference of the protein.
     *
     * @return the primary database cross-reference
     */
    DbXref getPrimaryDbXref();

    /**
     * Sets the primary database cross-reference of the protein.
     *
     * @param dbXref the primary database cross-reference
     */
    void setPrimaryDbXref(DbXref dbXref);

    /**
     * Returns the protein sequence.
     *
     * @return the protein sequence
     */
    ProteinSequence getSequence();

    /**
     * Sets the protein sequence.
     *
     * @param sequence the sequence
     */
    void setSequence(ProteinSequence sequence);

    

}
