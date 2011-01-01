package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.MassOverChargeRange;

import java.util.*;

/**
 * Implementation of MassOverChargeRange
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 12-Oct-2007<br>
 * Time: 12:35:31<br>
 */
public class MassOverChargeRangeImpl implements MassOverChargeRange {

    /**
     * the peptide mass the m/z range is based on
     */
    private double peptideMass = 0;

    /**
     * the mass theoretical accuracy
     */
    private double accuracy = 1;

    /**
     * the minimum peptide ion charge
     */
//    private int minimumCharge = 1;

    /**
     * the maximum peptide ion charge
     */
//    private int maximumCharge = 1;

    /**
     * the mass ranges flanking this mass range at the lower mass bound; Map key
     * is the charge state z, map value is the peptide mass.
     */
    private Map<Integer, Double> lowerBounds = new HashMap<Integer, Double>();

    /**
     * the mass ranges flanking this mass range at the upper mass bound; Map key
     * is the charge state z, map value is the peptide mass.
     */
    private Map<Integer, Double> upperBounds = new HashMap<Integer, Double>();

    /**
     * the m/z values the peptide can have based on the charge range defined by the
     * minimum and maximum charge
     */
    private Map<Integer, Double> massesOverCharges = new HashMap<Integer, Double>();

    /**
     * the allowed peptide ion charges
     */
    private Set<Integer> chargeStates = new TreeSet<Integer>();

//    /**
//     * Constructs a m/z range for a peptide mass.
//     *
//     * @param peptideMass   the peptide mass
//     * @param minimumCharge the allowed minimum charge of the peptide ion
//     * @param maximumCharge the allowed maximum charge of the peptide ion
//     * @param accuracy      the mass accuracy of the mass spectrometer
//     */
//    public MassOverChargeRangeImpl(double peptideMass,
//                                   int minimumCharge,
//                                   int maximumCharge,
//                                   double accuracy) {
//
//        this.peptideMass = peptideMass;
//        this.minimumCharge = minimumCharge;
//        this.maximumCharge = maximumCharge;
//        this.accuracy = accuracy;
//        this.calculateBounds();
//
//    }

    /**
     * Constructs a m/z range for a peptide mass.
     *
     * @param peptideMass   the peptide mass
     * @param chargeStates the allowed charges of the peptide ion
     * @param accuracy      the mass accuracy of the mass spectrometer
     */
    public MassOverChargeRangeImpl(double peptideMass,
                                   Set<Integer> chargeStates,
                                   double accuracy) {

        this.peptideMass = peptideMass;
        this.chargeStates = chargeStates;
        this.accuracy = accuracy;
        this.calculateBounds();
    }

    /**
     * {@inherit}
     */
    public double getMassAccuracy() {
        return accuracy;
    }

    /**
     * {@inherit}
     */
    public void setMassAccuracy(double accuracy) {
        this.accuracy = accuracy;
        //recalculate bounds
        this.calculateBounds();
    }

    /**
     * Calculates all m/z values within the charge range defined by the min. and max. charge the peptide ions can have.
     */
    private void calculateMassesOverCharges() {
        this.massesOverCharges.clear();
        for(int z : chargeStates){
        //for (int z = minimumCharge; z <= maximumCharge; z++) {
            this.massesOverCharges.put(z, PrecursorIonImpl.calculateMassOverCharge(peptideMass, z));
        }
    }

    /**
     * Calculates the peptide masses of m/z ranges that flank this range given the mass accuracy and the
     * allowed charge stages and stores them in a hash map.
     */
    private void calculateBounds() {

        //calculate masses over charges for all z in z-range
        this.calculateMassesOverCharges();

        this.lowerBounds.clear();
        this.upperBounds.clear();
        for(int z : chargeStates){
        //for (int z = minimumCharge; z <= maximumCharge; z++) {
            this.lowerBounds.put(z, this.massesOverCharges.get(z) - accuracy);
            this.upperBounds.put(z, this.massesOverCharges.get(z) + accuracy);
        }
    }

