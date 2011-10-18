package com.compomics.sigpep.webapp.listener;

import com.compomics.pepnovo.FragmentPredictionTask;
import com.compomics.pepnovo.beans.IntensityPredictionBean;
import com.compomics.pepnovo.beans.PeptideInputBean;
import com.compomics.pepnovo.beans.PeptideOutputBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideResultMetaBean;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;
import sun.misc.ConditionLock;

import java.io.File;
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
    private final MyVaadinApplication iApplication;
    private final File iFile;
    private final PeptideResultMetaBean iPeptideResultMetaBean;
    public Notifique iNotifique;
    public CustomProgressIndicator iProgressIndicator;

    /**
     * Create a ClickListener that will execute a RSource/RFilter combination on a ClickEvent.
     *
     * @param aPushable
     * @param aApplication
     * @param aFile
     * @param aPeptideResultMetaBean
     */
    public IntensityPredictionClickListener(Set<PeptideInputBean> aInputBeans, Pushable aPushable, MyVaadinApplication aApplication, File aFile, PeptideResultMetaBean aPeptideResultMetaBean) {
        super();
        iInputBeans = aInputBeans;
        iPushable = aPushable;
        iApplication = aApplication;
        iFile = aFile;
        iPeptideResultMetaBean = aPeptideResultMetaBean;
    }


    /**
     * {@inheritDoc}
     */
    public void buttonClick(Button.ClickEvent aClickEvent) {


        iNotifique = iApplication.getNotifique();
        iProgressIndicator = new CustomProgressIndicator("r script is waiting in the processing queue ...", 1);
        iNotifique.add(null, iProgressIndicator, Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

        final Object[] lPeptideOutputBeans = new Object[1];

        Runnable lRunner = new Runnable() {
            public void run() {
                lPeptideOutputBeans[0] = FragmentPredictionTask.predictFragmentIons(iInputBeans);
            }
        };
        final Future lFuture = MyVaadinApplication.getExecutorService().submit(lRunner);

        // Keep busy until the Thread has finished.
        Executors.newSingleThreadExecutor().submit(
                new Runnable() {
                    public void run() {
                        ConditionLock lConditionLock = new ConditionLock();
                        iProgressIndicator.proceed("predicting fragmention intensities by PepNovo ...");

                        synchronized (lConditionLock) {
                            while (lFuture.isDone() != true) {
                                try {
                                    lConditionLock.wait(1000);
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage(), e);
                                }
                                System.out.println(".");
                            }

                            final Window lDialog = new Window();
                            lDialog.setCaption("fragmention intensities predicted by PepNovo");
                            lDialog.setModal(true);
                            lDialog.setWidth("75%");
                            lDialog.setHeight("75%");

                            // cast back to the collection.
                            PeptideOutputBean lOutputBean = ((Set<PeptideOutputBean>) lPeptideOutputBeans[0]).iterator().next();


                            Table lTable = new Table(lOutputBean.getPeptideSequence() + " - " + lOutputBean.getCharge());
                            lTable.addContainerProperty("add", CheckBox.class, null);
                            lTable.addContainerProperty("rank", Label.class, null);
                            lTable.addContainerProperty("score", Label.class, null);
                            lTable.addContainerProperty("iontype", Label.class, null);
                            lTable.addContainerProperty("ionnumber", Label.class, null);
                            lTable.setWidth("100%");

                            for (IntensityPredictionBean item : lOutputBean.getPredictionBeanSet()) {
                                // Add a new item to the table.
                                Object id = lTable.addItem();

                                lTable.getContainerProperty(id, "add").setValue(generateSelectButton(item));
                                lTable.getContainerProperty(id, "rank").setValue(item.getRank());
                                lTable.getContainerProperty(id, "score").setValue(item.getScore());
                                lTable.getContainerProperty(id, "iontype").setValue(item.getPeptideFragmentIon().getIonType());
                                lTable.getContainerProperty(id, "ionnumber").setValue(item.getPeptideFragmentIon().getNumber());

                            }

                            lTable.setColumnWidth("add", 40);

                            iNotifique.clear();

                            lDialog.addComponent(lTable);
                            iApplication.getMainWindow().addWindow(lDialog);
                        }
                    }
                });
    }

    private Button generateSelectButton(IntensityPredictionBean aPredictionBean) {
// Create a new button, display as a link.
        CheckBox aCheckBox = new CheckBox("");
        aCheckBox.setImmediate(true);

        SelectPepnovoTransitionListener lSelectTransitionListener = new SelectPepnovoTransitionListener(iFile, iApplication, aCheckBox, iPeptideResultMetaBean, aPredictionBean);

        aCheckBox.addListener(lSelectTransitionListener); // react to clicks
        return aCheckBox;
    }
}