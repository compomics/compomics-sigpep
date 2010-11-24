package com.compomics.sigpep.web.app;

import com.compomics.sigpep.model.PeptideFeature;

import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 28-Jul-2008<br/>
 * Time: 16:48:30<br/>
 */
public class SignaturePeptideResultBean {

    private SigPepSessionBean sigPepSessionBean;
    private List<PeptideFeature> signaturePeptideFeatures;

    public SigPepSessionBean getSigPepSessionBean() {
        return sigPepSessionBean;
    }

    public void setSigPepSessionBean(SigPepSessionBean sigPepSessionBean) {
        this.sigPepSessionBean = sigPepSessionBean;
    }

    public void setSignaturePeptideFeature(List<PeptideFeature> signaturePeptideFeatures) {
        this.signaturePeptideFeatures = signaturePeptideFeatures;
    }

    public List<PeptideFeature> getSignaturePeptideFeatures(){

        if(signaturePeptideFeatures==null){
            this.setSignaturePeptideFeature(sigPepSessionBean.getSignaturePeptides());
        }

        return signaturePeptideFeatures;
    }


}
