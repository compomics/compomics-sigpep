package com.compomics.sigpep.analysis;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 15-Aug-2008<br/>
 * Time: 14:55:36<br/>
 */
public interface ChargeProbabilityCalculator {

    double[] getProbabilityOfChargeGivenMass(int charge, double... mass);

    Map<Double, Map<Integer, Double>> getChargeProbablitiesGivenMass(double... mass);

    Set<Integer> getAllowedChargeStates(); 

}
