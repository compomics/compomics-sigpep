package com.compomics.sigpep.analysis;

/**
     * The different types of signature transition searches.
     * <p/>
     * Created by IntelliJ IDEA.<br>
     * User: mmueller<br>
     * Date: 04-Aug-2008<br>
     * Time: 15:12:48<br>
     */
public enum SignatureTransitionFinderType {

    /**
     * Returns the first signature transition found
     */
    FIRST,

    /**
     * Returns only signature transition with
     * the minimal number of product ions required
     * for the transition to be unique
     */
    MINIMAL,

    /**
     * Returns all signature transitions found
     */
    ALL


}
