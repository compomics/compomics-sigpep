package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.Feature;
import com.compomics.sigpep.model.FeatureObject;
import com.compomics.sigpep.model.SequenceLocation;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 14:57:07<br/>
 */
public abstract class AbstractFeature<O  extends FeatureObject> implements Feature<O> {

    protected SequenceLocation location;
    protected O featureObject;

    /**
     * @TODO: JavaDoc missing
     */
    protected AbstractFeature() {
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param location
     * @param featureObject
     */
    protected AbstractFeature(SequenceLocation location, O featureObject) {
        this.location = location;
        this.featureObject = featureObject;
    }

    /**
     * Returns the location of the implementing object.
     *
     * @return the object locations
     */
    public SequenceLocation getLocation() {
        return location;
    }

    /**
     * Sets the location of the implementing object.
     *
     * @param location the object location
     */
    public void setLocation(SequenceLocation location) {
        this.location=location;
    }

    /**
     * Returns the object representing the feature.
     *
     * @return the feature object
     */
    public O getFeatureObject() {
        return featureObject;
    }

    /**
     * The the object representing the feature.
     *
     * @param featureObject the feature object
     */
    public void setFeatureObject(O featureObject) {
        this.featureObject=featureObject;
    }
}
