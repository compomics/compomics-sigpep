package com.compomics.sigpep.model;

import java.util.Set;

/**
 * An object that can be mapped to a sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 15:21:31<br/>
 */
public interface FeatureObject<F extends Feature> {

    /**
     * Returns the features represented by the object.
     *
     * @return the features
     */
    Set<F> getFeatures();

    /**
     * Sets the features represented by the object.
     *
     * @param features the features
     */
    void setFeatures(Set<F> features);

    /**
     * Returns the sequences the peptide is emitted by not taking into account the protease.
     *
     * @return the parent sequences
     */
    Set<ProteinSequence> getParentSequences();

}
