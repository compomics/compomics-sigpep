package com.compomics.sigpep.model;

import java.util.Set;

/**
 * Maps a splice event to a sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 19:13:11<br/>
 */
public interface SpliceEventFeature extends Feature<FeatureSpliceEvent> {

    /**
     * Returns the peptides spanning the splice site in the translation.
     *
     * @return the peptides
     */
    Set<PeptideFeature> getPeptideFeatures();

    /**
     * Sets the peptides spanning the splice site in the translation.
     *
     * @param peptides the peptides
     */
    void setPeptideFeatures(Set<PeptideFeature> peptides);

}
