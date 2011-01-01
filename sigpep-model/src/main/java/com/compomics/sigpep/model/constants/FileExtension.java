package com.compomics.sigpep.model.constants;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 19-Sep-2007<br>
 * Time: 11:44:25<br>
 */
public enum FileExtension {

    TAB_SEPARATED_VALUES("tsv");

    final String stringValue;

    /**
     * Creates a new FileExtension.
     * @param stringValue
     */
    FileExtension(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return stringValue;
    }
}
