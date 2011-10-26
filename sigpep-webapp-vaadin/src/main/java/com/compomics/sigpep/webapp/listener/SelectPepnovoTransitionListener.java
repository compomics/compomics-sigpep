package com.compomics.sigpep.webapp.listener;

import com.compomics.pepnovo.beans.IntensityPredictionBean;
import com.compomics.sigpep.jtraml.SigpepTransitionBean;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.report.SignatureTransitionMassMatrixReader;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideResultMetaBean;
import com.compomics.util.experiment.biology.atoms.Hydrogen;
import com.compomics.util.protein.Protein;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * This class is a
 */
public class SelectPepnovoTransitionListener implements Button.ClickListener {
    private static Logger logger = Logger.getLogger(SelectPepnovoTransitionListener.class);

    /**
     * The filename with the sigpep barcode that is to be added to the session.
     */
    private final File iPeptideFile;
    private final MyVaadinApplication iApplication;
    private final CheckBox iCheckBox;
    private final PeptideResultMetaBean iPeptideResultMetaBean;
    private final IntensityPredictionBean iIntensityPredictionBean;
    public MathContext iMathContext = new MathContext(2, RoundingMode.FLOOR);


    public SelectPepnovoTransitionListener(File aFile, MyVaadinApplication aApplication, CheckBox aCheckBox, PeptideResultMetaBean aPeptideResultMetaBean, IntensityPredictionBean aIntensityPredictionBean) {
        iPeptideFile = aFile;
        iApplication = aApplication;
        iCheckBox = aCheckBox;
        iPeptideResultMetaBean = aPeptideResultMetaBean;
        iIntensityPredictionBean = aIntensityPredictionBean;
    }


    /**
     * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
     * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
     *
     * @param event An event containing information about the click.
     */
    public void buttonClick(Button.ClickEvent event) {
        logger.debug("adding pepnovo transition to kl");

        double lTargetMZ = -1;
        String lPeptide = getPeptide();
        int lCharge = iPeptideResultMetaBean.getPeptideCharge();

        Double lMass = new Protein("", lPeptide).getMass();
        Double lMZ = (lMass + (lCharge * Hydrogen.H.mass)) / Math.abs(lCharge);

        // Add to list!
        SignatureTransitionMassMatrixReader stmm = new SignatureTransitionMassMatrixReader(iPeptideFile);
        String[] lTargetElements = stmm.getTarget().get(0);

        // Match the pepnovo mass and t
        List<String> lMassesString = Arrays.asList(lTargetElements).subList(2, lTargetElements.length);
        List<Double> lMasses = Lists.transform(lMassesString, new Function<String, Double>() {
            @Override
            public Double apply(@Nullable String s) {
                BigDecimal v = new BigDecimal(Double.parseDouble(s), iMathContext);
                return v.doubleValue();
            }
        });

        int lIonNumber = iIntensityPredictionBean.getPeptideFragmentIon().getNumber();
        lTargetMZ = lMasses.get(lIonNumber - 1);

        // Is the button selected?
        if (iCheckBox.booleanValue()) {

            // B- or Y-ion type?
            String lIonTypeString = lTargetElements[1];
            ProductIonType lType = null;
            if (lIonTypeString.equals(ProductIonType.Y.getName()))
                lType = ProductIonType.Y;

            if (lIonTypeString.equals(ProductIonType.B.getName()))
                lType = ProductIonType.B;


            SigpepTransitionBean lTransitionBean = new SigpepTransitionBean();

            lTransitionBean.setQ1Mass(lMZ);
            lTransitionBean.setQ3Mass(lTargetMZ);

            //CSASVLPVDVQTLNSSGPPFGK.2y16-1
            lTransitionBean.setPeptideSequence(lPeptide);
            lTransitionBean.setProteinAccessions(new HashSet<String>(iPeptideResultMetaBean.getProteins()));

            lTransitionBean.setIonType(lType.getName().toCharArray());
            lTransitionBean.setIonNumber(lIonNumber);
            lTransitionBean.setIonCharge(1);

            lTransitionBean.setEndTime(iPeptideResultMetaBean.getRetentionTime());

            lTransitionBean.addPredictionTool("pepnovo");

            iApplication.addTransitionBean(lTransitionBean);
        } else {
            // Remove from list!
            iApplication.removeTransitionBean(lPeptide, lTargetMZ);
        }
    }

    /**
     * This method creates an apropriate filename to display in the table.
     *
     * @return
     */

    private String getPeptide() {
        // Remove the extension of the filename
        return iPeptideFile.getName().substring(0, iPeptideFile.getName().indexOf("."));
    }
}
