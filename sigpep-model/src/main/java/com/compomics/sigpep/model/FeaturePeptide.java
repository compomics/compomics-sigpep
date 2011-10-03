package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A peptide that can be mapped to a sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 19:22:38<br/>
 */
public interface FeaturePeptide extends Peptide, FeatureObject<PeptideFeature> {

    /**
     * Returns the sequences the peptide is emitted by using the specified protease.
     *
     * @param protease the protease
     * @return the parent sequences
     */
    Set<ProteinSequence> getParentSequences(Protease protease);

    /**
     * Returns whether or not the peptide is a signature peptide.
     *
     * @return true if the peptide is a signature peptide
     */
    boolean isSignaturePeptide();

    /**
     * Sets whether or not the peptide is a signature peptide.
     *
     * @param isSignaturePeptide whether or not the peptide is a signature peptide
     */
    void setSignaturePeptide(boolean isSignaturePeptide);

    /**
     * Returns the proteases that generate this peptide.
     *
     * @return the proteases
     */
    Set<Protease> getProteases();

    /**
     * Sets the proteases that generate this peptide.
     *
     * @param proteases the proteases
     */
    void setProteases(Set<Protease> proteases);
}
