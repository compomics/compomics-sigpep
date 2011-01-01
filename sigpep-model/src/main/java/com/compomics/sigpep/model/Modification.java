package com.compomics.sigpep.model;

/**
 * A post-translational protein modification.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Feb-2008<br/>
 * Time: 15:18:00<br/>
 */
public interface Modification {

    /**
     * Returns the one-letter amino acid code of the residue the
     * PTM occurs.
     *
     * @return the one-letter amino acid code of the residue
     */
    String getResidue();

    /**
     * Sets the one-letter amino acid code of the residue the
     * PTM occurs.
     *
     * @param residue the one-letter amino acid code
     */
    void setResidue(String residue);

    /**
     * Returns the PTM name.
     *
     * @return the PTM name
     */
    String getName();

    /**
     * Sets the PTM name.
     *
     * @param name the PTM name
     */
    void setName(String name);

    /**
     * Returns the description of the PTM.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Sets the description of the PTM.
     *
     * @param description the description
     */
    void setDescription(String description);

    /**
     * Returns the mass difference resulting from the PTM.
     *
     * @return the mass difference in Da
     */
    double getMassDifference();


    /**
     * Sets the mass difference resulting from the PTM.
     *
     * @param massDifference the mass difference in Da
     */
    void setMassDifference(double massDifference);

    /**
     * Returns whether the PTM is stable.
     *
     * @return true if the PTM is stable
     */
    boolean isStable();

    /**
     * Sets whether the PTM is stable.
     *
     * @param stable true if the PTM is stable
     */
    void setStable(boolean stable);

    /**
     * Returns whether the PTM is static.
     *
     * @return true if the PTM is static
     */
    boolean isStatic();

    /**
     * Sets whether the PTM is static.
     *
     * @param stable true if the PTM is static
     */
    void setStatic(boolean stable);

    /**
     * Returns the chemical formula of the modification.
     *
     * @return the formula
     */
    String getFormula();

    /**
     * Sets the chemical formula of the modification.
     *
     * @param formula the formula
     */
    void setFormula(String formula);

    /**
     * Returns the position of the modification e.g. N-term, C-term.
     *
     * @return the position
     */
    ModificationPosition getPosition();

    /**
     * Returns whether or not the modification depends on the position of the residue.
     *
     * @return true if the modification depends on the position of the residue
     */
    boolean isPositional();

}
