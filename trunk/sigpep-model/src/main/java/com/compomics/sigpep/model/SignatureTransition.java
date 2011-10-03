package com.compomics.sigpep.model;

import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
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
     * @return
     * @TODO: JavaDoc missing.
     */
    Set<ProductIonType> getTargetProductIonTypes();

    /**
     * @param productIonTypes
     * @TODO: JavaDoc missing.
     */
    void setTargetProductIonTypes(Set<ProductIonType> productIonTypes);

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    Set<ProductIonType> getBackgroundProductIonTypes();

    /**
     * @param productIonTypes
     * @TODO: JavaDoc missing.
     */
    void setBackgroundProductIonTypes(Set<ProductIonType> productIonTypes);

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    Set<Integer> getPrecursorIonChargeStates();

    /**
     * @param chargeStates
     * @TODO: JavaDoc missing.
     */
    void setPrecursorIonChargeStates(Set<Integer> chargeStates);

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    Set<Integer> getProductIonChargeStates();

    /**
     * @param chargeStates
     * @TODO: JavaDoc missing.
     */
    void setProductIonChargeStates(Set<Integer> chargeStates);

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    double getMassAccuracy();

    /**
     * @param massAccuracy
     * @TODO: JavaDoc missing.
     */
    void setMassAccuracy(double massAccuracy);

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    int getTargetPeptideChargeState();

    /**
     * @param z
     * @TODO: JavaDoc missing.
     */
    void setTargetPeptideChargeState(int z);
}
