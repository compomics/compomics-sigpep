package com.compomics.sigpep.analysis.impl;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import com.compomics.sigpep.RConnectionProvider;
import com.compomics.sigpep.analysis.ChargeProbabilityCalculator;

import java.util.*;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Aug-2008<br/>
 * Time: 10:41:15<br/>
 */
public class KernelBasedChargeProbabilityCalculator implements ChargeProbabilityCalculator {

    private List<Map<Double, Integer>> observedPeptideMassChargeStateCombinations;
    private Set<Integer> allowedChargeStates;
    private RConnection rConnection;

    /**
     * @param observedPeptideMassChargeStateCombinations
     *
     * @TODO: JavaDoc missing.
     */
    public KernelBasedChargeProbabilityCalculator(List<Map<Double, Integer>> observedPeptideMassChargeStateCombinations) {

        this.observedPeptideMassChargeStateCombinations = observedPeptideMassChargeStateCombinations;
        try {
            this.rConnection = setUpREnvironment();
        } catch (RserveException e) {
            throw new RuntimeException("Excpetion while connecting to R. Make sure Rserve is installed and running.", e);
        }
    }

    /**
     * @param mass
     * @return
     * @TODO: JavaDoc missing.
     */
    public Map<Double, Map<Integer, Double>> getChargeProbablitiesGivenMass(double... mass) {

        Map<Double, Map<Integer, Double>> retVal = new TreeMap<Double, Map<Integer, Double>>();
        for (Integer charge : allowedChargeStates) {

            double[] probabilities = getProbabilityOfChargeGivenMass(charge, mass);

            for (int i = 0; i < mass.length; i++) {

                double m = mass[i];
                double p = probabilities[i];


                if (!retVal.containsKey(m)) {
                    retVal.put(m, new TreeMap<Integer, Double>());
                }
                retVal.get(m).put(charge, p);

            }
        }
        return retVal;
    }

    /**
     * @param z
     * @param mass
     * @return
     * @TODO: JavaDoc missing.
     */
    public double[] getProbabilityOfChargeGivenMass(int z, double... mass) {

        double[] retVal = new double[0];

        if (this.allowedChargeStates.contains(z)) {

            try {

                REXP massRexp = new REXPDouble(mass);
                REXP chargeRexp = new REXPInteger(z);

                rConnection.assign("m", massRexp);
                rConnection.assign("z", chargeRexp);

                //get probability
                retVal = rConnection.eval("p.z.given.m(m, z, z.given.m.model)").asDoubles();

            } catch (REXPMismatchException e) {
                throw new RuntimeException("Exception while communicating with R.", e);
            } catch (RserveException e) {
                throw new RuntimeException("Exception while communicating with R.", e);
            }
        }

        return retVal;
    }

