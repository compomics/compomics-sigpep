package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.io.Files;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Link;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This factory creates often used Vaadin components.
 */
public class ComponentFactory {

    /**
     * Creates a Vaadin Link to download the specified File.
     *
     * @param lFile
     * @return Vaadin Link instance.
     * @throws java.io.IOException
     */
    public static Link createFileDownloadLink(File lFile) throws IOException {
        final InputStream is = Files.newInputStreamSupplier(lFile).getInput();

        StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource lStreamResource = new StreamResource(lStreamSource, lFile.getName(), MyVaadinApplication.getApplication());

        Link lDownload = new Link("Download", lStreamResource);
        lDownload.setStyleName("v-download-link");
        return lDownload;
    }


    /**
     * Created an Vaadin Embedded image object from a specified File.
     * @param aOutputFile
     * @param aImageCaption
     * @return
     * @throws IOException
     */
    public static Embedded createImage(File aOutputFile, String aImageCaption) throws IOException {
        final InputStream is = Files.newInputStreamSupplier(aOutputFile).getInput();
        StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource lStreamResource = new StreamResource(lStreamSource, aOutputFile.getName(), MyVaadinApplication.getApplication());
        return new Embedded(aImageCaption, lStreamResource);
    }
}
