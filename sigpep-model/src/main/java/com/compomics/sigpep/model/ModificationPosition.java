package com.compomics.sigpep.model;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 13-Mar-2008<br/>
 * Time: 10:54:40<br/>
 */
public enum ModificationPosition {

    N_TERMINAL("N-terminal", 0),
    C_TERMINAL("C-terminal", -1),
    INTERNAL("internal", -2),
    NON_POSITIONAL("non-positional", -3);

    /**
     * the position name
     */
    private final String name;

    /**
     * the position integer value
     */
    private final int integerValue;

    /**
     * Creates a position with name <code>name</code> and integer value <code>integerValue</value>
     *
     * @param name the position name
     * @param integerValue the position integer value
     */
    ModificationPosition(String name, int integerValue) {
        this.name = name;
        this.integerValue = integerValue;
    }

    /**
     * Returns the position name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the position name.
     *
     * @return the name
     */
    public int getIntegerValue() {
        return integerValue;
    }
}
