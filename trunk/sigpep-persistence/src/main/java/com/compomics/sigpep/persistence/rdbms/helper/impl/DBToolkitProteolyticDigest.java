package com.compomics.sigpep.persistence.rdbms.helper.impl;

import be.proteomics.dbtoolkit.toolkit.EnzymeDigest;
import org.apache.log4j.Logger;
import com.compomics.sigpep.persistence.rdbms.helper.ProteolyticDigest;

import java.net.URL;
import java.util.Arrays;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-May-2009<br/>
 * Time: 18:40:19<br/>
 */
public class DBToolkitProteolyticDigest implements ProteolyticDigest {

    /**
     * the log4j logger
     */
    private static Logger logger = Logger.getLogger(DBToolkitProteolyticDigest.class);

    private String enzyme = null;

    private int missedCleavages = 0;

    private double lowMass = 600;

    private double highMass = 4000;

    /**
     * Default constructor.
     */
    public DBToolkitProteolyticDigest() {
    }

    /**
     * @param enzyme
     * @param missedCleavages
     * @TODO: JavaDoc missing
     */
    public DBToolkitProteolyticDigest(String enzyme, int missedCleavages) {
        this.enzyme = enzyme;
        this.missedCleavages = missedCleavages;
    }

    /**
     * @param enzyme
     * @param missedCleavages
     * @param lowMass
     * @param highMass
     * @TODO: JavaDoc missing
     */
    public DBToolkitProteolyticDigest(String enzyme, int missedCleavages, int lowMass, int highMass) {
        this.enzyme = enzyme;
        this.missedCleavages = missedCleavages;
        this.lowMass = lowMass;
        this.highMass = highMass;
    }

    /**
     * Digests a database of protein sequences in FASTA format and generates a
     * database of proteolytic peptide sequences in FASTA format.
     *
     * @param sequenceDatabaseInput the protein sequences to digest
     * @param digestDestination     the generated peptide sequences
     * @return true if the digest was successful
     */
    public boolean digestSequenceDatabase(URL sequenceDatabaseInput, URL digestDestination) {

        if (enzyme == null) {
            throw new RuntimeException("Exception while digesting proteins in file " + sequenceDatabaseInput.getPath() + ". No enzyme set.");
        }

        String[] args = new String[9];
        args[0] = "--enzyme";
        args[1] = enzyme;
        args[2] = "--lowMass";
        args[3] = "" + lowMass;
        args[4] = "--highMass";
        args[5] = "" + highMass;
        args[6] = "--input";
        args[7] = sequenceDatabaseInput.getPath();
        args[8] = digestDestination.getPath();

        System.out.println(Arrays.toString(args));
        EnzymeDigest.main(args);

        //"Usage:\n\tEnzymeDigest
        // [--enzymeFile <custom_enzyme_file>]
        // [--enzyme <enzymeName> [--mc <number_of_missed_cleavages>]]
        // [--filter <filter_name> [--filterParam \"<filter_parameter>\"]]
        // [--lowMass <lower_mass_treshold>
        // --highMass <higher_mass_treshold>]
        // --input <input_db_name>
        // <output_db_name>\n\n\tNote that an existing output file will be silently overwritten!"

        return false;
    }

    public String getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    public int getMissedCleavages() {
        return missedCleavages;
    }

    public void setMissedCleavages(int missedCleavages) {
        this.missedCleavages = missedCleavages;
    }

    public double getLowMass() {
        return lowMass;
    }

    public void setLowMass(double lowMass) {
        this.lowMass = lowMass;
    }

    public double getHighMass() {
        return highMass;
    }

    public void setHighMass(double highMass) {
        this.highMass = highMass;
    }
}
