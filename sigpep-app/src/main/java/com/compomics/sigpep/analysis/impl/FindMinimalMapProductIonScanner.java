package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.ExecutorServiceLocator;
import com.compomics.sigpep.analysis.ExclusionScoreCalculator;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.util.Combinations;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 13:35:35<br/>
 */
public class FindMinimalMapProductIonScanner extends AbstractMapProductIonScanner {

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
    public FindMinimalMapProductIonScanner(HashSet<ProductIonType> targetProductIonTypes,
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

        //get thread pool
        ExecutorService executorService = ExecutorServiceLocator.getInstance().getExecutorService();
        List<Future<Map<Set<ProductIon>, Double>>> results = new ArrayList<Future<Map<Set<ProductIon>, Double>>>();

        //iterate over all combinations in size range...
        for (int k = minCombinationSize; k <= maxCombinationSize; k++) {

            Combinations<ProductIon> combinations = new Combinations<ProductIon>(k, targetProductIons);
            while (combinations.hasNext()) {

                Set<ProductIon> combination = combinations.next();

                //...and create and execute a thread to calculate the
                //exclusion score for each combination
                ExclusionScoreCalculator<Map<Set<ProductIon>, Double>> calculator = exclusionScoreCalculatorFactory.getCalculator(combination, exclusionMatrix);
                Future<Map<Set<ProductIon>, Double>> f = executorService.submit(calculator);
                results.add(f);
            }

            //...get the results for each combination
            for (Future<Map<Set<ProductIon>, Double>> result : results) {

                try {
                    retVal.putAll(result.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException("Exception while finding unique product ion combination.", e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception while finding unique product ion combination.", e);
                }
            }

            //stop search if  combination size k yields
            //unique product ion combination(s)...
            if (retVal.size() > 0) {
                break;
            }
        }

        return retVal;
    }
}
