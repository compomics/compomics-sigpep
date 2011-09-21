package com.compomics.sigpep.webapp.component;

import com.compomics.jtraml.beans.TransitionBean;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.jtraml.model.ThermoToTraml;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.io.Files;
import com.vaadin.ui.*;
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
public class TransitionSelectionComponent extends HorizontalLayout {
    private static Logger logger = Logger.getLogger(TransitionSelectionComponent.class);

    private final MyVaadinApplication iApplication;

    Label iStatus;
    Button iCreateTraML;
    Link iDownloadTraML;
    File iTraMLDownload;
    VerticalLayout iButtonLayout = new VerticalLayout();


    public TransitionSelectionComponent(MyVaadinApplication aApplication) {
        super();
        iApplication = aApplication;

        try {
            initComponents();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initComponents() throws IOException {
        this.setStyleName("v-selection");

        iButtonLayout.setSpacing(true);
        iButtonLayout.setMargin(true);

        setWidth("200px");

        iStatus = new Label("no transitions selected yet");
        iButtonLayout.addComponent(iStatus);

        iCreateTraML = new Button("create TraML");
        iCreateTraML.setEnabled(false);
        iCreateTraML.addListener(new SaveToTraMLClickListener());
        iButtonLayout.addComponent(iCreateTraML);

        this.addComponent(iButtonLayout);

        setDownloadLink(false);
    }

    private void setDownloadLink(boolean aVisible) throws IOException {
        Link lOldLink = iDownloadTraML;
        if (lOldLink == null) {
            iDownloadTraML = new Link();
            iDownloadTraML.setEnabled(aVisible);
            iDownloadTraML.setVisible(aVisible);

            iButtonLayout.addComponent(iDownloadTraML);
        } else {
            iDownloadTraML = ComponentFactory.createFileDownloadLink(iTraMLDownload);
            iDownloadTraML.setEnabled(aVisible);
            iDownloadTraML.setVisible(aVisible);
            iButtonLayout.removeComponent(lOldLink);
            iButtonLayout.addComponent(iDownloadTraML);
        }
    }


    @Override
    public void requestRepaintAll() {
        int lSize = iApplication.getSelectedTransitionList().size();
        if (lSize > 0) {
            iCreateTraML.setEnabled(true);
            iStatus.setValue(lSize + " transitions selected");
        } else {
            iCreateTraML.setEnabled(false);
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

                setDownloadLink(true);

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
}
