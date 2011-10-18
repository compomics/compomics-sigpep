package com.compomics.sigpep.webapp.listener;

import com.compomics.sigpep.jtraml.SigpepTransitionBean;
import com.compomics.sigpep.report.SignatureTransitionMassMatrixReader;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideResultMetaBean;
import com.compomics.util.experiment.biology.atoms.Hydrogen;
import com.compomics.util.protein.Protein;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashSet;

/**
 * This class is a
 */
public class SelectTransitionListener implements Button.ClickListener {
    private static Logger logger = Logger.getLogger(SelectTransitionListener.class);

    /**
     * The filename with the sigpep barcode that is to be added to the session.
     */
    private final File iPeptideFile;
    private final MyVaadinApplication iApplication;
    private final CheckBox iCheckBox;
    private final PeptideResultMetaBean iPeptideResultMetaBean;


    public SelectTransitionListener(File aFile, MyVaadinApplication aApplication, CheckBox aCheckBox, PeptideResultMetaBean aPeptideResultMetaBean) {
        iPeptideFile = aFile;
        iApplication = aApplication;
        iCheckBox = aCheckBox;
        iPeptideResultMetaBean = aPeptideResultMetaBean;
    }


    /**
     * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
     * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
     *
     * @param event An event containing information about the click.
     */
    public void buttonClick(Button.ClickEvent event) {
        String lFirstLine = null;
        String lPeptide = getPeptide();
        Double lMass = new Protein("", lPeptide).getMass();
        int lCharge = iPeptideResultMetaBean.getPeptideCharge();

        Double lMZ = (lMass + (lCharge * Hydrogen.H.mass)) / Math.abs(lCharge);


        // Is the button selected?
        if (iCheckBox.booleanValue()) {
            // Add to list!
            SignatureTransitionMassMatrixReader stmm = new SignatureTransitionMassMatrixReader(iPeptideFile);
            String[] lElements = stmm.getBarcode().get(0);

            for (int i = 1; i < lElements.length; i++) {
                String lQ3Mass = lElements[i];
                if (!lQ3Mass.equals("")) {
                    SigpepTransitionBean lTransitionBean = new SigpepTransitionBean();
                    double lQ3MassDouble = Double.parseDouble(lQ3Mass);

                    //CSASVLPVDVQTLNSSGPPFGK.2y16-1
                    lTransitionBean.setPeptideSequence(lPeptide);
                    lTransitionBean.setProteinAccessions(new HashSet<String>(iPeptideResultMetaBean.getProteins()));

                    int index = iPeptideResultMetaBean.getMassMatchIndex(lQ3MassDouble);
                    lTransitionBean.setQ3Mass(lQ3MassDouble);
                    lTransitionBean.setQ1Mass(lMZ);
                    lTransitionBean.setIonType(new char[]{iPeptideResultMetaBean.getBarcodeIonType(index)});
                    lTransitionBean.setIonNumber(iPeptideResultMetaBean.getBarcodeIonNumber(index));
                    lTransitionBean.setIonCharge(1);

                    iApplication.addTransitionBean(lTransitionBean);
                }
            }
        } else {
            // Remove from list!
            iApplication.removeTransitionBeansBySequence(lPeptide);
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
