package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.ModificationPosition;
import com.compomics.sigpep.model.Modification;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Feb-2008<br/>
 * Time: 15:31:16<br/>
 */
public class ModificationImpl implements Modification {

    private String residue;
    private String name;
    private String description;
    private String formula;
    private double massDifference;
    private boolean stable;
    private boolean isStatic;
    private ModificationPosition position;

    /**
     * Constructs a post translational modification.
     *
     * @param description    the description
     * @param residue        the residue to be modified
     * @param name           the name of the modification
     * @param formula        the formula of the modification
     * @param massDifference the mass difference
     * @param isStable         whether the modification is stable
     * @param isStatic       whether the modification is static
     * @param position       the position of the modification
     */
    public ModificationImpl(String name,
                                             String description,
                                             String residue,
                                             String formula,
                                             double massDifference,
                                             boolean isStable,
                                             boolean isStatic,
                                             ModificationPosition position) {
        this.residue = residue;
        this.formula = formula;
        this.description = description;
        this.name = name;
        this.massDifference = massDifference;
        this.stable = isStable;
        this.isStatic = isStatic;
        this.position = position;
    }

    /**
     * Returns the one-letter amino acid code of the residue the
     * PTM occurs.
     *
     * @return the one-letter amino acid code of the residue
     */
    public String getResidue() {
        return residue;
    }

    /**
     * Sets the one-letter amino acid code of the residue the
     * PTM occurs.
     *
     * @param residue the one-letter amino acid code
     */
    public void setResidue(String residue) {
        this.residue = residue;
    }

    /**
     * Returns the PTM name.
     *
     * @return the PTM name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the PTM name.
     *
     * @param name the PTM name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the mass difference resulting from the PTM.
     *
     * @return the mass difference in Da
     */
    public double getMassDifference() {
        return massDifference;
    }

    /**
     * Sets the mass difference resulting from the PTM.
     *
     * @param massDifference the mass difference in Da
     */
    public void setMassDifference(double massDifference) {
        this.massDifference = massDifference;
    }

    /**
     * Returns whether the PTM is stable.
     *
     * @return true if the PTM is stable
     */
    public boolean isStable() {
        return stable;
    }

    /**
     * Sets whether the PTM is stable.
     *
     * @param stable true if the PTM is stable
     */
    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModificationPosition getPosition() {
        return position;
    }

    public void setPosition(ModificationPosition position) {
        this.position = position;
    }

    /**
     * Returns whether or not the modification depends on the position of the residue.
     *
     * @return true if the modification depends on the position of the residue
     */
    public boolean isPositional() {
        return (position == null || position != ModificationPosition.NON_POSITIONAL);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modification)) return false;

        Modification that = (Modification) o;

        if (isStatic != that.isStatic()) return false;
        if (stable != that.isStable()) return false;
        if (!formula.equals(that.getFormula())) return false;
        if (!name.equals(that.getName())) return false;
        if (!residue.equals(that.getResidue())) return false;
        if (!position.equals(that.getPosition())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = residue.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + formula.hashCode();
        result = 31 * result + (stable ? 1 : 0);
        result = 31 * result + (isStatic ? 1 : 0);
        result = 31 * result + position.hashCode();
        return result;
    }

    public String toString() {
        return "ModificationImpl{" +
                "name=" + name +
                ", residue=" + residue +
                ", formula=" + formula +
                ", massDifference=" + massDifference +
                ", isStatic=" + isStatic +
                ", isStable=" + stable +
                ", position=" + position +
                '}';
    }

}
