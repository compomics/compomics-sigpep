package com.compomics.sigpep.model;

/**
 * Implemented by objects with a sequence location.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Feb-2008<br/>
 * Time: 15:40:31<br/>
 */
public interface Locatable {

    /**
     * Returns the location of the implementing object.
     *
     * @return the object location
     */
    SequenceLocation getLocation();

    /**
     * Sets the location of the implementing object.
     *
     * @param location the object location
     */
    void setLocation(SequenceLocation location);


}
