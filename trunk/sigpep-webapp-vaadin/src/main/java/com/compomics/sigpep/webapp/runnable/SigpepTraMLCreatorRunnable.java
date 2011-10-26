package com.compomics.sigpep.webapp.runnable;

import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.sigpep.jtraml.SigpepToTraml;
import com.compomics.sigpep.jtraml.TransitionBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.analytics.AnalyticsLogger;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.vaadin.terminal.StreamResource;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SigpepTraMLCreatorRunnable implements Runnable {
    private static Logger logger = Logger.getLogger(SigpepTraMLCreatorRunnable.class);

    MyVaadinApplication iApplication;
    private CustomProgressIndicator iProgressIndicator;

    public SigpepTraMLCreatorRunnable(MyVaadinApplication aApplication, CustomProgressIndicator aProgressIndicator) {

        iApplication = aApplication;
        iProgressIndicator = aProgressIndicator;
    }


    public void run() {

        //add custom progress indicator
        ArrayList<TransitionBean> lSelectedTransitionList = iApplication.getSelectedTransitionList();
        iProgressIndicator.proceed(PropertiesConfigurationHolder.getInstance().getString("form_progress.traml_create"));

        try {
            ObjectFactory lObjectFactory = new ObjectFactory();
            TraMLType lTraMLType = lObjectFactory.createTraMLType();
            lTraMLType.setCvList(CVFactory.getCvListType());

            TSVFileImportModel lTSVFileImportModel = new SigpepToTraml();

            logger.debug("creating " + lSelectedTransitionList.size() + " transitions into TraML objects");
            for (TransitionBean lTransitionBean : lSelectedTransitionList) {
                lTSVFileImportModel.addRowToTraml(lTraMLType, lTransitionBean.getSeparatedOrder());
            }

            // Ok, all rows have been added.
            TraMLCreator lTraMLCreator = new TraMLCreator();
            lTraMLCreator.setTraML(lTraMLType);

            // Ok. The File should have been written!

            logger.debug("creating inputstream from TraML file");
            final InputStream is = new ByteArrayInputStream(lTraMLCreator.asString().getBytes(Charset.defaultCharset()));
            StreamResource.StreamSource ss = new StreamResource.StreamSource() {
                public InputStream getStream() {
                    return is;
                }
            };

            String lFilename = new String("sigpep_" + System.currentTimeMillis() + ".traml");
            StreamResource streamResource = new StreamResource(ss, lFilename, iApplication);
            streamResource.setCacheTime(5000); // no cache (<=0) does not work with IE8
//                streamResource.setMIMEType("application/xml");
//                streamResource.getStream().setParameter("Content-Disposition", "attachment; filename="+lFilename);

            iApplication.getNotifique().clear();
            logger.debug("opening TraML download on the main Window");
            iApplication.getMainWindow().open(streamResource, "_blank");

            iApplication.getMainWindow().requestRepaintAll();

            AnalyticsLogger.runTraMLDownload(iApplication.getHttpSessionID());

        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
    }
}