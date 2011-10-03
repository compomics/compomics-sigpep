package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.SequenceLocation;
import com.compomics.sigpep.model.ProteinSequence;

/**
 * Implementation of SequenceLocation.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 16-Jan-2008<br/>
 * Time: 17:42:35<br/>
 */
public class SequenceLocationImpl implements SequenceLocation {

    /**
     * the protein sequence the peptide is contained in
     */
    ProteinSequence proteinSequence;

    /**
     * the start coordinate of the peptide location
     */
    int start;

    /**
     * the end coordinate of the peptide location
     */
    int end;

    /**
     * Constructs a peptide location.
     */
    public SequenceLocationImpl() {
    }

    /**
     * Constructs a peptide location.
     *
     * @param proteinSequence the protein sequence the peptide is contained in
     * @param start           the start coordinate of the peptide location
     * @param end             the end coordinate of the peptide location
     */
    public SequenceLocationImpl(ProteinSequence proteinSequence, int start, int end) {
        this.proteinSequence = proteinSequence;
        this.start = start;
        this.end = end;
    }

    /**
     * {@inherit}
     */
    public ProteinSequence getSequence() {
        return proteinSequence;
    }

    /**
     * {@inherit}
     */
    public void setSequence(ProteinSequence proteinSequence) {
        this.proteinSequence = proteinSequence;
    }

    /**
     * {@inherit}
     */
    public int getStart() {
        return start;
    }

    /**
     * {@inherit}
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * {@inherit}
     */
    public int getEnd() {
        return end;
    }

    /**
     * {@inherit}
     */
    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceLocationImpl)) return false;

        SequenceLocation location = (SequenceLocation) o;

        if (end != location.getEnd()) return false;
        if (start != location.getStart()) return false;
        if (!proteinSequence.equals(location.getSequence())) return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        int result;
        result = proteinSequence.hashCode();
        result = 31 * result + start;
        result = 31 * result + end;
        return result;
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "SequenceLocationImpl{" +
                "sequence=" + this.proteinSequence.getSequenceString() +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
