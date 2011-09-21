package com.compomics.sigpep.webapp.listener;

import com.compomics.jtraml.beans.ThermoTransitionBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.util.protein.Protein;
import com.google.common.io.Files;
import com.vaadin.ui.Button;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

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

    public SelectTransitionListener(File aPeptideFile, MyVaadinApplication aApplication) {
        this.iPeptideFile = aPeptideFile;
        iApplication = aApplication;
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



        try {
            lFirstLine = Files.readFirstLine(iPeptideFile, Charset.defaultCharset());
            if (lFirstLine.startsWith("bc")) {
                String[] lElements = lFirstLine.split("\t");

                for (int i = 1; i < lElements.length; i++) {
                    String lQ3Mass = lElements[i];
                    if(!lQ3Mass.equals("")){
                        ThermoTransitionBean lTransitionBean = new ThermoTransitionBean();
                        lTransitionBean.setQ1Mass(lMass);
                        lTransitionBean.setQ3Mass(Double.parseDouble(lQ3Mass));
                        //CSASVLPVDVQTLNSSGPPFGK.2y16-1
                        lTransitionBean.setID(lPeptide + ".1x1-" + i);

                        iApplication.addTransitionBean(lTransitionBean);
                    }

                }

            } else {
                throw new IllegalArgumentException(iPeptideFile + " does not start with a barcode as it is supposed to be!!");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This method creates an apropriate filename to display in the table.
     * @return
     */
    private String getPeptide() {
        // Remove the extension of the filename
        return iPeptideFile.getName().substring(0, iPeptideFile.getName().indexOf("."));
    }
}
