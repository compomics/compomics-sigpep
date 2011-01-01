package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.*;

/**
 * @TODO: JavaDoc missing
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 07-Mar-2008<br/>
 * Time: 15:14:38<br/>
 */
public class TransitionImpl implements Transition {

    private Peptide peptide;
    private List<ProductIon> productIons;

    /**
     * @TODO: JavaDoc missing
     *
     * @param peptide
     */
    public TransitionImpl(Peptide peptide) {
        this.peptide = peptide;
        this.productIons = new ArrayList<ProductIon>();
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param productIons
     */
    public TransitionImpl(List<ProductIon> productIons) {

        if(productIons.size() == 0){
            throw new IllegalArgumentException("The collection of product ions defining the transition has to contain at least one precursor.");                              
        }

        productIonSanityCheck(productIons);

        this.productIons = productIons;
        this.peptide = productIons.iterator().next().getPrecursorIon().getPeptide();
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public List<ProductIon> getProductIons() {
        return productIons;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param productIons
     */
    public void setProductIons(List<ProductIon> productIons) {
        productIonSanityCheck(productIons);
        this.productIons = productIons;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Peptide getPeptide() {
        return peptide;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param type
     * @param length
     */
    public void addProductIon(ProductIonType type, int length){
        ProductIon  pi = peptide.getPrecursorIon().getProductIon(type, length);
        this.productIons.add(pi);
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param productIons
     * @throws IllegalArgumentException
     */
    private void productIonSanityCheck(Collection<ProductIon> productIons) throws IllegalArgumentException {

        //check that all product ions come from the same precursor
        PrecursorIon previousPrecursor = null;
        for(ProductIon product : productIons){
            PrecursorIon precursor = product.getPrecursorIon();
            if(precursor == null){
                throw new IllegalArgumentException("The precursor ion of one or more product ion(s) is NULL.");
            }
            if(previousPrecursor != null && !precursor.equals(previousPrecursor)){
                throw new IllegalArgumentException("All product ions defining the transition have to come from the same precursor.");
            }
            previousPrecursor = precursor;
        }
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public String toString() {
        return "TransitionImpl{" +
                "productIons=" + productIons +
                '}';
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;

        Transition that = (Transition) o;

        if (!productIons.equals(that.getProductIons())) return false;

        return true;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int hashCode() {
        return productIons.hashCode();
    }
}
