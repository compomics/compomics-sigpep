package com.compomics.sigpep.model;

import com.compomics.sigpep.model.impl.FeaturePeptideImpl;
import com.compomics.sigpep.model.impl.PeptideImpl;

import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 12:07:04<br/>
 */
public abstract class PeptideFactory {

    /**
     * @param sequenceString
     * @return
     * @TODO: JavaDoc missing.
     */
    public static Peptide createPeptide(String sequenceString) {
        return new PeptideImpl(sequenceString);
    }

    /**
     * @param location
     * @param protease
     * @return
     * @TODO: JavaDoc missing.
     */
    public static Peptide createPeptide(SequenceLocation location, Protease protease) {
        return new FeaturePeptideImpl(location, protease);
    }

    /**
     * @param location
     * @param proteases
     * @return
     * @TODO: JavaDoc missing.
     */
    public static Peptide createPeptide(SequenceLocation location, Set<Protease> proteases) {
        return new FeaturePeptideImpl(location, proteases);
    }
}
