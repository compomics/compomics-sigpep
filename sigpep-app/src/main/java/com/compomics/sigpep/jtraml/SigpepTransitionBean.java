package com.compomics.sigpep.jtraml;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;

/**
 * This class is a Bean representation of a single transition in the .csv file format of Thermo instruments
 */
public class SigpepTransitionBean extends TransitionBean {
    private static Logger logger = Logger.getLogger(SigpepTransitionBean.class);

    /**
     * Empty constructor
     */
    public SigpepTransitionBean() {
    }

    /**
     * Create a new Bean from the specified Strings.
     *
     * @param aQ1Mass
     * @param aQ3Mass
     * @param aStartTime
     * @param aEndTime
     * @param aPolarity
     * @param aTrigger
     * @param aReactionCategory
     * @param aID
     */
    public SigpepTransitionBean(String aQ1Mass, String aQ3Mass, String aCollisionEnergy, String aStartTime, String aEndTime, String aPolarity, String aTrigger, String aReactionCategory, String aID) {
        setQ1Mass(Double.parseDouble(aQ1Mass));
        setQ3Mass(Double.parseDouble(aQ3Mass));
        setCollisionEnergy(Double.parseDouble(aCollisionEnergy));
        setStartTime(Double.parseDouble(aStartTime));
        setEndTime(Double.parseDouble(aEndTime));
        setPolarity(aPolarity);
        setTrigger(aTrigger);
        setReactionCategory(aReactionCategory);
        setID(aID);
    }

    /**
     * Create a new Bean from an array of Strings. Note that the order in the array is essential!!
     *
     * @param aValues size = 9
     */
    public SigpepTransitionBean(String[] aValues) {
        this(
                aValues[0],
                aValues[1],
                aValues[2],
                aValues[3],
                aValues[4],
                aValues[5],
                aValues[6],
                aValues[7],
                aValues[8]
        );
    }

    /**
     * The start time
     */
    private double iStartTime = -1;

    /**
     * The end time
     */
    private double iEndTime = -1;

    /**
     * The collision energy to be used.
     */
    private double iCollisionEnergy = -1;


    /**
     * The polarity
     */
    private String iPolarity = "NA";


    /**
     * The trigger (cfr iSRM)
     */
    private String iTrigger = "NA";


    /**
     * The reaction category value of this transition
     */
    private String iReactionCategory = "NA";


    /**
     * Sets the iPolarity.
     *
     * @param aPolarity The iPolarity value to set.
     */
    public void setPolarity(String aPolarity) {
        this.iPolarity = aPolarity;
    }

    /**
     * Sets the iTrigger.
     *
     * @param aTrigger The iTrigger value to set.
     */
    public void setTrigger(String aTrigger) {
        this.iTrigger = aTrigger;
    }

    /**
     * Gets the iStartTime.
     *
     * @return The iStartTime value.
     */
    public double getStartTime() {
        return iStartTime;
    }

    /**
     * Gets the iTrigger.
     *
     * @return The iTrigger value.
     */
    public String getTrigger() {
        return iTrigger;
    }

    /**
     * Gets the iEndTime.
     *
     * @return The iEndTime value.
     */
    public double getEndTime() {
        return iEndTime;
    }

    /**
     * Sets the iEndTime.
     *
     * @param aEndTime The iEndTime value to set.
     */
    public void setEndTime(double aEndTime) {
        this.iEndTime = aEndTime;
    }

    /**
     * Sets the iStartTime.
     *
     * @param aStartTime The iStartTime value to set.
     */
    public void setStartTime(double aStartTime) {
        this.iStartTime = aStartTime;
    }

    /**
     * Gets the iPolarity.
     *
     * @return The iPolarity value.
     */
    public String getIPolarity() {
        return iPolarity;
    }

    /**
     * Sets the iReactionCategory.
     *
     * @param aReactionCategory The iReactionCategory value to set.
     */
    public void setReactionCategory(String aReactionCategory) {
        this.iReactionCategory = aReactionCategory;
    }

    /**
     * Gets the iReactionCategory.
     *
     * @return The iReactionCategory value.
     */
    public String getReactionCategory() {
        return iReactionCategory;
    }


    /**
     * Sets the iCollisionEnergy.
     *
     * @param aCollisionEnergy The iCollisionEnergy value to set.
     */
    public void setCollisionEnergy(double aCollisionEnergy) {
        this.iCollisionEnergy = aCollisionEnergy;
    }

    /**
     * Gets the iCollisionEnergy.
     *
     * @return The iCollisionEnergy value.
     */
    public double getCollisionEnergy() {
        return iCollisionEnergy;
    }

    /**
     * Returns a String[] with the specific order as they variables would appear in the Thermo CSV file.
     * 564.9618,663.4081,10,LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6,29.3
     *
     * @return
     */
    @Override
    public String[] getSeparatedOrder() {

        return new String[]{
                "" + getQ1Mass(),
                "" + getQ3Mass(),
                "" + getCollisionEnergy(),
                getID(),
                "" + iEndTime,
                };
    }

    public String getID(){
        String id = new String("" + getPeptideSequence() + "." + concatenateProteins() + "." + builIonID());
        return id;
    }

    public String builIonID() {
        String lIonID = "" + getIonCharge() + String.valueOf(getIonType()) + getIonNumber();
        return lIonID;
    }

    public String concatenateProteins(){
        String lConcatenatedProteins = Joiner.on(".").join(getProteinAccessions());
        return lConcatenatedProteins;
    }

    /**
     * @return
     */
    @Override
    public String asCSVLine() {
        return Joiner.on(',').join(getSeparatedOrder());
    }

}
