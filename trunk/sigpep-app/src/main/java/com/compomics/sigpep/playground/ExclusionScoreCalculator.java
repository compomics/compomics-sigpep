package com.compomics.sigpep.playground;

import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.Peptide;

import java.util.Set;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Aug-2008<br/>
 * Time: 12:02:04<br/>
 */
public class ExclusionScoreCalculator extends Thread {

    private Set<ProductIon> productIonCombination;
    private Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix;
    private int number1;
    private int number2;

    private double score;

    public ExclusionScoreCalculator(Set<ProductIon> productIonCombination,
                                    Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix) {
        this.productIonCombination = productIonCombination;
        this.exclusionMatrix = exclusionMatrix;
        this.run();
    }

    public ExclusionScoreCalculator(int number1, int number2) {
        this.number1 = number1;
        this.number2 = number2;
        this.run();
    }

    public synchronized double getScore() {
        return score;
    }

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> setupDatabase object, then that
     * <code>Runnable</code> object's <code>setupDatabase</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p/>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     * @see #Thread(ThreadGroup, Runnable, String)
     */
    public void run() {
        calculateExclusionScore();
    }

    private synchronized void calculateExclusionScore() {

//        score = number1 + number2;

//            score = 0;
//
//            Map<Peptide, Integer> combinedExclusion = new LinkedHashMap<Peptide, Integer>();
//
//            for (ProductIon ion : productIonCombination) {
//
//                Map<Peptide, Integer> overlap = exclusionMatrix.get(ion);
//
//                for (Peptide peptide : overlap.keySet()) {
//
//                    Integer excludes = overlap.get(peptide);
//
//                    if (combinedExclusion.containsKey(peptide)) {
//
//                        int exclusionCount = combinedExclusion.get(peptide);
//                        exclusionCount = exclusionCount + excludes;
//                        combinedExclusion.put(peptide, exclusionCount);
//
//                    } else {
//                        combinedExclusion.put(peptide, excludes);
//                    }
//
//                }
//
//            }
//
//            //check if all peptides are excluded...
//            boolean excludesAll = !combinedExclusion.containsValue(0);
//
//            //...if it does calculate score not
//            if (excludesAll) {
//
//                int combinationSize = productIonCombination.size();
//                int overlappingPeptideCount = combinedExclusion.size();
//                double maxExclusionCount = combinationSize * overlappingPeptideCount;
//
//                double combinedExlcusionCount = 0;
//                for (Integer excludes : combinedExclusion.values()) {
//                    combinedExlcusionCount = +excludes;
//                }
//
//                score = combinedExlcusionCount / maxExclusionCount;
//
//            }


    }


    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            ExclusionScoreCalculator calculator = new ExclusionScoreCalculator(i, i);
            System.out.println(i + " + " + i + " = " + calculator.getScore());
        }
    }
}
