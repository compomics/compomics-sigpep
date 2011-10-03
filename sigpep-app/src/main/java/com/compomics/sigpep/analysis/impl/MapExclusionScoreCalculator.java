package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.analysis.ExclusionScoreCalculator;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 11:57:34<br/>
 */
public class MapExclusionScoreCalculator implements ExclusionScoreCalculator<Map<Set<ProductIon>, Double>> {

    private Set<ProductIon> productIonCombination;
    private Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix;

    /**
     * @param productIonCombination
     * @param exclusionMatrix
     * @TODO: JavaDoc missing.
     */
    public MapExclusionScoreCalculator(Set<ProductIon> productIonCombination,
                                       Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix) {
        this.productIonCombination = productIonCombination;
        this.exclusionMatrix = exclusionMatrix;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public Map<Set<ProductIon>, Double> call() throws Exception {
        return calculateExclusionScore();
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    private Map<Set<ProductIon>, Double> calculateExclusionScore() {

        Map<Set<ProductIon>, Double> result = new HashMap<Set<ProductIon>, Double>();
        double score = 0;

        Map<Peptide, Integer> combinedExclusion = new LinkedHashMap<Peptide, Integer>();

        for (ProductIon ion : productIonCombination) {

            Map<Peptide, Integer> overlap = exclusionMatrix.get(ion);

            for (Peptide peptide : overlap.keySet()) {

                Integer excludes = overlap.get(peptide);

                if (combinedExclusion.containsKey(peptide)) {

                    int exclusionCount = combinedExclusion.get(peptide);
                    exclusionCount = exclusionCount + excludes;
                    combinedExclusion.put(peptide, exclusionCount);

                } else {
                    combinedExclusion.put(peptide, excludes);
                }
            }
        }

        //check if all peptides are excluded...
        boolean excludesAll = !combinedExclusion.containsValue(0);

        //...if it does calculate score not
        if (excludesAll) {

            int combinationSize = productIonCombination.size();
            int overlappingPeptideCount = combinedExclusion.size();
            double maxExclusionCount = combinationSize * overlappingPeptideCount;

            double combinedExlcusionCount = 0;
            for (Integer excludes : combinedExclusion.values()) {
                combinedExlcusionCount += excludes;
            }

            score = combinedExlcusionCount / maxExclusionCount;
            result.put(productIonCombination, score);
        }

        return result;
    }
}
