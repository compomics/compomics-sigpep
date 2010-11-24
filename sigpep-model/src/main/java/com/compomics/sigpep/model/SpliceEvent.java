package com.compomics.sigpep.model;

import java.util.Set;

/**
 * A splice event is defined by an upstream and downstream exon.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Feb-2008<br/>
 * Time: 15:29:42<br/>
 */
public interface SpliceEvent {

    /**
     * Returns the upstream exon defining the splice event.
     *
     * @return the exon
     */
    Exon getUpstreamExon();

    /**
     * Sets the upsteam exon defining the splice event.
     *
     * @param exon the exon
     */
    void setUpstreamExon(Exon exon);

    /**
     * Returns the upstream exon defining the splice event.
     *
     * @return the exon
     */
    Exon getDownstreamExon();

    /**
     * Sets the upsteam exon defining the splice event.
     *
     * @param exon the exon
     */
    void setDownstreamExon(Exon exon);

    
}
