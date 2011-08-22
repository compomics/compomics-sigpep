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
import com.compomics.sigpep.webapp.factory.SigPepFormFieldFactory;
import com.compomics.sigpep.webapp.runner.SigPepRunner;
import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
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
    private Vector<String> iOrder;

    public SigPepForm(String aCaption, MyVaadinApplication aMyVaadinApplication) {
        this.setCaption(aCaption);
        iMyVaadinApplication = aMyVaadinApplication;

        iSigPepFormFieldFactory = new SigPepFormFieldFactory(iMyVaadinApplication);
        this.setFormFieldFactory(iSigPepFormFieldFactory);

        BeanItem<SigPepFormBean> lBeanItem = new BeanItem<SigPepFormBean>(iMyVaadinApplication.getSigPepFormBean());
        this.setItemDataSource(lBeanItem);

        Button iSubmitButton = new Button("Submit", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                try {
                    commit();
                    resetValidation();

                    ExecutorService lExecutorService = Executors.newSingleThreadExecutor();
                    Runnable lSigPepRunner = new SigPepRunner(iMyVaadinApplication);
                    lExecutorService.execute(lSigPepRunner);
                    lExecutorService.shutdown();

                } catch (Validator.InvalidValueException e) {
                    // Failed to commit. The validation errors are
                    // automatically shown to the user.
                }
            }
        });
        this.getFooter().addComponent(iSubmitButton);
        Button iDiscardButton = new Button("Reset", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                iMyVaadinApplication.setSigPepFormBean(new SigPepFormBean());
                BeanItem<SigPepFormBean> lBeanItem = new BeanItem<SigPepFormBean>(iMyVaadinApplication.getSigPepFormBean());
                SigPepForm.this.setItemDataSource(lBeanItem);
                resetValidation();
            }
        });
        this.getFooter().addComponent(iDiscardButton);

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

    private void setOrder(){
        this.setVisibleItemProperties(iOrder);
    }

}