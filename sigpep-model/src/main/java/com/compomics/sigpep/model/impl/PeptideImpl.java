package com.compomics.sigpep.model.impl;

import org.apache.log4j.Logger;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 11:13:49<br/>
 */
public class PeptideImpl extends AbstractPeptide {

    private String sequence;
    /**
     * the logger
     */
    protected static Logger logger = Logger.getLogger(PeptideImpl.class);

    /**
     * @TODO: JavaDoc missing
     */
    protected PeptideImpl() {
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param sequence
     */
    public PeptideImpl(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @TODO: JavaDoc missing
     * @return
     */
    public String getSequenceString() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeptideImpl)) {
            return false;
        }

        PeptideImpl peptide = (PeptideImpl) o;

        if (!sequence.equals(peptide.sequence)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return sequence.hashCode();
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "PeptideImpl{"
                + "sequenceString=" + this.getSequenceString()
                + ", proteases=" + proteases
                + '}';
    }
}
