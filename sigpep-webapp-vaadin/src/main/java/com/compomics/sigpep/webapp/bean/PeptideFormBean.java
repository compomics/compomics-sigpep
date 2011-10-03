package com.compomics.sigpep.webapp.bean;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.model.Peptide;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:00
 * To change this template use File | Settings | File Templates.
 */
public class PeptideFormBean extends AbstractFormBean {

    /**
     * The peptide sequence requested by the user.
     */
    private String iPeptideSequence = "LGKLYWLVTQNVDALHTK";

    /**
     * The sigpep PeptideGenerator insance
     */
    private PeptideGenerator iPeptideGenerator = null;

    /**
     * The collection with backgroundpeptides used to calculate the transition redundancy
     */
    private Set<Peptide> iBackgroundPeptides = null;


    /**
     * The protein(s) to which the peptide maps.
     */
    private Set<String> iProteinAccessions = null;


    public String getPeptideSequence() {
        return iPeptideSequence;
    }

    public void setPeptideSequence(String aPeptideSequence) {
        iPeptideSequence = aPeptideSequence;
    }

    public PeptideGenerator getPeptideGenerator() {
        return iPeptideGenerator;
    }

    public void setPeptideGenerator(PeptideGenerator aPeptideGenerator) {
        iPeptideGenerator = aPeptideGenerator;
    }

    public Set<Peptide> getBackgroundPeptides() {
        return iBackgroundPeptides;
    }

    public void setBackgroundPeptides(Set<Peptide> aBackgroundPeptides) {
        iBackgroundPeptides = aBackgroundPeptides;
    }


    public void setProteinAccessions(Set<String> aProteinAccessions) {
        iProteinAccessions = aProteinAccessions;
    }

    public Set<String> getProteinAccessions() {
        return iProteinAccessions;
    }

}
