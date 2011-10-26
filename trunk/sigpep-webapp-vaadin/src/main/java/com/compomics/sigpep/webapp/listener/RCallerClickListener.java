package com.compomics.sigpep.webapp.listener;

import com.compomics.acromics.rcaller.RFilter;
import com.compomics.acromics.rcaller.RRunner;
import com.compomics.acromics.rcaller.RSource;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.analytics.AnalyticsLogger;
import com.compomics.sigpep.webapp.component.ComponentFactory;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;
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
    private final MyVaadinApplication iApplication;
    public Notifique iNotifique;
    public CustomProgressIndicator iProgressIndicator;

    /**
     * Create a ClickListener that will execute a RSource/RFilter combination on a ClickEvent.
     *
     * @param aRSource
     * @param aRFilter
     * @param aPushable
     * @param aApplication
     */
    public RCallerClickListener(RSource aRSource, RFilter aRFilter, Pushable aPushable, MyVaadinApplication aApplication) {
        super();

        iRSource = aRSource;
        iRFilter = aRFilter;
        iPushable = aPushable;
        iApplication = aApplication;
        logger.debug("RFilter out \t" + aRFilter.get("file.output"));
        logger.debug("RFilter in \t" + aRFilter.get("file.input"));

        iRSource.filter(aRFilter);
    }


    /**
     * {@inheritDoc}
     */
    public void buttonClick(Button.ClickEvent aClickEvent) {

        // make a RRunner from this RSource.
        Runnable lRRunner = new RRunner(iRSource);

        iNotifique = iApplication.getNotifique();
        iProgressIndicator = new CustomProgressIndicator("r script is waiting in the processing queue ...", 1);
        iNotifique.add(null, iProgressIndicator, Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

        final Future lFuture = MyVaadinApplication.getExecutorService().submit(lRRunner);

        // Keep busy until the Thread has finished.
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            public void run() {
                ConditionLock lConditionLock = new ConditionLock();
                iProgressIndicator.proceed("visualising signature peptide background ...");

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

                        final Window lDialog = new Window();
                        lDialog.setCaption("signature peptide background");
                        lDialog.setModal(true);
                        lDialog.setWidth("75%");
                        lDialog.setHeight("75%");

                        File lOutputFile = new File(lOutputFilename);

                        if (lOutputFile.exists()) {
                            try {
//                                        String lImageCaption = "signature peptide background";
                                Embedded e = ComponentFactory.createImage(lOutputFile, "", iApplication);

                                iNotifique.clear();

                                lDialog.addComponent(e);
                                iApplication.getMainWindow().addWindow(lDialog);

                                AnalyticsLogger.runRVisualization(iApplication.getHttpSessionID());

                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        });
    }
}