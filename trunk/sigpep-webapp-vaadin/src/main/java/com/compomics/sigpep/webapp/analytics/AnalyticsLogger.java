package com.compomics.sigpep.webapp.analytics;

import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import org.apache.log4j.Logger;

/**
 * This class is a
 */
public class AnalyticsLogger {
    final static Logger analytics = Logger.getLogger("ANALYTICS");
    private static Logger logger = Logger.getLogger(AnalyticsLogger.class);
    private static final char sep = ';';

    private static boolean doAnalytics = PropertiesConfigurationHolder.doAnalytics();

    public enum JobType {PEPTIDEFORM("PEPTIDE"), PROTEINFORM("PROTEIN"), PEPTIDECHECKFORM("PEPTIDECHECK");
        private final String iName;

        JobType(String aName) {
            iName = aName;
        }

        public String getName() {
            return iName;
        }
    };

    /**
     * Log a statement when a new session is created
     * @param aSessionID
     */
    public static void newSession(String aSessionID){
        logMetric(aSessionID + sep + "NEW_SESSION");
    }


    public static void startSigpepJob(String aSessionID, JobType aJobType) {
        logMetric(aSessionID + sep + "NEW_SIGPEP_JOB" + sep + aJobType.getName());
    }

    public static void endSigpepJob(String aSessionID, JobType aJobType) {
        logMetric(aSessionID + sep + "END_SIGPEP_JOB" + sep + aJobType.getName());
    }

    public static void runPepnovoPrediction(String aSessionID) {
        logMetric(aSessionID + sep + "PEPNOVO");
    }

    public static void runTraMLConversion(String aSessionID) {
        logMetric(aSessionID + sep + "TRAML_CONVERSION");
    }

    public static void runTraMLDownload(String aSessionID) {
        logMetric(aSessionID + sep + "TRAML_DOWNLOAD");
    }

    public static void runRVisualization(String aSessionID) {
        logMetric(aSessionID + sep + "R_VIS");
    }

    private static void logMetric(String aMessage){
        if(doAnalytics){
            analytics.info(aMessage);
        }
    }
}
