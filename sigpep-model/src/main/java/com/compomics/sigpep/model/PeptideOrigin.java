package com.compomics.sigpep.model;

/**
 * Specifies from which part of a protein a peptide originates.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 13-Mar-2008<br/>
 * Time: 09:49:40<br/>
 */
public enum PeptideOrigin {

    N_TERMINAL("N-terminal"),
    INTERNAL("internal"),
    C_TERMINAL("C-terminal"),
    UNKNOWN("unknown");

    /**
     * the origin name
     */
    private final String name;

    /**
     * Creates an origing with name <code>name</code>.
     *
     * @param name the product ion name
     */
    PeptideOrigin(String name) {
        this.name = name;
    }

    /**
     * Returns the origin name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
