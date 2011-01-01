package com.compomics.sigpep.impl;

import com.compomics.sigpep.SigPepApplication;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.model.ProductIonType;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * The SigPepApplication bean.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 14:17:21<br/>
 */
public class SigPepApplicationImpl implements SigPepApplication {

    /** provides access to the configuration in the sigpep-app.properties file */
    private static Configuration config = Configuration.getInstance();

    /** the default target product ion types considered in signature transition searches */
    private static Set<ProductIonType> defaultTargetProductIonTypes = new HashSet<ProductIonType>();

    /** the default background product ion types considered in signature transition searches */
    private static Set<ProductIonType> defaultBackgroundProductIonTypes = new HashSet<ProductIonType>();

    /** the default precursor ion charge states used in signature transition searches */
    private static Set<Integer> defaultPrecursorIonChargeStates = new HashSet<Integer>();

    /** the default product ion charge states used in signature transition searches */
    private static Set<Integer> defaultProductIonChargeStates = new HashSet<Integer>();

    /** the default mass accuracy used in signature transition searches */
    private static double defaultMassAccuracy = config.getDouble("sigpep.app.mass.accuracy");

    /** the default minimal number of product ions defining a signature transition */
    private static int defaultMinimumSignatureTransitionSize = config.getInt("sigpep.app.min.signature.transition.size");

    /** the default maximum number of product ions defining a signature transition */
    private static int defaultMaximumSignatureTransitionSize = config.getInt("sigpep.app.max.signature.transition.size");

    static{

        //populate target product ion type set
        List<String> targetProductIonTypes = config.getList("sigpep.app.target.product.ion.types");
        for(String type : targetProductIonTypes){
            defaultTargetProductIonTypes.add(ProductIonType.valueOf(type));
        }

        //populate default target product ion set
        List<String> backgroundProductIonTypes = config.getList("sigpep.app.background.product.ion.types");
        for(String type : backgroundProductIonTypes){
            defaultBackgroundProductIonTypes.add(ProductIonType.valueOf(type));
        }

        //populate default precursor ion charge state set
        List<String> precursorIonChargeStates = config.getList("sigpep.app.precursor.ion.charge.states");
        for(String chargeState : precursorIonChargeStates){
            defaultPrecursorIonChargeStates.add(new Integer(chargeState));
        }

        //populate default product ion charge state set
        List<String> productIonChargeStates = config.getList("sigpep.app.product.ion.charge.states");
        for(String chargeState : productIonChargeStates){
            defaultProductIonChargeStates.add(new Integer(chargeState));
        }
    }

    /** the SigPepSession factory  */
    private SigPepSessionFactory sigPepSessionFactory;

    /**
     * Returns an instance of the SigPepSessionFactory.
     *
     * @return the SigPepSession factory
     */
    public SigPepSessionFactory getSigPepSessionFactory() {
        return sigPepSessionFactory;
    }

    /**
     * Sets the SigPepSessionFactory of the SigPepApplication.
     *
     * @param sigPepSessionFactory the SigPepSession factory
     */
    public void setSigPepSessionFactory(SigPepSessionFactory sigPepSessionFactory) {
        this.sigPepSessionFactory=sigPepSessionFactory;
    }

    /**
     * Returns the default set of target product ion types considered
     * in signature transition searches.
     *
     * @return a set of ProductIonTypes
     */
    public Set<ProductIonType> getDefaultTargetProductIonTypes() {
        return defaultTargetProductIonTypes;
    }

    /**
     * Returns the default set of background product ion types considered
     * in signature transition searches.
     *
     * @return a set of ProductIonTypes
     */
    public Set<ProductIonType> getDefaultBackgroundProductIonTypes() {
        return defaultBackgroundProductIonTypes;
    }

    /**
     * Returns the default set of precursor ion charges considered
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public Set<Integer> getDefaultPrecursorIonChargeStates() {
        return defaultPrecursorIonChargeStates;
    }

    /**
     * Returns the default set of product ion charges considered
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public Set<Integer> getDefaultProductIonChargeStates() {
        return defaultProductIonChargeStates;
    }

    /**
     * Returns the default mass accuracy used
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public double getDefaultMassAccuracy() {
        return defaultMassAccuracy;
    }

    /**
     * Returns the default minimum number of product ions
     * defining a signature transition.
     *
     * @return the minimum number of product ions defining a signature transition
     */
    public int getDefaultMinimumSignatureTransitionSize() {
        return defaultMinimumSignatureTransitionSize;
    }

    /**
     * Returns the default maximum number of product ions
     * defining a signature transition.
     *
     * @return the maximum number of product ions defining a signature transition
     */
    public int getDefaultMaximumSignatureTransitionSize() {
        return defaultMaximumSignatureTransitionSize;
    }
}
