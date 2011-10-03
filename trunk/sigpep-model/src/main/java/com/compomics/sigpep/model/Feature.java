package com.compomics.sigpep.model;

/**
 * Maps an object to a sequence.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 19-Feb-2008<br/>
 * Time: 14:48:56<br/>
 */
public interface Feature<O extends FeatureObject> extends Locatable {

    /**
     * Returns the object representing the feature.
     *
     * @return the feature object
     */
    O getFeatureObject();

    /**
     * The the object representing the feature.
     *
     * @param featureObject the feature object
     */
    void setFeatureObject(O featureObject);

}
