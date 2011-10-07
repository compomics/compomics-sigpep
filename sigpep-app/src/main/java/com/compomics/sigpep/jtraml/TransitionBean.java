package com.compomics.sigpep.jtraml;

import java.util.Set;

/**
 * This class is a
 */
public abstract class TransitionBean {

    /**
     * The peptide sequence of the precursor ion
     */
    private String iPeptideSequence = null;

    /**
     * The accession(s) of the parent proteins
     */
    private Set<String> iProteinAccessions = null;

    /**
     * The ionnumber of the transition
     */
    private int iIonNumber = 0;

    /**
     * The iontype of the transition
     */
    private char[] iIonType = "na".toCharArray();


    /**
     * The charge of the transition
     */
    private int iIonCharge = 0;

    /**
     * The precursor ion mass
     */
    private double iQ1Mass = 0.0;
    /**
     * The product ion mass
     */
    private double iQ3Mass = 0.0;
    /**
     * The identifier of this transition
     */
    private String iID = "";

    /**
     * Sets the iQ3Mass.
     *
     * @param aQ3Mass The iQ3Mass value to set.
     */
    public void setQ3Mass(double aQ3Mass) {
        this.iQ3Mass = aQ3Mass;
    }

    /**
     * Gets the iID.
     *
     * @return The iID value.
     */
    public String getID() {
        return iID;
    }

    /**
     * Sets the iID.
     *
     * @param aID The iID value to set.
     */
    public void setID(String aID) {
        this.iID = aID;
    }

    /**
     * Sets the iQ1Mass.
     *
     * @param aQ1Mass The iQ1Mass value to set.
     */
    public void setQ1Mass(double aQ1Mass) {
        this.iQ1Mass = aQ1Mass;
    }

    /**
     * Gets the iQ3Mass.
     *
     * @return The iQ3Mass value.
     */
    public double getQ3Mass() {
        return iQ3Mass;
    }

    /**
     * Gets the iQ1Mass.
     *
     * @return The iQ1Mass value.
     */
    public double getQ1Mass() {
        return iQ1Mass;
    }





    public abstract String[] getSeparatedOrder();

    public abstract String asCSVLine();

    /**
     * Sets the iPeptideSequence.
     *
     * @param aPeptideSequence The iPeptideSequence value to set.
     */
    public void setPeptideSequence(String aPeptideSequence) {
        this.iPeptideSequence = aPeptideSequence;
    }

    /**
     * Gets the iIonType.
     *
     * @return The iIonType value.
     */
    public char[] getIonType() {
        return iIonType;
    }

    /**
     * Sets the iIonCharge.
     *
     * @param aIonCharge The iIonCharge value to set.
     */
    public void setIonCharge(int aIonCharge) {
        this.iIonCharge = aIonCharge;
    }

    /**
     * Sets the iIonNumber.
     *
     * @param aIonNumber The iIonNumber value to set.
     */
    public void setIonNumber(int aIonNumber) {
        this.iIonNumber = aIonNumber;
    }

    /**
     * Sets the iProteinAccessions.
     *
     * @param aProteinAccessions The iProteinAccessions value to set.
     */
    public void setProteinAccessions(Set<String> aProteinAccessions) {
        this.iProteinAccessions = aProteinAccessions;
    }

    /**
     * Gets the iProteinAccessions.
     *
     * @return The iProteinAccessions value.
     */
    public Set<String> getProteinAccessions() {
        return iProteinAccessions;
    }

    /**
     * Gets the iIonCharge.
     *
     * @return The iIonCharge value.
     */
    public int getIonCharge() {
        return iIonCharge;
    }

    /**
     * Gets the iPeptideSequence.
     *
     * @return The iPeptideSequence value.
     */
    public String getPeptideSequence() {
        return iPeptideSequence;
    }

    /**
     * Gets the iIonNumber.
     *
     * @return The iIonNumber value.
     */
    public int getIonNumber() {
        return iIonNumber;
    }


    /**
     * Sets the iIonType.
     *
     * @param aIonType The iIonType value to set.
     */
    public void setIonType(char[] aIonType) {
        this.iIonType = aIonType;
    }
}
