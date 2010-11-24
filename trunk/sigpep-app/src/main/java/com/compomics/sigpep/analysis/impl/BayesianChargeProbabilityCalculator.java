package com.compomics.sigpep.analysis.impl;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import com.compomics.sigpep.analysis.ChargeProbabilityCalculator;
import com.compomics.sigpep.util.DelimitedTableReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Aug-2008<br/>
 * Time: 16:45:29<br/>
 */
public class BayesianChargeProbabilityCalculator {

    private List<Map<Double, Integer>> observedPeptideMassChargeStateCombinations;
    private Set<Integer> allowedChargeStates;
    private Map<Integer, Double> peptideChargeProbabilityMassDistribution;
//    private DensityDistribution peptideChargeProbabilityMassDistribution;

    private DensityDistribution peptideMassProbabilityDensityDistribution;
    private Map<Integer, DensityDistribution> peptideMassProbabilityDensityDistributionByCharge;


    public BayesianChargeProbabilityCalculator(List<Map<Double, Integer>> observedPeptideMassChargeStateCombinations) {

        this.observedPeptideMassChargeStateCombinations = observedPeptideMassChargeStateCombinations;

        this.peptideMassProbabilityDensityDistribution = getPeptideMassProbabilityDensityDistribution();
        this.peptideChargeProbabilityMassDistribution = getPeptideChargeProbablityMassDistribution();
        this.peptideMassProbabilityDensityDistributionByCharge = getPeptideMassProbabilityDensityDistributionByCharge();

        this.allowedChargeStates = peptideChargeProbabilityMassDistribution.keySet();

    }

    /**
     * @return
     */
    private Map<Integer, Double> getPeptideChargeProbablityMassDistribution() {

        Map<Integer, Double> retVal = new TreeMap<Integer, Double>();

        Map<Integer, Integer> chargeCount = new TreeMap<Integer, Integer>();
        int n = observedPeptideMassChargeStateCombinations.size();
        for (Map<Double, Integer> combination : observedPeptideMassChargeStateCombinations) {
            for (Double mass : combination.keySet()) {
                Integer charge = combination.get(mass);
                if (!chargeCount.containsKey(charge)) {
                    chargeCount.put(charge, 1);
                } else {
                    int count = chargeCount.get(charge);
                    count++;
                    chargeCount.put(charge, count);
                }
            }
        }

        for (Integer charge : chargeCount.keySet()) {

            double density = (double) chargeCount.get(charge) / (double) n;
            retVal.put(charge, density);

        }

        return retVal;

    }
//    private DensityDistribution getPeptideChargeProbablityMassDistribution() {
//
//        try {
//
//            //create integer array of charges
//            List<Double> chargeList = new ArrayList<Double>();
//            for (Map<Double, Integer> combination : observedPeptideMassChargeStateCombinations) {
//                for (Double mass : combination.keySet()) {
//                    chargeList.add(new Double(combination.get(mass)));
//                }
//
//            }
//
//            double[] charges = collectionToArrayOfPrimitives(chargeList);
//
//            //create R expression for charges
//            REXP chargesRexp = new REXPDouble(charges);
//
//            //connect to R
//            RConnection c = new RConnection();
//
//            //assign charges to R variable
//            c.assign("charges", chargesRexp);
//
//            //get density distribution
//            RList result = c.eval("density(charges)").asList();
//
//            double[] x = result.at("x").asDoubles();
//            double[] y = result.at("y").asDoubles();
//
//            c.close();
//
//            return new DensityDistribution(x, y);
//
//        } catch (RserveException e) {
//            throw new RuntimeException("Excpetion while connecting to R. Make sure Rserve is installed and running.", e);
//        } catch (REXPMismatchException e) {
//            throw new RuntimeException("Exception while converting R types to Java types.", e);
//        }
//
//
//    }

