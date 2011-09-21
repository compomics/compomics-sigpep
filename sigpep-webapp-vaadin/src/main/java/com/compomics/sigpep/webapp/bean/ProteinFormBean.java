package com.compomics.sigpep.webapp.bean;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class ProteinFormBean extends AbstractFormBean {

    private String iProteinAccession = "ENSP00000444838";

    public String getProteinAccession() {
        return iProteinAccession;
    }

    public void setProteinAccession(String aProteinAccession) {
        iProteinAccession = aProteinAccession;
    }

}
