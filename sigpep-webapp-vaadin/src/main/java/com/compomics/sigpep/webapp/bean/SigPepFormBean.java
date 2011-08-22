package com.compomics.sigpep.webapp.bean;

import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.vaadin.ui.Button;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class SigPepFormBean {

    private Organism iSpecies;
    private double iMassAccuracy = 0.2;
    private int iMinimumCombinationSize = 1;
    private int iMaximumCombinationSize = 5;
    private SignatureTransitionFinderType iSignatureTransitionFinderType;
    private String iProteaseName;
    private String iProteinAccession = "ENSP00000444838";

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

    public String getProteinAccession() {
        return iProteinAccession;
    }

    public void setProteinAccession(String aProteinAccession) {
        iProteinAccession = aProteinAccession;
    }

}
