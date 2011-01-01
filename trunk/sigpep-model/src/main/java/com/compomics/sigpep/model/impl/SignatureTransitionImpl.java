package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.SignatureTransition;
import com.compomics.sigpep.model.ProductIonType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 31-Jul-2008<br/>
 * Time: 17:15:45<br/>
 */
public class SignatureTransitionImpl extends TransitionImpl implements SignatureTransition {

    private Map<Double, Integer> backgroundProductIonMassDistribution = new TreeMap<Double, Integer>();
    private Set<Peptide> backgroundPeptides;
    private double exclusionScore;
    private Set<ProductIonType> targetProductIonTypes;
    private Set<ProductIonType> backgroundProductIonTypes;
    private Set<Integer> precursorIonChargeStates;
    private Set<Integer> productIonChargeStates;
    private double massAccuracy;
    private int targetPeptideChargeState;

    /**
     * @TODO: JavaDoc missing
     *
     * @param target
     * @param backgroundPeptides
     */
    public SignatureTransitionImpl(Peptide target, Set<Peptide> backgroundPeptides) {
        super(target);
        this.backgroundPeptides = backgroundPeptides;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @param targetProductIons
     * @param backgroundPeptides
     */
    public SignatureTransitionImpl(List<ProductIon> targetProductIons, Set<Peptide> backgroundPeptides) {
        super(targetProductIons);
       this.backgroundPeptides = backgroundPeptides;
    }

    /**
     * Returns the number of background peptide precursors the transition discriminates against.
     *
     * @return the number of background peptide precursors
     */
    public int getBackgroundPrecursorIonSetSize() {
        return this.backgroundPeptides.size();
    }

    /**
     * Returns the mass distribution of product ions emitted from the background precursor set.
     *
     * @return a map of product ion masses and frequencies
     */
    public Map<Double, Integer> getBackgroundProductIonMassDistribution() {
        return this.backgroundProductIonMassDistribution;
    }

    /**
     * Sets the mass distribution of product ions emitted from the background precursor set.
     *
     * @param backgroundProductIonMassDistribution
     *         a map of product ion masses and frequencies
     */
    public void setBackgroundProductIonMassDistribution(Map<Double, Integer> backgroundProductIonMassDistribution) {
        this.backgroundProductIonMassDistribution = backgroundProductIonMassDistribution;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<Peptide> getBackgroundPeptides() {
        return backgroundPeptides;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param backgroundPeptides
     */
    public void setBackgroundPeptides(Set<Peptide> backgroundPeptides) {
        this.backgroundPeptides = backgroundPeptides;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public double getExclusionScore() {
        return exclusionScore;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param exclusionScore
     */
    public void setExclusionScore(double exclusionScore) {
        this.exclusionScore = exclusionScore;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<ProductIonType> getTargetProductIonTypes() {
        return targetProductIonTypes;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param targetProductIonTypes
     */
    public void setTargetProductIonTypes(Set<ProductIonType> targetProductIonTypes) {
        this.targetProductIonTypes = targetProductIonTypes;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<ProductIonType> getBackgroundProductIonTypes() {
        return backgroundProductIonTypes;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param backgroundProductIonTypes
     */
    public void setBackgroundProductIonTypes(Set<ProductIonType> backgroundProductIonTypes) {
        this.backgroundProductIonTypes = backgroundProductIonTypes;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<Integer> getPrecursorIonChargeStates() {
        return precursorIonChargeStates;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param precursorIonChargeStates
     */
    public void setPrecursorIonChargeStates(Set<Integer> precursorIonChargeStates) {
        this.precursorIonChargeStates = precursorIonChargeStates;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<Integer> getProductIonChargeStates() {
        return productIonChargeStates;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param productIonChargeStates
     */
    public void setProductIonChargeStates(Set<Integer> productIonChargeStates) {
        this.productIonChargeStates = productIonChargeStates;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public double getMassAccuracy() {
        return massAccuracy;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param massAccuracy
     */
    public void setMassAccuracy(double massAccuracy) {
        this.massAccuracy = massAccuracy;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int getTargetPeptideChargeState() {
        return targetPeptideChargeState;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param targetPeptideChargeState
     */
    public void setTargetPeptideChargeState(int targetPeptideChargeState) {
        this.targetPeptideChargeState = targetPeptideChargeState;
    }
}
