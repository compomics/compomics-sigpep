package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.analysis.ChargeProbabilityCalculator;
import com.compomics.sigpep.analysis.PeptideIonStore;
import com.compomics.sigpep.model.MassOverChargeRange;
import com.compomics.sigpep.model.PeptideIon;
import com.compomics.sigpep.model.impl.MassOverChargeRangeImpl;
import com.compomics.sigpep.util.SigPepUtil;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Aug-2008<br/>
 * Time: 11:31:10<br/>
 */
public class ProbabilityBasedPeptideIonStore<P extends PeptideIon> implements PeptideIonStore<P> {

    protected static Logger logger = Logger.getLogger(ProbabilityBasedPeptideIonStore.class);
    private SortedMap<Double, Set<P>> store;
    private int massPrecission = Configuration.getInstance().getInt("sigpep.app.monoisotopic.mass.precision");
    private List<Map<Double, Integer>> observedMassChargeStateCombinations;
    private double massAccuracy;
    private ChargeProbabilityCalculator chargeProbabilityCalculator;
    private double probabilityThreshold = 0.1;

    /**
     * @param observedMassChargeStateCombinations
     *
     * @param massAccuracy
     * @param massPrecission
     * @TODO: JavaDoc missing.
     */
    public ProbabilityBasedPeptideIonStore(List<Map<Double, Integer>> observedMassChargeStateCombinations,
                                           double massAccuracy,
                                           int massPrecission) {
        this(observedMassChargeStateCombinations, massAccuracy);
        this.massPrecission = massPrecission;
    }

    /**
     * @param observedMassChargeStateCombinations
     *
     * @param massAccuracy
     * @TODO: JavaDoc missing.
     */
    public ProbabilityBasedPeptideIonStore(List<Map<Double, Integer>> observedMassChargeStateCombinations,
                                           double massAccuracy) {
        this.chargeProbabilityCalculator = new KernelBasedChargeProbabilityCalculator(observedMassChargeStateCombinations);
        this.observedMassChargeStateCombinations = observedMassChargeStateCombinations;
        this.massAccuracy = massAccuracy;
    }

    /**
     * @param peptideIons
     * @TODO: JavaDoc missing.
     */
    public void populate(Collection<P> peptideIons) {

        //logger.info("ProbabilityBasedPeptideIonStore.populate");
        store = new TreeMap<Double, Set<P>>();
        for (P peptideIon : peptideIons) {

            double mass = peptideIon.getNeutralMassPeptide();
            mass = SigPepUtil.round(mass, massPrecission);

            if (!store.containsKey(mass)) {
                store.put(mass, new HashSet<P>());
            }
            store.get(mass).add(peptideIon);

        }

        assignChargeStates();
    }

    /**
     * @TODO: JavaDoc missing.
     */
    private void assignChargeStates() {

        //logger.info("ProbabilityBasedPeptideIonStore.assignChargeStates");

        double[] uniqueMasses = new double[store.keySet().size()];
        int i = 0;
        for (Double mass : store.keySet()) {
            uniqueMasses[i] = mass;
            i++;
        }

        Map<Double, Map<Integer, Double>> chargeProbabilities = chargeProbabilityCalculator.getChargeProbablitiesGivenMass(uniqueMasses);

        for (double mass : chargeProbabilities.keySet()) {

            Map<Integer, Double> probabilities = chargeProbabilities.get(mass);

            for (P peptideIon : store.get(mass)) {

                for (Integer z : probabilities.keySet()) {
                    double p = probabilities.get(z);
                    if (z > 0) {
                        peptideIon.addAllowedChargeState(z, p);
                    }
                }
            }
        }

        //logger.info("done");
    }

    /**
     * @param mass
     * @return
     * @TODO: JavaDoc missing.
     */
    public Set<P> getPeptideIonsWithMass(double mass) {
        return store.get(mass);
    }