    /**
     * @return
     */
    private DensityDistribution getPeptideMassProbabilityDensityDistribution() {


        try {

            //create integer array of charges
            List<Double> massList = new ArrayList<Double>();
            for (Map<Double, Integer> combination : observedPeptideMassChargeStateCombinations) {
                for (Double mass : combination.keySet()) {
                    massList.add(mass);
                }

            }

            double[] masses = collectionToArrayOfPrimitives(massList);

            //create R expression for charges
            REXP massesRexp = new REXPDouble(masses);

            //connect to R
            RConnection c = new RConnection();

            //assign charges to R variable
            c.assign("masses", massesRexp);

            //get density distribution
            RList result = c.eval("density(masses)").asList();

            double[] x = result.at("x").asDoubles();
            double[] y = result.at("y").asDoubles();

            c.close();

            return new DensityDistribution(x, y);

        } catch (RserveException e) {
            throw new RuntimeException("Excpetion while connecting to R. Make sure Rserve is installed and running.", e);
        } catch (REXPMismatchException e) {
            throw new RuntimeException("Exception while converting R types to Java types.", e);
        }

    }

    private Map<Integer, DensityDistribution> getPeptideMassProbabilityDensityDistributionByCharge() {

        Map<Integer, DensityDistribution> retVal = new TreeMap<Integer, DensityDistribution>();

        //group masses by charge
        Map<Integer, List<Double>> groupedMasses = new TreeMap<Integer, List<Double>>();
        for (Map<Double, Integer> combination : observedPeptideMassChargeStateCombinations) {

            for (Double mass : combination.keySet()) {

                Integer charge = combination.get(mass);

                if (!groupedMasses.containsKey(charge)) {
                    groupedMasses.put(charge, new ArrayList<Double>());
                }

                groupedMasses.get(charge).add(mass);

            }

        }

        //get density distribution for each charge
        for (Integer charge : groupedMasses.keySet()) {

            try {

                List<Double> massList = groupedMasses.get(charge);
                double[] masses = collectionToArrayOfPrimitives(massList);

                //create R expression for charges
                REXP massesRexp = new REXPDouble(masses);

                //connect to R
                RConnection c = new RConnection();
                String variableName = "massesForCharge";
                //assign charges to R variable
                c.assign(variableName, massesRexp);

                //get density distribution
                RList result = c.eval("density(" + variableName + ")").asList();

                double[] x = result.at("x").asDoubles();
                double[] y = result.at("y").asDoubles();

                c.close();

                retVal.put(charge, new DensityDistribution(x, y));

            } catch (RserveException e) {
                throw new RuntimeException("Excpetion while connecting to R. Make sure Rserve is installed and running.", e);
            } catch (REXPMismatchException e) {
                throw new RuntimeException("Exception while converting R types to Java types.", e);
            }

        }

        return retVal;
    }

    private double[] collectionToArrayOfPrimitives(Collection<Double> doubles) {

        double[] retVal = new double[doubles.size()];
        int i = 0;
        for (Double d : doubles) {
            retVal[i] = d;
            i++;
        }
        return retVal;
    }

    public List<Map<Double, Integer>> getObservedPeptideMassChargeStateCombinations() {
        return observedPeptideMassChargeStateCombinations;
    }

    public void setObservedPeptideMassChargeStateCombinations(List<Map<Double, Integer>> observedPeptideMassChargeStateCombinations) {
        this.observedPeptideMassChargeStateCombinations = observedPeptideMassChargeStateCombinations;
    }

    public Map<Integer, Double> getChargeProbablitiesGivenMass(double mass){

        Map<Integer, Double> retVal = new TreeMap<Integer, Double>();
        for(Integer charge : allowedChargeStates){
            retVal.put(charge, getProbabilityOfChargeGivenMass(charge, mass));
        }
        return retVal;

    }

