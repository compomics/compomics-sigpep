package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.*;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:27:01<br/>
 */
public class FeaturePeptideImpl extends PeptideImpl implements FeaturePeptide, Persistable {

    private int id;
    private Object sessionFactory;

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Object getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param sessionFactory
     */
    public void setSessionFactory(Object sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    /**
     * the proteome locations of the peptide
     */
    private Set<PeptideFeature> peptideFeatures = new HashSet<PeptideFeature>();
    /**
     * whether the peptide is a signature peptide
     */
    private boolean signaturePeptide;

    /**
     * @TODO: JavaDoc missing
     */
    public FeaturePeptideImpl() {
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param location
     * @param proteases
     */
    public FeaturePeptideImpl(SequenceLocation location, Set<Protease> proteases) {
        super();
        this.peptideFeatures.add(new PeptideFeatureImpl(this, location, proteases));
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param location
     * @param protease
     */
    public FeaturePeptideImpl(SequenceLocation location, Protease protease) {
        super();
        Set<Protease> proteases = new HashSet<Protease>();
        proteases.add(protease);
        this.peptideFeatures.add(new PeptideFeatureImpl(this, location, proteases));
    }

    /**
     * Returns the features represented by the peptide.
     *
     * @return the peptide features
     */
    public Set<PeptideFeature> getFeatures() {
        return peptideFeatures;
    }

    /**
     * Sets the features represented by the peptide.
     *
     * @param peptideFeatures the peptide features
     */
    public void setFeatures(Set<PeptideFeature> peptideFeatures) {
        this.peptideFeatures = peptideFeatures;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns whether or not the peptide is a signature peptide.
     *
     * @return true if the peptide is a signature peptide
     */
    public boolean isSignaturePeptide() {
        return signaturePeptide;
    }

    /**
     * Sets whether or not the peptide is a signature peptide.
     *
     * @param isSignaturePeptide whether or not the peptide is a signature peptide
     */
    public void setSignaturePeptide(boolean isSignaturePeptide) {
        this.signaturePeptide = isSignaturePeptide;
    }

    /**
     * {@inherit}
     */
    public Set<ProteinSequence> getParentSequences() {

        Set<ProteinSequence> retVal = new HashSet<ProteinSequence>();

        for (Feature f : getFeatures()) {
            retVal.add(f.getLocation().getSequence());
        }

        return retVal;
    }

    /**
     * {@inherit}
     */
    public Set<ProteinSequence> getParentSequences(Protease protease) {

        Set<ProteinSequence> retVal = new HashSet<ProteinSequence>();

        for (PeptideFeature pf : getFeatures()) {
            if (pf.getProteases().contains(protease)) {
                retVal.add(pf.getLocation().getSequence());
            }
        }

        return retVal;
    }

    /**
     * {@inherit}
     */
    public String getSequenceString() {

        Feature peptideFeature = getFeatures().iterator().next();
        return peptideFeature.getLocation().getSequence().getSequenceString().substring(
                peptideFeature.getLocation().getStart() - 1,
                peptideFeature.getLocation().getEnd());
    }

    /**
     * Returns where in the protein sequence (N-terminus, C-terminus, internal) the peptide can originate from.
     *
     * @return a set of PeptideOrigins
     */
    public Set<PeptideOrigin> getOrigins() {

        Set<PeptideOrigin> retVal = new HashSet<PeptideOrigin>();
        for (Feature peptideFeature : this.getFeatures()) {

            int proteinLength = peptideFeature.getLocation().getSequence().getSequenceString().length();
            int featureStart = peptideFeature.getLocation().getStart();
            int featureEnd = peptideFeature.getLocation().getEnd();
            if (featureStart == 1) {
                retVal.add(PeptideOrigin.N_TERMINAL);
            } else if (featureEnd == proteinLength) {
                retVal.add(PeptideOrigin.C_TERMINAL);
            } else {
                retVal.add(PeptideOrigin.INTERNAL);
            }
        }

        if (retVal.size() == 0) {
            retVal.add(PeptideOrigin.UNKNOWN);
        }

        return retVal;
    }

    /**
     * Sets where in the protein sequence (N-terminus, C-terminus, internal) the peptide can originate from.
     *
     * @param origins a set of PeptideOrigins
     * @throws RuntimeException if called as origin of a feature peptide cannot be changed
     */
    public void setOrigins(Set<PeptideOrigin> origins) {
        throw new RuntimeException("Cannot change the origin of a feature peptide.");
    }

    /**
     * {@inherit}
     */
    public String toString() {

        return "FeaturePeptideImpl {"
                + "peptideFeatures = '" + peptideFeatures + "'"
                + "}";
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeaturePeptide)) {
            return false;
        }

        FeaturePeptide that = (FeaturePeptide) o;

        if (!peptideFeatures.equals(that.getFeatures())) {
            return false;
        }

        return true;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @return
     */
    public int hashCode() {
        return peptideFeatures.hashCode();
    }
}
