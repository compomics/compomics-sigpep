package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * Implementation of PrecursorIon
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 12-Oct-2007<br>
 * Time: 12:35:31<br>
 */
public class PrecursorIonImpl extends AbstractPeptideIon implements PrecursorIon {

    /**
     * the neutral mass of the peptide residues excluding N- and C-terminal groups
     */
    private double residueMass;

    /**
     * default mass of the N-terminal group (default is mass of hydrogen)
     */
    private double massNTerminalGroup = massH;

    /**
     * default mass of the C-terminal group (mass of an hydroxy group)
     */
    private double massCTerminalGroup = massH + massO;

    /**
     * the peptide this the peptide ion oroginates from
     */
    public Peptide peptide;

    /**
     * Constructs a <code>PrecursorIon</code> based on a <code>LocatablePeptide</code> object.
     *
     * @param peptide the peptide object
     */
    PrecursorIonImpl(Peptide peptide) {
        this.peptide = peptide;
        this.residueMass = calculateResidueMass(peptide.getSequenceString());
    }

    /**
     * Returns the peptide the peptide ion is derived from if it has been constructed using a LocatablePeptide object.
     *
     * @return the LocatablePeptide and null if the PrecursorIon has been constructed from a sequence string
     */
    public Peptide getPeptide() {
        return peptide;
    }

    /**
     * Returns the product ion of the specified type and length.
     * For d, v and w ions for which there are more then one possible
     * product ion use <code>getProductIons(ProductIonType type, int length)</code>
     * instead.
     *
     * @param type   the product ion type
     * @param length the product ion length
     * @return the product ion
     */
    public ProductIon getProductIon(ProductIonType type, int length) {

        if (length > peptide.getSequenceLength()) {
            throw new IllegalArgumentException("The product ion length (" + length + ") cannot be greater then the peptide length (" + peptide.getSequenceLength() + "). ");
        }

        int startCoordinate = 0;
        int endCoordinate = 0;

        if (type == ProductIonType.A ||
                type == ProductIonType.A_CIRCLE ||
                type == ProductIonType.A_STAR ||
                type == ProductIonType.B ||
                type == ProductIonType.B_CIRCLE ||
                type == ProductIonType.B_STAR ||
                type == ProductIonType.C) {

            startCoordinate = 1;
            endCoordinate = length;

        } else if (type == ProductIonType.X ||
                type == ProductIonType.Y ||
                type == ProductIonType.Y_CIRCLE ||
                type == ProductIonType.Y_STAR ||
                type == ProductIonType.Z) {

            startCoordinate = this.getSequenceLength() - length + 1;
            endCoordinate = this.getSequenceLength();

        } else {
            throw new UnsupportedOperationException("Prodcut ion type " + type + " not supported.");
            //TODO implement d, v, and w ions
        }

        return new ProductIonImpl(this, type, startCoordinate, endCoordinate);
    }

    /**
     * Returns all product ions of the specified type that can be emitted by the precursor ion.
     *
     * @param type the product ion type
     * @return the product ions
     */
    public List<ProductIon> getProductIons(ProductIonType type) {

        List<ProductIon> retVal = new ArrayList<ProductIon>();

        for (int l = 1; l <= this.getSequenceLength(); l++) {
            ProductIon pi = getProductIon(type, l);
            retVal.add(pi);
        }

        return retVal;
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) N-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassNTerminalGroup() {
        return massNTerminalGroup;
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) C-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassCTerminalGroup() {
        return massCTerminalGroup;
    }

    /**
     * Sets the neutral molecular mass of the (uncharged) N-terminal group (default value is the monoisotopic mass of hydrogen (H)).
     *
     * @param massNTerminalGroup the monoisotopic mass in Da
     */
    public void setNeutralMassNTerminalGroup(double massNTerminalGroup) {
        this.massNTerminalGroup = massNTerminalGroup;
    }

    /**
     * Sets the neutral molecular mass of the (uncharged) C-terminal group (default value is the monoisotopic mass of an hydroxy group (OH)).
     *
     * @param massCTerminalGroup the monoisotopic mass in Da
     */
    public void setNeutralMassCTerminalGroup(double massCTerminalGroup) {
        this.massCTerminalGroup = massCTerminalGroup;
    }

    /**
     * Returns a string representation of the peptide sequence.
     *
     * @return the peptide sequence string
     */
    public String getSequenceString() {
        return peptide.getSequenceString();
    }

    /**
     * Returns the length of the peptide sequence.
     *
     * @return the length
     */
    public int getSequenceLength() {
        return peptide.getSequenceLength();
    }

    /**
     * Returns the theoretical m/z value of the precursor ion with charge z.
     *
     * @param z the ion charge
     * @return the m/z value
     */
    public double getMassOverCharge(int z) {
        return calculateMassOverCharge(getNeutralMassPeptide(), z);
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) amino acid residues
     * (excluding N- and C-terminal groups).
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassResidues() {
        double unmodifiedMass = residueMass;
        double modificationMass = getModificationMass();
        return unmodifiedMass + modificationMass;
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) peptide
     * (neutral mass N-terminal group + neutral mass residues + neutral mass C-terminal group).
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassPeptide() {
        double unmodifiedMass = massNTerminalGroup + residueMass + massCTerminalGroup;
        double modificationMass = getModificationMass();
        return unmodifiedMass + modificationMass;
    }

    /**
     * Returns the mass of the post-translational modifications.
     *
     * @return the monoisotopic mass in Da
     */
    public double getModificationMass() {

        double retVal = 0;

        if (peptide instanceof ModifiedPeptide) {
            ModifiedPeptide modifiedPeptide = (ModifiedPeptide) peptide;

            for (Integer pos : modifiedPeptide.getPostTranslationalModifications().keySet()) {
                Modification ptm = modifiedPeptide.getPostTranslationalModifications().get(pos);
                double mass = ptm.getMassDifference();
                retVal = retVal + mass;
            }
        }

        return retVal;
    }

    /**
     * Returns a list of post-translational modifications occuring on the peptide.
     *
     * @return a list of PTMs which is empty if there are no PTMs occuring on the peptide
     */
    public Collection<Modification> getPostTranslationalModifications() {
        Collection<Modification> retVal = new ArrayList<Modification>();
        if (peptide instanceof ModifiedPeptide) {
            ModifiedPeptide modifiedPeptide = (ModifiedPeptide) peptide;
            retVal = modifiedPeptide.getPostTranslationalModifications().values();
        }
        return retVal;
    }

    /**
     * Tests for equality of two peptide ions based on their proteinSequence.
     *
     * @param o the object to test for equality with
     * @return true if the PrecursorIonImpl is the same object or has identical proteinSequence
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrecursorIonImpl that = (PrecursorIonImpl) o;

        if (!peptide.equals(that.peptide)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return peptide.hashCode();
    }

    /**
     * Compares to PrecursorIonImpl objects based on their proteinSequence.
     *
     * @param that the other PrecursorIonImpl
     * @return
     */
    public int compareTo(PrecursorIon that) {
        return this.getSequenceString().compareTo(that.getSequenceString());
    }

    @Override
    public String toString() {
        return "Precursor ion " + this.getSequenceString() + "; length = " + this.getSequenceLength() + "; M(neutral) = " + this.getNeutralMassPeptide();
    }
}
