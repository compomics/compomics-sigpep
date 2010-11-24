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

    public SignatureTransitionImpl(Peptide target, Set<Peptide> backgroundPeptides) {
        super(target);
        this.backgroundPeptides = backgroundPeptides;
    }

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

    public Set<Peptide> getBackgroundPeptides() {
        return backgroundPeptides;
    }

    public void setBackgroundPeptides(Set<Peptide> backgroundPeptides) {
        this.backgroundPeptides = backgroundPeptides;
    }

    public double getExclusionScore() {
        return exclusionScore;
    }

    public void setExclusionScore(double exclusionScore) {
        this.exclusionScore = exclusionScore;
    }

    public Set<ProductIonType> getTargetProductIonTypes() {
        return targetProductIonTypes;
    }

    public void setTargetProductIonTypes(Set<ProductIonType> targetProductIonTypes) {
        this.targetProductIonTypes = targetProductIonTypes;
    }

    public Set<ProductIonType> getBackgroundProductIonTypes() {
        return backgroundProductIonTypes;
    }

    public void setBackgroundProductIonTypes(Set<ProductIonType> backgroundProductIonTypes) {
        this.backgroundProductIonTypes = backgroundProductIonTypes;
    }

    public Set<Integer> getPrecursorIonChargeStates() {
        return precursorIonChargeStates;
    }

    public void setPrecursorIonChargeStates(Set<Integer> precursorIonChargeStates) {
        this.precursorIonChargeStates = precursorIonChargeStates;
    }

    public Set<Integer> getProductIonChargeStates() {
        return productIonChargeStates;
    }

    public void setProductIonChargeStates(Set<Integer> productIonChargeStates) {
        this.productIonChargeStates = productIonChargeStates;
    }

    public double getMassAccuracy() {
        return massAccuracy;
    }

    public void setMassAccuracy(double massAccuracy) {
        this.massAccuracy = massAccuracy;
    }

    public int getTargetPeptideChargeState() {
        return targetPeptideChargeState;
    }

    public void setTargetPeptideChargeState(int targetPeptideChargeState) {
        this.targetPeptideChargeState = targetPeptideChargeState;
    }
    
}
