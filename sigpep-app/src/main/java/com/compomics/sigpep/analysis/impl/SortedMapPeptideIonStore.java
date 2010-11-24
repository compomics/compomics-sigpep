package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.analysis.PeptideIonStore;
import com.compomics.sigpep.model.MassOverChargeRange;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.PeptideIon;
import com.compomics.sigpep.model.impl.MassOverChargeRangeImpl;
import com.compomics.sigpep.util.SigPepUtil;

import java.util.*;

/**
 * An implementation of PeptideStore that uses a SortedMap to access peptide ions by
 * their neutral mass or by overlaping m/z values given a specified mass accuracy and
 * a set of allowed charge states.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jul-2008<br/>
 * Time: 18:24:24<br/>
 */
public class SortedMapPeptideIonStore<P extends PeptideIon> implements PeptideIonStore<P> {

    private SortedMap<Double, Set<P>> store = new TreeMap<Double, Set<P>>();
    private int massPrecission = Configuration.getInstance().getInt("sigpep.app.monoisotopic.mass.precision");
    private Map<Integer, Double> chargeStates;
    private double massAccuracy;


    public SortedMapPeptideIonStore(Set<Integer> chargeStates,
                                    double massAccuracy,
                                    int massPrecission) {
        this(chargeStates, massAccuracy);
        this.massPrecission = massPrecission;

    }

    public SortedMapPeptideIonStore(Set<Integer> chargeStates,
                                    double massAccuracy) {

        this.setChargeStates(chargeStates);        
        this.massAccuracy = massAccuracy;
    }

    public void populate(Collection<P> peptideIons) {

        for (P peptideIon : peptideIons) {

            double mass = peptideIon.getNeutralMassPeptide();
            mass = SigPepUtil.round(mass, massPrecission);

            if (!store.containsKey(mass)) {
                store.put(mass, new HashSet<P>());
            }
            store.get(mass).add(peptideIon);

        }
    }

    public Set<P> getPeptideIonsWithMass(double mass) {

        return store.get(mass);

    }

    public Set<P> getPeptideIonsInMassRange(double lowerMassLimit, double upperMassLimit) {

        Set<P> retVal = new HashSet<P>();
        for (Set<P> peptideIons : store.subMap(lowerMassLimit, upperMassLimit).values()) {
            retVal.addAll(peptideIons);
        }
        return retVal;

    }

    public Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(P peptideIon) {

        double mass = peptideIon.getNeutralMassPeptide();
        mass = SigPepUtil.round(mass, massPrecission);
        return getPeptideIonsWithOverlappingMassOverCharge(mass);

    }

    public Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(double neutralMassPeptide) {

        double mass = SigPepUtil.round(neutralMassPeptide, massPrecission);

        Map<Integer, Set<P>> retVal = new TreeMap<Integer, Set<P>>();

        for (Integer charge : this.chargeStates.keySet()) {

            HashSet<Integer> chargeSet = new HashSet<Integer>();
            chargeSet.add(charge);

            MassOverChargeRange targetPeptideMassOverChargeRange = new MassOverChargeRangeImpl(mass, chargeSet, massAccuracy);
            Set<P> overlappingPeptides = new HashSet<P>();

            for (MassOverChargeRange[] backgroundPeptideMassOverChargeRange : targetPeptideMassOverChargeRange.getFlankingPeptideMassOverChargeRanges(chargeStates.keySet())) {

                double lowerFlankingMass = backgroundPeptideMassOverChargeRange[0].getNeutralPeptideMass();
                double upperFlankingMass = backgroundPeptideMassOverChargeRange[1].getNeutralPeptideMass();

                for (P peptideIon : this.getPeptideIonsInMassRange(lowerFlankingMass, upperFlankingMass)) {
                    peptideIon.setAllowedChargeStates(chargeStates);
                    overlappingPeptides.add(peptideIon);
                }

            }

            retVal.put(charge, overlappingPeptides);
        }

        return retVal;
    }

    public Set<Double> getUniqueNeutralPeptideIonMasses() {
        return store.keySet();
    }

    public Set<Integer> getChargeStates() {
        return chargeStates.keySet();
    }

    public void setChargeStates(Set<Integer> chargeStates) {
        this.chargeStates = new TreeMap<Integer, Double>();
        for(Integer chargeState : chargeStates){
            this.chargeStates.put(chargeState, 1.0);
        }
    }

    public double getMassAccuracy() {
        return massAccuracy;
    }

    public void setMassAccuracy(double massAccuracy) {
        this.massAccuracy = massAccuracy;
    }

    public int getMassPrecission() {
        return massPrecission;
    }

    public void setMassPrecission(int massPrecission) {
        this.massPrecission = massPrecission;
    }
}
