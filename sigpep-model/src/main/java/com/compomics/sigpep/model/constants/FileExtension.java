package com.compomics.sigpep.model.constants;

/**
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 19-Sep-2007<br>
 * Time: 11:44:25<br>
 */
public enum FileExtension {

    TAB_SEPARATED_VALUES("tsv");

    final String stringValue;

    FileExtension(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return stringValue;
    }

}
