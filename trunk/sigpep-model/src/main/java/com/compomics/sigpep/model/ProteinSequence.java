package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A protein sequence that can be associated with one or more protein objects and
 * thus transitively with more than one gene. The latter case arises if the sequence
 * is a translation of transcripts which differ in their non coding sequence but
 * not in ther coding sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 17:49:00<br/>
 */
public interface ProteinSequence {

    /**
     * Returns the proteins the sequence is associated with.
     *
     * @return one or more proteins
     */
    Set<Protein> getProteins();

    void setProteins(Set<Protein> proteins);

    /**
     * Returns the proteolytic peptides emitted by this sequence.
     *
     * @return the peptides
     */
    Set<PeptideFeature> getPeptides();

    void setPeptides(Set<PeptideFeature> peptides);

    Set<PeptideFeature> getPeptides(Protease protease);

    /**
     * Returns a string representation of the amino acid sequence.
     *
     * @return the sequence string
     */
    String getSequenceString();

    /**
     * Sets the amino acid sequence of the protein sequence.
     *
     * @param sequence the sequence string
     */
    void setSequenceString(String sequence);

    /**
     * Returns the locations of the transcript splice sites in the protein sequence.
     *
     * The position corresponds to the position of the last amino acid encoded
     * by an exon. If the splice site falls onto a codon the position corresponds to the
     * last amino acid before that codon.
     *
     * @return the splice site positions
     */
    Set<SpliceEventFeature> getSpliceEvents();

    /**
     * Sets the locations of the transcript splice sites in the protein sequence.
     *
     * The position corresponds to the position of the last amino acid encoded
     * by an exon. If the splice site falls onto a codon the position corresponds to the
     * last amino acid before that codon.
     * 
     * @param spliceEvents the positions of splice sites
     */
    void setSpliceEvents(Set<SpliceEventFeature> spliceEvents);

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    public Set<PeptideFeature> getSignaturePeptides();

    /**
     * @TODO: JavaDoc missing.
     * 
     * @param signaturePeptides
     */
    public void setSignaturePeptides(Set<PeptideFeature> signaturePeptides);
}
