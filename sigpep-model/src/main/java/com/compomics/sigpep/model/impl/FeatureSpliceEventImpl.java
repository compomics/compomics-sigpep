package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @TODO: JavaDoc missing
 *
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
        this.features=features;
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
