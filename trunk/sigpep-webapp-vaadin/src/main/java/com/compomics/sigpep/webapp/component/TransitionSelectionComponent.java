package com.compomics.sigpep.webapp.component;

import com.compomics.jtraml.beans.TransitionBean;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.jtraml.model.ThermoToTraml;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.io.Files;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * This class is a
 */
public class TransitionSelectionComponent extends VerticalLayout {
    private static Logger logger = Logger.getLogger(TransitionSelectionComponent.class);

    private final MyVaadinApplication iApplication;

    private Label iStatus;

    private File iTraMLDownload = null;
    private VerticalLayout iTreeLayout = new VerticalLayout();
    private Link iDownloadTraML;


    public TransitionSelectionComponent(MyVaadinApplication aApplication) {
        super();
        this.setCaption("Transitions Overview");
        iApplication = aApplication;

        try {
            initComponents();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initComponents() throws IOException {
        this.setStyleName("v-transition-selection");
        this.setSpacing(true);

        iStatus = new Label("");
        this.addComponent(iStatus, 0);



        iDownloadTraML = new Link();
        iDownloadTraML.setEnabled(true);
        iDownloadTraML.setVisible(false);
        iDownloadTraML.setStyleName(Reindeer.BUTTON_LINK);
        iDownloadTraML.setIcon(new ThemeResource("download_traml.png"));
        this.addComponent(iDownloadTraML, 1);

        this.addComponent(iTreeLayout);

        this.setComponentAlignment(iStatus, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(iTreeLayout, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(iDownloadTraML, Alignment.MIDDLE_LEFT);

    }

    @Override
    public void requestRepaintAll() {
        int lSize = iApplication.getSelectedTransitionList().size();
        if (lSize > 0) {
            iStatus.setValue(lSize + " transitions selected");
            iTreeLayout.removeAllComponents();
            iTreeLayout.addComponent(new TransitionSetTree(iApplication.getSelectedTransitionList()));
            iDownloadTraML.setVisible(true);
        } else {
            iTreeLayout.removeAllComponents();
            iDownloadTraML.setVisible(false);
            iStatus.setValue("no transitions selected");
        }
        super.requestRepaintAll();
    }

    private class SaveToTraMLClickListener implements Button.ClickListener {


        /**
         * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
         * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
         *
         * @param event An event containing information about the click.
         */
        public void buttonClick(Button.ClickEvent event) {
            ArrayList<TransitionBean> lSelectedTransitionList = iApplication.getSelectedTransitionList();

            BufferedWriter br = null;
            File lTempDir = Files.createTempDir();
            File lTargetFile = new File(lTempDir, System.currentTimeMillis() + ".traml");
            try {
                lTargetFile.createNewFile();
                br = Files.newWriter(lTargetFile, Charset.defaultCharset());

                ObjectFactory lObjectFactory = new ObjectFactory();
                TraMLType lTraMLType = lObjectFactory.createTraMLType();
                lTraMLType.setCvList(CVFactory.getCvListType());


                TSVFileImportModel lTSVFileImportModel = new ThermoToTraml();

                for (TransitionBean lTransitionBean : lSelectedTransitionList) {
                    lTSVFileImportModel.addRowToTraml(lTraMLType, lTransitionBean.getSeparatedOrder());
                }

                // Ok, all rows have been added.
                TraMLCreator lTraMLCreator = new TraMLCreator();
                lTraMLCreator.setTraML(lTraMLType);


                br.write(lTraMLCreator.asString());
                // Ok. The File should have been written!
                br.flush();
                br.close();

                iTraMLDownload = lTargetFile;
                updateDownloadTramlLink();

                requestRepaintAll();

            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } catch (JAXBException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void updateDownloadTramlLink() throws IOException {
        this.removeComponent(iDownloadTraML);

        iDownloadTraML = ComponentFactory.createFileDownloadLink(iTraMLDownload, iApplication);
        iDownloadTraML.setIcon(new ThemeResource("download_traml.png"));
        iDownloadTraML.setStyleName(Reindeer.BUTTON_LINK);

        this.addComponent(iDownloadTraML, 3);
    }

}
