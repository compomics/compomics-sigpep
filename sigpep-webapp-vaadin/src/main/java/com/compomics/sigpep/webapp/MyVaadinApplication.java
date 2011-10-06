/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.compomics.sigpep.webapp;

import com.compomics.acromics.config.RCallerConfiguration;
import com.compomics.jtraml.beans.TransitionBean;
import com.compomics.sigpep.*;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.webapp.component.FormHelp;
import com.compomics.sigpep.webapp.component.FormTabSheet;
import com.compomics.sigpep.webapp.component.ResultsTable;
import com.compomics.sigpep.webapp.component.TransitionSelectionComponent;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.google.common.io.Files;
import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.notifique.Notifique;
import org.vaadin.overlay.CustomOverlay;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application implements Pushable {
    private static final Logger logger = Logger.getLogger(MyVaadinApplication.class);

    /**
     * This service limits the number of sigpep jobs that are run simultaneously.
     */
    private static ExecutorService iExecutor = Executors.newFixedThreadPool(PropertiesConfigurationHolder.getApplicationExecutorServiceThreadCount());

    /**
     * This ArrayList will hold the Transitions that are selected by the user on a per-session level.
     */
    private ArrayList<TransitionBean> iSelectedTransitionList = new ArrayList<TransitionBean>();

    /**
     * Icepush instance field for asynchronous calls
     */
    private ICEPush pusher = new ICEPush();

    /**
     * Notifique notification bar
     */
    private Notifique iNotifique;

    /**
     * Sigpep instance fields
     */
    private SigPepSessionFactory iSigPepSessionFactory;
    private SigPepSession iSigPepSession;
    private SigPepQueryService iSigPepQueryService;

    /**
     * Form help instance field
     */
    FormHelp iFormHelp;

    /**
     * Vaadin components
     */
    private Application iApplication;
    public TransitionSelectionComponent iSelectionComponent;
    private Panel iCenterLeft;
    private HorizontalLayout iHeaderLayout;
    private VerticalLayout iBottomLayoutResults;
    private Panel iCenterRight;
    private FormTabSheet iFormTabSheet;

    /**
     * Get the static instance for executing Threads in the sigpep application.
     *
     * @return
     */
    public static ExecutorService getExecutorService() {
        return iExecutor;
    }


    @Override
    public void init() {
        iApplication = this;
        iSigPepSessionFactory = ApplicationLocator.getInstance().getApplication().getSigPepSessionFactory();

        //add theme
        setTheme("sigpep");
//        setTheme("reindeer");
//        setTheme("my-sigpep-cameleon");

        //add main window
        Window lMainwindow = new Window("Sigpep Application");
        setMainWindow(lMainwindow);
        lMainwindow.addStyleName("v-app-my");

        //add notification component
        iNotifique = new Notifique(Boolean.FALSE);
        CustomOverlay lCustomOverlay = new CustomOverlay(iNotifique, getMainWindow());
        getMainWindow().addComponent(lCustomOverlay);

        //add form help
        iFormHelp = new FormHelp();
        iFormHelp.setFollowFocus(Boolean.TRUE);
        this.getMainWindow().getContent().addComponent(iFormHelp);

        //add panels
        iCenterLeft = new Panel();
        iCenterLeft.addStyleName(Reindeer.PANEL_LIGHT);
        iCenterLeft.setHeight("400px");
        iCenterLeft.setWidth("100%");

        iCenterRight = new Panel();
        iCenterRight.addStyleName(Reindeer.PANEL_LIGHT);
        iCenterRight.setHeight("400px");
        iCenterRight.setWidth("75%");

        //add form tabs
        iFormTabSheet = new FormTabSheet(this);
        iCenterLeft.addComponent(iFormTabSheet);


        iBottomLayoutResults = new VerticalLayout();

        if (PropertiesConfigurationHolder.showTestDemoFolder()) {
            Button lButton = new Button("load test data");
            lButton.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    new BackgroundThread().run();
                }
            });
            iBottomLayoutResults.addComponent(lButton);
        }

        // Add the selector component
        iSelectionComponent = new TransitionSelectionComponent(MyVaadinApplication.this);
        iCenterRight.addComponent(iSelectionComponent);


        iHeaderLayout = new HorizontalLayout();
        iHeaderLayout.setSizeFull();
        iHeaderLayout.setHeight("100px");
        iHeaderLayout.addStyleName("v-header");


        VerticalLayout lVerticalLayout = new VerticalLayout();


        GridLayout lGridLayout = new GridLayout(2, 1);
        lGridLayout.setSpacing(true);
        lGridLayout.setSizeFull();
