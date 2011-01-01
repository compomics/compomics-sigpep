package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.Set;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 15:09:26<br/>
 */
public class SpliceEventFeatureImpl extends AbstractFeature<FeatureSpliceEvent> implements SpliceEventFeature, Persistable {

    private int id;
    private Object sessionFactory;
    private FeatureSpliceEvent spliceEvent;
    private Set<PeptideFeature> peptideFeatures;

    /**
     * Returns the object representing the feature.
     *
     * @return the feature object
     */
    public FeatureSpliceEvent getFeatureObject() {
        return spliceEvent;
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
     * The the object representing the feature.
     *
     * @param spliceEvent the feature object
     */
    public void setFeatureObject(FeatureSpliceEvent spliceEvent) {
        this.spliceEvent = spliceEvent;
    }

    /**
     * Returns the peptides spanning the splice site in the translation.
     *
     * @return the peptides
     */
    public Set<PeptideFeature> getPeptideFeatures() {
        return peptideFeatures;
    }

    /**
     * Sets the peptides spanning the splice site in the translation.
     *
     * @param peptideFeatures the peptides
     */
    public void setPeptideFeatures(Set<PeptideFeature> peptideFeatures) {
        this.peptideFeatures = peptideFeatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpliceEventFeature)) {
            return false;
        }

        SpliceEventFeature that = (SpliceEventFeature) o;

        if (!spliceEvent.equals(that.getFeatureObject())) {
            return false;
        }
        if (!location.equals(that.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = spliceEvent.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SpliceEventFeatureImpl{"
                + "spliceEvent=" + spliceEvent
                + ", location=" + location + "}";
    }
}
