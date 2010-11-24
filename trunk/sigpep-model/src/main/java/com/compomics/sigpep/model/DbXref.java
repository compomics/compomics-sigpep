package com.compomics.sigpep.model;

/**
 * A database cross-reference.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:24:31<br/>
 */
public interface DbXref {

    /**
     * Returns the database the cross-reference comes from.
     *
     * @return the database name
     */
    String getDatabaseName();

    /**
     * Sets the database name the cross-reference comes from.
     *
     * @param databaseName the database name
     */
    void setDatabaseName(String databaseName);

    /**
     * Returns the database identifier.
     *
     * @return the identifier
     */
    String getIdentifier();

    /**
     * Sets the database entry identifier.
     *
     * @param identifier the identifier
     */
    void setIdentifier(String identifier);

    /**
     * Returns the database entry accession.
     *
     * @return the accession
     */
    String getAccession();

    /**
     * Sets the database entry accession.
     *
     * @param accession the accession
     */
    void setAccession(String accession);

    /**
     * Returns the database entry version.
     *
     * @return the version
     */
    int getVersion();

    /**
     * Sets the database entry version.
     *
     * @param version the version
     */
    void setVersion(int version);

}
