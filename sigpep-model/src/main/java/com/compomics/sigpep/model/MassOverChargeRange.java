package com.compomics.sigpep.model;

import java.util.List;
import java.util.Set;

/**
 * Representation of an m/z range of a peptide mass given a specified mass accuracy
 * and a minimum and maximum allowed charge z of the peptide.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:45:00<br/>
 */
public interface MassOverChargeRange extends Comparable<MassOverChargeRange> {

    /**
     * Returns the mass accuracy.
     *
     * @return the mass accuracy in m/z units.
     */
    double getMassAccuracy();

    /**
     * Sets the mass accuracy.
     *
     * @param accuracy the mass accuracy in m/z units.
     */
    void setMassAccuracy(double accuracy);

    /**
     * Returns pairs of <code>MassOverChargeRange</code>s that represent the upper
     * and lower mass bounderies that flank this mass range given the
     * allowed charge stages and the mass accuracy. The returned <code>MassOverChargeRange</code>s
     * have the same allowed charge states and mass accuracy like this mass range.
     * <p/>
     * e.g. mass accuracy = 1 m/z:<br/>
     * range z=1:]835.401525 - 836.401525 - 837.401525[<br/>
     * lower flanking range z=1:]833.401525 - 834.401525 - 835.401525[<br/>
     * upper flanking range z=1:]837.401525 - 838.401525 - 839.401525[<br/>
     *
     * @return a list arrays of mass ranges (length = 2) with the lower flanking
     *         range as the first element of the array and the upper flanking
     *         range as the second element of the array
     */
    List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges();

    /**
     * Returns pairs of <code>MassOverChargeRange</code>s that represent the upper
     * and lower mass bounderies that flank this mass range given their specified
     * allowed charge stages. The returned <code>MassOverChargeRange</code>s
     * have the allow charge states specified.
     * <p/>
     * e.g. mass accuracy = 1 m/z:<br/>
     * range z=1:]835.401525 - 836.401525 - 837.401525[<br/>
     * lower flanking range z=1:]833.401525 - 834.401525 - 835.401525[<br/>
     * upper flanking range z=1:]837.401525 - 838.401525 - 839.401525[<br/>
     *
     * @param chargeStatesThat the allowed charge states of the returned mass ranges
     * @return a list arrays of mass ranges (length = 2) with the lower flanking
     *         range as the first element of the array and the upper flanking
     *         range as the second element of the array
     */
    List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges(Set<Integer> chargeStatesThat);

    /**
     * @param chargeStateThat
     * @return
     * @TODO: JavaDoc missing.
     */
    List<MassOverChargeRange[]> getFlankingPeptideMassOverChargeRanges(int chargeStateThat);

    /**
     * Returns the neutral mass of the peptide.
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralPeptideMass();

    /**
     * Sets the neutral mass of the peptide.
     *
     * @param peptideMass the monoisotopic mass in Da
     */
    void setNeutralPeptideMass(double peptideMass);

    /**
     * Returns the neutral flanking mass at the lower boundary of the m/z range.
     *
     * @param z the charge of the ion
     * @return the monoisotopic mass in Da
     */
    double getLowerBound(int z);

    /**
     * Returns the neutral flanking mass at the upper boundary of the m/z range.
     *
     * @param z the charge of the ion
     * @return the monoisotopic mass in Da
     */
    double getUpperBound(int z);

//    /**
//     * Returns the minimum ion charge allowed.
//     *
//     * @return the charge
//     */
//    int getMinimumCharge();
//
//    /**
//     * Sets the minimum ion charge allowed.
//     *
//     * @param z the charge
//     */
//    void setMinimumCharge(int z);
//
//    /**
//     * Returns the maximum ion charge allowed.
//     *
//     * @return the charge
//     */
//    int getMaximumCharge();

//    /**
//     * Sets the maximum ion charge allowed.
//     *
//     * @param z the charge
//     */
//    void setMaximumCharge(int z);

    /**
     * Returns the m/z value of the peptide ion with the specified charge (= (peptide mass + z * mass(H))/z)
     *
     * @param z the peptide charge
     * @return the m/z value
     */
    double getPeptideIonMassOverCharge(int z);

    /**
     * Checks if an m/z range overlaps with this m/z range.
     *
     * @param that       the m/z range to check for overlap
     * @param minOverlap the minimum mass overlap in Da to consider to mass ranges as overlapping
     * @return true if the mass ranges overlap
     */
    boolean overlappsWith(MassOverChargeRange that, double minOverlap);

    /**
     * Returns a set of allowed peptide ion charge states.
     *
     * @return a set of charge states
     */
    Set<Integer> getChargeStates();
}
