package com.compomics.sigpep.webapp.bean;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:00
 * To change this template use File | Settings | File Templates.
 */
public class PeptideFormBean extends AbstractFormBean {

    private String iPeptideSequence = "LGKLYWLVTQNVDALHTK";

    public String getPeptideSequence() {
        return iPeptideSequence;
    }

    public void setPeptideSequence(String aPeptideSequence) {
        iPeptideSequence = aPeptideSequence;
    }

}
