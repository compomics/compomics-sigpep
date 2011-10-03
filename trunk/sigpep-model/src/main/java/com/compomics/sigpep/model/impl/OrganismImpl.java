package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.model.Persistable;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:39:46<br/>
 */
public class OrganismImpl implements Organism, Persistable {

    private int id;
    private Object sessionFactory;

    /**
     * the NCBI taxon ID
     */
    private int taxonId;

    /**
     * the scientific species name
     */
    private String scientificName;

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
     * {@inherit}
     */
    public int getTaxonId() {
        return taxonId;
    }

    /**
     * {@inherit}
     */
    public void setTaxonId(int ncbiTaxonId) {
        this.taxonId = ncbiTaxonId;
    }

    /**
     * {@inherit}
     */
    public String getScientificName() {
        return scientificName;
    }

    /**
     * {@inherit}
     */
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    /**
     * Checks for equality to another object. Two <code>Organisms</code>s are equal if their
     * NCBI taxon IDs are equal.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organism)) return false;

        Organism organism = (Organism) o;

        if (taxonId != organism.getTaxonId()) return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        return taxonId;
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "OrganismImpl{" +
                "ncbiTaxonId=" + taxonId +
                ", scientificName='" + scientificName + '\'' +
                '}';
    }
}
