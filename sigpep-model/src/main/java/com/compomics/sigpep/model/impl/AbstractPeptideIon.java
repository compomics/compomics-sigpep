package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.PeptideIon;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.constants.MonoAminoAcidMasses;
import com.compomics.sigpep.model.constants.MonoElementMasses;

import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract implementation of PeptideIon.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jan-2008<br/>
 * Time: 15:41:56<br/>
 */
public abstract class AbstractPeptideIon implements PeptideIon {

    /**
     * the monoisotopic amino acid masses
     */
    protected static MonoAminoAcidMasses aaMasses = MonoAminoAcidMasses.getInstance();

    /**
     * the monoisotopic element masses
     */
    protected static MonoElementMasses elementMasses = MonoElementMasses.getInstance();

    /**
     * the monoisotopic mass of nitrogen
     */
    protected static double massN = elementMasses.getDouble("N");

    /**
     * the monoisotopic mass of carbon
     */
    protected static double massC = elementMasses.getDouble("C");

    /**
     * the monoisotopic mass of oxygen
     */
    protected static double massO = elementMasses.getDouble("O");

    /**
     * the monoisotopic mass of hydrogen
     */
    protected static double massH = elementMasses.getDouble("H");

    /**
     * the charge states the peptide ion is allowed to be in
     */
    protected Map<Integer, Double> allowedChargeStates = new TreeMap<Integer, Double>();

    /**
     * Calculates the neutral mass of a product ion of the given type and length.
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
     * @param sequence       the proteinSequence the fragment is derived from
     * @param type           the fragment ion type (a, a*, a°, b, b*, b°, c, x, y, y*, y°, z)
     * @param fragmentLength the length of the fragment
     * @param massNTerm      mass of the N-terminal group
     * @param massCTerm      mass of the C-terminal group
     * @return the fragment ion mass
     */
    public static double calculateProductIonMass(String sequence, ProductIonType type, int fragmentLength, double massNTerm, double massCTerm) {

        if (fragmentLength > sequence.length()){
            throw new IllegalArgumentException("The the length of the fragment ion cannot be greater then the length of the peptide proteinSequence. (" + fragmentLength + " > " + sequence.length() + ")");
        }

        String fragmentSequence;
        double fragmentResidueMass;
        double retVal;

        switch (type) {


            case A:

                fragmentSequence = sequence.substring(0, fragmentLength);
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massNTerm + fragmentResidueMass - (massC + massH + massO);
                break;

            case A_CIRCLE:

                retVal = calculateProductIonMass(sequence, ProductIonType.A, fragmentLength, massNTerm, massCTerm) - (2 * massH + massO);
                break;

            case A_STAR:

                retVal = calculateProductIonMass(sequence, ProductIonType.A, fragmentLength, massNTerm, massCTerm) - (massN + 3 * massH);
                break;

            case B:

                fragmentSequence = sequence.substring(0, fragmentLength);
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massNTerm + fragmentResidueMass - massH;
                break;

            case B_CIRCLE:

                retVal = calculateProductIonMass(sequence, ProductIonType.B, fragmentLength, massNTerm, massCTerm) - (2 * massH + massO);
                break;

            case B_STAR:

                retVal = calculateProductIonMass(sequence, ProductIonType.B, fragmentLength, massNTerm, massCTerm) - (massN + 3 * massH);
                break;

            case C:

                fragmentSequence = sequence.substring(0, fragmentLength);
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massNTerm + fragmentResidueMass + (massN + 2 * massH);
                break;

            case X:

                fragmentSequence = sequence.substring(sequence.length() - fragmentLength, sequence.length());
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massCTerm + fragmentResidueMass + (massC + massO) - massH;
                break;

            case Y:

                fragmentSequence = sequence.substring(sequence.length() - fragmentLength, sequence.length());
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massCTerm + fragmentResidueMass + massH;
                break;

            case Y_CIRCLE:

                retVal = calculateProductIonMass(sequence, ProductIonType.Y, fragmentLength, massNTerm, massCTerm) - (2 * massH + massO);
                break;

            case Y_STAR:

                retVal = calculateProductIonMass(sequence, ProductIonType.Y, fragmentLength, massNTerm, massCTerm) - (massN + 3 * massH);
                break;

            case Z:

                fragmentSequence = sequence.substring(sequence.length() - fragmentLength, sequence.length());
                fragmentResidueMass = calculateResidueMass(fragmentSequence);
                retVal = massCTerm + fragmentResidueMass + (massN + 2 * massH);
                break;

            default:

                retVal = -1;

        }

        return retVal;

    }

