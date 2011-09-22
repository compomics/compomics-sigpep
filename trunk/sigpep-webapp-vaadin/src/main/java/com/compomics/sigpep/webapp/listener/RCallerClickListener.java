package com.compomics.sigpep.webapp.listener;

import com.compomics.acromics.rcaller.RFilter;
import com.compomics.acromics.rcaller.RRunner;
import com.compomics.acromics.rcaller.RSource;
import com.compomics.sigpep.webapp.component.ComponentFactory;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.vaadin.Application;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This button will make a call to R and display an image in the end.
 */
public class RCallerClickListener implements Button.ClickListener {
    private static Logger logger = Logger.getLogger(RCallerClickListener.class);

    // The RSource that will be ran on a ClickEvent
    private RSource iRSource;

    // The RFilter variables
    private RFilter iRFilter;
    private final Pushable iPushable;
    /**
     * The parent Application in which this listener is running.
     */
    private final Application iApplication;

    /**
     * Create a ClickListener that will execute a RSource/RFilter combination on a ClickEvent.
     *
     * @param aRSource
     * @param aRFilter
     * @param aPushable
     * @param aApplication
     */
    public RCallerClickListener(RSource aRSource, RFilter aRFilter, Pushable aPushable, Application aApplication) {
        super();

        iRSource = aRSource;
        iRFilter = aRFilter;
        iPushable = aPushable;
        iApplication = aApplication;

        iRSource.filter(aRFilter);
    }


    /**
     * {@inheritDoc}
     */
    public void buttonClick(Button.ClickEvent aClickEvent) {

        // make a RRunner from this RSource.


        Runnable lRRunner = new RRunner(iRSource);
//            lRRunner.
        final Future lFuture = Executors.newSingleThreadExecutor().submit(lRRunner);

        final Window lDialog = new Window();
        lDialog.setModal(true);
        lDialog.setCaption("Processing the background/signature peptides");
        lDialog.setWidth("75%");
        lDialog.setHeight("75%");

        final ProgressIndicator lProgressIndicator = new ProgressIndicator();
        lProgressIndicator.setIndeterminate(true);
        lProgressIndicator.setPollingInterval(1000);

        lDialog.addComponent(lProgressIndicator);

        iApplication.getMainWindow().addWindow(lDialog);

        // Keep busy until the Thread has finished.
        Executors.newSingleThreadExecutor().submit(
                new Runnable() {
                    public void run() {
                        ConditionLock lConditionLock = new ConditionLock();

                        synchronized (lConditionLock) {
                            while (lFuture.isDone() != true) {
                                try {
                                    lConditionLock.wait(1000);
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage(), e);
                                }
                                System.out.println(".");
                            }
                            String lOutputFilename = iRFilter.get("file.output");

                            if (lOutputFilename != null) {

                                lDialog.setCaption("finished processing " + iRFilter.get("file.input"));
                                File lOutputFile = new File(lOutputFilename);

                                if (lOutputFile.exists()) {
                                    try {
                                        String lImageCaption = "R result";
                                        Embedded e = ComponentFactory.createImage(lOutputFile, lImageCaption, iApplication);

                                        lProgressIndicator.setVisible(false);
                                        lDialog.removeComponent(lProgressIndicator);
                                        lDialog.addComponent(e);

                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                    }
                });
    }
}