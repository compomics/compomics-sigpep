package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.Set;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 15:02:45<br/>
 */
public class PeptideFeatureImpl extends AbstractFeature<FeaturePeptide> implements PeptideFeature, Persistable {

    private int id;
    private Object sessionFactory;

    private FeaturePeptide peptide;
    private Set<Protease> proteases;
    private Set<SpliceEventFeature> spliceEventFeatures;

    /**
     * @TODO: JavaDoc missing
     */
    protected PeptideFeatureImpl() {
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param peptide
     * @param location
     * @param proteases
     */
    public PeptideFeatureImpl(FeaturePeptide peptide, SequenceLocation location, Set<Protease> proteases) {
        super(location, peptide);
        this.proteases=proteases;
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
     * Returns the object representing the feature.
     *
     * @return the feature object
     */
    public FeaturePeptide getFeatureObject() {
        return peptide;
    }

    /**
     * The the object representing the feature.
     *
     * @param peptide the feature object
     */
    public void setFeatureObject(FeaturePeptide peptide) {
        this.peptide=peptide;
    }

    /**
     * Returns the proeases producing the peptide feature.
     *
     * @return the proteases
     */
    public Set<Protease> getProteases() {
        return proteases;
    }

    /**
     * Sets the proteases producing the peptide feature.
     *
     * @param proteases the proteases
     */
    public void setProteases(Set<Protease> proteases) {
        this.proteases=proteases;
    }

    /**
     * Returns the splice sites the peptide spans.
     *
     * @return the splice sites
     */
    public Set<SpliceEventFeature> getSpliceEventFeatures() {
        return spliceEventFeatures;
    }

    /**
     * Sets the splice events the peptide spans.
     *
     * @param spliceEventFeatures the splice events
     */
    public void setSpliceEventFeatures(Set<SpliceEventFeature> spliceEventFeatures) {
        this.spliceEventFeatures=spliceEventFeatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeptideFeature)) return false;

        PeptideFeature that = (PeptideFeature) o;

        if (!location.equals(that.getLocation())) return false;
        if (!proteases.equals(that.getProteases())) return false;

        return true;
    }

    @Override
    public int hashCode() {        
        int result = location.hashCode();
        result = 31 * result + proteases.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PeptideFeatureImpl{" +
                "location=" + location +
                ", proteases=" + proteases +
                '}';
    }
}
