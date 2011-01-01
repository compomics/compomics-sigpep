package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @TODO: JavaDoc missing
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Jan-2008<br/>
 * Time: 18:58:56<br/>
 */
public class ProteinSequenceImpl implements ProteinSequence, Persistable {

    private int id;
    private Object sessionFactory;

    private Set<PeptideFeature> signaturePeptides;

    /**
     * the peptides emitted by the sequence
     */
    private Set<PeptideFeature> peptides;

    /**
     * the proteins associated with the sequence
     */
    private Set<Protein> proteins;

    /**
     * the amino acid sequence
     */
    private String sequenceString;

    /**
     * the splice sites
     */
    private Set<SpliceEventFeature> spliceEvents;

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public Object getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param sessionFactory
     */
    public void setSessionFactory(Object sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inherit}
     */
    public Set<Protein> getProteins() {
        return proteins;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @return
     */
    public Set<PeptideFeature> getSignaturePeptides() {
        return signaturePeptides;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @param signaturePeptides
     */
    public void setSignaturePeptides(Set<PeptideFeature> signaturePeptides) {
        this.signaturePeptides = signaturePeptides;
    }

    /**
     * {@inherit}
     */
    public Set<PeptideFeature> getPeptides() {
        return peptides;
    }

    /**
     * {@inherit}
     */
    public void setPeptides(Set<PeptideFeature> peptides) {
        this.peptides = peptides;
    }

    /**
     * {@inherit}
     */
    public Set<PeptideFeature> getPeptides(Protease protease) {
        Set<PeptideFeature> retVal = new HashSet<PeptideFeature>();

        //filter by protease
        for (PeptideFeature pf : this.getPeptides()) {
            if (pf.getProteases().contains(protease)) {
                retVal.add(pf);
            }
        }

        return retVal;
    }

    /**
     * {@inherit}
     */
    public String getSequenceString() {
        return sequenceString;
    }

    /**
     * {@inherit}
     */
    public void setProteins(Set<Protein> proteins) {
        this.proteins = proteins;
    }

    /**
     * {@inherit}
     */
    public void setSequenceString(String sequenceString) {
        this.sequenceString = sequenceString;
    }

    /**
     * {@inherit}
     */
    public Set<SpliceEventFeature> getSpliceEvents() {
        return spliceEvents;
    }

    /**
     * {@inherit}
     */
    public void setSpliceEvents(Set<SpliceEventFeature> spliceEvents) {
        this.spliceEvents = spliceEvents;
    }

    /**
     * Checks for equality to another object. Two protein sequences are equal if they have equal
     * sequence strings.
     *
     * @param o the object to check for equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProteinSequence)) return false;

        ProteinSequence sequence = (ProteinSequence) o;

        if (sequenceString != null ? !sequenceString.equals(sequence.getSequenceString()) : sequence.getSequenceString() != null)
            return false;

        return true;
    }

    /**
     * {@inherit}
     */
    public int hashCode() {
        return sequenceString.hashCode();
    }

    /**
     * {@inherit}
     */
    public String toString() {
        return "ProteinSequenceImpl{" +
                "sequenceString='" + sequenceString + '\'' +
                '}';
    }
}
