package com.compomics.sigpep.webapp.component;

import com.compomics.pepnovo.beans.PeptideInputBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideResultMetaBean;
import com.compomics.sigpep.webapp.interfaces.Pushable;
import com.compomics.sigpep.webapp.listener.IntensityPredictionClickListener;
import com.compomics.sigpep.webapp.listener.RCallerClickListener;
import com.compomics.sigpep.webapp.listener.SelectTransitionListener;
import com.compomics.sigpep.webapp.rcaller.RFilter;
import com.compomics.sigpep.webapp.rcaller.RSource;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a series of .tsv transition files as a Table.
 */
public class ResultsTable extends VerticalLayout {

    private static Logger logger = Logger.getLogger(ResultsTable.class);

    /**
     * The table component that shows of the files passed by the constructor.
     */
    Table iTable = new Table();

    HashMap<String, Object> iSequenceToIDs = new HashMap<String, Object>();

    public static final String COLUMN_LABEL_ADD = "add";

    public static final String COLUMN_LABEL_PEPTIDE = "peptide";
    public static final String COLUMN_LABEL_RETENTION = "retention (min)";
    public static final String COLUMN_LABEL_PROTEIN = "protein";
    public static final String COLUMN_LABEL_TRANSITION_COUNT = "transitions";

    public static final String COLUMN_LABEL_GRAPH_1 = "graph-1";
    public static final String COLUMN_LABEL_GRAPH_2 = "graph-2";
    public static final String COLUMN_LABEL_DOWNLOAD = "download";
    public static final String COLUMN_LABEL_PREDICT = "pepnovo";

    /**
     * Temporary folder to generate the images.
     */
    File iTempFolder = Files.createTempDir();

    /**
     * Object to push async events.
     */
    private final Pushable iPushable;
    /**
     * The application in which the Table is running.
     */
    private final MyVaadinApplication iApplication;


