package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.analysis.ExclusionScoreCalculatorFactory;
import com.compomics.sigpep.analysis.ExclusionScoreCalculator;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.Peptide;

import java.util.Set;
import java.util.Map;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 12:02:14<br/>
 */
public class MapExclusionScoreCalculatorFactory extends ExclusionScoreCalculatorFactory {

    /**
     * @TODO: JavaDoc missing.
     *
     * @param productIonCombination
     * @param exclusionMatrix
     * @return
     */
    public ExclusionScoreCalculator getCalculator(Set<ProductIon> productIonCombination, Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix) {
        return new MapExclusionScoreCalculator(productIonCombination, exclusionMatrix);
    }
}
