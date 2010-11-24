package com.compomics.sigpep.model;

import java.util.List;

/**
 * An ionised peptide generated at the first stage of an MS run.
 * <br/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:13:01<br/>
 */
public interface PrecursorIon extends PeptideIon, Comparable<PrecursorIon> {

    /**
     * Returns the peptide the peptide ion is derived from.
     *
     * @return the peptide
     */
    Peptide getPeptide();

    /**
     * Returns the product ion of the specified type
     * and length (for d, v and w ions for which there
     * are more then one possible product ion use
     * <code>getProductIons(ProductIonType type, int length)</code>
     * instead).
     *
     * @param type   the product ion type
     * @param length the product ion length
     * @return the product ion
     */
    ProductIon getProductIon(ProductIonType type, int length);

    /**
     * Returns all product ions of the specified type
     * the can be emitted by the precursor ion.
     *
     * @param type the product ion type
     * @return a list of product ions
     */
    List<ProductIon> getProductIons(ProductIonType type);

    /**
     * Returns the neutral mass of the N-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassNTerminalGroup();

    /**
     * Returns the neutral mass of the C-terminal group.
     *
     * @return the monoisotopic mass in Da
     */
    double getNeutralMassCTerminalGroup();

    /**
     * Sets the neutral mass of the N-terminal group.
     *
     * @param massNTerminalGroup the monoisotopic mass in Da
     */
    void setNeutralMassNTerminalGroup(double massNTerminalGroup);

    /**
     * Sets the neutral mass of the C-terminal group.
     *
     * @param massCTerminalGroup the monoisotopic mass in Da
     */
    void setNeutralMassCTerminalGroup(double massCTerminalGroup);


}
