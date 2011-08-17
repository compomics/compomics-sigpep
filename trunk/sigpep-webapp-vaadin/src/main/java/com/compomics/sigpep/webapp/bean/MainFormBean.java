package com.compomics.sigpep.webapp.bean;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class MainFormBean {

    private double massAccuracy;
    private String species;

    public double getMassAccuracy() {
        return massAccuracy;
    }

    public void setMassAccuracy(double aMassAccuracy) {
        massAccuracy = aMassAccuracy;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String aSpecies) {
        species = aSpecies;
    }
}
