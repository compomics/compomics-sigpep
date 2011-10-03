package com.compomics.sigpep.persistence.dao;

import com.compomics.sigpep.model.Organism;

import java.util.Set;

/**
 * Provides access to the organisms available in the SigPep database.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 18-Jul-2008<br/>
 * Time: 10:17:16<br/>
 */
public interface CatalogDao {

    /**
     * Returns the organisms available in the SigPep database.
     *
     * @return a set of organisms
     */
    Set<Organism> getOrganisms();

    /**
     * Returns the organism identified by the NCBI taxon ID.
     *
     * @param taxonId the NCBI taxon ID
     * @return an organism object, null if the organism is not available in the SigPep database
     */
    Organism getOrganismByTaxonId(int taxonId);

}
