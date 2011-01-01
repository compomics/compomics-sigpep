package com.compomics.sigpep.model;

import com.compomics.sigpep.model.impl.FeaturePeptideImpl;
import com.compomics.sigpep.model.impl.PeptideImpl;

import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 12:07:04<br/>
 */
public abstract class PeptideFactory {

    /**
     * @TODO: JavaDoc missing.
     *
     * @param sequenceString
     * @return
     */
    public static Peptide createPeptide(String sequenceString){
        return new PeptideImpl(sequenceString);
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param location
     * @param protease
     * @return
     */
    public static Peptide createPeptide(SequenceLocation location, Protease protease){
        return new FeaturePeptideImpl(location, protease);
    }

    /**
     * @TODO: JavaDoc missing.
     * 
     * @param location
     * @param proteases
     * @return
     */
    public static Peptide createPeptide(SequenceLocation location, Set<Protease> proteases){
        return new FeaturePeptideImpl(location, proteases);
    }
}
