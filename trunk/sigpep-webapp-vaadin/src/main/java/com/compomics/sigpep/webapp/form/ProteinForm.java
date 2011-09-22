package com.compomics.sigpep.webapp.form;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.SignatureTransition;
import com.compomics.sigpep.report.SignatureTransitionMassMatrix;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.ProteinFormBean;
import com.compomics.sigpep.webapp.component.ComponentFactory;
import com.compomics.sigpep.webapp.component.ResultsTable;
import com.compomics.sigpep.webapp.factory.ProteinFormFieldFactory;
import com.google.common.io.Files;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class ProteinForm extends Form {
    private static Logger logger = Logger.getLogger(ProteinForm.class);

    private MyVaadinApplication iApplication;

    private ProteinFormBean iProteinFormBean;

    private Vector<String> iOrder;

    private HorizontalLayout iFormButtonLayout;
    private HorizontalLayout iProgressIndicatorLayout;
    private Button iSubmitButton;
    private Button iResetButton;

    public ProteinForm(String aCaption, MyVaadinApplication aApplication) {
        this.setCaption(aCaption);
        iApplication = aApplication;

        this.setFormFieldFactory(new ProteinFormFieldFactory());

        iProteinFormBean = new ProteinFormBean();
        BeanItem<ProteinFormBean> lBeanItem = new BeanItem<ProteinFormBean>(iProteinFormBean);
        this.setItemDataSource(lBeanItem);

        iSubmitButton = new Button("Submit", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                try {
                    commit();
                    resetValidation();

                    ProteinFormThread lSigPepFormThread = new ProteinFormThread();
                    lSigPepFormThread.start();

                    //add label and progress indicator
                    iProgressIndicatorLayout = ComponentFactory.createProgressIndicator("Processing...");

                    iFormButtonLayout.addComponent(iProgressIndicatorLayout);
                    iFormButtonLayout.requestRepaint();

                    //disable form buttons during run
                    iSubmitButton.setEnabled(Boolean.FALSE);
                    iResetButton.setEnabled(Boolean.FALSE);

                } catch (Validator.InvalidValueException e) {
                    // Failed to commit. The validation errors are
                    // automatically shown to the user.
                }
            }
        });

        iResetButton = new Button("Reset", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                resetForm();
            }
        });

        iFormButtonLayout = new HorizontalLayout();
        iFormButtonLayout.setSpacing(Boolean.TRUE);
        iFormButtonLayout.addComponent(iSubmitButton);
        iFormButtonLayout.addComponent(iResetButton);
        this.getFooter().addComponent(iFormButtonLayout);

        iOrder = new Vector();
        iOrder.add("species");
        iOrder.add("massAccuracy");
        iOrder.add("minimumCombinationSize");
        iOrder.add("maximumCombinationSize");
        iOrder.add("signatureTransitionFinderType");
        iOrder.add("proteaseName");
        iOrder.add("chargeStates");
        iOrder.add("proteinAccession");
        this.setOrder();

        this.setImmediate(Boolean.TRUE);

    }

    private void resetValidation() {
        this.setComponentError(null);
        this.setValidationVisible(false);
        this.setOrder();
    }

    private void resetForm() {
        iProteinFormBean = new ProteinFormBean();
        BeanItem<ProteinFormBean> lBeanItem = new BeanItem<ProteinFormBean>(iProteinFormBean);
        ProteinForm.this.setItemDataSource(lBeanItem);
        iApplication.clearResultTableComponent();

        resetValidation();
    }


    private void setOrder() {
        this.setVisibleItemProperties(iOrder);
    }

    private class ProteinFormThread extends Thread {

        public void run() {

            SigPepSession lSigPepSession = MyVaadinApplication.getSigPepSession();

            File outputFolder = Files.createTempDir();
            logger.info(outputFolder);

            if(MyVaadinApplication.getSigPepQueryService() == null){
                MyVaadinApplication.setSigPepQueryService(MyVaadinApplication.getSigPepSession().createSigPepQueryService());
            }

            Protease aProtease = MyVaadinApplication.getSigPepQueryService().getProteaseByShortName(iProteinFormBean.getProteaseName());

            //create peptide generator for protease
            logger.info("creating peptide generator");
            PeptideGenerator lGenerator = lSigPepSession.createPeptideGenerator(aProtease);

            //get peptides generated by protease
            logger.info("generating lBackgroundPeptides");
            Set<Peptide> lBackgroundPeptides = lGenerator.getPeptides();

            logger.info("generating signature peptides");
            Set<Peptide> lSignaturepeptides = lGenerator.getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(iProteinFormBean.getProteinAccession(), 1);
            for (Peptide peptide : lSignaturepeptides) {
                logger.info(peptide.getSequenceString());
            }

            //create signature transition finder
            logger.info("creating signature transition finder");

            HashSet lPrecursorChargeStates = new HashSet();
            lPrecursorChargeStates.add(2);
            lPrecursorChargeStates.add(3);

            Set<ProductIonType> lTargetProductIonTypes = new HashSet<ProductIonType>();
            lTargetProductIonTypes.add(ProductIonType.Y);

            Set<ProductIonType> lBackgroundProductIonTypes = new HashSet<ProductIonType>();
            lBackgroundProductIonTypes.add(ProductIonType.Y);
            lBackgroundProductIonTypes.add(ProductIonType.B);

            Set<Integer> lProductIonChargeStates = new HashSet<Integer>();
            lProductIonChargeStates.add(1);

            SignatureTransitionFinder finder = lSigPepSession.createSignatureTransitionFinder(
                    lBackgroundPeptides,
                    lTargetProductIonTypes,
                    lBackgroundProductIonTypes,
                    lPrecursorChargeStates,
                    lProductIonChargeStates,
                    iProteinFormBean.getMassAccuracy(),
                    iProteinFormBean.getMinimumCombinationSize(),
                    iProteinFormBean.getMaximumCombinationSize(),
                    iProteinFormBean.getSignatureTransitionFinderType());

            logger.info("finding signature transitions");
            List<SignatureTransition> st = finder.findSignatureTransitions(lSignaturepeptides);

            for (SignatureTransition t : st) {
                logger.info("printing peptide " + t.getPeptide().getSequenceString());
                try {
                    OutputStream os = new FileOutputStream(outputFolder.getAbsolutePath() + File.separator + t.getPeptide().getSequenceString() + ".tsv");

                    SignatureTransitionMassMatrix m = new SignatureTransitionMassMatrix(t);
                    m.write(os);
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ArrayList lResultFiles = new ArrayList();
            logger.info("generated " + lResultFiles.size() + " peptide result files");
            Collections.addAll(lResultFiles, outputFolder.listFiles(new FileFilter() {
                public boolean accept(File aFile) {
                    return aFile.getName().endsWith(".tsv");
                }
            }));

            synchronized (iApplication) {
                //enable form buttons after run
                iSubmitButton.setEnabled(Boolean.TRUE);
                iResetButton.setEnabled(Boolean.TRUE);

                iFormButtonLayout.removeComponent(iProgressIndicatorLayout);
                iApplication.setResultTableComponent(new ResultsTable(lResultFiles, iApplication, iApplication));
            }

            iApplication.push();

        }
    }

}