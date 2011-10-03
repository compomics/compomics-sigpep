package com.compomics.sigpep.model;

import java.util.Map;

/**
 * A peptide that carries post-translational modification.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 13-Feb-2008<br/>
 * Time: 19:16:57<br/>
 */
public interface ModifiedPeptide extends Peptide {

    /**
     * Returns the post translational modifications.
     *
     * @return a map of post translational modifications and their respective position in the sequence
     */
    Map<Integer, Modification> getPostTranslationalModifications();

    /**
     * Returns the unmodified peptide.
     *
     * @return the peptide
     */
    Peptide getUnmodifiedPeptide();
}
