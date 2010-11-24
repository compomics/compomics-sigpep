package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;

/**
 * Implementation of SpliceEvent.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Feb-2008<br/>
 * Time: 15:47:57<br/>
 */
public class SpliceEventImpl implements SpliceEvent {


    /**
     * the upstream exon
     */
    private Exon upstreamExon;

    /**
     * the downstream exon
     */
    private Exon downstreamExon;

    /**
     * Returns the upstream exon defining the splice event.
     *
     * @return the exon
     */
    public Exon getUpstreamExon() {
        return upstreamExon;
    }

    /**
     * Sets the upsteam exon defining the splice event.
     *
     * @param exon the exon
     */
    public void setUpstreamExon(Exon exon) {
        this.upstreamExon = exon;
    }

    /**
     * Returns the upstream exon defining the splice event.
     *
     * @return the exon
     */
    public Exon getDownstreamExon() {
        return downstreamExon;
    }

    /**
     * Sets the upsteam exon defining the splice event.
     *
     * @param exon the exon
     */
    public void setDownstreamExon(Exon exon) {
        this.downstreamExon = exon;
    }

    /**
     * Two splice events are equal if they occur between the same
     * down stream and up stream exons.
     *
     * @param o the object to compare
     * @return true if two splice events are equal
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpliceEvent)) return false;

        SpliceEvent that = (SpliceEvent) o;

        if (!downstreamExon.equals(that.getDownstreamExon())) return false;
        if (!upstreamExon.equals(that.getUpstreamExon())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = upstreamExon.hashCode();
        result = 31 * result + downstreamExon.hashCode();
        return result;
    }


    public String toString() {
        return "SpliceEventImpl{" +
                "upstreamExon=" + upstreamExon +
                ", downstreamExon=" + downstreamExon +
                '}';
    }
}
