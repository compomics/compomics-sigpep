package com.compomics.sigpep.util;

import Jama.Matrix;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides utilities.
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: Michael Mueller<br>
 * Date: 09-Oct-2007<br>
 * Time: 13:04:16<br>
 */
public class SigPepUtil {

    public static double round(double value, int decimals) {

        // see the Javadoc about why we use a String in the constructor
        // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Returns the sum of the values of an integer array.
     *
     * @param array array of integers
     * @return the sum
     */
    public static int sumArray(int array[]) {
        int sum = 0;
        for (int i : array) {
            sum += i;
        }
        return sum;
    }

    /**
     * Returns the sum of the values of a double array.
     *
     * @param array array of doubles
     * @return the sum
     */
    public static double sumArray(double array[]) {
        double sum = 0;
        for (double i : array) {
            sum += i;
        }
        return sum;
    }

    /**
     * @param array
     * @param ps
     * @TODO: JavaDoc missing
     */
    public static void printArray(int[][] array, PrintStream ps) {

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                ps.print(array[i][j]);
                if (j != array[i].length - 1) {
                    ps.print("\t");
                }
            }
            ps.println();
        }
    }

    /**
     * @param n
     * @return
     * @TODO: JavaDoc missing
     */
    public static long factorial(int n) {
        if (n <= 1) { // base case
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    /**
     * Returns the number of k-combinations (each of size k) from a set S with n elements (size n)
     * where n is the number of objects from which you can choose and r is the number to be chosen
     * (see binomialCoefficiant()).
     *
     * @param n the set size
     * @param k the combination size
     * @return the number of k-combinations
     */
    public static long combinationsWithoutRepitition(int n, int k) {
        return binomialCoefficient(n, k);
    }

    /**
     * Returns the number of k-combinations (each of size k) from a set S with n elements (size n)
     * where n is the number of objects from which you can choose and r is the number to be chosen
     * and each element can be chosen more then once.
     * (see multisetCoefficient()).
     *
     * @param n the set size
     * @param k the combination size
     * @return the number of k-combinations
     */
    public static long combinationsWithRepitition(int n, int k) {
        return multisetCoefficient(n, k);
    }

    /**
     * Returns the number of k-combinations (each of size k) from a set S with n elements (size n)
     * where n is the number of objects from which you can choose and r is the number to be chosen.
     *
     * @param n the set size
     * @param k the combination size
     * @return the number of k-combinations
     */
    public static long binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    /**
     * Returns the number of k-combinations (each of size k) from a set S with n elements (size n)
     * where n is the number of objects from which you can choose and r is the number to be chosen
     * and each element can be chosen more then once.
     *
     * @param n the set size
     * @param k the combination size
     * @return the number of k-combinations
     */
    public static long multisetCoefficient(int n, int k) {
        return binomialCoefficient(n + k - 1, k);
    }

    /**
     * @param data
     * @param binSize
     * @param minBinValue
     * @param maxBinValue
     * @return
     * @TODO: JavaDoc missing
     */
    public static Map<Integer, Integer> bin(Map<Double, Integer> data, int binSize, int minBinValue, int maxBinValue) {

        Map<Integer, Integer> retVal = new TreeMap<Integer, Integer>();

        for (int i = minBinValue; i <= maxBinValue; i++) {
            retVal.put(i, 0);
        }

        for (Double value : data.keySet()) {

            int frequency = data.get(value);
            int bin = value.intValue();

            if (bin >= minBinValue && bin <= maxBinValue) {

                if (retVal.containsKey(bin)) {
                    int currentFrequency = retVal.get(bin);
                    int newFrequency = currentFrequency + frequency;
                    retVal.put(bin, newFrequency);
                } else {

                    //find next smaller bin
                    for (int b = 1; b <= binSize; b++) {

                        if (retVal.containsKey(bin)) {
                            int currentFrequency = retVal.get(bin);
                            int newFrequency = currentFrequency + frequency;
                            retVal.put(bin, newFrequency);
                            break;
                        }
                    }
                }
            }
        }

        return retVal;
    }

    /**
     * Merges to boolean matrices of zeros and ones using the OR condition,
     * i.e. if a value is one in either of the matrices the value
     * in the returned matrix will be zero.
     * <p/>
     * The input matrices have to have the same dimensions.
     *
     * @param a input matrix a
     * @param b input matrix b
     * @return the union matrix c
     */
    public static Matrix union(Matrix a, Matrix b) {

        if (a.getColumnDimension() != b.getColumnDimension()) {
            throw new IllegalArgumentException("Input matrices have to have the same dimensions. Column dimension a = "
                    + a.getColumnDimension() + " != column dimension b = " + b.getColumnDimension());
        }

        if (a.getRowDimension() != b.getRowDimension()) {
            throw new IllegalArgumentException("Input matrices have to have the same dimensions. Row dimension a = "
                    + a.getRowDimension() + " != row dimension b = " + b.getRowDimension());
        }

        Matrix c = new Matrix(a.getRowDimension(), a.getColumnDimension());

        for (int m = 0; m < c.getRowDimension(); m++) {
            for (int n = 0; n < c.getColumnDimension(); n++) {

                if (a.get(m, n) == 1 || b.get(m, n) == 1) {
                    c.set(m, n, 1);
                } else {
                    c.set(m, n, 0);
                }
            }
        }

        return c;
    }

    /**
     * Merges to boolean matrices of zeros and ones using the AND condition,
     * i.e. if a value is zero in either of the matrices the value
     * in the returned matrix will be zero.
     * <p/>
     * The input matrices have to have the same dimensions.
     *
     * @param a input matrix a
     * @param b input matrix b
     * @return the intersection matrix c
     */
    public static Matrix intersection(Matrix a, Matrix b) {

        if (a.getColumnDimension() != b.getColumnDimension()) {
            throw new IllegalArgumentException("Input matrices have to have the same dimensions. Column dimension a = "
                    + a.getColumnDimension() + " != column dimension b = " + b.getColumnDimension());
        }

        if (a.getRowDimension() != b.getRowDimension()) {
            throw new IllegalArgumentException("Input matrices have to have the same dimensions. Row dimension a = "
                    + a.getRowDimension() + " != row dimension b = " + b.getRowDimension());
        }

        Matrix c = new Matrix(a.getRowDimension(), a.getColumnDimension());

        for (int m = 0; m < c.getRowDimension(); m++) {
            for (int n = 0; n < c.getColumnDimension(); n++) {

                if (a.get(m, n) == 1 && b.get(m, n) == 1) {
                    c.set(m, n, 1);
                } else {
                    c.set(m, n, 0);
                }
            }
        }

        return c;
    }

    /**
     * @param args
     * @TODO: JavaDoc missing
     */
    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/mmueller/data/sigpep/mass_overlap_human_zmin2_zmax2_acc1_overmin0.tab"));

            Map<Double, Integer> mass2SigPepFrequency = new TreeMap<Double, Integer>();
            Map<Double, Integer> mass2PepFrequency = new TreeMap<Double, Integer>();

            String line;
            while ((line = br.readLine()) != null) {

                String[] columns = line.split("\t");

                Double mass = new Double(columns[0]);
                Integer sigPepFreq = new Integer(columns[1]);
                Integer pepFreq = new Integer(columns[2]);

                mass2SigPepFrequency.put(mass, sigPepFreq);
                mass2PepFrequency.put(mass, pepFreq);
            }

            br.close();

            Map<Integer, Integer> mass2SigPepFrequencyBinned = bin(mass2SigPepFrequency, 1, 600, 4000);
            Map<Integer, Integer> mass2PepFrequencyBinned = bin(mass2PepFrequency, 1, 600, 4000);

            PrintWriter pw = new PrintWriter("/home/mmueller/data/sigpep/mass_overlap_human_zmin2_zmax2_acc1_overmin0_binned.tab");

            for (Integer massBin : mass2SigPepFrequencyBinned.keySet()) {

                Integer sigPepFreq = mass2SigPepFrequencyBinned.get(massBin);
                Integer pepFreq = mass2PepFrequencyBinned.get(massBin);

                pw.println(massBin + "\t" + sigPepFreq + "\t" + pepFreq);
            }

            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
