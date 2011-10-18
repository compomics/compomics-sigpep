package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.compomics.sigpep.webapp.runnable.SigpepTraMLCreaterToConverterRunnable;
import com.compomics.sigpep.webapp.runnable.SigpepTraMLCreatorRunnable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;

import java.io.File;
import java.io.IOException;


/**
 * This class is a
 */
public class TransitionSelectionComponent extends VerticalLayout {
    private static Logger logger = Logger.getLogger(TransitionSelectionComponent.class);

    protected enum Type{Download, Converter};


    private final MyVaadinApplication iApplication;

    private Label iStatus;

    private File iTraMLDownload = null;
    private VerticalLayout iTreeLayout = new VerticalLayout();
    private Button iDownloadTraML;
    private Button iTraMLConverter;
    private CustomProgressIndicator iCustomProgressIndicator;


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

    public static Logger getLogger() {
        return logger;
    }

    private void initComponents() throws IOException {
        this.setStyleName("v-transition-selection");
        this.setSpacing(true);

        iStatus = new Label("");
        this.addComponent(iStatus, 0);

        iDownloadTraML = new Button();
        iDownloadTraML.setEnabled(true);
        iDownloadTraML.setVisible(false);
        iDownloadTraML.setStyleName(Reindeer.BUTTON_LINK);
        iDownloadTraML.setIcon(new ThemeResource("traml_download.png"));
        iDownloadTraML.addListener(new SaveToTraMLClickListener(Type.Download));

        iTraMLConverter = new Button();
        iTraMLConverter.setVisible(false);
        iTraMLConverter.setIcon(new ThemeResource("traml_converter.png"));
        iTraMLConverter.addListener(new SaveToTraMLClickListener(Type.Converter));
        iTraMLConverter.setStyleName(Reindeer.BUTTON_LINK);

        HorizontalLayout lTraMLButtons = new HorizontalLayout();

        lTraMLButtons.addComponent(iDownloadTraML);
        lTraMLButtons.addComponent(iTraMLConverter);

        lTraMLButtons.setSpacing(true);
        lTraMLButtons.setMargin(true);
        lTraMLButtons.setComponentAlignment(iDownloadTraML, Alignment.TOP_LEFT);
        lTraMLButtons.setComponentAlignment(iTraMLConverter, Alignment.TOP_LEFT);

        this.addComponent(lTraMLButtons, 1);

        this.addComponent(iTreeLayout);


        this.setComponentAlignment(lTraMLButtons, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(iStatus, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(iTreeLayout, Alignment.MIDDLE_LEFT);

    }

    @Override
    public void requestRepaintAll() {
        int lSize = iApplication.getSelectedTransitionList().size();
        if (lSize > 0) {
            iStatus.setValue(lSize + " transitions selected");
            iTreeLayout.removeAllComponents();
            iTreeLayout.addComponent(new TransitionSetTree(iApplication));
            iDownloadTraML.setVisible(true);
            iTraMLConverter.setVisible(true);
        } else {
            iTreeLayout.removeAllComponents();
            iDownloadTraML.setVisible(false);
            iTraMLConverter.setVisible(false);
            iStatus.setValue("no transitions selected");
        }
        super.requestRepaintAll();
    }


    private class SaveToTraMLClickListener implements Button.ClickListener {


        private final Type iType;

        private SaveToTraMLClickListener(Type aType) {
            iType = aType;
        }

        /**
         * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
         * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
         *
         * @param event An event containing information about the click.
         */
        public void buttonClick(Button.ClickEvent event) {

            //add custom progress indicator
            String lMessage = PropertiesConfigurationHolder.getInstance().getString("form_progress.traml_queue");
            iCustomProgressIndicator = new CustomProgressIndicator(lMessage, 2);

            iApplication.getNotifique().add(null, iCustomProgressIndicator, Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

            if(iType.equals(Type.Download)){
                MyVaadinApplication.getExecutorService().execute(new SigpepTraMLCreatorRunnable(iApplication, iCustomProgressIndicator));
            }else if(iType.equals(Type.Converter)){
                MyVaadinApplication.getExecutorService().execute(new SigpepTraMLCreaterToConverterRunnable(iApplication, iCustomProgressIndicator));
            }
        }
    }


}
