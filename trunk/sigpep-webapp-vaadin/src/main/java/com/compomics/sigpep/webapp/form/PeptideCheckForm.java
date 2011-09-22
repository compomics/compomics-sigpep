package com.compomics.sigpep.webapp.form;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideFormBean;
import com.compomics.sigpep.webapp.component.ComponentFactory;
import com.compomics.sigpep.webapp.factory.PeptideCheckFormFieldFactory;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class PeptideCheckForm extends Form {
    private static Logger logger = Logger.getLogger(PeptideCheckForm.class);

    private MyVaadinApplication iMyVaadinApplication;

    private PeptideFormBean iPeptideFormBean;

    private Vector<String> iOrder;

    private HorizontalLayout iFormButtonLayout;
    private HorizontalLayout iProgressIndicatorLayout;
    private Button iSubmitButton;
    private Button iResetButton;

    public PeptideCheckForm(String aCaption, MyVaadinApplication aMyVaadinApplication) {
        this.setCaption(aCaption);
        iMyVaadinApplication = aMyVaadinApplication;

        this.setFormFieldFactory(new PeptideCheckFormFieldFactory());

        iPeptideFormBean = new PeptideFormBean();
        BeanItem<PeptideFormBean> lBeanItem = new BeanItem<PeptideFormBean>(iPeptideFormBean);
        this.setItemDataSource(lBeanItem);

        iSubmitButton = new Button("Submit", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                try {
                    commit();
                    resetValidation();

                    PeptideCheckFormThread lPeptideCheckFormThread = new PeptideCheckFormThread();
                    lPeptideCheckFormThread.start();

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
        iOrder.add("proteaseName");
        iOrder.add("peptideSequence");
        this.setOrder();

        this.setImmediate(Boolean.TRUE);

    }

    private void resetValidation() {
        this.setComponentError(null);
        this.setValidationVisible(false);
        this.setOrder();
    }

    private void resetForm() {
        iPeptideFormBean = new PeptideFormBean();
        BeanItem<PeptideFormBean> lBeanItem = new BeanItem<PeptideFormBean>(iPeptideFormBean);
        PeptideCheckForm.this.setItemDataSource(lBeanItem);
        resetValidation();
    }

    private void setOrder() {
        this.setVisibleItemProperties(iOrder);
    }

    private class PeptideCheckFormThread extends Thread {

        public void run() {

            SigPepSession lSigPepSession = MyVaadinApplication.getSigPepSession();

            if (MyVaadinApplication.getSigPepQueryService() == null) {
                MyVaadinApplication.setSigPepQueryService(MyVaadinApplication.getSigPepSession().createSigPepQueryService());
            }

            Protease aProtease = MyVaadinApplication.getSigPepQueryService().getProteaseByShortName(iPeptideFormBean.getProteaseName());

            //create peptide generator for protease
            logger.info("creating peptide generator");
            PeptideGenerator lGenerator = lSigPepSession.createPeptideGenerator(aProtease);

            //get peptides
            logger.info("generating lBackgroundPeptides");
            boolean lIsFound = false;
            Peptide lFoundPeptide = null;
            Set<Peptide> lBackgroundPeptides = lGenerator.getPeptides();
            logger.info("looking for peptide " + iPeptideFormBean.getPeptideSequence());
            for (Peptide lPeptide : lBackgroundPeptides) {
                if (lPeptide.getSequenceString().equals(iPeptideFormBean.getPeptideSequence())) {
                    logger.info("peptide found: " + lPeptide.getSequenceString());
                    lFoundPeptide = lPeptide;
                    lIsFound = true;
                    break;
                }
            }

            if (!lIsFound) {
                iMyVaadinApplication.getMainWindow().showNotification("Peptide not found", "The peptide sequence " + iPeptideFormBean.getPeptideSequence() + "</br>was not found for organism " + iPeptideFormBean.getSpecies().getScientificName() +
                        " and protease " + aProtease.getFullName() + ".", Window.Notification.TYPE_ERROR_MESSAGE);
                resetForm();
            } else {
                iMyVaadinApplication.getFormTabSheet().proceedPeptideForm(iPeptideFormBean, lFoundPeptide);
            }

            synchronized (iMyVaadinApplication) {
                //enable form buttons after run
                iSubmitButton.setEnabled(Boolean.TRUE);
                iResetButton.setEnabled(Boolean.TRUE);

                iFormButtonLayout.removeComponent(iProgressIndicatorLayout);

            }

            iMyVaadinApplication.push();
        }
    }

}

