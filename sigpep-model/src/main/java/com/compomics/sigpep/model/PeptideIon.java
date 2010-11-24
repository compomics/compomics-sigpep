package com.compomics.sigpep.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An ionised peptide.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jan-2008<br/>
 * Time: 15:38:24<br/>
 */
public interface PeptideIon {

    /**
     * Returns the theoretical m/z value of the precursor ion with charge z.
     *
     * @param z the ion charge
     * @return the m/z value
     */
    double getMassOverCharge(int z);

    /**
     * Returns the neutral molecular mass of the (uncharged) precursor ion
     * including N- and C-terminal groups. (neutral mass N-terminal group
     * + neutral mass residues + neutral mass C-terminal group).
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassPeptide();

    /**
     * Returns the neutral molecular mass of the (uncharged) amino acid
     * residues (excluding N- and C-terminal groups). (neutral mass residues).
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassResidues();

    /**
     * Returns the mass of the post-translational modifications.
     *
     * @return the monoisotopic mass in Da
     */
    double getModificationMass();

    /**
     * Returns a list of post-translational modifications occuring on the peptide.
     *
     * @return a list of PTMs which is empty if there are no PTMs occuring on the peptide
     */
    Collection<Modification> getPostTranslationalModifications();

    /**
     * Returns the peptide sequence.
     *
     * @return the sequence string
     */
    String getSequenceString();

    /**
     * Returns the length of the peptide sequence.
     *
     * @return the length
     */
    int getSequenceLength();

    /**
     * Returns the neutral molecular mass of the (uncharged) N-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassNTerminalGroup();

    /**
     * Returns the neutral molecular mass of the (uncharged) C-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassCTerminalGroup();

    /**
     * Returns the charge states the peptide is allowed to have.
     *
     * @return the allowed charge states and their probabilies
     */
    Map<Integer, Double> getAllowedChargeStates();

    /**
     * Sets the charge states the peptide is allowd to have.
     *
     * @param allowedChargeStates the allowed charge states and their probabilities
     */
    void setAllowedChargeStates(Map<Integer, Double> allowedChargeStates);

    /**
     * Adds an allowed charge state and its probability
     * @param z           the charge state
     * @param probability the probability
     */
    void addAllowedChargeState(int z, double probability);

}
