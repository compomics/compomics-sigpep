package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

/**
 * Implementation of Protein.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:57:57<br/>
 */
public class ProteinImpl implements Protein, Persistable {

    private int id;
    private Organism organism;
    private Object sessionFactory;
    /**
     * the gene that encodes the protein
     */
    private Gene gene;
    /**
     * the primary database cross-reference of the protein
     */
    private DbXref primaryDbXref;
    /**
     * the protein sequence
     */
    private ProteinSequence proteinSequence;

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     * @TODO: JavaDoc missing
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Object getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory
     * @TODO: JavaDoc missing
     */
    public void setSessionFactory(Object sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Organism getOrganism() {
        return organism;
    }

    /**
     * @param organism
     * @TODO: JavaDoc missing
     */
    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    /**
     * {@inherit}
     */
    public Gene getGene() {
        return gene;
    }

    /**
     * {@inherit}
     */
    public void setGene(Gene gene) {
        this.gene = gene;
    }

    /**
     * {@inherit}
     */
    public DbXref getPrimaryDbXref() {
        return primaryDbXref;
    }

    /**
     * {@inherit}
     */
    public void setPrimaryDbXref(DbXref primaryDbXref) {
        this.primaryDbXref = primaryDbXref;
    }

    /**
     * {@inherit}
     */
    public ProteinSequence getSequence() {
        return proteinSequence;
    }

    /**
     * {@inherit}
     */
    public void setSequence(ProteinSequence proteinSequence) {
        this.proteinSequence = proteinSequence;
    }

    /**
     * Checks for equality to another object. Two proteins are equal if they have the same
     * primary database cross-reference.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Protein)) {
            return false;
        }

        Protein protein = (Protein) o;

        if (!primaryDbXref.equals(protein.getPrimaryDbXref())) {
            return false;
        }

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        return primaryDbXref.hashCode();
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "ProteinImpl{"
                + "primaryDbXref=" + primaryDbXref
                + '}';
    }
}
