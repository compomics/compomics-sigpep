package com.compomics.sigpep.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 07-Mar-2008<br/>
 * Time: 15:13:43<br/>
 */
public interface Transition {

    /**
     * Returns the product ions defining the transition.
     *
     * @return a list of product ions
     */
    List<ProductIon> getProductIons();

    /**
     * Sets the product ions defining the transition.
     *
     * @param productIons the product ions
     */
    void setProductIons(List<ProductIon> productIons);
/**
     * Returns the peptide identified by the transition.
     *
     * @return the peptide
     */
    Peptide getPeptide();

    /**
     * Adds a product ion to the set of product ions defining the transition.
     *
     * @param type   the product ion type
     * @param length the product ion length
     */
    void addProductIon(ProductIonType type, int length);
}
