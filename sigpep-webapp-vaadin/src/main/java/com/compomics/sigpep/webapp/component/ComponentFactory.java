package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.google.common.io.Files;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;

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
    public static FireableLink createFileDownloadLink(File lFile, MyVaadinApplication aApplication) throws IOException {
        final InputStream is = Files.newInputStreamSupplier(lFile).getInput();

        StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource lStreamResource = new StreamResource(lStreamSource, lFile.getName(), aApplication);

        FireableLink lDownload = new FireableLink("Download", lStreamResource);
        lDownload.addStyleName("v-download-link");
        return lDownload;
    }


    /**
     * Created an Vaadin Embedded image object from a specified File.
     *
     * @param aOutputFile
     * @param aImageCaption
     * @return
     * @throws IOException
     */
    public static Embedded createImage(File aOutputFile, String aImageCaption, Application aApplication) throws IOException {
        final InputStream is = Files.newInputStreamSupplier(aOutputFile).getInput();
        StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource lStreamResource = new StreamResource(lStreamSource, aOutputFile.getName(), aApplication);
        return new Embedded(aImageCaption, lStreamResource);
    }

    /**
     * Add a notification window to the application in case of an uncaught exception
     *
     * @param aCaption
     * @param aPropertyString
     * @param aMyVaadinApplication
     */
    public static void addUncaughtExceptionWindow(String aCaption, String aPropertyString, final MyVaadinApplication aMyVaadinApplication) {
        aMyVaadinApplication.getNotifique().clear();

        //show error notification window
        final Window lWindow = new Window(aCaption);
        lWindow.setClosable(Boolean.FALSE);
        lWindow.setModal(Boolean.TRUE);
        lWindow.setResizable(Boolean.FALSE);
        lWindow.setWidth("22%");
        lWindow.setHeight("16%");

        Label lLabel = new Label(PropertiesConfigurationHolder.getInstance().getString(aPropertyString), Label.CONTENT_RAW);
        Button lButton = new Button("OK", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent aClickEvent) {
                aMyVaadinApplication.getMainWindow().removeWindow(lWindow);
                //close the application
                aMyVaadinApplication.close();
            }
        });

        VerticalLayout lVerticalLayout = new VerticalLayout();
        lVerticalLayout.setSpacing(Boolean.TRUE);
        lVerticalLayout.setSizeFull();
        lVerticalLayout.addComponent(lLabel);
        lVerticalLayout.addComponent(lButton);
        lVerticalLayout.setComponentAlignment(lButton, Alignment.BOTTOM_RIGHT);
        lWindow.addComponent(lVerticalLayout);

        aMyVaadinApplication.getMainWindow().addWindow(lWindow);
    }


}
