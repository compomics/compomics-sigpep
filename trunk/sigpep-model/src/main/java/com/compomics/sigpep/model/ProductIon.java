package com.compomics.sigpep.model;

/**
 * A peptide fragment that arises when a precursor ion gets fragmented at the second stage of an MS run.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:14:08<br/>
 */
public interface ProductIon extends PeptideIon {

    /**
     * Returns the product ion type.
     *
     * @return the product ion type
     */
    ProductIonType getType();

    /**
     * Returns the precursor ion the product ion has is generated from.
     *
     * @return the precursor ion
     */
    PrecursorIon getPrecursorIon();

    /**
     * Returns the number of residues that are of the specified amino acid.
     *
     * @param aminoAcid one letter amino acid code
     * @return the residue count
     */
    int getResidueCount(String aminoAcid);

}
