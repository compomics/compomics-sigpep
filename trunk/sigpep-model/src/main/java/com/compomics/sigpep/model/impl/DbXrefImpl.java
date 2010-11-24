package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.DbXref;

/**
 * Implementation of the DbXref interface.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 17-Jan-2008<br/>
 * Time: 10:53:48<br/>
 */
public class DbXrefImpl implements DbXref {

    /**
     * the database name
     */
    private String databaseName;

    /**
     * the database accession
     */
    private String accession;

    /**
     * the database identifier
     */
    private String identifier;

    /**
     * the entry version
     */
    private int version;

    /**
     * {@inherit}
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * {@inherit}
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    /**
     * {@inherit}
     */
    public String getAccession() {
        return accession;
    }

    /**
     * {@inherit}
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * {@inherit}
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * {@inherit}
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * {@inherit}
     */
    public int getVersion() {
        return version;
    }

    /**
     * {@inherit}
     */
    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * Checks for equality.
     *
     * @param o the object the check for equality
     * @return return true if the object is a DbXref object with identical database name, accession and version
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DbXref)) return false;

        DbXref dbXref = (DbXref) o;

        if (version != dbXref.getVersion()) return false;
        if (accession != null ? !accession.equals(dbXref.getAccession()) : dbXref.getAccession() != null) return false;
        if (databaseName != null ? !databaseName.equals(dbXref.getDatabaseName()) : dbXref.getDatabaseName() != null)
            return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        int result;
        result = (accession != null ? accession.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (databaseName != null ? databaseName.hashCode() : 0);
        return result;
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "DbXrefImpl{" +
                "databaseName='" + databaseName + '\'' +
                ", accession='" + accession + '\'' +
                ", version=" + version +
                '}';
    }
}
