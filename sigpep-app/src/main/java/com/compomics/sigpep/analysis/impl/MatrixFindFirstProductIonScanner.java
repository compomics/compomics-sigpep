package com.compomics.sigpep.analysis.impl;

import Jama.Matrix;
import org.apache.log4j.Logger;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.model.impl.MassOverChargeRangeImpl;
import com.compomics.sigpep.model.impl.SignatureTransitionImpl;
import com.compomics.sigpep.util.SigPepUtil;

import java.util.*;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Feb-2008<br/>
 * Time: 17:46:49<br/>
 */
public class MatrixFindFirstProductIonScanner {

    protected static Logger logger = Logger.getLogger(MatrixFindFirstProductIonScanner.class);
    int massPrecission = 4;

    /**
     * @TODO: JavaDoc missing.
     */
    public MatrixFindFirstProductIonScanner() {
    }

    /**
     * @TODO: JavaDoc missing.
     * 
     * @param peptides
     * @param productIonTypes
     * @return
     */
    private SortedMap<Double, Set<ProductIon>> createBackgroundProductIonStore(Collection<? extends Peptide> peptides, Set<ProductIonType> productIonTypes) {

        SortedMap<Double, Set<ProductIon>> retVal = new TreeMap<Double, Set<ProductIon>>();

        for (ProductIonType type : productIonTypes) {

            for (Peptide peptide : peptides) {

                for (ProductIon product : peptide.getPrecursorIon().getProductIons(type)) {

                    double mass = SigPepUtil.round(product.getNeutralMassPeptide(), 4);

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
     * Returns a set of product ions that can dssgriminate a target sequence
     * from a set of background sequences.
     *
     * @param targetPeptide             the target peptide
     * @param backgroundPeptides        the background peptides
     * @param targetProductIonTypes     the target product ion types to take into account
     * @param backgroundProductIonTypes the background product ion types to take into account
     * @param productIonChargeStates    the allowed product ion charge states
     * @param massAccuracy              the mass accuracy of the mass spectrometer
     * @return a transition unique to the target peptide
     */
    public List<SignatureTransition> findSignatureTransitions(Peptide targetPeptide,
            Set<Peptide> backgroundPeptides,
            Set<ProductIonType> targetProductIonTypes,
            Set<ProductIonType> backgroundProductIonTypes,
            Set<Integer> productIonChargeStates,
            double massAccuracy,
            int minimumCombinationSize,
            int maximumCombinationSize,
            SignatureTransitionFinderType searchType) {

        List<SignatureTransition> retVal = new ArrayList<SignatureTransition>();
        SignatureTransition transition = new SignatureTransitionImpl(targetPeptide, backgroundPeptides);

        //get target product ion count and background peptide count defining the overlap matrix dimensions
        int targetProductIonCount = 0;
        for (ProductIonType type : targetProductIonTypes) {
            targetProductIonCount = targetProductIonCount + targetPeptide.getPrecursorIon().getProductIons(type).size();
        }
        int backgroundPeptideCount = backgroundPeptides.size();

        //populate product ion store
        SortedMap<Double, Set<ProductIon>> backgroundProductIonStore = createBackgroundProductIonStore(backgroundPeptides, backgroundProductIonTypes);

        //create precursor ion for target peptide sequence
        PrecursorIon targetPrecursorIon = targetPeptide.getPrecursorIon();

        //insert background product ion masses into table
        Map<Double, Integer> productIonMassDistribution = new TreeMap<Double, Integer>();
        for (Double mass : backgroundProductIonStore.keySet()) {
            int frequency = backgroundProductIonStore.get(mass).size();
            productIonMassDistribution.put(mass, frequency);
        }
        transition.setBackgroundProductIonMassDistribution(productIonMassDistribution);

        //for each target peptide product ion check
        //if its mass overlaps with any of the product ions
        // of the background peptides

        //create overlap matrix
        Map<String, Integer> targetProductIonName2Index = new TreeMap<String, Integer>();
        Map<Integer, String> targetProductIonIndex2Name = new TreeMap<Integer, String>();
        Map<Peptide, Integer> backgroundPeptide2Index = new HashMap<Peptide, Integer>();
        int peptideIndex = 0;
        for (Peptide peptide : backgroundPeptides) {
            backgroundPeptide2Index.put(peptide, peptideIndex);
            peptideIndex++;
        }

        //create a matrix with m (= number of background peptides) x  n (= number of target peptide product ions) columns
        Matrix backgroundPeptideByTargetProductIonOverlapMatrix = new Matrix(backgroundPeptideCount, targetProductIonCount, 0);
        int n = 0;
        int m = 0;
        //Map<String, Double> targetProductIonMasses = new HashMap<String, Double>();
        for (ProductIon targetProductIon : targetPrecursorIon.getProductIons(ProductIonType.Y)) {

            //get target product ion mass
            ProductIonType targetProductIonType = targetProductIon.getType();
            int targetProductIonLength = targetProductIon.getSequenceLength();
            String targetProductIonName = targetProductIonType.toString() + "-" + targetProductIonLength;
            double targetProductIonMass = targetProductIon.getNeutralMassPeptide();
            //targetProductIonMasses.put(targetProductIonName, targetProductIonMass);

            targetProductIonMass = SigPepUtil.round(targetProductIonMass, 4);

            //add product ion name and index to maps
            targetProductIonName2Index.put(targetProductIonName, n);
            targetProductIonIndex2Name.put(n, targetProductIonName);
            n++;
            int targetProductIonIndex = targetProductIonName2Index.get(targetProductIonName);

            //create target product ion mass range based on the given mass accuracy
            MassOverChargeRange targetProductIonMassRange = new MassOverChargeRangeImpl(targetProductIonMass, productIonChargeStates, massAccuracy);

            for (MassOverChargeRange[] overLappingProductIonMassRange : targetProductIonMassRange.getFlankingPeptideMassOverChargeRanges()) {

                //fetch all background product ions overlapping with the the mass range
                //from the temporary product ion table...
                double lowerFlankingMass = overLappingProductIonMassRange[0].getNeutralPeptideMass();
                double upperFlankingMass = overLappingProductIonMassRange[1].getNeutralPeptideMass();

                Map<Double, Set<ProductIon>> overlappingProductIons = backgroundProductIonStore.subMap(lowerFlankingMass, upperFlankingMass);

                //...and update to overlap matrix

                for (Double mass : overlappingProductIons.keySet()) {

                    for (ProductIon productIon : overlappingProductIons.get(mass)) {

                        Peptide backgroundPeptide = productIon.getPrecursorIon().getPeptide();
                        //int backgroundPeptideId = rs.getInt("peptide_id");
                        if (!backgroundPeptide2Index.containsKey(backgroundPeptide)) {
                            backgroundPeptide2Index.put(backgroundPeptide, m++);
                        }

                        int backgroundPeptideIndex = backgroundPeptide2Index.get(backgroundPeptide);
                        //logger.info("backgroundPeptideIndex = " + backgroundPeptideIndex + "; targetProductIonIndex = " + targetProductIonIndex);
                        try {
                            backgroundPeptideByTargetProductIonOverlapMatrix.set(backgroundPeptideIndex, targetProductIonIndex, 1);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            logger.error(e);
                        }
                    }
                }
            }
        }

        //get unique combination of target product ions
        Set<Integer> targetProductIonCombination = determineProductIonBarcodes(backgroundPeptideByTargetProductIonOverlapMatrix);

        //get target product ion names and add them to return value
        for (Integer targetProductIonIndex : targetProductIonCombination) {
            String targetProductIonName = "";
            try {

                targetProductIonName = targetProductIonIndex2Name.get(targetProductIonIndex);

                String typeName = targetProductIonName.split("-")[0];
                int residueCount = new Integer(targetProductIonName.split("-")[1]);

                ProductIonType type = ProductIonType.valueOf(typeName);

                transition.addProductIon(type, residueCount);

            } catch (NullPointerException e) {
                logger.warn("NullPointerException when trying to parse target product ion name " + targetProductIonName + " of target peptide " + targetPeptide + ".");
            }
        }

        retVal.add(transition);
        return retVal;
    }

    /**
     * Determines a set of product ions which is required and sufficient
     * to distinguish a precursor ion from other precursor ions with
     * overlapping mass.
     * <p/>
     * The method takes a matrix as input that specifies the overlap
     * between precursor ions of the peptide to be uniquely identified
     * and the product ions of the background peptides. The matrix
     * has to contain a column for each product ion of the peptide
     * to be identified and a row for each peptide with overlapping
     * mass. The matrix value is '1' if there is an overlap between the
     * product ion mass of the peptide to be identified and a
     * product ion mass of the background peptide. The matrix values
     * is 0 if there is no overlap.
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
     * @param matrix a matrix speficying the overlap between product ions of the peptide
     *               to be uniquely identified and product ions of background precursor ions
     * @return the column indexes of precursor ions uniquely identifying
     *         a product ion
     */
    private Set<Integer> determineProductIonBarcodes(Matrix matrix) {

        //create an empty set for processed columns
        Set<Integer> processedColumns = new TreeSet<Integer>();

        //matrix representing the current overlap between precursor
        //to be identified uniquely and background peptides. Contains
        //a one for each peptide overlapping. As we haven't started
        //finding a unique product ion combination all values are '1'.
        Matrix overlappingPeptides = new Matrix(matrix.getRowDimension(), 1, 1);

        //the number of peptides that cannot be distinguished from the
        //precursor to be uniquely identified using the current combination
        //of product ions. As we haven't started finding a unique product
        //ion combination this number is equal to the number of overlapping
        // peptides.
        double remainingOverlappingPeptides = matrix.getRowDimension();

        return determineColumnCombination(matrix,
                processedColumns,
                remainingOverlappingPeptides,
                overlappingPeptides,
                0);
    }

    /**
     * Recursive method to deterime a set of product ions that can uniquely identify
     * a precursor ion from background peptides in the same mass range.
     * <p/>
     * Starting with one column in the matrix (the left most column is picked first)
     * the algorithm tries to find a combination of column whose intersection has
     * a column sum of 0. With each recursion an additional column is added to the
     * combination (from the left to the right of the matrix) as long as the column
     * sum decreases. If the column sum reaches a value of 0 the respective column
     * set is returned. If the column sum does not decrease from one recursion to
     * the next or the set contains all columns and the column sum is still not 0
     * an empty set is returned as then there is no discriminatory combination.
     *
     * @param matrix               the overlap matrix
     * @param includedColumns      the columns already combined
     * @param previousColumnSum    the column sum of the current column combination
     * @param previousIntersection the intersection of the current column combination
     * @param iteration            the iteration
     * @return a set of column indexes
     */
    private Set<Integer> determineColumnCombination(Matrix matrix,
            Set<Integer> includedColumns,
            double previousColumnSum,
            Matrix previousIntersection,
            int iteration) {

        iteration++;

        //unit vector to calculate column sums (T = u*X)
        Matrix u = new Matrix(1, matrix.getRowDimension(), 1);

        int i0 = 0;
        int i1 = matrix.getRowDimension() - 1;
        int j0;
        int j1;

        Matrix bestIntersection = previousIntersection;
        //find maximum column sum by intersecting columns starting from the left of the matrix
        // (this is where the C-terminal Y ions are which tend to be the most frequently
        // observed ones)
        int indexColumnWithBestColumnSum = -1;
        double bestColumnSum = previousColumnSum;

        for (int n = 0; n < matrix.getColumnDimension(); n++) {

            //skip processed columns
            if (includedColumns.contains(n)) {
                continue;
            }

            //get next column n
            j0 = n;
            j1 = n;

            Matrix nextColumn = matrix.getMatrix(i0, i1, j0, j1);

            //intersect with current overlap matrix
            Matrix intersection = SigPepUtil.intersection(nextColumn, previousIntersection);

            //calculate column sum
            Matrix columnSum = u.times(intersection);
            double sum = columnSum.get(0, 0);

            //if the column sum is smaller then the previous column sum
            if (sum < bestColumnSum) {

                //set current column sum to the new minimum sum
                bestColumnSum = sum;

                //save the index of the column whose addition
                //lead to the smaller sum
                indexColumnWithBestColumnSum = n;

                //set the current overlap vector to
                bestIntersection = intersection;

            }

            if (bestColumnSum == 0) {
                break;
            }
        }

        //add the column index resulting in the smallest column sum to processed columns
        includedColumns.add(indexColumnWithBestColumnSum);

        //if we have reached a column sum of zero
        //return the columns that were used to get
        // this result
        if (bestColumnSum == 0) {
            return includedColumns;
        } //if none of the columns lead to an improvement of the combined column sum
        // and the column sum is still not 0 there is no discriminatory combination
        else if (indexColumnWithBestColumnSum == -1 && bestColumnSum > 0) {
            return new TreeSet<Integer>();
        } //else keep searching...
        else {
            return determineColumnCombination(matrix, includedColumns, bestColumnSum, bestIntersection, iteration);
        }
    }
}