    /**
     * {@inherit}
     */
    public List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges() {

        List<MassOverChargeRange[]> retVal = new ArrayList<MassOverChargeRange[]>();
        for(int zThis : chargeStates){
        //for (int zThis = minimumCharge; zThis <= maximumCharge; zThis++) {

            double thisLowerBoundMassOverCharge = this.getLowerBound(zThis);
            double thisUpperBoundMassOverCharge = this.getUpperBound(zThis);

            for(int zThat : chargeStates){
            //for (int zThat = minimumCharge; zThat <= maximumCharge; zThat++) {

                double thatLowerBoundMass = PrecursorIonImpl.calculatePeptideMassFromMassOverCharge(thisLowerBoundMassOverCharge - accuracy, zThat);
                double thatUpperBoundMass = PrecursorIonImpl.calculatePeptideMassFromMassOverCharge(thisUpperBoundMassOverCharge + accuracy, zThat);

                MassOverChargeRangeImpl[] retValArray = new MassOverChargeRangeImpl[2];
//                retValArray[0] = new MassOverChargeRangeImpl(thatLowerBoundMass, minimumCharge, maximumCharge, accuracy);
//                retValArray[1] = new MassOverChargeRangeImpl(thatUpperBoundMass, minimumCharge, maximumCharge, accuracy);

                retValArray[0] = new MassOverChargeRangeImpl(thatLowerBoundMass, chargeStates, accuracy);
                retValArray[1] = new MassOverChargeRangeImpl(thatUpperBoundMass, chargeStates, accuracy);
                retVal.add(retValArray);

            }
        }

        return retVal;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param chargeStatesThat
     * @return
     */
    public List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges(Set<Integer> chargeStatesThat) {

        List<MassOverChargeRange[]> retVal = new ArrayList<MassOverChargeRange[]>();
        for(int zThis : this.chargeStates){

            double thisLowerBoundMassOverCharge = this.getLowerBound(zThis);
            double thisUpperBoundMassOverCharge = this.getUpperBound(zThis);

            for(int zThat : chargeStatesThat){

                double thatLowerBoundMass = PrecursorIonImpl.calculatePeptideMassFromMassOverCharge(thisLowerBoundMassOverCharge - accuracy, zThat);
                double thatUpperBoundMass = PrecursorIonImpl.calculatePeptideMassFromMassOverCharge(thisUpperBoundMassOverCharge + accuracy, zThat);

                MassOverChargeRangeImpl[] retValArray = new MassOverChargeRangeImpl[2];

                retValArray[0] = new MassOverChargeRangeImpl(thatLowerBoundMass, chargeStatesThat, accuracy);
                retValArray[1] = new MassOverChargeRangeImpl(thatUpperBoundMass, chargeStatesThat, accuracy);
                retVal.add(retValArray);
            }
        }

        return retVal;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param chargeStateThat
     * @return
     */
    public List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges(int chargeStateThat) {
        HashSet<Integer> chargeStateSet = new HashSet<Integer>();
        chargeStateSet.add(chargeStateThat);
        return getFlankingPeptideMassOverChargeRanges(chargeStateSet);
    }

    /**
     * {@inherit}
     */
    public boolean overlappsWith(MassOverChargeRange that, double minOverlap) {

        double overlap = 0;

        for(int zThis : chargeStates){
        //for (int zThis = minimumCharge; zThis <= maximumCharge; zThis++) {

            for(int zThat : that.getChargeStates()){
            //for (int zThat = that.getMinimumCharge(); zThat <= that.getMaximumCharge(); zThat++) {

                double overlapTemp = 0;
                double lowerBoundThat = that.getLowerBound(zThat);
                double upperBoundThat = that.getUpperBound(zThat);
                double lowerBoundThis = this.getLowerBound(zThis);
                double upperBoundThis = this.getUpperBound(zThis);

                if (lowerBoundThat <= upperBoundThis && lowerBoundThat >= lowerBoundThis)
                    overlapTemp = upperBoundThis - lowerBoundThat;

                else if (upperBoundThat >= lowerBoundThis && upperBoundThat <= upperBoundThis)
                    overlapTemp = upperBoundThat - lowerBoundThis;

                if (overlapTemp > overlap)
                    overlap = overlapTemp;
            }
        }

        return overlap > minOverlap;
    }

    /**
     * {@inherit}
     */
    public double getPeptideIonMassOverCharge(int z) {
        try {
            return massesOverCharges.get(z);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("z = " + z + " not an allowed charge state of this m/z range.");
        }
    }

    /**
     * {@inherit}
     */
    public double getNeutralPeptideMass() {
        return peptideMass;
    }

    /**
     * {@inherit}
     */
    public void setNeutralPeptideMass(double peptideMass) {
        this.peptideMass = peptideMass;
        //recalculate bounds
        this.calculateBounds();
    }

    /**
     * {@inherit}
     */
    public double getLowerBound(int z) {
        try {
            return this.lowerBounds.get(z);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("z = " + z + " not an allowed charge state of this m/z range.");
        }
    }

    /**
     * {@inherit}
     */
    public double getUpperBound(int z) {
        try {
            return this.upperBounds.get(z);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("z = " + z + " not an allowed charge state of this m/z range.");
        }
    }

//    /**
//     * {@inherit}
//     */
//    public int getMinimumCharge() {
//        return minimumCharge;
//    }
//
//    /**
//     * {@inherit}
//     */
//    public void setMinimumCharge(int z) {
//        this.minimumCharge = z;
//        this.calculateBounds();
//    }
//
//    /**
//     * {@inherit}
//     */
//    public int getMaximumCharge() {
//        return maximumCharge;
//    }
//
//    /**
//     * {@inherit}
//     */
//    public void setMaximumCharge(int z) {
//        this.maximumCharge = z;
//        this.calculateBounds();
//    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Set<Integer> getChargeStates() {
        return chargeStates;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param chargeStates
     */
    public void setChargeStates(Set<Integer> chargeStates) {
        this.chargeStates = chargeStates;
    }

    @Override
    public String toString() {
        StringBuffer retVal = new StringBuffer();

        for (Integer z : upperBounds.keySet()) {

            retVal.append("z=").append(z).append(":")
                    .append("]")
                    .append(lowerBounds.get(z))
                    .append(" - ")
                    .append(PrecursorIonImpl.calculateMassOverCharge(this.getNeutralPeptideMass(), z))
                    .append(" - ")
                    .append(upperBounds.get(z))
                    .append("[ ");
        }
        return retVal.toString();
    }

    /**
     * Checks the m/z range for equality with another object. Two m/z ranges are equal if
     * they have they same accuracy, minum charge, maximum charge and peptide mass.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        MassOverChargeRangeImpl that = (MassOverChargeRangeImpl) o;
//
//        if (Double.compare(that.accuracy, accuracy) != 0) return false;
//        if (chargeStates != that.chargeStates) return false;
////        if (maximumCharge != that.maximumCharge) return false;
////        if (minimumCharge != that.minimumCharge) return false;
//        if (Double.compare(that.peptideMass, peptideMass) != 0) return false;
//
//        return true;
//    }

    /**
     * {@inherit}
     */
//    public int hashCode() {
//        int result;
//        long temp;
//        temp = peptideMass != +0.0d ? Double.doubleToLongBits(peptideMass) : 0L;
//        result = (int) (temp ^ (temp >>> 32));
//        temp = accuracy != +0.0d ? Double.doubleToLongBits(accuracy) : 0L;
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + minimumCharge;
//        result = 31 * result + maximumCharge;
//        return result;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MassOverChargeRangeImpl that = (MassOverChargeRangeImpl) o;

        if (Double.compare(that.accuracy, accuracy) != 0) return false;
        if (Double.compare(that.peptideMass, peptideMass) != 0) return false;
        if (!chargeStates.equals(that.chargeStates)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = peptideMass != +0.0d ? Double.doubleToLongBits(peptideMass) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = accuracy != +0.0d ? Double.doubleToLongBits(accuracy) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + chargeStates.hashCode();
        return result;
    }

    /**
     * Compares this m/z range to another m/z range. The comparison is based on the neutral peptide mass.
     *
     * @param that the m/z range to be compared
     * @return returns 0 if the neutral peptide mass of the two ranges are is equal,
     *         -1 if the neutral peptide mass of the m/z range passed as an argument is smaller,
     *         1 if it is greater
     */
    public int compareTo(MassOverChargeRange that) {
        return Double.compare(this.getNeutralPeptideMass(), that.getNeutralPeptideMass());
    }
}
