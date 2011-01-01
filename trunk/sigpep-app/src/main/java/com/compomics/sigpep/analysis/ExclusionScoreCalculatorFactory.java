package com.compomics.sigpep.analysis;

import com.compomics.sigpep.Configuration;
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
 * Time: 11:53:35<br/>
 */
public abstract class ExclusionScoreCalculatorFactory {

    private static ExclusionScoreCalculatorFactory ourInstance;
    protected static ExclusionScoreCalculator exclusionScoreCalculator;

    /**
     * @TODO: JavaDoc missing.
     *
     * @return
     */
    public static ExclusionScoreCalculatorFactory getInstance() {

        Configuration config = Configuration.getInstance();
        String calculatorClass = config.getString("sigpep.app.analysis.exclusion.score.calculator.factory.class");

        if (ourInstance == null) {

            try {
                ourInstance = (ExclusionScoreCalculatorFactory) Class.forName(calculatorClass).newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return ourInstance;
    }

    /**
     * @TODO: JavaDoc missing.
     *
     * @param productIonCombination
     * @param exclusionMatrix
     * @return
     */
    public abstract ExclusionScoreCalculator<Map<Set<ProductIon>, Double>> getCalculator(Set<ProductIon> productIonCombination,
                                                                                         Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix);
}
