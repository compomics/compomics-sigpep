package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 19:05:15<br/>
 */
public class FeatureSpliceEventImpl extends SpliceEventImpl implements FeatureSpliceEvent, Persistable {

    private int id;
    private Object sessionFactory;

    private Set<SpliceEventFeature> features;

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     * @TODO: JavaDoc missing
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Object getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory
     * @TODO: JavaDoc missing
     */
    public void setSessionFactory(Object sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Returns the features represented by the object.
     *
     * @return the features
     */
    public Set<SpliceEventFeature> getFeatures() {
        return features;
    }

    /**
     * Sets the features represented by the object.
     *
     * @param features the features
     */
    public void setFeatures(Set<SpliceEventFeature> features) {
        this.features = features;
    }

    /**
     * {@inherit}
     */
    public Set<ProteinSequence> getParentSequences() {

        Set<ProteinSequence> retVal = new HashSet<ProteinSequence>();

        for (Feature f : features) {
            retVal.add(f.getLocation().getSequence());
        }

        return retVal;
    }
}
