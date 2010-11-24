package com.compomics.sigpep;

import com.compomics.sigpep.model.Organism;

import java.util.Set;

/**
 * Creates SigPepSessions for a specified organism.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 17:57:23<br/>
 */
public interface SigPepSessionFactory {

    /**
     * Returns the organisms available in the SigPep database.
     *
     * @return a set of organisms
     */
    Set<Organism> getOrganisms();

    /**
     * Returns the organisms specified by the taxon ID.
     *
     * @param taxonId a taxon ID
     * @return an organism
     */
    Organism getOrganism(int taxonId);

    /**
     * Creates a SigPep session for an organism.
     *
     * @param organism the organism
     * @return a SigPep session
     */
    SigPepSession createSigPepSession(Organism organism);

}
