package com.compomics.sigpep.webapp.bean;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.model.Peptide;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:00
 * To change this template use File | Settings | File Templates.
 */
public class PeptideFormBean extends AbstractFormBean {

    private String iPeptideSequence = "LGKLYWLVTQNVDALHTK";

    private PeptideGenerator iPeptideGenerator;

    private Set<Peptide> lBackgroundPeptides;

    public String getPeptideSequence() {
        return iPeptideSequence;
    }

    public void setPeptideSequence(String aPeptideSequence) {
        iPeptideSequence = aPeptideSequence;
    }

    public PeptideGenerator getPeptideGenerator() {
        return iPeptideGenerator;
    }

    public void setPeptideGenerator(PeptideGenerator aPeptideGenerator) {
        iPeptideGenerator = aPeptideGenerator;
    }

    public Set<Peptide> getlBackgroundPeptides() {
        return lBackgroundPeptides;
    }

    public void setlBackgroundPeptides(Set<Peptide> aLBackgroundPeptides) {
        lBackgroundPeptides = aLBackgroundPeptides;
    }

}
