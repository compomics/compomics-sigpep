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
import com.compomics.sigpep.webapp.analytics.AnalyticsLogger;
import com.compomics.sigpep.webapp.bean.ProteinFormBean;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.component.InfoLink;
import com.compomics.sigpep.webapp.component.ResultsTable;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.compomics.sigpep.webapp.form.factory.ProteinFormFieldFactory;
import com.google.common.io.Files;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class ProteinForm extends Form {
    private static final Logger logger = Logger.getLogger(ProteinForm.class);

    private MyVaadinApplication iApplication;

    private ProteinFormBean iProteinFormBean;

    private Vector<String> iOrder;

    private HorizontalLayout iFormButtonLayout;
    private CustomProgressIndicator iCustomProgressIndicator;
    private Button iSubmitButton;
    private Button iResetButton;

    public ProteinForm(String aCaption, MyVaadinApplication aApplication) {
        this.setCaption(aCaption);
        iApplication = aApplication;

        this.setFormFieldFactory(new ProteinFormFieldFactory(iApplication));

        iProteinFormBean = new ProteinFormBean();
        BeanItem<ProteinFormBean> lBeanItem = new BeanItem<ProteinFormBean>(iProteinFormBean);
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
                    iResetButton.setEnabled(Boolean.FALSE);

                    ProteinFormThread lProteinFormThread = new ProteinFormThread();
                    MyVaadinApplication.getExecutorService().execute(lProteinFormThread);

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

        // Link to wiki
        InfoLink info = new InfoLink(iApplication, InfoLink.InfoPages.FORM_OPTIONS);
        iFormButtonLayout.addComponent(info);

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
        resetValidation();
        iSubmitButton.setEnabled(Boolean.TRUE);
        iResetButton.setEnabled(Boolean.TRUE);
        iApplication.clearResultTableComponent();
        iApplication.getNotifique().clear();
    }


    private void setOrder() {
        this.setVisibleItemProperties(iOrder);
    }

    private class ProteinFormThread extends Thread {

        public void run() {

            // Log the activity to the analytics logger
            AnalyticsLogger.startSigpepJob(iApplication.getHttpSessionID(), AnalyticsLogger.JobType.PROTEINFORM);


            SigPepSession lSigPepSession = iApplication.getSigPepSession();

            File outputFolder = Files.createTempDir();

            Protease aProtease = iApplication.getSigPepQueryService().getProteaseByFullName(iProteinFormBean.getProteaseName());

            //create peptide generator for protease
            iCustomProgressIndicator.proceed(MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_progress.peptide_generator"), aProtease.getFullName()));
            logger.info("creating peptide generator for protease " + aProtease.getFullName());
            PeptideGenerator lGenerator = lSigPepSession.createPeptideGenerator(aProtease);

            //create background peptides
            iCustomProgressIndicator.proceed(MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_progress.peptide_background"), aProtease.getFullName()));
            logger.info("creating background peptides for protease " + aProtease.getFullName());
            Set<Peptide> lBackgroundPeptides = lGenerator.getPeptides();

            logger.info("generating signature peptides");
            iCustomProgressIndicator.proceed(PropertiesConfigurationHolder.getInstance().getString("form_progress.signature_peptides"));
            Set<Peptide> lSignaturepeptides = lGenerator.getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(iProteinFormBean.getProteinAccession(), 1);
            for (Peptide peptide : lSignaturepeptides) {
                logger.info(peptide.getSequenceString());
            }

            //create signature transition finder
            iCustomProgressIndicator.proceed(PropertiesConfigurationHolder.getInstance().getString("form_progress.signature_transition_finder_1"));
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
            iCustomProgressIndicator.proceed(PropertiesConfigurationHolder.getInstance().getString("form_progress.signature_transition_finder_2"));
            List<SignatureTransition> st = finder.findSignatureTransitions(lSignaturepeptides);
            HashSet lResultFiles = new HashSet();

            for (SignatureTransition t : st) {
                logger.info("printing peptide " + t.getPeptide().getSequenceString());
                try {
                    SignatureTransitionMassMatrix m = new SignatureTransitionMassMatrix(t);

                    String lFileName = outputFolder.getAbsolutePath() + File.separator + t.getPeptide().getSequenceString() + ".tsv";
                    File lResultFile = new File(lFileName);
                    OutputStream os1 = new FileOutputStream(lResultFile);
                    lResultFiles.add(lResultFile);
                    m.write(os1);
                    os1.close();

                    OutputStream os2 = new FileOutputStream(outputFolder.getAbsolutePath() + File.separator + t.getPeptide().getSequenceString() + ".meta.properties");
                    m.writeMetaData(os2, iProteinFormBean.getProteinAccession());
                    os2.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            logger.info("generated " + lResultFiles.size() + " peptide result files");
            iCustomProgressIndicator.proceed(MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_progress.peptide_result_files"), lResultFiles.size()));
            Collections.addAll(lResultFiles, outputFolder.listFiles(new FileFilter() {
                public boolean accept(File aFile) {
                    return aFile.getName().endsWith(".tsv");
                }
            }));

            synchronized (iApplication) {
                //enable form buttons after run
                iSubmitButton.setEnabled(Boolean.TRUE);
                iResetButton.setEnabled(Boolean.TRUE);

                iApplication.getNotifique().clear();
                iApplication.setResultTableComponent(new ResultsTable(lResultFiles, iApplication, iApplication));
            }

            iApplication.push();
        }
    }

}