package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductIon.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jan-2008<br/>
 * Time: 15:49:25<br/>
 */
public class ProductIonImpl extends AbstractPeptideIon implements ProductIon {

    private PrecursorIon precursorIon;
    private ProductIonType type;
    private int startCoordinate;
    private int endCoordinate;

    /**
     * Constructs a product ion of specified type from a precursor ion.
     *
     * @param precursorIon    the precursor ion
     * @param type            the product ion type
     * @param startCoordinate the start coordinate in the precursor ion proteinSequence
     * @param endCoordinate   the end coordinate in the precursor ion proteinSequence
     */
    ProductIonImpl(PrecursorIon precursorIon,
                   ProductIonType type,
                   int startCoordinate,
                   int endCoordinate) {

        if (precursorIon == null) {
            throw new IllegalArgumentException("PrecursorIon cannot be NULL.");
        }

        if (type == null) {
            throw new IllegalArgumentException("ProductIonType cannot be NULL.");
        }

        if (startCoordinate < 1 || startCoordinate > precursorIon.getSequenceLength()) {
            throw new IllegalArgumentException("Start coordinate out of range: " + startCoordinate + "; precursor ion length = " + precursorIon.getSequenceLength());
        }

        if (endCoordinate < 1 || endCoordinate > precursorIon.getSequenceLength()) {
            throw new IllegalArgumentException("End coordinate out of range: " + endCoordinate + "; precursor ion length = " + precursorIon.getSequenceLength());
        }

        if (startCoordinate > endCoordinate) {
            throw new IllegalArgumentException("Start coordinate has to be smaller or equal to end coordinate. Start coordinate = " + startCoordinate + "; end coordinate = " + endCoordinate);
        }

        this.precursorIon = precursorIon;
        this.type = type;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    /**
     * Returns the peptide proteinSequence.
     *
     * @return the proteinSequence string
     */
    public String getSequenceString() {
        return precursorIon.getSequenceString().substring(startCoordinate - 1, endCoordinate);
    }

    /**
     * Returns the length of the peptide proteinSequence.
     *
     * @return the length
     */
    public int getSequenceLength() {
        return endCoordinate - startCoordinate + 1;
    }

    /**
     * {@inherit}
     * <p/>
     * If the product ion is derived from the C-terminal part of the precursor ion (x,y and z ions)
     * the mass returned is equal to the mass of the C-terminal group of the precursor ion. Otherwise
     * the mass is 0.
     */
    public double getNeutralMassCTerminalGroup() {

        double retVal = 0;

        if (type == ProductIonType.X
                || type == ProductIonType.Y
                || type == ProductIonType.Y_CIRCLE
                || type == ProductIonType.Y_STAR
                || type == ProductIonType.Z) {

            retVal = precursorIon.getNeutralMassCTerminalGroup();

        }

        return retVal;
    }

    /**
     * {@inherit}
     * <p/>
     * If the product ion is derived from the N-terminal part of the precursor ion (a,b and c ions)
     * the mass returned is equal to the mass of the N-terminal group of the precursor ion. Otherwise
     * the mass is 0.
     */
    public double getNeutralMassNTerminalGroup() {

        double retVal = 0;

        if (type == ProductIonType.A
                || type == ProductIonType.A_CIRCLE
                || type == ProductIonType.A_STAR
                || type == ProductIonType.B
                || type == ProductIonType.B_CIRCLE
                || type == ProductIonType.B_STAR
                || type == ProductIonType.C) {

            retVal = precursorIon.getNeutralMassNTerminalGroup();

        }

        return retVal;
    }

    /**
     * Returns the product ion type.
     *
     * @return the type
     */
    public ProductIonType getType() {
        return type;
    }

    /**
     * Returns the precursor ion the product ion has been emitted from.
     *
     * @return the precursor ion
     */
    public PrecursorIon getPrecursorIon() {
        return precursorIon;
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) peptide
     * (neutral mass N-terminal group + neutral mass residues + neutral mass C-terminal group).
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassPeptide() {
        double unmodifiedMass = this.calculateNeutralMass(type);
        double modificationMass = getModificationMass();
        return unmodifiedMass + modificationMass;
    }

    /**
     * Returns the neutral molecular mass of the (uncharged) amino acid residues
     * (excluding N- and C-terminal groups).
     *
     * @return the monoisotopic mass in Da
     */
    public double getNeutralMassResidues() {
        double unmodifiedMass = calculateResidueMass(this.getSequenceString());
        double modificationMass = getModificationMass();
        return unmodifiedMass + modificationMass;
    }

    /**
     * Returns a list of post-translational modifications present on the fragment.
     *
     * @return a list of PTMs which is empty if there are not PTMs occuring on the fragment
     */
    public List<Modification> getPostTranslationalModifications() {
        List<Modification> retVal = new ArrayList<Modification>();
        if (precursorIon.getPeptide() instanceof ModifiedPeptide) {
            ModifiedPeptide modifiedPeptide = (ModifiedPeptide) precursorIon.getPeptide();
            for (Integer pos : modifiedPeptide.getPostTranslationalModifications().keySet()) {

                //check if modification is in coordinate range of fragment ion
                if (pos >= startCoordinate && pos <= endCoordinate) {
                    Modification ptm = modifiedPeptide.getPostTranslationalModifications().get(pos);
                    retVal.add(ptm);
                }

            }

        }

        return retVal;
    }

    /**
     * Returns the mass of the post-translational modifications.
     *
     * @return the monoisotopic mass in Da
     */
    public double getModificationMass() {
        double retVal = 0;

        if (precursorIon.getPeptide() instanceof ModifiedPeptide) {
            ModifiedPeptide modifiedPeptide = (ModifiedPeptide) precursorIon.getPeptide();
            for (Integer pos : modifiedPeptide.getPostTranslationalModifications().keySet()) {

                //check if modification is in coordinate range of fragment ion
                if (pos >= startCoordinate && pos <= endCoordinate) {
                    Modification ptm = modifiedPeptide.getPostTranslationalModifications().get(pos);
                    double mass = ptm.getMassDifference();
                    retVal = retVal + mass;
                }
            }
        }

        return retVal;
    }

    /**
     * Returns the neutral molecular mass of the product ion.
     * </p>
     * Neutral product masses are calculated as follows:<br/>
     * <br/>
     * Ion<br/>
     * Type 	Neutral Mr <br/>
     * a    	[N]+[M]-CHO<br/>
     * a* 	    a-NH3<br/>
     * a°  	    a-H2O<br/>
     * b   	    [N]+[M]-H<br/>
     * b*  	    b-NH3<br/>
     * b°  	    b-H2O<br/>
     * c    	[N]+[M]+NH2<br/>
     * d   	    a - partial side chain  (not implemented)<br/>
     * v   	    y - complete side chain (not implemented)<br/>
     * w   	    z - partial side chain  (not implemented)<br/>
     * x   	    [C]+[M]+CO-H<br/>
     * y   	    [C]+[M]+H<br/>
     * y*  	    y-NH3<br/>
     * y°  	    y-H2O<br/>
     * z   	    [C]+[M]-NH2<br/>
     * <p/>
     * [N] = molecular mass of the neutral N-terminal group<br/>
     * [C] = molecular mass of the neutral C-terminal group<br/>
     * [M] = molecular mass of the neutral amino acid residues<br/>
     *
     * @param type the product ion type
     * @return the product ion mass
     */
    private double calculateNeutralMass(ProductIonType type) {

        double fragmentResidueMass;
        double retVal;

        switch (type) {

            case A:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassNTerminalGroup() + fragmentResidueMass - (massC + massH + massO);
                break;

            case A_STAR:

                retVal = calculateNeutralMass(ProductIonType.A) - (massN + 3 * massH);
                break;

            case A_CIRCLE:

                retVal = calculateNeutralMass(ProductIonType.A) - (2 * massH + massO);
                break;

            case B:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassNTerminalGroup() + fragmentResidueMass - massH;
                break;

            case B_STAR:

                retVal = calculateNeutralMass(ProductIonType.B) - (massN + 3 * massH);
                break;

            case B_CIRCLE:

                retVal = calculateNeutralMass(ProductIonType.B) - (2 * massH + massO);
                break;

            case C:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassNTerminalGroup() + fragmentResidueMass + (massN + 2 * massH);
                break;

            case X:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassCTerminalGroup() + fragmentResidueMass + (massC + massO) - massH;
                break;

            case Y:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassCTerminalGroup() + fragmentResidueMass + massH;
                break;

            case Y_STAR:

                retVal = calculateNeutralMass(ProductIonType.Y) - (massN + 3 * massH);
                break;


            case Y_CIRCLE:

                retVal = calculateNeutralMass(ProductIonType.Y) - (2 * massH + massO);
                break;

            case Z:

                fragmentResidueMass = calculateResidueMass(this.getSequenceString());
                retVal = this.getNeutralMassCTerminalGroup() + fragmentResidueMass + (massN + 2 * massH);
                break;

            default:

                retVal = -1;
        }

        return retVal;
    }

    /**
     * Returns the theoretical m/z value of the product ion.
     *
     * @param z the ion charge
     * @return the product ion m/z value
     */
    public double getMassOverCharge(int z) {
        double neutralMass = this.getNeutralMassPeptide();
        return calculateMassOverCharge(neutralMass, z);
    }

    /**
     * Returns the number of occurances of an amino acid in the ion proteinSequence.
     *
     * @param aminoAcid the amino acid
     * @return the number of occurances
     */
    public int getResidueCount(String aminoAcid) {
        String sequenceWithoutResidues = this.getSequenceString().toUpperCase().replaceAll(aminoAcid.toUpperCase(), "");
        return this.getSequenceString().length() - sequenceWithoutResidues.length();
    }

    /**
     * Checks for equality to another object.
     *
     * @param o the object to check for quality
     * @return two product ions are equal if the have to same precursor ion,
     *         start and end coordinates and are of the same type
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductIon)) {
            return false;
        }

        ProductIonImpl that = (ProductIonImpl) o;

        if (endCoordinate != that.endCoordinate) {
            return false;
        }
        if (startCoordinate != that.startCoordinate) {
            return false;
        }
        if (precursorIon != null ? !precursorIon.equals(that.precursorIon) : that.precursorIon != null) {
            return false;
        }
        if (type != that.type) {
            return false;
        }

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        int result;
        result = (precursorIon != null ? precursorIon.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + startCoordinate;
        result = 31 * result + endCoordinate;
        return result;
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "Product ion " + type.getName() + " (" + this.getSequenceLength() + ") of "
                + precursorIon.getSequenceString() + "["
                + startCoordinate + "-" + endCoordinate + "]"
                + "; M(neutral) = " + this.getNeutralMassPeptide()
                + "; m/z (z=1) = " + this.getMassOverCharge(1)
                + "; proteinSequence = " + this.getSequenceString();
    }
}
