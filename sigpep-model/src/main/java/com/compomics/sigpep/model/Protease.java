package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A protease.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:07:14<br/>
 */
public interface Protease {

    /**
     * Returns the protease name.
     *
     * @return the name
     */
    String getShortName();

    /**
     * Sets the protease name.
     *
     * @param name the name
     */
    void setShortName(String name);

    /**
     * Returns the full protease name.
     *
     * @return the protease name
     */
    String getFullName();

    /**
     * Sets the full protease name.
     *
     * @param fullName the protease name
     */
    void setFullName(String fullName);

    /**
     * Returns the amino acids at which the protease cleaves a protein.
     *
     * @return the one letter amino acid codes of the cleavage sites
     */
    Set<String> getCleavageSites();

    /**
     * Sets the amino acids at which the protease cleaves a protein.
     *
     * @param cleavageSites the one letter amino acid codes of the cleavage sites
     */
    void setCleavageSites(Set<String> cleavageSites);

}
