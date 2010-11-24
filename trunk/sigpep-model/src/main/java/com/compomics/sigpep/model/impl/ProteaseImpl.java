package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.Persistable;

import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of Protease.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:56:46<br/>
 */
public class ProteaseImpl implements Protease, Persistable {

    private int id;
    private Object sessionFactory;

    /**
     * the protease shortName
     */
    private String shortName;

    private String fullName;

    private String cleaves;

    protected ProteaseImpl(){        
    }

    public ProteaseImpl(String name) {
        this.shortName = name;
    }

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
     * {@inherit}
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * {@inherit}
     */
    public void setShortName(String name) {
        this.shortName = name;
    }

    /**
     * Returns the full protease name.
     *
     * @return the protease name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full protease name.
     *
     * @param fullName the protease name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns a concateanation of the one letter amino acid codes of the cleavage sites.
     *
     * @return the cleavage sites
     */
    protected String getCleaves() {
        return cleaves;
    }

     /**
     * Sets the cleavage sites.
     *
     * @param cleaves a concatanation of the one letter amino acid codes of the cleavage sites
     */
    protected void setCleaves(String cleaves) {
        this.cleaves = cleaves;
    }

    /**
     * Returns the amino acids at which the protease cleaves a protein.
     *
     * @return the one letter amino acid codes of the cleavage sites
     */
    public Set<String> getCleavageSites() {
        Set<String> retVal = new TreeSet<String>();
        for(char aa : this.cleaves.toCharArray()){
            retVal.add("" + aa);
        }
        return retVal;
    }

    /**
     * Sets the amino acids at which the protease cleaves a protein.
     *
     * @param cleavageSites the one letter amino acid codes of the cleavage sites
     */
    public void setCleavageSites(Set<String> cleavageSites) {
        StringBuilder sb = new StringBuilder();
        for(String aa : cleavageSites){
            sb.append(aa);
        }
        this.cleaves=sb.toString();
    }

    /**
     * Checks for equality to another object. Two Proteases are equal if they have the same shortName.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProteaseImpl)) return false;

        Protease protease = (Protease) o;

        if (!shortName.equals(protease.getShortName())) return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        return shortName.hashCode();
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "ProteaseImpl{" +
                "shortName='" + shortName + '\'' +
                '}';
    }
}
