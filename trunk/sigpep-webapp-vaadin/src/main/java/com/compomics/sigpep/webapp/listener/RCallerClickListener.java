package com.compomics.sigpep.webapp.listener;

import com.compomics.acromics.rcaller.RFilter;
import com.compomics.acromics.rcaller.RRunner;
import com.compomics.acromics.rcaller.RSource;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.io.Files;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This button will make a call to R and display an image in the end.
 */
public class RCallerClickListener implements Button.ClickListener {
    private static Logger logger = Logger.getLogger(RCallerClickListener.class);
    private RSource iRSource;
    private RFilter iRFilter;

    public RCallerClickListener(RSource aRSource, RFilter aRFilter) {
        super();
        iRSource = aRSource;
        iRFilter = aRFilter;

        iRSource.filter(aRFilter);
    }


    public void buttonClick(Button.ClickEvent aClickEvent) {

        // make a RRunner from this RSource.
        try {
            Runnable lRRunner = new RRunner(iRSource);
//            lRRunner.
            Future lFuture = Executors.newSingleThreadExecutor().submit(lRRunner);

            ConditionLock lConditionLock = new ConditionLock();

            // Keep busy until the Thread has finished.
            synchronized (lConditionLock) {
                while (lFuture.isDone() != true) {
                    lConditionLock.wait(1000);
                    System.out.println(".");
                }
                System.out.println("process finished!");
                String lOutputFilename = iRFilter.get("file.output");
                if (lOutputFilename != null) {
                    File lOutputFile = new File(lOutputFilename);
                    if (lOutputFile.exists()) {

                        try {
                            final InputStream is = Files.newInputStreamSupplier(lOutputFile).getInput();

                            StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
                                public InputStream getStream() {
                                    return is;
                                }
                            };
                            StreamResource lStreamResource = new StreamResource(lStreamSource, lOutputFile.getName(), MyVaadinApplication.getApplication());
                            Window lDialog = new Window();
                            lDialog.setModal(true);


                            Embedded e = new Embedded("R result", lStreamResource);
                            lDialog.addComponent(e);

                            MyVaadinApplication.getApplication().getMainWindow().addWindow(lDialog);



                        } catch (IOException e) {
                        }


                    }
                }
            }

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