    /**
     * @return
     * @throws RserveException
     * @TODO: JavaDoc missing.
     */
    private RConnection setUpREnvironment() throws RserveException {

        //connect to R
        RConnection retVal = RConnectionProvider.getInstance().getRConnection();

        allowedChargeStates = new TreeSet<Integer>();

        int chargeBinSize = 1;
        double loessSpan = 0.45;

        List<Integer> chargeList = new ArrayList<Integer>();
        List<Double> massList = new ArrayList<Double>();
        for (Map<Double, Integer> massChargePair : observedPeptideMassChargeStateCombinations) {

            for (Double mass : massChargePair.keySet()) {
                Integer charge = massChargePair.get(mass);
                chargeList.add(charge);
                massList.add(mass);
                allowedChargeStates.add(charge);
            }
        }

        double[] masses = collectionOfDoublesToArrayOfPrimitives(massList);
        int[] charges = collectionOfIntegersToArrayOfPrimitives(chargeList);

        //create R expression for charges and masses
        REXP massesRexp = new REXPDouble(masses);
        REXP chargesRexp = new REXPInteger(charges);

        REXP binSizeRexp = new REXPInteger(chargeBinSize);
        REXP loessSpanRexp = new REXPDouble(loessSpan);


        //assign charges and masses to R variable
        retVal.assign("masses", massesRexp);
        retVal.assign("charges", chargesRexp);
        retVal.assign("bin.size", binSizeRexp);
        retVal.assign("loess.span", loessSpanRexp);

        //create matrix of masses and charges
        retVal.voidEval("mass.z <- cbind(masses,charges)");

        //get unique charge states
        retVal.voidEval("charge.states <- sort(unique(mass.z[,2]))");

        //categorise peptide charges by masses (bin size 1)
        retVal.voidEval("z.by.mass <- split(mass.z[,2],round(mass.z[,1]/bin.size, digits=0))\n" +
                "names(z.by.mass) <- bin.size * as.numeric(names(z.by.mass))");

        //calculate charg probabilities by mass
        retVal.voidEval("z.given.mass <- matrix(ncol=1+length(charge.states), nrow=length(z.by.mass))\n" +
                "z.given.mass[,] <- -1 \n" +
                "z.given.mass[,1] <- as.numeric(names(z.by.mass))\n" +
                "colnames(z.given.mass) <- c(\"mass\",charge.states)\n" +
                "rownames(z.given.mass) <- names(z.by.mass)\n" +
                "for(mass in names(z.by.mass)){\n" +
                "  n <- length(z.by.mass[[mass]])        \n" +
                "  z.abs.freq <- split(z.by.mass[[mass]], z.by.mass[[mass]])\n" +
                "    for(z in names(z.abs.freq)){\n" +
                "      z.rel.freq <- length(z.abs.freq[[z]])/n\n" +
                "      z.given.mass[paste(mass),paste(z)] <- z.rel.freq\t\t\n" +
                "    }\n" +
                "}");

        //fit curve through charge probabilities
        retVal.voidEval("z.given.m.model <- vector(\"list\", length(charge.states))");

        retVal.voidEval("names(z.given.m.model) <- charge.states");
        retVal.voidEval("for(z in names(z.given.m.model)){\n" +
                "  z <- paste(z)\n" +
                "  x <- z.given.mass[,1]\n" +
                "  y <- z.given.mass[,z]\n" +
                "  z.given.m.model[[z]] <- loess(y ~ x, span=loess.span, data.frame(x=x, y=y))\n" +
                "}");

        //define  function to predict probability of charge given mass
        retVal.voidEval("p.z.given.m <- function(m, z, zgivenmmodel){\n" +
                "   model <- zgivenmmodel[[paste(z)]]\n" +
                "   p <- predict(model, m)\n" +
                "   idx <- which(p < 0)\n" +
                "   p[idx] <- 0\n" +
                "   idx <- which(p > 1)\n" +
                "   p[idx] <- 1\n" +
                "   return (p)\n" +
                "}");

        return retVal;
    }

    /**
     * @param doubles
     * @return
     * @TODO: JavaDoc missing.
     */
    private double[] collectionOfDoublesToArrayOfPrimitives(Collection<Double> doubles) {

        double[] retVal = new double[doubles.size()];
        int i = 0;
        for (Double d : doubles) {
            retVal[i] = d;
            i++;
        }
        return retVal;
    }

    /**
     * @param integers
     * @return
     * @TODO: JavaDoc missing.
     */
    private int[] collectionOfIntegersToArrayOfPrimitives(Collection<Integer> integers) {

        int[] retVal = new int[integers.size()];
        int i = 0;
        for (Integer d : integers) {
            retVal[i] = d;
            i++;
        }
        return retVal;
    }

    /**
     * @return
     * @TODO: JavaDoc missing.
     */
    public Set<Integer> getAllowedChargeStates() {
        return allowedChargeStates;
    }

    /**
     * @throws Throwable
     * @TODO: JavaDoc missing.
     */
    protected void finalize() throws Throwable {
        //clean up
        rConnection.close();
        super.finalize();
    }
}
