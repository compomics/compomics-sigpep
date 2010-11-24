package com.compomics.sigpep.model;

/**
 * An organism.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:30:10<br/>
 */
public interface Organism {

    /**
     * Returns the NCBI taxon ID of the species.
     *
     * @return the NCBI taxon ID
     */
    int getTaxonId();

    /**
     * Sets the NCBI taxon ID of the species.
     *
     * @param taxonId the NCBI taxon ID
     */
    void setTaxonId(int taxonId);

    /**
     * Returns the scientific name of the species.
     *
     * @return the species name
     */
    String getScientificName();

    /**
     * Sets the scientific name of the species.
     *
     * @param scientificName the scientific name
     */
    void setScientificName(String scientificName);


}