    /**
     * Calculates the molecular mass of the neutral residues of a peptide
     * excluding N-terminal group and C-terminal group.
     *
     * @param peptideSequence the peptide proteinSequence
     * @return the monoisotopic mass in Da
     */
    public static double calculateResidueMass(String peptideSequence) {

        //sum up residue masses
        double totalResidueMass = 0;
        for (char aa : peptideSequence.toCharArray())
            totalResidueMass += aaMasses.getDouble("" + aa);

        return totalResidueMass;

    }

    /**
     * Returns the neutral molecular mass of a peptide.
     *
     * @param peptideSequence the peptide proteinSequence
     * @param massNTerm       mass of the N-terminal group
     * @param massCTerm       mass of the C-terminal group
     * @return the monoisotopic mass in Da
     */
    public static double calculatePeptideMass(String peptideSequence, double massNTerm, double massCTerm) {

        //sum up residue masses
        double totalResidueMass = 0;
        for (char aa : peptideSequence.toCharArray())
            totalResidueMass += aaMasses.getDouble("" + aa);

        //add H2O mass
        return totalResidueMass + massNTerm + massCTerm;

    }

    /**
     * Returns the neutral molecular mass of a peptide with an unmodified N-terminal (hydrogen) group
     * and an unmodified C-termal (hydroxy) group.
     *
     * @param peptideSequence the peptide proteinSequence
     * @return the monoisotopic mass in Da
     */
    public static double calculatePeptideMass(String peptideSequence) {

        return calculatePeptideMass(peptideSequence, massH, massH + massO);

    }

    /**
     * Returns the theoretical m/z value of a peptide ion with an unmodified N-terminal (hydrogen) group
     * and an unmodified C-termal (hydroxy) group.
     *
     * @param peptideSequence the peptide proteinSequence
     * @param z               the ion charge
     * @return the theoretical m/z
     */
    public static double calculateMassOverCharge(String peptideSequence, int z) {

        //calculate peptide mass
        double peptideMass = calculatePeptideMass(peptideSequence);

        //calculate mass over charge
        return calculateMassOverCharge(peptideMass, z);

    }

    /**
     * Returns the theoretical m/z value of a peptide ion.
     *
     * @param peptideSequence the peptide proteinSequence
     * @param massNTerm       mass of the N-terminal group
     * @param massCTerm       mass of the C-terminal group
     * @param z               the ion charge
     * @return the theoretical m/z
     */
    public static double calculateMassOverCharge(String peptideSequence, double massNTerm, double massCTerm, int z) {

        //calculate peptide mass
        double peptideMass = calculatePeptideMass(peptideSequence, massNTerm, massCTerm);

        //calculate mass over charge
        return calculateMassOverCharge(peptideMass, z);

    }

    /**
     * Returns the theoretical m/z of a peptide ion of the specified neutral mass and charge.
     *
     * @param peptideMass the peptide mass in Dalton
     * @param z           the ion charge
     * @return the theoretical m/z
     */
    public static double calculateMassOverCharge(double peptideMass, int z) {

        //if charge is 0 mass is unaltered
        if (z == 0) {
            return peptideMass;
        }

        //calculate ion mass by adding/substracting z protons
        double ionMass = peptideMass + (z * massH);

        //if mass is negative get absolute value by multiplying by -1
        if (z < 0) {
            z = -1 * z;
        }

        //return mass over charge
        return ionMass / z;

    }

    /**
     * Returns the peptide mass of an unmodified precursor ion given an m/z value and a charge z.
     *
     * @param massOverCharge the m/z value
     * @param z              the ion charge
     * @return the peptide mass in Da
     */
    public static double calculatePeptideMassFromMassOverCharge(double massOverCharge, int z) {

        return massOverCharge * z - z * massH;

    }

    public Map<Integer, Double> getAllowedChargeStates() {
        return allowedChargeStates;
    }

    public void setAllowedChargeStates(Map<Integer, Double> allowedChargeStates) {
        this.allowedChargeStates = allowedChargeStates;
    }

    public void addAllowedChargeState(int z, double probability){
        this.allowedChargeStates.put(z, probability);
    }
}
