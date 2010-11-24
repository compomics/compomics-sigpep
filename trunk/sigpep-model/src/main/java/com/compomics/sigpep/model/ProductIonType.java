package com.compomics.sigpep.model;

/**
 * The different types of product ions that can arise
 * when a precursor ion is fragmented.
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 26-Oct-2007<br>
 * Time: 15:12:48<br>
 */
public enum ProductIonType {

    A("a"),
    A_STAR("a*"),
    A_CIRCLE("a°"),
    B("b"),
    B_STAR("b*"),
    B_CIRCLE("b°"),
    C("c"),
    X("x"),
    Y("y"),
    Y_STAR("y*"),
    Y_CIRCLE("y°"),
    Z("z");
    //    D("d"),
    //    V("v"),
    //    W("w"),

    /**
     * the product ion name
     */
    private final String name;

    /**
     * Creates a product ion of with name <code>name</code>.
     *
     * @param name the product ion name
     */
    ProductIonType(String name) {
        this.name = name;
    }

    /**
     * Returns the product ion name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
