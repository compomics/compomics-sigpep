package com.compomics.sigpep.analysis;

import com.compomics.sigpep.model.*;
import java.util.*;


/**
 * Provides methodes that return signature transitions for peptides.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 10:28:20<br/>
 */
public interface SignatureTransitionFinder {

    /**
     * Returns signature transitions for a peptide.
     *
     * @param peptide the peptide to identify a signature transition for
     * @return list of signature transitions;
     *         empty if no signature transition exists for a given peptide
     */
    List<SignatureTransition> findSignatureTransitions(Peptide peptide);

    /**
     * Returns signature transitions for a collection of peptides.
     *
     * @param peptides  the peptides to identify a signature transition for
     * @return list of signature transitions;
     *         empty if no signature transition exists for a given peptide
     */
    List<SignatureTransition> findSignatureTransitions(Collection<Peptide> peptides);   
}
