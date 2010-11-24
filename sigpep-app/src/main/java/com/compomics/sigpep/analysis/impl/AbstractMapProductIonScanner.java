package com.compomics.sigpep.analysis.impl;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.analysis.ExclusionScoreCalculatorFactory;
import com.compomics.sigpep.analysis.ProductIonScanner;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.model.impl.MassOverChargeRangeImpl;
import com.compomics.sigpep.model.impl.SignatureTransitionImpl;
import com.compomics.sigpep.util.SigPepUtil;

import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 11:41:25<br/>
 */
public abstract class AbstractMapProductIonScanner implements ProductIonScanner {

    protected Configuration config = Configuration.getInstance();
    protected int massPrecission = config.getInt("sigpep.app.monoisotopic.mass.precision");

    protected Set<ProductIonType> targetProductIonTypes;
    protected Set<ProductIonType> backgroundProductIonTypes;
    protected Set<Integer> productIonChargeStates;
    protected double massAccuracy;
    protected int minimumCombinationSize;
    protected int maximumCombinationSize;

    protected ExclusionScoreCalculatorFactory exclusionScoreCalculatorFactory = ExclusionScoreCalculatorFactory.getInstance();

    /**
     * Constructs a product ion scanner.
     *
     * @param targetProductIonTypes     the target product ion types to take into account
     * @param backgroundProductIonTypes the background product ion types to take into account
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the mass accuracy of the mass spectrometer
     * @param minimumCombinationSize    the minimum product ion combination size of the signature transition
     * @param maximumCombinationSize    the maximum product ion combination size of the signature transition
     */
    public AbstractMapProductIonScanner(HashSet<ProductIonType> targetProductIonTypes,
                                        HashSet<ProductIonType> backgroundProductIonTypes,
                                        HashSet<Integer> productIonChargeStates,
                                        Double massAccuracy,
                                        Integer minimumCombinationSize,
                                        Integer maximumCombinationSize) {

        this.targetProductIonTypes = targetProductIonTypes;
        this.backgroundProductIonTypes = backgroundProductIonTypes;
        this.productIonChargeStates = productIonChargeStates;
        this.massAccuracy = massAccuracy;
        this.minimumCombinationSize = minimumCombinationSize;
        this.maximumCombinationSize = maximumCombinationSize;

    }

    /**
     * Returns a set of product ions that can distinguish a target sequence
     * from a set of isobaric background sequences
     *
     * @param targetPeptide    the target peptide
     * @param isobaricPeptides the isobaric peptides
     * @return a map of scored combinations
     */
    public List<SignatureTransition> findSignatureTransitions(Peptide targetPeptide,
                                                              Set<Peptide> isobaricPeptides) {

        List<SignatureTransition> retVal = new ArrayList<SignatureTransition>();

        Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix = createExclusionMatrix(targetPeptide,
                isobaricPeptides,
                targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy);

        Map<Set<ProductIon>, Double> signatureProductIonCombinations = getUniqueProductIonCombinations(exclusionMatrix,
                    minimumCombinationSize,
                    maximumCombinationSize);

        for (Set<ProductIon> combination : signatureProductIonCombinations.keySet()) {

            double score = signatureProductIonCombinations.get(combination);

            List<ProductIon> combinationList = new ArrayList<ProductIon>();
            combinationList.addAll(combination);

            SignatureTransition transition = new SignatureTransitionImpl(targetPeptide, isobaricPeptides);

            transition.setProductIons(combinationList);
            transition.setExclusionScore(score);
            transition.setTargetProductIonTypes(targetProductIonTypes);
            transition.setBackgroundProductIonTypes(backgroundProductIonTypes);
            transition.setMassAccuracy(massAccuracy);
            transition.setProductIonChargeStates(productIonChargeStates);

            retVal.add(transition);

        }

        return retVal;

    }

    /**
     * Scores the product ion combination based on the combined number of background peptides excluded.
     * <p/>
     * Combinations that do not exclude all background peptides get a score of 0.
     *
     * @param productIonCombination the product ion combination to score
     * @param exclusionMatrix       the mass overlap matrix
     * @return a score between 0 and 1
     */
    protected double calculateExclusionScore(Set<ProductIon> productIonCombination, Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix) {

        double retVal = 0;

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
                combinedExlcusionCount = +excludes;
            }

