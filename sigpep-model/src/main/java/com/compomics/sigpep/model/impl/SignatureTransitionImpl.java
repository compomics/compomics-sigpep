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
 * <p/>
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
     * @param target
     * @param backgroundPeptides
     * @TODO: JavaDoc missing
     */
    public SignatureTransitionImpl(Peptide target, Set<Peptide> backgroundPeptides) {
        super(target);
        this.backgroundPeptides = backgroundPeptides;
    }

    /**
     * @param targetProductIons
     * @param backgroundPeptides
     * @TODO: JavaDoc missing
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
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<Peptide> getBackgroundPeptides() {
        return backgroundPeptides;
    }

    /**
     * @param backgroundPeptides
     * @TODO: JavaDoc missing
     */
    public void setBackgroundPeptides(Set<Peptide> backgroundPeptides) {
        this.backgroundPeptides = backgroundPeptides;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public double getExclusionScore() {
        return exclusionScore;
    }

    /**
     * @param exclusionScore
     * @TODO: JavaDoc missing
     */
    public void setExclusionScore(double exclusionScore) {
        this.exclusionScore = exclusionScore;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<ProductIonType> getTargetProductIonTypes() {
        return targetProductIonTypes;
    }

    /**
     * @param targetProductIonTypes
     * @TODO: JavaDoc missing
     */
    public void setTargetProductIonTypes(Set<ProductIonType> targetProductIonTypes) {
        this.targetProductIonTypes = targetProductIonTypes;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<ProductIonType> getBackgroundProductIonTypes() {
        return backgroundProductIonTypes;
    }

    /**
     * @param backgroundProductIonTypes
     * @TODO: JavaDoc missing
     */
    public void setBackgroundProductIonTypes(Set<ProductIonType> backgroundProductIonTypes) {
        this.backgroundProductIonTypes = backgroundProductIonTypes;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<Integer> getPrecursorIonChargeStates() {
        return precursorIonChargeStates;
    }

    /**
     * @param precursorIonChargeStates
     * @TODO: JavaDoc missing
     */
    public void setPrecursorIonChargeStates(Set<Integer> precursorIonChargeStates) {
        this.precursorIonChargeStates = precursorIonChargeStates;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<Integer> getProductIonChargeStates() {
        return productIonChargeStates;
    }

    /**
     * @param productIonChargeStates
     * @TODO: JavaDoc missing
     */
    public void setProductIonChargeStates(Set<Integer> productIonChargeStates) {
        this.productIonChargeStates = productIonChargeStates;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public double getMassAccuracy() {
        return massAccuracy;
    }

    /**
     * @param massAccuracy
     * @TODO: JavaDoc missing
     */
    public void setMassAccuracy(double massAccuracy) {
        this.massAccuracy = massAccuracy;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public int getTargetPeptideChargeState() {
        return targetPeptideChargeState;
    }

    /**
     * @param targetPeptideChargeState
     * @TODO: JavaDoc missing
     */
    public void setTargetPeptideChargeState(int targetPeptideChargeState) {
        this.targetPeptideChargeState = targetPeptideChargeState;
    }
}
