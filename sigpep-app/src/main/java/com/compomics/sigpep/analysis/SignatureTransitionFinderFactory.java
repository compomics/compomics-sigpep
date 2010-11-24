package com.compomics.sigpep.analysis;

import com.compomics.sigpep.analysis.impl.SignatureTransitionFinderImpl;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIonType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creates signature transition finders.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 10:30:47<br/>
 */
public class SignatureTransitionFinderFactory {

    /** the singleton instance  */
    private static SignatureTransitionFinderFactory instance = new SignatureTransitionFinderFactory();

    /** the product ion scanner factory  */
    private static ProductIonScannerFactory scannerFactory = ProductIonScannerFactory.getInstance();

    /**
     * Constructs an instance of SignatureTransitionFinderFactory
     */
    private SignatureTransitionFinderFactory() {
    }

    /**
     * Returns the singleton instance of the signature transition finder factory.
     *
     * @return the signature transition finder factory
     */
    public static SignatureTransitionFinderFactory getInstance() {
        return instance;
    }

    /**
     * Creates a signature transition finder that will return the first and only
     * the first signature transition found for a given peptide in the set size range specified.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param precursorIonChargeStates  the allowed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindFirstSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                              Set<ProductIonType> targetProductIonTypes,
                                                                              Set<ProductIonType> backgroundProductIonTypes,
                                                                              List<Map<Double, Integer>> precursorIonChargeStates,
                                                                              Set<Integer> productIonChargeStates,
                                                                              double massAccuracy,
                                                                              int minimumCombinationSize,
                                                                              int maximumCombinationSize) {

        ProductIonScanner scanner = scannerFactory.createFindFirstProductIonScanner(targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

        return new SignatureTransitionFinderImpl(backgroundPeptides,
                precursorIonChargeStates,
                massAccuracy,
                scanner);

    }

    /**
     * Creates a signature transition finder that will return the first and only
     * the first signature transition found for a given peptide in the set size range specified.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param precursorIonChargeStates  the allowed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindFirstSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                              Set<ProductIonType> targetProductIonTypes,
                                                                              Set<ProductIonType> backgroundProductIonTypes,
                                                                              Set<Integer> precursorIonChargeStates,
                                                                              Set<Integer> productIonChargeStates,
                                                                              double massAccuracy,
                                                                              int minimumCombinationSize,
                                                                              int maximumCombinationSize) {

        ProductIonScanner scanner = scannerFactory.createFindFirstProductIonScanner(targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

        return new SignatureTransitionFinderImpl(backgroundPeptides,
                precursorIonChargeStates,
                massAccuracy,
                scanner);

    }

    /**
     * Creates a signature transition finder that will return the signature transition
     * consisting of the smallest number of product ions required to uniquely identify
     * a given peptide against the background in the set size range specified.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param precursorIonChargeStates  the allowed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindMinimalSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                                Set<ProductIonType> targetProductIonTypes,
                                                                                Set<ProductIonType> backgroundProductIonTypes,
                                                                                List<Map<Double, Integer>> precursorIonChargeStates,
                                                                                Set<Integer> productIonChargeStates,
                                                                                double massAccuracy,
                                                                                int minimumCombinationSize,
                                                                                int maximumCombinationSize) {

        ProductIonScanner scanner = scannerFactory.createFindMinimalProductIonScanner(targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

        return new SignatureTransitionFinderImpl(backgroundPeptides,
                precursorIonChargeStates,
                massAccuracy,
                scanner);
    }

    /**
     * Creates a signature transition finder that will return the signature transition
     * consisting of the smallest number of product ions required to uniquely identify
     * a given peptide against the background in the set size range specified.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param precursorIonChargeStates  the allowed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindMinimalSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                                Set<ProductIonType> targetProductIonTypes,
                                                                                Set<ProductIonType> backgroundProductIonTypes,
                                                                                Set<Integer> precursorIonChargeStates,
                                                                                Set<Integer> productIonChargeStates,
                                                                                double massAccuracy,
                                                                                int minimumCombinationSize,
                                                                                int maximumCombinationSize) {

        ProductIonScanner scanner = scannerFactory.createFindMinimalProductIonScanner(targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

        return new SignatureTransitionFinderImpl(backgroundPeptides,
                precursorIonChargeStates,
                massAccuracy,
                scanner);
    }

    /**
     * Creates a signature transition finder that will return all signature transitions
     * for a given peptide in the set size range specified.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param precursorIonChargeStates  the allowed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindAllSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                            Set<ProductIonType> targetProductIonTypes,
                                                                            Set<ProductIonType> backgroundProductIonTypes,
                                                                            Set<Integer> precursorIonChargeStates,
                                                                            Set<Integer> productIonChargeStates,
                                                                            double massAccuracy,
                                                                            int minimumCombinationSize,
                                                                            int maximumCombinationSize) {

        ProductIonScanner scanner = scannerFactory.createFindAllProductIonScanner(targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

        return new SignatureTransitionFinderImpl(backgroundPeptides,
                precursorIonChargeStates,
                massAccuracy,
                scanner);


    }

    /**
     * Creates a signature transition finder that will return all signature transitions
     * for a given peptide in the specified set size range. A list of observed precursor
     * ion mass-charge-state combinations is used to calculate a charge state probability
     * for each peptide which is used in the target peptide background estimation.
     *
     * @param backgroundPeptides        the set of background peptides to find signature transitions against
     * @param targetProductIonTypes     the product ion types of the target peptides to include in the search
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account in the search
     * @param observedPrecursorIonChargeStates  the observed precursor ion charge states
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the assumed mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum number of product ions a signature transition is defined by
     * @param maximumCombinationSize    the maximum number of product ions a signature transition is defined by
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createFindAllSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                                Set<ProductIonType> targetProductIonTypes,
                                                                                Set<ProductIonType> backgroundProductIonTypes,
                                                                                List<Map<Double, Integer>> observedPrecursorIonChargeStates,
                                                                                Set<Integer> productIonChargeStates,
                                                                                double massAccuracy,
                                                                                int minimumCombinationSize,
                                                                                int maximumCombinationSize) {

            ProductIonScanner scanner = scannerFactory.createFindAllProductIonScanner(targetProductIonTypes,
                    backgroundProductIonTypes,
                    productIonChargeStates,
                    massAccuracy,
                    minimumCombinationSize,
                    maximumCombinationSize);

            return new SignatureTransitionFinderImpl(backgroundPeptides,
                    observedPrecursorIonChargeStates,
                    massAccuracy,
                    scanner);


        }


}
