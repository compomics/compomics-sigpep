package com.compomics.sigpep.report;

/**
 * This class is a
 */
public enum MetaNamesEnumeration {

    PROTEIN("sigpep.meta.protein.accession"),
    PEPTIDE("sigpep.meta.peptide.sequence"),
    BARCODE_MASSES("sigpep.meta.barcode.mass"),
    BARCODE_IONTYPE("sigpep.meta.barcode.iontype"),
    BARCODE_IONNUMBER("sigpep.meta.barcode.ionnumber");

    public String NAME;
    MetaNamesEnumeration(String aName) {
        this.NAME = aName;
    }
}