    public double getProbabilityOfChargeGivenMass(int z, double mass) {

        double retVal = 0;

        double priorProbabilityPeptideCharge = 0;
        double priorProbabilityPeptideMass = 0;
        double conditionalProbablityPeptideMassGivenCharge = 0;

        //priorProbabilityPeptideCharge = approximateProbability(z, peptideChargeProbabilityMassDistribution);

        priorProbabilityPeptideCharge = peptideChargeProbabilityMassDistribution.get(z);

        priorProbabilityPeptideMass = approximateProbability(mass, peptideMassProbabilityDensityDistribution);

        conditionalProbablityPeptideMassGivenCharge = approximateProbability(mass, peptideMassProbabilityDensityDistributionByCharge.get(z));

        if (priorProbabilityPeptideMass > 0) {

            retVal = (conditionalProbablityPeptideMassGivenCharge * priorProbabilityPeptideCharge) /
                    priorProbabilityPeptideMass;

        }

        return retVal;

    }

    private double approximateProbability(double value, DensityDistribution densityDistribution) {

        try {

            double[] x = densityDistribution.getX();
            double[] y = densityDistribution.getY();

            //create R expression for charges
            REXP xRexp = new REXPDouble(x);
            REXP yRexp = new REXPDouble(y);
            REXP valueRexp = new REXPDouble(value);

            //connect to R
            RConnection c = new RConnection();

            //assign charges to R variable
            c.assign("x", xRexp);
            c.assign("y", yRexp);
            c.assign("value", valueRexp);

            double result = c.eval("approx(x, y, value)$y").asDouble();

            c.close();

            return result;

        } catch (RserveException e) {
            throw new RuntimeException("Excpetion while connecting to R. Make sure Rserve is installed and running.", e);
        } catch (REXPMismatchException e) {
            throw new RuntimeException("Exception while converting R types to Java types.", e);
        }

    }

    public Set<Integer> getAllowedChargeStates() {
        return allowedChargeStates;
    }

    public static void main(String[] args) {

        try {

            List<Map<Double, Integer>> massToCharge = new ArrayList<Map<Double, Integer>>();
            DelimitedTableReader dtr = new DelimitedTableReader(new FileInputStream("/home/mmueller/data/sigpep/collab_waters/michael_charge_details.tab"), "\t");
            for (Iterator<String[]> rows = dtr.read(); rows.hasNext();) {

                String[] row = rows.next();

                Map<Double, Integer> combination = new HashMap<Double, Integer>();
                try {
                    double mz = new Double(row[2]);
                    int charge = new Integer(row[1]);
                    double mass = (mz * charge) - charge;
                    combination.put(mass, new Integer(row[1]));
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
                massToCharge.add(combination);
            }

            ChargeProbabilityCalculator calc = new KernelBasedChargeProbabilityCalculator(massToCharge);

            List<Map<Integer, Double>> chargeMass = new ArrayList<Map<Integer, Double>>();
            Map<Integer, Double> chargeMassPair1 = new HashMap<Integer, Double>();
            chargeMassPair1.put(3, 1000.0);
            Map<Integer, Double> chargeMassPair2 = new HashMap<Integer, Double>();
            chargeMassPair2.put(2, 1000.0);
            Map<Integer, Double> chargeMassPair3 = new HashMap<Integer, Double>();
            chargeMassPair3.put(1, 4200.0);
            Map<Integer, Double> chargeMassPair4 = new HashMap<Integer, Double>();
            chargeMassPair4.put(4, 4500.0);

            chargeMass.add(chargeMassPair1);
            chargeMass.add(chargeMassPair2);
            chargeMass.add(chargeMassPair3);
            chargeMass.add(chargeMassPair4);
//            int charge = 4;
//            for (int mass = 4000; mass < 5000; mass++) {

            for (Map<Integer, Double> pair : chargeMass) {
                for (Integer charge : pair.keySet()) {
                    Double mass = pair.get(charge);

                    double[] p = calc.getProbabilityOfChargeGivenMass(charge, mass);

                    System.out.println(charge + " " + mass + " " + p);
                }
            }
            //}
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    private class DensityDistribution {

        private double[] x;
        private double[] y;

        private DensityDistribution(double[] x, double[] y) {
            this.x = x;
            this.y = y;
        }

        public double[] getX() {
            return x;
        }

        public void setX(double[] x) {
            this.x = x;
        }

        public double[] getY() {
            return y;
        }

        public void setY(double[] y) {
            this.y = y;
        }

    }
}
