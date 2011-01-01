package com.compomics.sigpep.analysis.impl;

import org.apache.log4j.Logger;
import com.compomics.sigpep.analysis.PeptideIonStore;
import com.compomics.sigpep.analysis.ProductIonScanner;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.PrecursorIon;
import com.compomics.sigpep.model.SignatureTransition;

import java.util.*;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 10:46:04<br/>
 */
public class SignatureTransitionFinderImpl implements SignatureTransitionFinder {

    protected static Logger logger = Logger.getLogger(SignatureTransitionFinderImpl.class);
    protected Set<Peptide> backgroundPeptides;
    protected PeptideIonStore<PrecursorIon> backgroundPeptideIonStore;
    protected Set<Integer> precursorIonChargeStates;
    protected double massAccuracy;
    protected ProductIonScanner productIonScanner;
    protected List<Map<Double, Integer>> observedMassChargeStateCombinations;

    /**
     * @TODO: JavaDoc missing.
     *
     * @param backgroundPeptides
     * @param precursorIonChargeStates
     * @param massAccuracy
     * @param productIonScanner
     */
    public SignatureTransitionFinderImpl(Set<Peptide> backgroundPeptides,
            Set<Integer> precursorIonChargeStates,
            double massAccuracy,
            ProductIonScanner productIonScanner) {

        this.backgroundPeptides = backgroundPeptides;
        this.precursorIonChargeStates = precursorIonChargeStates;
        this.massAccuracy = massAccuracy;
        this.productIonScanner = productIonScanner;
        this.observedMassChargeStateCombinations = null;
        this.backgroundPeptideIonStore = createPrecursorIonStore(backgroundPeptides);
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param backgroundPeptides
     * @param observedMassChargeStateCombinations
     * @param massAccuracy
     * @param productIonScanner
     */
    public SignatureTransitionFinderImpl(Set<Peptide> backgroundPeptides,
            List<Map<Double, Integer>> observedMassChargeStateCombinations,
            double massAccuracy,
            ProductIonScanner productIonScanner) {

        this.backgroundPeptides = backgroundPeptides;
        this.precursorIonChargeStates = null;
        this.massAccuracy = massAccuracy;
        this.productIonScanner = productIonScanner;
        this.observedMassChargeStateCombinations = observedMassChargeStateCombinations;
        this.backgroundPeptideIonStore = createPrecursorIonStore(backgroundPeptides);
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param targetPeptide
     * @return
     */
    public List<SignatureTransition> findSignatureTransitions(Peptide targetPeptide) {
        Set<Peptide> targetPeptideSet = new HashSet<Peptide>();
        targetPeptideSet.add(targetPeptide);
        return this.findSignatureTransitions(targetPeptideSet);
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param targetPeptides
     * @return
     */
    public List<SignatureTransition> findSignatureTransitions(Collection<Peptide> targetPeptides) {

        List<SignatureTransition> retVal = new ArrayList<SignatureTransition>();

        Set<Peptide> peptideSet = new HashSet<Peptide>();
        peptideSet.addAll(targetPeptides);

        PeptideIonStore<PrecursorIon> targetPeptideIonStore = createPrecursorIonStore(peptideSet);

        //for each unique target peptide mass...
        for (Double targetPeptideMass : targetPeptideIonStore.getUniqueNeutralPeptideIonMasses()) {

            //get sequences of target targetPeptides with this mass
            Set<PrecursorIon> targetPeptideGroup = targetPeptideIonStore.getPeptideIonsWithMass(targetPeptideMass);

            //get sequences of targetPeptides whose mass overlapps with the target peptide mass
            Map<Integer, Set<PrecursorIon>> overlappingBackgroundPeptideIons = backgroundPeptideIonStore.getPeptideIonsWithOverlappingMassOverCharge(targetPeptideMass);//, precursorIonChargeStates, massAccuracy);

            for (Integer chargeState : overlappingBackgroundPeptideIons.keySet()) {

                Set<Peptide> overlappingBackgroundPeptides = new HashSet<Peptide>();

                for (PrecursorIon backgroundIon : overlappingBackgroundPeptideIons.get(chargeState)) {
                    overlappingBackgroundPeptides.add(backgroundIon.getPeptide());
                }

                //for each target peptide sequence check if there is a set of product
                // ions that can distinguish it from the rest of the overlapping targetPeptides...
                for (PrecursorIon targetPeptideIon : targetPeptideGroup) {

                    Peptide targetPeptide = targetPeptideIon.getPeptide();

                    if (!backgroundPeptides.contains(targetPeptide)) {
                        throw new IllegalArgumentException("Target targetPeptides have to be element of the background peptide set. " + targetPeptide);
                    }

                    //remove target peptide sequence from overlapping sequences
                    overlappingBackgroundPeptides.remove(targetPeptide);

                    List<SignatureTransition> signatureTransitions =
                            productIonScanner.findSignatureTransitions(
                            targetPeptide,
                            overlappingBackgroundPeptides);

                    //put the target peptide sequence and it's mass back to the set
                    //of overlapping sequence ids
                    overlappingBackgroundPeptides.add(targetPeptide);

//                    if (signatureTransitions.size() == 0) {
//                        logger.warn("Null transition!!!");
//                    }

                    for (SignatureTransition signatureTransition : signatureTransitions) {
                        signatureTransition.setTargetPeptideChargeState(chargeState);
                        retVal.add(signatureTransition);
                    }
                }
            }
        }

        return retVal;
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param peptides
     * @return
     */
    private PeptideIonStore<PrecursorIon> createPrecursorIonStore(Set<Peptide> peptides) {

        PeptideIonStore<PrecursorIon> retVal = null;

        //great background precursor ion store
        Set<PrecursorIon> precursorIons = new HashSet<PrecursorIon>();
        for (Peptide peptide : peptides) {
            precursorIons.add(peptide.getPrecursorIon());
        }

        if (observedMassChargeStateCombinations == null) {
            retVal = new SortedMapPeptideIonStore<PrecursorIon>(precursorIonChargeStates, massAccuracy);
        } else {
            retVal = new ProbabilityBasedPeptideIonStore<PrecursorIon>(observedMassChargeStateCombinations, massAccuracy);
        }

        retVal.populate(precursorIons);

        return retVal;
    }
//    private PeptideIonStore<PrecursorIon> createPrecursorIonStore(Set<Peptide> peptides,
//                                                                  List<Map<Double, Integer>> observedMassChargeStateCombinations) {
//
//        logger.info("massAccuracy = " + massAccuracy);
//
//        //great background precursor ion store
//        Set<PrecursorIon> precursorIons = new HashSet<PrecursorIon>();
//        for (Peptide peptide : peptides) {
//            precursorIons.add(peptide.getPrecursorIon());
//        }
//
//
//        return retVal;
//
//    }
}
