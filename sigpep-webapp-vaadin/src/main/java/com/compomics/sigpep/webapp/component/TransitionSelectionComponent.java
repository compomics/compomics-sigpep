package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.runnable.SigpepTraMLCreatorRunnable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.apache.log4j.Logger;
import org.vaadin.notifique.Notifique;

import java.io.*;


/**
 * This class is a
 */
public class TransitionSelectionComponent extends VerticalLayout {
    private static Logger logger = Logger.getLogger(TransitionSelectionComponent.class);

    private final MyVaadinApplication iApplication;

    private Label iStatus;

    private File iTraMLDownload = null;
    private VerticalLayout iTreeLayout = new VerticalLayout();
    private Button iDownloadTraML;
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
        iDownloadTraML.setIcon(new ThemeResource("download_traml.png"));
        iDownloadTraML.addListener(new SaveToTraMLClickListener());
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

            //add custom progress indicator
            iCustomProgressIndicator = new CustomProgressIndicator("creating TraML file...", 1);
            iApplication.getNotifique().add(null, iCustomProgressIndicator, Notifique.Styles.MAGIC_BLACK, Boolean.FALSE);

            MyVaadinApplication.getExecutorService().execute(new SigpepTraMLCreatorRunnable(iApplication));

        }
    }


}