//      lGridLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
//      lGridLayout.setWidth(500, Sizeable.UNITS_PIXELS);

        lGridLayout.addComponent(iCenterLeft, 0, 0);
        lGridLayout.setComponentAlignment(iCenterLeft, Alignment.TOP_LEFT);

        lGridLayout.addComponent(iCenterRight, 1, 0);
        lGridLayout.setComponentAlignment(iCenterRight, Alignment.TOP_CENTER);

        lVerticalLayout.addComponent(iHeaderLayout);
        lVerticalLayout.addComponent(lGridLayout);
        lVerticalLayout.addComponent(iBottomLayoutResults);

        lVerticalLayout.setComponentAlignment(iHeaderLayout, Alignment.MIDDLE_CENTER);
        lVerticalLayout.setComponentAlignment(lGridLayout, Alignment.MIDDLE_CENTER);
        lVerticalLayout.setComponentAlignment(iBottomLayoutResults, Alignment.MIDDLE_CENTER);

        lMainwindow.addComponent(lVerticalLayout);
        lMainwindow.addComponent(new Label(RCallerConfiguration.getRscriptLocation()));
        lMainwindow.addComponent(pusher);

        //add close listener to main window
        lMainwindow.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                logger.info("Closing the application");
                iExecutor.shutdown();
                getMainWindow().getApplication().close();
            }
        });

    }

    /**
     * Persist a push event.
     */
    public void push() {
        logger.debug("pushing ");
        pusher.push();
    }

    public void setResultTableComponent(ResultsTable aResultsTable) {
        clearResultTableComponent();
        iBottomLayoutResults.addComponent(aResultsTable);
    }

    public void clearResultTableComponent() {
        iBottomLayoutResults.removeAllComponents();
    }


    public class BackgroundThread extends Thread {

        @Override
        public void run() {
            // Simulate background work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // Update UI
            synchronized (MyVaadinApplication.this) {
                getMainWindow().addComponent(new Label("All done"));

                File lResultFolder = PropertiesConfigurationHolder.getTestDemoFolder();
                logger.debug("demo folder " + lResultFolder.getAbsolutePath());

                HashSet lResultFiles = new HashSet();
                Collections.addAll(lResultFiles, lResultFolder.listFiles(new FileFilter() {
                    public boolean accept(File aFile) {
                        return aFile.getName().endsWith(".tsv");
                    }
                }));
                setResultTableComponent(new ResultsTable(lResultFiles, MyVaadinApplication.this, MyVaadinApplication.this));
            }
            // Push the changes
            pusher.push();
        }

    }

    public Application getApplication() {
        return iApplication;
    }

    public SigPepSessionFactory getSigPepSessionFactory() {
        return iSigPepSessionFactory;
    }

    public SigPepSession getSigPepSession() {
        return iSigPepSession;
    }

    public void setSigPepSession(SigPepSession aSigPepSession) {
        iSigPepSession = aSigPepSession;
    }

    public SigPepQueryService getSigPepQueryService() {
        return iSigPepQueryService;
    }

    public void setSigPepQueryService(SigPepQueryService aSigPepQueryService) {
        iSigPepQueryService = aSigPepQueryService;
    }

    public void addTransitionBean(TransitionBean aTransitionBean) {
        iSelectedTransitionList.add(aTransitionBean);
        iSelectionComponent.requestRepaintAll();
    }


    public void removeTransitionBeansBySequence(String aPeptideSequence) {
        ArrayList<TransitionBean> lRemovables = new ArrayList<TransitionBean>();

        // Find beans to be removed.
        for (TransitionBean lTransitionBean : iSelectedTransitionList) {
            String lSequence = lTransitionBean.getPeptideSequence();
            if (lSequence != null && lSequence.equals(aPeptideSequence)) {
                lRemovables.add(lTransitionBean);
            }
        }
        // Remove the beans from the set.
        for (TransitionBean lTransitionBean : lRemovables) {
            iSelectedTransitionList.remove(lTransitionBean);
        }

        iSelectionComponent.requestRepaintAll();
    }

    public ArrayList<TransitionBean> getSelectedTransitionList() {
        return iSelectedTransitionList;
    }

    public FormTabSheet getFormTabSheet() {
        return iFormTabSheet;
    }

    public void setFormTabSheet(FormTabSheet aFormTabSheet) {
        iFormTabSheet = aFormTabSheet;
    }

    public Notifique getNotifique() {
        return iNotifique;
    }

    public void setNotifique(Notifique aINotifique) {
        iNotifique = aINotifique;
    }

    public FormHelp getFormHelp() {
        return iFormHelp;
    }

    public void setFormHelp(FormHelp aFormHelp) {
        iFormHelp = aFormHelp;
    }

}