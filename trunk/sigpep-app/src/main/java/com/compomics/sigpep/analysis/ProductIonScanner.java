package com.compomics.sigpep.analysis;

import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.SignatureTransition;
import java.util.List;
import java.util.Set;

/**
 * Implementing classes scan product ions of the target peptide for one or more
 * combinations that are unique to the target peptide against the background of
 * isobaric peptides.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 27-Nov-2007<br/>
 * Time: 16:50:39<br/>
 */
public interface ProductIonScanner {

    /**
     * Returns a set of product ions that can distinguish a target sequence
     * from a set of isobaric background sequences as a SignatureTransition
     * object.
     *
     * @param targetPeptide    the target peptide
     * @param isobaricPeptides the isobaric peptides
     * @return a list of signature transitions
     */
    List<SignatureTransition> findSignatureTransitions(Peptide targetPeptide, Set<Peptide> isobaricPeptides);
}
