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

import com.compomics.jtraml.beans.TransitionBean;
import com.compomics.sigpep.ApplicationLocator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.webapp.component.*;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import org.aspectj.apache.bcel.generic.NEW;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.notifique.Notifique;
import org.vaadin.overlay.CustomOverlay;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application implements Pushable {
    private static Logger logger = Logger.getLogger(MyVaadinApplication.class);

    /**
     * This ArrayList will hold the Transitions that are selected by the user on a per-session level.
     */
    private ArrayList<TransitionBean> iSelectedTransitionList = new ArrayList<TransitionBean>();

    private ICEPush pusher = new ICEPush();
    private Application iApplication;
    private Notifique iNotifique;
    private SigPepSessionFactory iSigPepSessionFactory;
    private SigPepSession iSigPepSession;
    private SigPepQueryService iSigPepQueryService;
    public TransitionSelectionComponent iSelectionComponent;

    private Panel iCenterLayout;
    private VerticalLayout iCenterLayoutResults;

    private Panel iBottomLayout;
    private FormTabSheet iFormTabSheet;


    @Override
    public void init() {
        iApplication = this;
        iSigPepSessionFactory = ApplicationLocator.getInstance().getApplication().getSigPepSessionFactory();

        setTheme("sigpep");

        Window mainWindow = new Window("Sigpep Application");
        setMainWindow(mainWindow);

        //add notification component
        iNotifique = new Notifique(Boolean.FALSE);
//        iNotifique.set
        CustomOverlay lCustomOverlay = new CustomOverlay(iNotifique, getMainWindow());
        getMainWindow().addComponent(lCustomOverlay);
        /*iNotifique.add(
                null,
                "Welcome! This is a demo application for the <a href=\"http://vaadin.com/addon/notifique\">Notifique</a> add-on for Vaadin.",
                true, Notifique.Styles.BROWSER_FF3, true);*/
        //iNotifique.add(null, new CustomProgressIndicator("test"), Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

        iCenterLayout = new Panel();
        iCenterLayout.setSizeFull();

        iBottomLayout = new Panel();
        iBottomLayout.setSizeFull();

        iFormTabSheet = new FormTabSheet(this);
        iCenterLayout.addComponent(iFormTabSheet);

        iCenterLayoutResults = new VerticalLayout();
        iCenterLayout.addComponent(iCenterLayoutResults);

        Button lButton = new Button("load test data");
        lButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                new BackgroundThread().run();
            }
        });
        iCenterLayout.addComponent(lButton);

        // Add the selector component
        iSelectionComponent = new TransitionSelectionComponent(MyVaadinApplication.this);
        iBottomLayout.addComponent(iSelectionComponent);


        VerticalSplitPanel vsplit = new VerticalSplitPanel();
//        vsplit.setSplitPosition(80, Sizeable.);
        vsplit.setSplitPosition(500, Sizeable.UNITS_PIXELS);
        vsplit.setLocked(false);
//        vsplit.setHeight("100%");
        vsplit.setHeight("600px");
//        vsplit.setWidth("100%");
//        vsplit.addStyleName(Reindeer.SPLITPANEL_SMALL);

        mainWindow.addComponent(vsplit);


        vsplit.addComponent(iCenterLayout);
        vsplit.addComponent(iBottomLayout);


        mainWindow.addComponent(pusher);


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
        iCenterLayoutResults.addComponent(aResultsTable);
    }

    public void clearResultTableComponent() {
        iCenterLayoutResults.removeAllComponents();
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

                File lResultFolder = new File("/Users/kennyhelsens/tmp/sigpep/");
                ArrayList lResultFiles = new ArrayList();
                Collections.addAll(lResultFiles, lResultFolder.listFiles(new FileFilter() {
                    public boolean accept(File aFile) {
                        return aFile.getName().endsWith(".tsv");
                    }
                }));

                iCenterLayout.addComponent(new ResultsTable(lResultFiles, MyVaadinApplication.this, MyVaadinApplication.this));

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

}