package com.compomics.sigpep.model;

import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 31-Jul-2008<br/>
 * Time: 17:13:42<br/>
 */
public interface SignatureTransition extends Transition {

    /**
     * Returns the number of background peptide precursors the transition discriminates against.
     *
     * @return the number of background peptide precursors
     */
    public int getBackgroundPrecursorIonSetSize();

    /**
     * Returns the mass distribution of product ions emitted from the background precursor set.
     *
     * @return a map of product ion masses and frequencies
     */
    public Map<Double, Integer> getBackgroundProductIonMassDistribution();

    /**
     * Sets the mass distribution of product ions emitted from the background precursor set.
     *
     * @param backgroundProductIonMassDistribution
     *         a map of product ion masses and frequencies
     */
    public void setBackgroundProductIonMassDistribution(Map<Double, Integer> backgroundProductIonMassDistribution);

    /**
     * Returns the set of isobaric peptides the transition discriminates against.
     *
     * @return a set of peptides
     */
    public Set<Peptide> getBackgroundPeptides();

    /**
     * Sets the set of isobaric peptides the transition discriminates against.
     *
     * @param backgroundPeptides the peptides
     */
    public void setBackgroundPeptides(Set<Peptide> backgroundPeptides);

    /**
     * Returns a score for the transition.
     *
     * @return the score
     */
    double getExclusionScore();

    /**
     * Sets a score for the transition.
     *
     * @param score the score
     */
    void setExclusionScore(double score);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    Set<ProductIonType> getTargetProductIonTypes();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param productIonTypes
     */
    void setTargetProductIonTypes(Set<ProductIonType> productIonTypes);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    Set<ProductIonType> getBackgroundProductIonTypes();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param productIonTypes
     */
    void setBackgroundProductIonTypes(Set<ProductIonType> productIonTypes);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    Set<Integer> getPrecursorIonChargeStates();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param chargeStates
     */
    void setPrecursorIonChargeStates(Set<Integer> chargeStates);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    Set<Integer> getProductIonChargeStates();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param chargeStates
     */
    void setProductIonChargeStates(Set<Integer> chargeStates);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    double getMassAccuracy();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param massAccuracy
     */
    void setMassAccuracy(double massAccuracy);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    int getTargetPeptideChargeState();

    /**
     * @TODO: JavaDoc missing.
     *
     * @param z
     */
    void setTargetPeptideChargeState(int z);
}
