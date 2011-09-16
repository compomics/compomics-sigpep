package com.compomics.sigpep.webapp.form;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.report.SignatureTransitionMassMatrix;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.SigPepFormBean;
import com.compomics.sigpep.webapp.component.ComponentFactory;
import com.compomics.sigpep.webapp.component.ResultsTable;
import com.compomics.sigpep.webapp.factory.SigPepFormFieldFactory;
import com.google.common.io.Files;
import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class SigPepForm extends Form {
    private static Logger logger = Logger.getLogger(SigPepForm.class);

    private MyVaadinApplication iMyVaadinApplication;

    private SigPepFormFieldFactory iSigPepFormFieldFactory;
    private SigPepFormBean iSigPepFormBean;

    private Vector<String> iOrder;

    private Button iSubmitButton;
    private Button iResetButton;
    private HorizontalLayout iProgressIndicatorLayout;

    public SigPepForm(String aCaption, MyVaadinApplication aMyVaadinApplication) {
        this.setCaption(aCaption);
        iMyVaadinApplication = aMyVaadinApplication;

        iSigPepFormFieldFactory = new SigPepFormFieldFactory(iMyVaadinApplication);
        this.setFormFieldFactory(iSigPepFormFieldFactory);

        iSigPepFormBean = iMyVaadinApplication.getSigPepFormBean();
        BeanItem<SigPepFormBean> lBeanItem = new BeanItem<SigPepFormBean>(iSigPepFormBean);
        this.setItemDataSource(lBeanItem);

        iSubmitButton = new Button("Submit", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                try {
                    commit();
                    resetValidation();

                    ExecutorService lExecutorService = Executors.newSingleThreadExecutor();
                    Runnable lSigPepFormRunner = new SigPepFormRunner();
                    lExecutorService.execute(lSigPepFormRunner);
                    lExecutorService.shutdown();

                    //add label and progress indicator
                    iProgressIndicatorLayout = new HorizontalLayout();
                    iProgressIndicatorLayout.setSpacing(Boolean.TRUE);

                    Label lLabel = new Label("Processing...");
                    ProgressIndicator lProgressIndicator = new ProgressIndicator();
                    lProgressIndicator.setIndeterminate(true);
                    lProgressIndicator.setPollingInterval(5000);

                    iProgressIndicatorLayout.addComponent(lProgressIndicator);
                    iProgressIndicatorLayout.addComponent(lLabel);

                    SigPepForm.this.getFooter().addComponent(iProgressIndicatorLayout);
                    SigPepForm.this.getFooter().requestRepaint();

                    //disable form buttons during run
                    iSubmitButton.setEnabled(Boolean.TRUE);
                    iResetButton.setEnabled(Boolean.TRUE);

                } catch (Validator.InvalidValueException e) {
                    // Failed to commit. The validation errors are
                    // automatically shown to the user.
                }
            }
        });

        iResetButton = new Button("Reset", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                iMyVaadinApplication.setSigPepFormBean(new SigPepFormBean());
                BeanItem<SigPepFormBean> lBeanItem = new BeanItem<SigPepFormBean>(iMyVaadinApplication.getSigPepFormBean());
                SigPepForm.this.setItemDataSource(lBeanItem);
                resetValidation();
            }
        });

        HorizontalLayout lFormButtonLayout = new HorizontalLayout();
        lFormButtonLayout.setSpacing(Boolean.TRUE);
        lFormButtonLayout.addComponent(iSubmitButton);
        lFormButtonLayout.addComponent(iResetButton);
        this.getFooter().addComponent(lFormButtonLayout);

        iOrder = new Vector();
        iOrder.add("species");
        iOrder.add("massAccuracy");
        iOrder.add("minimumCombinationSize");
        iOrder.add("maximumCombinationSize");
        iOrder.add("signatureTransitionFinderType");
        iOrder.add("proteaseName");
        iOrder.add("proteinAccession");
        this.setOrder();

        this.setImmediate(Boolean.TRUE);

    }

    private void resetValidation() {
        this.setComponentError(null);
        this.setValidationVisible(false);
        this.setOrder();
    }

    private void setOrder() {
        this.setVisibleItemProperties(iOrder);
    }

    private class SigPepFormRunner implements Runnable {

        public void run() {

            SigPepSession lSigPepSession = iMyVaadinApplication.getSigPepSession();

            File outputFolder = Files.createTempDir();
            logger.info(outputFolder);

            SigPepQueryService lSigPepQueryService = iMyVaadinApplication.getSigPepSession().createSigPepQueryService();

            Protease aProtease = lSigPepQueryService.getProteaseByShortName(iSigPepFormBean.getProteaseName());

            //create peptide generator for protease
            logger.info("creating peptide generator");
            PeptideGenerator lGenerator = lSigPepSession.createPeptideGenerator(aProtease);

            //get peptides generated by protease
            logger.info("generating lBackgroundPeptides");
            Set<Peptide> lBackgroundPeptides = lGenerator.getPeptides();

            logger.info("generating signature peptides");
            Set<Peptide> lSignaturepeptides = lGenerator.getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(iSigPepFormBean.getProteinAccession(), 1);
            for (Peptide peptide : lSignaturepeptides) {
                logger.info(peptide.getSequenceString());
            }

            //create signature transition finder
            logger.info("creating signature transition finder");

            HashSet lChargeStates = new HashSet();
            lChargeStates.add(2);
            lChargeStates.add(3);

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
                    lChargeStates,
                    lProductIonChargeStates,
                    iSigPepFormBean.getMassAccuracy(),
                    iSigPepFormBean.getMinimumCombinationSize(),
                    iSigPepFormBean.getMaximumCombinationSize(),
                    iSigPepFormBean.getSignatureTransitionFinderType());

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
            Collections.addAll(lResultFiles, outputFolder.listFiles(new FileFilter() {
                public boolean accept(File aFile) {
                    return aFile.getName().endsWith(".tsv");
                }
            }));

            synchronized (iMyVaadinApplication) {
                SigPepForm.this.getFooter().removeComponent(iProgressIndicatorLayout);
                iMyVaadinApplication.getMainWindow().addComponent(new ResultsTable(lResultFiles, iMyVaadinApplication, iMyVaadinApplication));
            }

            iMyVaadinApplication.push();

        }
    }

}