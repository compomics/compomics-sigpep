package com.compomics.sigpep.webapp.form;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.SignatureTransition;
import com.compomics.sigpep.report.SignatureTransitionMassMatrix;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideFormBean;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.component.ResultsTable;
import com.compomics.sigpep.webapp.form.factory.PeptideFormFieldFactory;
import com.google.common.io.Files;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
public class PeptideForm extends Form {
    private static Logger logger = Logger.getLogger(PeptideForm.class);

    private MyVaadinApplication iApplication;

    private PeptideFormBean iPeptideFormBean;
    private Peptide iPeptide;

    private Vector<String> iOrder;

    private HorizontalLayout iFormButtonLayout;
    private CustomProgressIndicator iCustomProgressIndicator;
    private Button iSubmitButton;
    private Button iCancelButton;

    public PeptideForm(String aCaption, PeptideFormBean aPeptideFormBean, Peptide aPeptide, MyVaadinApplication aApplication) {
        this.setCaption(aCaption);
        iApplication = aApplication;

        this.setFormFieldFactory(new PeptideFormFieldFactory(iApplication));

        iPeptideFormBean = aPeptideFormBean;
        iPeptide = aPeptide;
        BeanItem<PeptideFormBean> lBeanItem = new BeanItem<PeptideFormBean>(iPeptideFormBean);
        lBeanItem.addItemProperty("scientificName", new NestedMethodProperty(lBeanItem.getBean(), "species.scientificName"));
        this.setItemDataSource(lBeanItem);

        iSubmitButton = new Button("Submit", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                try {
                    commit();
                    resetValidation();

                    //add custom progress indicator
                    iCustomProgressIndicator = new CustomProgressIndicator("sigpep protein job is waiting in the processing queue...", 6);
                    iApplication.getNotifique().add(null, iCustomProgressIndicator, Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

                    //disable form buttons during run
                    iSubmitButton.setEnabled(Boolean.FALSE);
                    iCancelButton.setEnabled(Boolean.FALSE);

                    PeptideFormThread lSigPepFormThread = new PeptideFormThread();
                    MyVaadinApplication.getExecutorService().submit(lSigPepFormThread);


                } catch (Validator.InvalidValueException e) {
                    // Failed to commit. The validation errors are
                    // automatically shown to the user.
                }
            }
        });

        iCancelButton = new Button("Reset", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                resetForm();
            }
        });

        iFormButtonLayout = new HorizontalLayout();
        iFormButtonLayout.setSpacing(Boolean.TRUE);
        iFormButtonLayout.addComponent(iSubmitButton);
        iFormButtonLayout.addComponent(iCancelButton);
        this.getFooter().addComponent(iFormButtonLayout);

        iOrder = new Vector();
        iOrder.add("scientificName");
        iOrder.add("proteaseName");
        iOrder.add("peptideSequence");
        iOrder.add("massAccuracy");
        iOrder.add("minimumCombinationSize");
        iOrder.add("maximumCombinationSize");
        iOrder.add("signatureTransitionFinderType");
        this.setOrder();

        this.setImmediate(Boolean.TRUE);

    }

    private void resetValidation() {
        this.setComponentError(null);
        this.setValidationVisible(false);
        iApplication.clearResultTableComponent();
        this.setOrder();
    }

    private void resetForm() {
        iApplication.getFormTabSheet().cancelPeptideForm();
        iApplication.clearResultTableComponent();
    }

    private void setOrder() {
        this.setVisibleItemProperties(iOrder);
    }

    private class PeptideFormThread extends Thread {

        public void run() {

            SigPepSession lSigPepSession = iApplication.getSigPepSession();

            File outputFolder = Files.createTempDir();
            logger.info(outputFolder);

            if (iApplication.getSigPepQueryService() == null) {
                iApplication.setSigPepQueryService(iApplication.getSigPepSession().createSigPepQueryService());
            }

            Protease aProtease = iApplication.getSigPepQueryService().getProteaseByFullName(iPeptideFormBean.getProteaseName());

            //create peptide generator for protease
            iCustomProgressIndicator.proceed("creating peptide generator");
            logger.info("creating peptide generator");
            //PeptideGenerator lGenerator = lSigPepSession.createPeptideGenerator(aProtease);
            PeptideGenerator lGenerator = iPeptideFormBean.getPeptideGenerator();

            //get peptides generated by protease
            iCustomProgressIndicator.proceed("generating background peptides");
            logger.info("generating lBackgroundPeptides");
            //Set<Peptide> lBackgroundPeptides = lGenerator.getPeptides();
            Set<Peptide> lBackgroundPeptides = iPeptideFormBean.getlBackgroundPeptides();

            //add the peptide to the signature peptides set
            Set<Peptide> lSignaturepeptides = new HashSet<Peptide>();
            lSignaturepeptides.add(iPeptide);

            //create signature transition finder
            iCustomProgressIndicator.proceed("creating signature transition finder");
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
                    iPeptideFormBean.getMassAccuracy(),
                    iPeptideFormBean.getMinimumCombinationSize(),
                    iPeptideFormBean.getMaximumCombinationSize(),
                    iPeptideFormBean.getSignatureTransitionFinderType());

            iCustomProgressIndicator.proceed("finding signature transitions");
            logger.info("finding signature transitions");
            List<SignatureTransition> st = finder.findSignatureTransitions(lSignaturepeptides);

            for (SignatureTransition t : st) {
                logger.info("printing peptide " + t.getPeptide().getSequenceString());
                try {
                    SignatureTransitionMassMatrix m = new SignatureTransitionMassMatrix(t);

                    OutputStream os1 = new FileOutputStream(outputFolder.getAbsolutePath() + File.separator + t.getPeptide().getSequenceString() + ".tsv");
                    m.write(os1);
                    os1.close();

                    OutputStream os2 = new FileOutputStream(outputFolder.getAbsolutePath() + File.separator + t.getPeptide().getSequenceString() + ".meta.properties");
                    m.writeMetaData(os2);
                    os2.close();

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

            synchronized (iApplication) {
                //enable form buttons after run
                iSubmitButton.setEnabled(Boolean.TRUE);
                iCancelButton.setEnabled(Boolean.TRUE);

                iApplication.getNotifique().clear();
                iApplication.setResultTableComponent(new ResultsTable(lResultFiles, iApplication, iApplication));
            }

            iApplication.push();
        }
    }

}
