package com.compomics.sigpep.persistence.rdbms.helper;

import java.net.URL;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-May-2009<br/>
 * Time: 18:33:35<br/>
 */
public interface ProteolyticDigest {

    /**
     * Digests a database of protein sequences in FASTA format and generates a
     * database of proteolytic peptide sequences in FASTA format.
     *
     * @param sequenceDatabaseInput the protein sequences to digest
     * @param digestDestination     the generated peptide sequences
     * @return true if the digest was successful
     */
    boolean digestSequenceDatabase(URL sequenceDatabaseInput, URL digestDestination);

    String getEnzyme();

    void setEnzyme(String enzyme);

    int getMissedCleavages();

    void setMissedCleavages(int missedCleavages);

    double getLowMass();

    void setLowMass(double lowMass);

    double getHighMass();

    void setHighMass(double highMass);
}
