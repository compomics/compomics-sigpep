package com.compomics.sigpep.webapp.component;

import com.compomics.jtraml.beans.TransitionBean;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.jtraml.model.ThermoToTraml;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.io.Files;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
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
public class TransitionSelectionComponent extends HorizontalLayout {
    private static Logger logger = Logger.getLogger(TransitionSelectionComponent.class);

    private final MyVaadinApplication iApplication;

    Label iStatus;
    Button iCreateTraML;
    Button iPreviewSelection;
    Link iDownloadTraML;
    File iTraMLDownload;


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

        this.setSpacing(true);

        iStatus = new Label("no transitions selected yet");
        iStatus.setStyleName("v-selection-count");
        this.addComponent(iStatus, 0);

        iPreviewSelection = new Button();
        iPreviewSelection.setEnabled(false);
        iPreviewSelection.setStyleName(BaseTheme.BUTTON_LINK);
        iPreviewSelection.setIcon(new ThemeResource("preview.png"));
        iPreviewSelection.addListener(new PreviewSelectionListener());
        this.addComponent(iPreviewSelection, 1);


        iCreateTraML = new Button("");
        iCreateTraML.setEnabled(false);
        iCreateTraML.addListener(new SaveToTraMLClickListener());
        iCreateTraML.setIcon(new ThemeResource("make_traml.png"));
        iCreateTraML.setStyleName(BaseTheme.BUTTON_LINK);
        this.addComponent(iCreateTraML, 2);


        iDownloadTraML = new Link();
        iDownloadTraML.setEnabled(false);
        iDownloadTraML.setVisible(false);
        iDownloadTraML.setStyleName(Reindeer.BUTTON_LINK);
        iDownloadTraML.setIcon(new ThemeResource("download_traml.png"));
        this.addComponent(iDownloadTraML, 3);

    }

    @Override
    public void requestRepaintAll() {
        int lSize = iApplication.getSelectedTransitionList().size();
        if (lSize > 0) {
            iCreateTraML.setEnabled(true);
            iPreviewSelection.setEnabled(true);
            iStatus.setValue(lSize + " transitions selected");
        } else {
            iCreateTraML.setEnabled(false);
            iPreviewSelection.setEnabled(false);
            iDownloadTraML.setEnabled(false);
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


    private class PreviewSelectionListener implements Button.ClickListener {
        /**
         * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
         * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
         *
         * @param event An event containing information about the click.
         */
        public void buttonClick(Button.ClickEvent event) {
            final Window lDialog = new Window();
            lDialog.setModal(true);
            lDialog.setCaption("Preview transition selection");
            lDialog.setWidth("75%");
            lDialog.setHeight("75%");

            iApplication.getMainWindow().addWindow(lDialog);


            Table lTable = new Table();
            lTable.setWidth("100%");
            lTable.addContainerProperty("id", Label.class, null);
            lTable.addContainerProperty("q1", Label.class, null);
            lTable.addContainerProperty("q3", Label.class, null);

            lTable.setColumnHeader("id", "transition ID");
            lTable.setColumnHeader("q1", "Q1 m/z");
            lTable.setColumnHeader("q3", "Q3 m/z");

            for (TransitionBean item : iApplication.getSelectedTransitionList()) {
                // Add a new item to the table.
                Object id = lTable.addItem();

                lTable.getContainerProperty(id, "id").setValue(item.getID());
                lTable.getContainerProperty(id, "q1").setValue(item.getQ1Mass());
                lTable.getContainerProperty(id, "q3").setValue(item.getQ3Mass());

            }

            lDialog.addComponent(lTable);
        }
    }
}
