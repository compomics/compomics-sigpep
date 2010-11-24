package com.compomics.sigpep.model;

/**
 * The proteome location of a peptide specified by a protein sequence and
 * the start and end coordinates of the peptide within that sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:53:26<br/>
 */
public interface SequenceLocation {

    /**
     * Returns the protein sequence the peptide is contained in.
     *
     * @return the protein sequence
     */
    ProteinSequence getSequence();


    /**
     * Sets the protein sequence the peptide is contained in.
     * @param proteinSequence the protein sequence
     */
    void setSequence(ProteinSequence proteinSequence);

    /**
     * Returns the start coordinate of the peptide location.
     *
     * @return the start coordinate
     */
    int getStart();

    /**
     * Sets the start coordinate of the peptide location.
     *
     * @param start  the start coordinate
     */
    void setStart(int start);

    /**
     * Returns the end coordinate of the peptide location.
     *
     * @return the end coordinate
     */
    int getEnd();

    /**
     * Sets the end coordinate of the peptide location.
     *
     * @param end the end coordinate
     */
    void setEnd(int end);

}
