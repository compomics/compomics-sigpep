package com.compomics.sigpep.model;

import com.compomics.sigpep.model.impl.SpliceEventFeatureImpl;

import java.util.Set;

/**
 * Maps a peptide to a sequence.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 18:34:35<br/>
 */
public interface PeptideFeature extends Feature<FeaturePeptide> {

    /**
     * Returns the proeases producing the peptide feature.
     *
     * @return the proteases
     */
    Set<Protease> getProteases();

    /**
     * Sets the proteases producing the peptide feature.
     *
     * @param proteases the proteases
     */
    void setProteases(Set<Protease> proteases);

    /**
     * Returns the splice sites the peptide spans.
     *
     * @return the splice sites
     */
    Set<SpliceEventFeature> getSpliceEventFeatures();

    /**
     * Sets the splice events the peptide spans.
     *
     * @param spliceEventFeatures the splice events
     */
    void setSpliceEventFeatures(Set<SpliceEventFeature> spliceEventFeatures);
    
}