            retVal = combinedExlcusionCount / maxExclusionCount;

        }

        return retVal;

    }


    /**
     * Creates a map of product ions grouped by neutral mass.
     *
     * @param peptides        the peptides to group product ions for
     * @param productIonTypes the product ion types taken into account
     * @return a map of monoisotopic masses and product ions sets
     */
    protected SortedMap<Double, Set<ProductIon>> createProductIonStore(Collection<? extends Peptide> peptides,
                                                                     Set<ProductIonType> productIonTypes) {

        SortedMap<Double, Set<ProductIon>> retVal = new TreeMap<Double, Set<ProductIon>>();

        for (ProductIonType type : productIonTypes) {

            for (Peptide peptide : peptides) {

                for (ProductIon product : peptide.getPrecursorIon().getProductIons(type)) {

                    double mass = SigPepUtil.round(product.getNeutralMassPeptide(), massPrecission);

                    if (!retVal.containsKey(mass)) {
                        retVal.put(mass, new HashSet<ProductIon>());
                    }
                    retVal.get(mass).add(product);

                }
            }

        }

        return retVal;
    }

    /**
     * Returns a map of product ion monoisotopic masses and their frequencies.
     *
     * @param peptides        the peptides to get the product ion mass frequency for
     * @param productIonTypes the product ion types taken into account
     * @return a map of monoisotopic masses and frequencies
     */
    protected Map<Double, Integer> getProductIonMassDistribution(Collection<? extends Peptide> peptides,
                                                               Set<ProductIonType> productIonTypes) {

        //insert background product ion masses into table
        Map<Double, Integer> retVal = new TreeMap<Double, Integer>();
        for (ProductIonType type : productIonTypes) {

            for (Peptide peptide : peptides) {

                for (ProductIon product : peptide.getPrecursorIon().getProductIons(type)) {

                    double mass = SigPepUtil.round(product.getNeutralMassPeptide(), massPrecission);

                    if (!retVal.containsKey(mass)) {
                        retVal.put(mass, 1);
                    }
                    int frequency = retVal.get(mass);
                    frequency++;
                    retVal.put(mass, frequency);

                }
            }

        }

        return retVal;

    }

    /**
     * The method returns a matrix that specifies the overlap
     * between precursor ions of the peptide to be uniquely identified
     * and the product ions of the background peptides.
     * <p/>
     * The matrix contains columns n = target peptide product ion count columns
     * and m = isobaric precursor peptide count rows. The matrix value is '1' if
     * there is the presence of target product ion n excludes the presence of
     * background peptide m and '0' otherwise.
     * <p/>
     * Example: Hypothetical Y ion series and 4 overlapping background
     * peptides.
     * <p/>
     * <code>       y1 y2 y3 y4 y5</code><br/>
     * <code>pep 1  0  0  0  0  1 </code><br/>
     * <code>pep 2  1  0  0  0  0 </code><br/>
     * <code>pep 3  0  1  1  0  0 </code><br/>
     * <code>pep 4  0  0  0  1  0 </code><br/>
     * <p/>
     * In the above example each of the Y ions has overlaps with a
     * product ion emitted by the overlapping background peptides, i.e.
     * one product ion is not sufficient to distinguish the precursor
     * from the background. By calling the recursive method
     * <code>determineColumnCombination()</code> a set of precursor
     * ions will be determined that allows to distinguish the peptide
     * from the background peptides starting from left most column. In
     * the above example a sufficient product ion set would be {y1,y2}
     * as the presence of those ions together excludes all other
     * peptides.
     *
     * @param targetPeptide
     * @param backgroundPeptides
     * @param targetProductIonTypes
     * @param backgroundProductIonTypes
     * @param productIonChargeStates
     * @param massAccuracy
     * @return
     */
    protected Map<ProductIon, Map<Peptide, Integer>> createExclusionMatrix(
            Peptide targetPeptide,
            Set<Peptide> backgroundPeptides,
            Set<ProductIonType> targetProductIonTypes,
            Set<ProductIonType> backgroundProductIonTypes,
            Set<Integer> productIonChargeStates,
            double massAccuracy) {

        //get precursor ion for target peptide sequence
        PrecursorIon targetPrecursorIon = targetPeptide.getPrecursorIon();

        //populate product ion store
        SortedMap<Double, Set<ProductIon>> backgroundProductIonStore = createProductIonStore(backgroundPeptides, backgroundProductIonTypes);

        //intialise exclusion matrix
        Map<ProductIon, Map<Peptide, Integer>> retVal = new LinkedHashMap<ProductIon, Map<Peptide, Integer>>();
        for (ProductIonType targetProductIonType : targetProductIonTypes) {

            for (ProductIon targetProductIon : targetPrecursorIon.getProductIons(targetProductIonType)) {

                retVal.put(targetProductIon, new LinkedHashMap<Peptide, Integer>());

                for (Peptide peptide : backgroundPeptides) {

                    retVal.get(targetProductIon).put(peptide, 1);

                }

            }

        }

        //populate exclusion matrix
        for (ProductIonType targetProductIonType : targetProductIonTypes) {

            for (ProductIon targetProductIon : targetPrecursorIon.getProductIons(targetProductIonType)) {

                //get target product ion mass
                double targetProductIonMass = targetProductIon.getNeutralMassPeptide();

                //round target product ion mass
                targetProductIonMass = SigPepUtil.round(targetProductIonMass, massPrecission);

                //create target product ion mass range based on the given mass accuracy
                MassOverChargeRange targetProductIonMassRange = new MassOverChargeRangeImpl(targetProductIonMass, productIonChargeStates, massAccuracy);

                for (MassOverChargeRange[] overLappingProductIonMassRange : targetProductIonMassRange.getFlankingPeptideMassOverChargeRanges()) {

                    //fetch all background product ions overlapping
                    // with the the mass range from the temporary
                    // product ion table...
                    double lowerFlankingMass = overLappingProductIonMassRange[0].getNeutralPeptideMass();
                    double upperFlankingMass = overLappingProductIonMassRange[1].getNeutralPeptideMass();
                    Map<Double, Set<ProductIon>> overlappingProductIons = backgroundProductIonStore.subMap(lowerFlankingMass, upperFlankingMass);

                    //...and update the exclusion matrix
                    for (Double mass : overlappingProductIons.keySet()) {

                        for (ProductIon productIon : overlappingProductIons.get(mass)) {

                            //set peptides to 0 which are not excluded by the target product ion
                            Peptide backgroundPeptide = productIon.getPrecursorIon().getPeptide();
                            retVal.get(targetProductIon).put(backgroundPeptide, 0);

                        }
                    }


                }

            }
        }

        return retVal;
    }

    protected abstract Map<Set<ProductIon>, Double> getUniqueProductIonCombinations(Map<ProductIon, Map<Peptide, Integer>> exclusionMatrix,
            int minCombinationSize,
            int maxCombinationSize);

}
