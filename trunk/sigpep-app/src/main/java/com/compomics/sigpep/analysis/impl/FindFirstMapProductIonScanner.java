package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.analysis.ExclusionScoreCalculator;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.util.Combinations;
import com.compomics.sigpep.ExecutorServiceLocator;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 11:40:20<br/>
 */
public class FindFirstMapProductIonScanner extends AbstractMapProductIonScanner {

    /**
     * @TODO: JavaDoc missing.
     *
     * @param targetProductIonTypes
     * @param backgroundProductIonTypes
     * @param productIonChargeStates
     * @param massAccuracy
     * @param minimumCombinationSize
     * @param maximumCombinationSize
     */
    public FindFirstMapProductIonScanner(HashSet<ProductIonType> targetProductIonTypes,
            HashSet<ProductIonType> backgroundProductIonTypes,
            HashSet<Integer> productIonChargeStates,
            Double massAccuracy,
            Integer minimumCombinationSize,
            Integer maximumCombinationSize) {

        super(targetProductIonTypes, backgroundProductIonTypes, productIonChargeStates, massAccuracy, minimumCombinationSize, maximumCombinationSize);
    }

    /**
     * Returns the first signature product ion combination found.
     *
     * @param exclusionMatrix    the product ion exclusion matrix
     * @param minCombinationSize the minimal combination size the combination should have
     * @param maxCombinationSize the maximal combination size the combination should have
     * @return a map of one product ion combination and the corresponding exclusion score
     */
    public Map<Set<ProductIon>, Double> getUniqueProductIonCombinations(
            Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix,
            int minCombinationSize,
            int maxCombinationSize) {

        Map<Set<ProductIon>, Double> retVal = Collections.synchronizedMap(new HashMap<Set<ProductIon>, Double>());
        Set<ProductIon> targetProductIons = exclusionMatrix.keySet();

        ExecutorService executorService = ExecutorServiceLocator.getInstance().getExecutorService();

        combinationFound:
        for (int k = minCombinationSize; k <= maxCombinationSize; k++) {

            Combinations<ProductIon> combinations = new Combinations<ProductIon>(k, targetProductIons);
            while (combinations.hasNext()) {

                Set<ProductIon> combination = combinations.next();

                ExclusionScoreCalculator<Map<Set<ProductIon>, Double>> calculator = exclusionScoreCalculatorFactory.getCalculator(combination, exclusionMatrix);
                Future<Map<Set<ProductIon>, Double>> f = executorService.submit(calculator);

                try {

                    Map<Set<ProductIon>, Double> result = f.get();

                    if (result.size() > 0) {
                        retVal = result;
                        break combinationFound;
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return retVal;
    }
}