    /**
     * Create a ResultsTable from a set of files.
     *
     * @param aFiles
     */
    public ResultsTable(HashSet<File> aFiles, Pushable aPushable, MyVaadinApplication aApplication) {
        super();
        String lSuffix = (aFiles.size() == 1) ? "" : "s";
        iTable.setCaption(aFiles.size() + " signature peptide" + lSuffix);
        iTable.addStyleName("v-formresults");


        iPushable = aPushable;
        iApplication = aApplication;

        // Link to wiki
        InfoLink info = new InfoLink(iApplication, InfoLink.InfoPages.GRAPH);
        this.addComponent(info);

        try {
            // Create a tmp folder for this Table.
            iTempFolder = Files.createTempDir();

            // Initiate the table.
            createTableColumns();
            doFormatting();

            // Fill the table.
            populateTable(aFiles);
            this.addComponent(iTable);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * Wrapper method which populates the table with an ArrayList of files.
     *
     * @param aFiles
     */

    private void populateTable(Set<File> aFiles) throws IOException {
        // Iterate all given files.
        for (File lFile : aFiles) {
            String lPeptideSequence = extractPeptideFromFilename(lFile);

            // Add a new item to the table.
            Object id = iTable.addItem();

            // Save the peptide to tableid mappings
            iSequenceToIDs.put(lPeptideSequence, id);

            // 1 - Filename.
            iTable.getContainerProperty(id, COLUMN_LABEL_PEPTIDE).setValue(lPeptideSequence);

            // Attempt to locate the meta information.
            String lMetaFileName = lFile.getName().substring(0, lFile.getName().indexOf(".tsv")) + ".meta.properties";
            File lMetaFile = new File(lFile.getParentFile(), lMetaFileName);
            PeptideResultMetaBean lPeptideResultMetaBean = null;

            if (lMetaFile.exists()) {
                lPeptideResultMetaBean = new PeptideResultMetaBean(lMetaFile);

                // Add proteins
                String lParentProteins = Joiner.on(",").join(lPeptideResultMetaBean.getProteins());
                iTable.getContainerProperty(id, COLUMN_LABEL_PROTEIN).setValue(lParentProteins);

                // Add number of transitions
                iTable.getContainerProperty(id, COLUMN_LABEL_TRANSITION_COUNT).setValue(lPeptideResultMetaBean.getBarcodeCount());

                // Add retention time
                double lRetentionTime = lPeptideResultMetaBean.getRetentionTime();
                logger.debug("rt " + lRetentionTime);
                BigDecimal lBigDecimal = new BigDecimal(lRetentionTime).setScale(1, RoundingMode.UP);
                logger.debug("bd rt " + lBigDecimal.doubleValue());
                iTable.getContainerProperty(id, COLUMN_LABEL_RETENTION).setValue(lBigDecimal.doubleValue());
            }

            // 2 - Download link to tsv file.
            Link l = ComponentFactory.createFileDownloadLink(lFile, iApplication);
            iTable.getContainerProperty(id, COLUMN_LABEL_DOWNLOAD).setValue(l);

            // 3 - Make an R button
            Button lRScatterButton = generateBackgroundSignatureButton(lFile);
            iTable.getContainerProperty(id, COLUMN_LABEL_GRAPH_1).setValue(lRScatterButton);

            // 3 - Make an R button
            Button lRBarplotButton = generateBackgroundBarchartButton(lFile);
            iTable.getContainerProperty(id, COLUMN_LABEL_GRAPH_2).setValue(lRBarplotButton);

            // 4 - Make a prediction button
            Button lPredictionButton = generatePredictionButton(lPeptideSequence, lFile, lPeptideResultMetaBean);
            iTable.getContainerProperty(id, COLUMN_LABEL_PREDICT).setValue(lPredictionButton);

            // 5 - Make the select peptide button
            Button lSelectTransitionButton = generateSelectButton(lFile, lPeptideResultMetaBean);
            iTable.getContainerProperty(id, COLUMN_LABEL_ADD).setValue(lSelectTransitionButton);
        }
    }



    private Button generateSelectButton(File aFile, PeptideResultMetaBean aPeptideResultMetaBean) throws IOException {
        // Create a new button, display as a link.
        CheckBox aCheckBox = new CheckBox("");
        aCheckBox.setImmediate(true);

        SelectTransitionListener lSelectTransitionListener = new SelectTransitionListener(aFile, iApplication, aCheckBox, aPeptideResultMetaBean);

        aCheckBox.addListener(lSelectTransitionListener); // react to clicks
        return aCheckBox;
    }

    private Button generatePredictionButton(String aPeptideSequence, File aFile, PeptideResultMetaBean aPeptideResultMetaBean) {

        // Create a new button, display as a link.
        Button lButton = new Button();
        lButton.addStyleName(BaseTheme.BUTTON_LINK);
        lButton.setIcon(new ClassResource("/images/graph_int_pred.png", iApplication));

        //
        PeptideInputBean lPeptideInputBean = new PeptideInputBean();
        lPeptideInputBean.setCharge(2);
        lPeptideInputBean.setPeptideSequence(aPeptideSequence);


        HashSet<PeptideInputBean> lPeptideInputBeans = new HashSet<PeptideInputBean>();
        lPeptideInputBeans.add(lPeptideInputBean);

        IntensityPredictionClickListener lIntensityPredictionClickListener = new IntensityPredictionClickListener(lPeptideInputBeans, iPushable, iApplication, aFile, aPeptideResultMetaBean);
        lButton.addListener(lIntensityPredictionClickListener);
        return lButton;


    }

    /**
     * This method creates a Button that will launch the barplot.rj sript
     *
     * @param aFile
     * @return
     */
    private Button generateBackgroundSignatureButton(File aFile) {
        // Create a new button, display as a link.
        Button lButton = new Button();
        lButton.addStyleName(BaseTheme.BUTTON_LINK);

        // Set the image icon from the classpath.
        lButton.setIcon(new ClassResource("/images/graph_sig_bg.png", iApplication));

        // Load the R-script.
        URL aResource = Resources.getResource("r/barplot.rj");
        File lRFile = new File(aResource.getPath());
        RSource lRSource = new RSource(lRFile);
        RFilter lRFilter = new RFilter();
        logger.debug(aFile.getAbsolutePath());
        lRFilter.add("file.input", aFile.getPath());
        File lTempFile = new File(iTempFolder, System.currentTimeMillis() + ".png");
        lRFilter.add("file.output", lTempFile.getPath());

        // Add a listener.
        RCallerClickListener lRCallerClickListener = new RCallerClickListener(lRSource, lRFilter, iPushable, iApplication);
        lButton.addListener(lRCallerClickListener);
        return lButton;
    }

      /**
     * This method creates a Button that will launch the barplot2.rj sript
     *
     * @return
     */
      private Button generateBackgroundBarchartButton(File aFile) {
        // Create a new button, display as a link.
        Button lButton = new Button();
        lButton.addStyleName(BaseTheme.BUTTON_LINK);

        // Set the image icon from the classpath.
        lButton.setIcon(new ClassResource("/images/graph_sig_tg.png", iApplication));

        // Load the R-script.
        URL aResource = Resources.getResource("r/barplot2.rj");
        File lRFile = new File(aResource.getPath());
        RSource lRSource = new RSource(lRFile);
        RFilter lRFilter = new RFilter();
        logger.debug(aFile.getAbsolutePath());
        lRFilter.add("file.input", aFile.getPath());
        File lTempFile = new File(iTempFolder, System.currentTimeMillis() + ".png");
        lRFilter.add("file.output", lTempFile.getPath());

        // Add a listener.
        RCallerClickListener lRCallerClickListener = new RCallerClickListener(lRSource, lRFilter, iPushable, iApplication);
        lButton.addListener(lRCallerClickListener);
        return lButton;

    }


    /**
     * This method creates an apropriate filename to display in the table.
     *
     * @param lFile
     * @return
     */
    private String extractPeptideFromFilename(File lFile) {
        // Remove the extension of the filename
        return lFile.getName().substring(0, lFile.getName().indexOf("."));
    }

    /**
     * Create the
     */
    private void createTableColumns() {
        // Define the Table
        iTable.addContainerProperty(COLUMN_LABEL_ADD, CheckBox.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_TRANSITION_COUNT, Label.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_PEPTIDE, Label.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_PROTEIN, Label.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_RETENTION, Label.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_DOWNLOAD, Link.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_GRAPH_1, Button.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_GRAPH_2, Button.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_PREDICT, Button.class, null);



    }

    /**
     * Table formatting
     */
    private void doFormatting() {
        iTable.setWidth("100%");
        iTable.setPageLength(4);
    }
}
