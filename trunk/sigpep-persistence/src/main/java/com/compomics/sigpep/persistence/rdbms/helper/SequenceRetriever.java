package com.compomics.sigpep.persistence.rdbms.helper;

import java.net.URL;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 30-Apr-2009<br/>
 * Time: 17:29:02<br/>
 */
public interface SequenceRetriever {

    /**
     * Fetches protein sequences from a database and
     * deposits them in FASTA format in the specified
     * destination file.
     *
     * @param organismScientificName the scientific name of the organism
     * @param organismNcbiTaxonID    the NCBI taxon ID of the organism
     * @param databaseVersion        the version of the database
     * @param destination            the target location
     * @return true if the sequences were fetched successfully, false otherwise
     * @throws RuntimeException if an exception occurs during sequence retrieval
     */
    boolean fetch(String organismScientificName, int organismNcbiTaxonID, String databaseVersion, URL destination) throws RuntimeException;
}
