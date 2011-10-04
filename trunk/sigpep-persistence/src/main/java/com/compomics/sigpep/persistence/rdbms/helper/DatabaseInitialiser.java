package com.compomics.sigpep.persistence.rdbms.helper;

import java.util.Map;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 30-Apr-2009<br/>
 * Time: 17:38:16<br/>
 */
public interface DatabaseInitialiser {

    /**
     * Initialises the SigPep database by setting up the catalog schema.
     *
     * @return true if the database has been initialised successfully, false if not.
     */
    boolean initialise();

    /**
     * Tests if the SigPep database has been initialised already.
     *
     * @return true if the database has been initialised, false if not.
     */
    boolean isInitialised();

    /**
     * Sets the username for database access.
     *
     * @param adminUsername username of user with administration privileges
     */
    void setAdminUsername(String adminUsername);

    /**
     * Sets the password for database access.
     *
     * @param adminPassword password of user with administration privileges
     */
    void setAdminPassword(String adminPassword);

    /**
     * Returns a map of organisms found in the catalog schema
     *
     * @return the map (key=ncbi_taxon_id, value=organism_name)
     */
    Map<Integer, String>
    getOrganismMap();
}
