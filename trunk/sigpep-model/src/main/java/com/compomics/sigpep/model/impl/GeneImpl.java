package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.Set;

/**
 * Implementation of the gene interface.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:32:25<br/>
 */
public class GeneImpl implements Gene, Persistable {

    private int id;
    private Object sessionFactory;
    /**
     * the organism
     */
    private Organism organism;
    /**
     * the primary database cross reference
     */
    private DbXref primaryDbXref;
    /**
     * the proteins encoded by this gene
     */
    private Set<Protein> proteins;

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
    public Organism getOrganism() {
        return organism;
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
    public Set<Protein> getProteins() {
        return proteins;
    }

    /**
     * {@inherit}
     */
    public void setOrganism(Organism organism) {
        this.organism = organism;
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
    public void setProteins(Set<Protein> proteins) {
        this.proteins = proteins;
    }

    /**
     * Checks for equality to another object. Two Genes are equal if their
     * primary <code>DbXref</coce>s are equal.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gene)) {
            return false;
        }

        Gene gene = (Gene) o;

        if (primaryDbXref != null ? !primaryDbXref.equals(gene.getPrimaryDbXref()) : gene.getPrimaryDbXref() != null) {
            return false;
        }

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
        return "GeneImpl{"
                + "primaryDbXref=" + primaryDbXref
                + '}';
    }
}
