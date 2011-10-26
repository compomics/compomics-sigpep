package com.compomics.sigpep.webapp.runnable;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.sigpep.jtraml.SigpepToTraml;
import com.compomics.sigpep.jtraml.TransitionBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.analytics.AnalyticsLogger;
import com.compomics.sigpep.webapp.component.CustomProgressIndicator;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.google.common.io.Files;
import com.vaadin.terminal.ExternalResource;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SigpepTraMLCreaterToConverterRunnable implements Runnable {
    private static Logger logger = Logger.getLogger(SigpepTraMLCreaterToConverterRunnable.class);

    MyVaadinApplication iApplication;
    private CustomProgressIndicator iProgressIndicator;

    public SigpepTraMLCreaterToConverterRunnable(MyVaadinApplication aApplication, CustomProgressIndicator aProgressIndicator) {

        iApplication = aApplication;
        iProgressIndicator = aProgressIndicator;
    }


    public void run() {

        //add custom progress indicator
        ArrayList<TransitionBean> lSelectedTransitionList = iApplication.getSelectedTransitionList();
        iProgressIndicator.proceed(PropertiesConfigurationHolder.getInstance().getString("form_progress.traml_create"));

        try {
            File lTempDir = Files.createTempDir();
            String lFilename = new String("sigpep_" + System.currentTimeMillis() + ".traml");
            File lTraMLFile = new File(lTempDir, lFilename);
            lTraMLFile.createNewFile();

            BufferedWriter lWriter = Files.newWriter(lTraMLFile, Charset.defaultCharset());

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

            logger.debug("creating File from the TraML content");
            String lTraMLContent = lTraMLCreator.asString();

            lWriter.write(lTraMLContent);
            lWriter.flush();
            lWriter.close();

            iApplication.getNotifique().clear();
            logger.debug("forwarding TraML download to the TraML Converter");
            String lTraMLConverterHome = PropertiesConfigurationHolder.getTraMLConverterHome();

            StringBuilder sb = new StringBuilder();
            sb.append(lTraMLConverterHome)
                    .append('?')
                    .append("input=")
                    .append(lTraMLFile.getCanonicalPath())
                    .append('&')
                    .append("importtype=")
                    .append(FileTypeEnum.TRAML.getName());

            logger.debug("url:" + sb.toString());
            iApplication.getMainWindow().open(new ExternalResource(sb.toString()), "_blank");
            iApplication.getMainWindow().requestRepaintAll();

            AnalyticsLogger.runTraMLConversion(iApplication.getHttpSessionID());

        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}