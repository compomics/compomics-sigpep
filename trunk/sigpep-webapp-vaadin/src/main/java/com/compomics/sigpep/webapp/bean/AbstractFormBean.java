package com.compomics.sigpep.webapp.bean;

import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 19/09/11
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFormBean {

    private Organism iSpecies;
    private double iMassAccuracy = 0.2;
    private int iMinimumCombinationSize = 1;
    private int iMaximumCombinationSize = 5;
    private SignatureTransitionFinderType iSignatureTransitionFinderType;
    private String iProteaseName;

    public double getMassAccuracy() {
        return iMassAccuracy;
    }

    public void setMassAccuracy(double aMassAccuracy) {
        iMassAccuracy = aMassAccuracy;
    }

    public Organism getSpecies() {
        return iSpecies;
    }

    public void setSpecies(Organism aSpecies) {
        iSpecies = aSpecies;
    }

    public int getMinimumCombinationSize() {
        return iMinimumCombinationSize;
    }

    public void setMinimumCombinationSize(int aMinimumCombinationSize) {
        iMinimumCombinationSize = aMinimumCombinationSize;
    }

    public int getMaximumCombinationSize() {
        return iMaximumCombinationSize;
    }

    public void setMaximumCombinationSize(int aMaximumCombinationSize) {
        iMaximumCombinationSize = aMaximumCombinationSize;
    }

    public SignatureTransitionFinderType getSignatureTransitionFinderType() {
        return iSignatureTransitionFinderType;
    }

    public void setSignatureTransitionFinderType(SignatureTransitionFinderType aSignatureTransitionFinderType) {
        iSignatureTransitionFinderType = aSignatureTransitionFinderType;
    }

    public String getProteaseName() {
        return iProteaseName;
    }

    public void setProteaseName(String aProteaseName) {
        iProteaseName = aProteaseName;
    }

}
