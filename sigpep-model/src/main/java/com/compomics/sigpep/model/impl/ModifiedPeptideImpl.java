package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.*;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 13-Feb-2008<br/>
 * Time: 19:17:51<br/>
 */
public class ModifiedPeptideImpl extends AbstractPeptide implements ModifiedPeptide {

    /**
     * the PTMs
     */
    private Map<Integer, Modification> postTranslationalModifications = new TreeMap<Integer, Modification>();

    /**
     * the peptide that is modified
     */
    private Peptide peptide;

    /**
     * Constructs a modified peptide for a peptide.
     *
     * @param peptide the peptide
     */
    protected ModifiedPeptideImpl(Peptide peptide) {
        this.peptide = peptide;
    }

    /**
     * Returns the post translational modifications.
     *
     * @return a map of post translational modifications and their respective position in the sequence
     */
    public Map<Integer, Modification> getPostTranslationalModifications() {
        return postTranslationalModifications;
    }

    /**
     * Returns the proteases that produce the peptide.
     *
     * @return the proteases
     */
    @Override
    public Set<Protease> getProteases() {
        return peptide.getProteases();
    }

    /**
     * Sets the proteases thtat produce the peptide.
     *
     * @param proteases the proteases
     */
    @Override
    public void setProteases(Set<Protease> proteases) {
        this.peptide.setProteases(proteases);
    }

    /**
     * Returns the peptide sequence as a string.
     *
     * @return the peptide sequence
     */
    public String getSequenceString() {
        return peptide.getSequenceString();
    }

    /**
     * Returns the length of the amino acid sequence.
     *
     * @return the sequence length
     */
    public int getSequenceLength() {
        return peptide.getSequenceLength();
    }

    /**
     * Returns the sequence positions of a residue.
     *
     * @param residue the one letter amino acid code
     * @return the sequence positions
     */
    public Set<Integer> getResiduePositions(String residue) {
        return peptide.getResiduePositions(residue);
    }

    public Peptide getUnmodifiedPeptide() {
        return peptide;
    }

    /**
     * Returns whether the peptide is modified.
     *
     * @return true if the peptide is modified
     */
    public boolean isModified() {
        return this.postTranslationalModifications.size() > 0;
    }

    @Override
    public Set<PeptideOrigin> getOrigins(){
        return peptide.getOrigins();
    }

    @Override
    public void setOrigins(Set<PeptideOrigin> origins){
        peptide.setOrigins(origins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifiedPeptideImpl)) return false;

        ModifiedPeptideImpl that = (ModifiedPeptideImpl) o;

        if (!peptide.equals(that.peptide)) return false;
        if (!postTranslationalModifications.equals(that.postTranslationalModifications)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * postTranslationalModifications.hashCode();
        result = 31 * result + peptide.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ModifiedPeptideImpl{" +
                "peptide=" + peptide +
                ", postTranslationalModifications=" + postTranslationalModifications +
                '}';
    }
}
