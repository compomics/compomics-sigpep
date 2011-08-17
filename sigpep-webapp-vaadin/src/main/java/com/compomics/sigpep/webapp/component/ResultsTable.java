package com.compomics.sigpep.webapp.component;

import com.compomics.acromics.rcaller.RFilter;
import com.compomics.acromics.rcaller.RSource;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.listener.RCallerClickListener;
import com.google.common.io.Files;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class represents a series of .tsv transition files as a Table.
 */
public class ResultsTable extends VerticalLayout {

    /**
     * The table component that shows of the files passed by the constructor.
     */
    Table iTable = new Table();

    public String COLUMN_LABEL_FILENAME = "filename";
    public String COLUMN_LABEL_GRAPH = "graph";
    public String COLUMN_LABEL_FILE = "download";

    /**
     * Temporary folder to generate the images.
     */
    File iTempFolder = Files.createTempDir();

    /**
     * Create a ResultsTable from a set of files.
     *
     * @param aFiles
     */
    public ResultsTable(ArrayList<File> aFiles) {
        super();

        iTempFolder = Files.createTempDir();

        doFormatting();
        createColumns();
        setListeners();

        populate(aFiles);


        this.addComponent(iTable);
    }

    private void setListeners() {

    }

    /**
     * Wrapper method which populates the table with an ArrayList of files.
     *
     * @param aFiles
     */
    private void populate(ArrayList<File> aFiles) {
        int lCounter = 0;
        for (File lFile : aFiles) {
            lCounter++;

            Object id = iTable.addItem();

            // Filename.
            iTable.getContainerProperty(id, COLUMN_LABEL_FILENAME).setValue(lFile.getName());

            // Download tsv file.
            try {
                final InputStream is = Files.newInputStreamSupplier(lFile).getInput();

                StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
                    public InputStream getStream() {
                        return is;
                    }
                };
                StreamResource lStreamResource = new StreamResource(lStreamSource, lFile.getName(), MyVaadinApplication.getApplication());

                Link l = new Link("Download", lStreamResource);
                iTable.getContainerProperty(id, COLUMN_LABEL_FILE).setValue(l);

            } catch (IOException e) {
            }

            // Make an R button
            Button lButton = new Button();
            lButton.setStyleName(BaseTheme.BUTTON_LINK);
            lButton.setIcon(new ClassResource("/images/graph.png", MyVaadinApplication.getApplication()));

            URL lResource = com.google.common.io.Resources.getResource("r/barplot.rj");
            File lRFile = new File(lResource.getPath());
            RSource lRSource = new RSource(lRFile);
            RFilter lRFilter = new RFilter();
            lRFilter.add("file.input", lFile.getPath());
            File lTempFile = new File(iTempFolder, System.currentTimeMillis() + ".png");
            lRFilter.add("file.output", lTempFile.getPath());

            lButton.addListener(new RCallerClickListener(lRSource, lRFilter));

            iTable.getContainerProperty(id, COLUMN_LABEL_GRAPH).setValue(lButton);


        }
    }

    /**
     * Create the
     */
    private void createColumns() {
        // Define the Table
        iTable.addContainerProperty(COLUMN_LABEL_FILENAME, Label.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_FILE, Link.class, null);
        iTable.addContainerProperty(COLUMN_LABEL_GRAPH, Button.class, null);

    }

    /**
     * Table formatting
     */
    private void doFormatting() {
        iTable.setWidth("500px");
        iTable.setPageLength(5);
    }
}
