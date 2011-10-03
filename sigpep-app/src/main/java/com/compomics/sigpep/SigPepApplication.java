package com.compomics.sigpep;

import com.compomics.sigpep.model.ProductIonType;

import java.util.Set;

/**
 * The SigPepApplication provides access to the SigPepSessionFactory
 * and to the default settings.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 14:08:15<br/>
 */
public interface SigPepApplication {

    /**
     * Returns an instance of the SigPepSessionFactory.
     *
     * @return the SigPepSession factory
     */
    SigPepSessionFactory getSigPepSessionFactory();

    /**
     * Sets the SigPepSessionFactory of the SigPepApplication.
     *
     * @param sigPepSessionFactory the SigPepSession factory
     */
    void setSigPepSessionFactory(SigPepSessionFactory sigPepSessionFactory);

    /**
     * Returns the default set of target product ion types considered
     * in signature transition searches.
     *
     * @return a set of ProductIonTypes
     */
    public Set<ProductIonType> getDefaultTargetProductIonTypes();

    /**
     * Returns the default set of background product ion types considered
     * in signature transition searches.
     *
     * @return a set of ProductIonTypes
     */
    public Set<ProductIonType> getDefaultBackgroundProductIonTypes();

    /**
     * Returns the default set of precursor ion charges considered
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public Set<Integer> getDefaultPrecursorIonChargeStates();

    /**
     * Returns the default set of product ion charges considered
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public Set<Integer> getDefaultProductIonChargeStates();

    /**
     * Returns the default mass accuracy used
     * in signature transition searches.
     *
     * @return a set of charge states
     */
    public double getDefaultMassAccuracy();

    /**
     * Returns the default minimum number of product ions
     * defining a signature transition.
     *
     * @return the minimum number of product ions defining a signature transition
     */
    public int getDefaultMinimumSignatureTransitionSize();

    /**
     * Returns the default maximum number of product ions
     * defining a signature transition.
     *
     * @return the maximum number of product ions defining a signature transition
     */
    public int getDefaultMaximumSignatureTransitionSize();
}
