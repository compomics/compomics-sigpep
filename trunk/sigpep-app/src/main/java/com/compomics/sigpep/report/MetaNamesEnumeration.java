package com.compomics.sigpep.report;

/**
 * This class is a
 */
public enum MetaNamesEnumeration {

    PROTEIN("sigpep.meta.protein.accession"),
    PEPTIDE("sigpep.meta.peptide.sequence"),
    PEPTIDE_CHARGE("sigpep.meta.peptide.charge"),
    BARCODE_MASSES("sigpep.meta.barcode.mass"),
    BARCODE_IONTYPE("sigpep.meta.barcode.iontype"),
    BARCODE_IONNUMBER("sigpep.meta.barcode.ionnumber");

    public String NAME;

    MetaNamesEnumeration(String aName) {
        this.NAME = aName;
    }
}
