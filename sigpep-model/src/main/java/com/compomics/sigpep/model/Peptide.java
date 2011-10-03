package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A short amino acid sequence generated through proteolytic cleavage of a protein.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 11:02:37<br/>
 */
public interface Peptide {

    /**
     * Returns the proteases that produce the peptide.
     *
     * @return the proteases
     */
    Set<Protease> getProteases();

    /**
     * Sets the proteases thtat produce the peptide.
     *
     * @param proteases the proteases
     */
    void setProteases(Set<Protease> proteases);

    /**
     * Returns the peptide sequence as a string.
     *
     * @return the peptide sequence
     */
    String getSequenceString();

    /**
     * Returns the precursor ion of the peptide.
     *
     * @return the precursor ion
     */
    PrecursorIon getPrecursorIon();

    /**
     * Returns the length of the amino acid sequence.
     *
     * @return the sequence length
     */
    int getSequenceLength();

    /**
     * Returns the sequence positions of a residue.
     *
     * @param residue the one letter amino acid code
     * @return the sequence positions
     */
    Set<Integer> getResiduePositions(String residue);

    /**
     * Applies a post-translational modification to a peptide.
     * <p/>
     * A residue can only carry one modification. Modifications that affect residues that
     * have already been modified previously will be ignored.
     *
     * @param modification the modification
     * @return a list of modified peptides
     */
    Set<Peptide> applyModification(Modification modification);

    /**
     * Applies a set of post-translational modifications to a peptide.
     * <p/>
     * Static modifications will be applied first (in the order they occur in the set).
     * The remaining modifications will be applied in the order they are contained in the set.
     * A residue can only carry one modification. Modifications that affect residues that
     * have already been modified by a preceeding PTM will be ignored.
     *
     * @param modifications the modifications
     * @return a list of modified peptides
     */
    Set<Peptide> applyModifications(Set<Modification> modifications);

    /**
     * Returns the number of residues that are of the specified amino acid.
     *
     * @param aminoAcid one letter amino acid code
     * @return the residue count
     */
    int getResidueCount(String aminoAcid);

    /**
     * Returns whether the peptide is modified.
     *
     * @return true if the peptide is modified
     */
    boolean isModified();

    /**
     * Returns where in a protein sequence (N-terminus, C-terminus, internal) the peptide can originate from.
     *
     * @return a set of PeptideOrigins
     */
    Set<PeptideOrigin> getOrigins();

    /**
     * Returns where in the protein sequence (N-terminus, C-terminus, internal) the peptide can originate from.
     *
     * @param origins a set of PeptideOrigins
     */
    void setOrigins(Set<PeptideOrigin> origins);
}
