package com.compomics.sigpep.webapp.listener;

import com.compomics.pepnovo.FragmentPredictionTask;
import com.compomics.pepnovo.beans.IntensityPredictionBean;
import com.compomics.pepnovo.beans.PeptideInputBean;
import com.compomics.pepnovo.beans.PeptideOutputBean;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.vaadin.Application;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This button will make a call to R and display an image in the end.
 */
public class IntensityPredictionClickListener implements Button.ClickListener {
    private static Logger logger = Logger.getLogger(IntensityPredictionClickListener.class);

    private final Set<PeptideInputBean> iInputBeans;
    private final Pushable iPushable;
    /**
     * The parent Application in which this listener is running.
     */
    private final Application iApplication;

    /**
     * Create a ClickListener that will execute a RSource/RFilter combination on a ClickEvent.
     *
     * @param aPushable
     * @param aApplication
     */
    public IntensityPredictionClickListener(Set<PeptideInputBean> aInputBeans, Pushable aPushable, Application aApplication) {
        super();
        iInputBeans = aInputBeans;

        iPushable = aPushable;
        iApplication = aApplication;

    }


    /**
     * {@inheritDoc}
     */
    public void buttonClick(Button.ClickEvent aClickEvent) {

        // make a RRunner from this RSource.

        final Object[] lPeptideOutputBeans = new Object[1];

//            lRRunner.
        Runnable lRunner = new Runnable() {
            public void run() {
                lPeptideOutputBeans[0] = FragmentPredictionTask.predictFragmentIons(iInputBeans);
            }
        };
        final Future lFuture = Executors.newSingleThreadExecutor().submit(lRunner);

        final Window lDialog = new Window();
        lDialog.setModal(true);
        lDialog.setCaption("Running pepnovo to predict peptide fragmentation");
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
                            // cast back to the collection.
                            PeptideOutputBean lOutputBean = ((Set<PeptideOutputBean>) lPeptideOutputBeans[0]).iterator().next();

                            lProgressIndicator.setVisible(false);
                            lDialog.removeComponent(lProgressIndicator);

                            Table lTable = new Table(lOutputBean.getPeptideSequence() + " - " + lOutputBean.getCharge());
                            lTable.addContainerProperty("rank", Label.class, null);
                            lTable.addContainerProperty("score", Label.class, null);
                            lTable.addContainerProperty("iontype", Label.class, null);
                            lTable.addContainerProperty("ionnumber", Label.class, null);

                            for (IntensityPredictionBean item: lOutputBean.getPredictionBeanSet()) {
                                // Add a new item to the table.
                                Object id = lTable.addItem();

                                lTable.getContainerProperty(id, "rank").setValue(item.getRank());
                                lTable.getContainerProperty(id, "score").setValue(item.getScore());
                                lTable.getContainerProperty(id, "iontype").setValue(item.getPeptideFragmentIon().getIonType());
                                lTable.getContainerProperty(id, "ionnumber").setValue(item.getPeptideFragmentIon().getNumber());

                            }

                            lDialog.addComponent(lTable);

                        }
                    }
                });
    }
}