    /**
     * @param lowerMassLimit
     * @param upperMassLimit
     * @return
     * @TODO: JavaDoc missing.
     */
    public Set<P> getPeptideIonsInMassRange(double lowerMassLimit, double upperMassLimit) {

        Set<P> retVal = new HashSet<P>();
        for (Set<P> peptideIons : store.subMap(lowerMassLimit, upperMassLimit).values()) {
            retVal.addAll(peptideIons);
        }
        return retVal;
    }

    /**
     * @param peptideIon
     * @return
     * @TODO: JavaDoc missing.
     */
    public Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(P peptideIon) {

        double mass = peptideIon.getNeutralMassPeptide();
        mass = SigPepUtil.round(mass, massPrecission);
        return getPeptideIonsWithOverlappingMassOverCharge(mass);
    }

    /**
     * @param neutralMassPeptide
     * @return
     * @TODO: JavaDoc missing.
     */
    public Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(double neutralMassPeptide) {

        double mass = SigPepUtil.round(neutralMassPeptide, massPrecission);
        Map<Integer, Set<P>> retVal = new TreeMap<Integer, Set<P>>();
        Map<Double, Map<Integer, Double>> chargeProbabilityByMass = chargeProbabilityCalculator.getChargeProbablitiesGivenMass(mass);
        Map<Integer, Double> chargeProbability = chargeProbabilityByMass.get(mass);

        //get most probable charge state
        int mostProbableCharge = -1;
        double previousProbability = 0;
        for (Integer charge : chargeProbability.keySet()) {

            double probability = chargeProbability.get(charge);
            if (probability > previousProbability) {
                mostProbableCharge = charge;
            }
            previousProbability = probability;
        }

        if (mostProbableCharge != -1) {

            Set<P> peptideIons = new HashSet<P>();

            Set<Integer> targetChargeState = new HashSet<Integer>();
            targetChargeState.add(mostProbableCharge);

            MassOverChargeRange targetPeptideMassOverChargeRange = new MassOverChargeRangeImpl(mass, targetChargeState, massAccuracy);

            for (Integer charge : chargeProbabilityCalculator.getAllowedChargeStates()) {

                List<MassOverChargeRange[]> backgroundPeptideMassOverChargeRanges = targetPeptideMassOverChargeRange.getFlankingPeptideMassOverChargeRanges(charge);

                for (MassOverChargeRange[] backgroundPeptideMassOverChargeRange : backgroundPeptideMassOverChargeRanges) {

                    double lowerFlankingMass = backgroundPeptideMassOverChargeRange[0].getNeutralPeptideMass();
                    double upperFlankingMass = backgroundPeptideMassOverChargeRange[1].getNeutralPeptideMass();

                    for (P peptideIon : this.getPeptideIonsInMassRange(lowerFlankingMass, upperFlankingMass)) {

                        double peptideMass = peptideIon.getNeutralMassPeptide();
                        double[] pCharge = chargeProbabilityCalculator.getProbabilityOfChargeGivenMass(charge, peptideMass);
                        if (pCharge[0] >= probabilityThreshold) {
                            peptideIons.add(peptideIon);
                        }
                    }
                }
            }

            retVal.put(mostProbableCharge, peptideIons);
        }

        return retVal;
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    public Set<Double> getUniqueNeutralPeptideIonMasses() {
        return store.keySet();
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    public List<Map<Double, Integer>> getObservedMassChargeStateCombinations() {
        return observedMassChargeStateCombinations;
    }

    /**
     * @param observedMassChargeStateCombinations
     *
     * @TODO: JavaDoc missing.
     */
    public void setObservedMassChargeStateCombinations(List<Map<Double, Integer>> observedMassChargeStateCombinations) {
        this.observedMassChargeStateCombinations = observedMassChargeStateCombinations;
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    public double getMassAccuracy() {
        return massAccuracy;
    }

    /**
     * @param massAccuracy
     * @TODO: JavaDoc missing.
     */
    public void setMassAccuracy(double massAccuracy) {
        this.massAccuracy = massAccuracy;
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    public int getMassPrecission() {
        return massPrecission;
    }

    /**
     * @param massPrecission
     * @TODO: JavaDoc missing.
     */
    public void setMassPrecission(int massPrecission) {
        this.massPrecission = massPrecission;
    }
}
