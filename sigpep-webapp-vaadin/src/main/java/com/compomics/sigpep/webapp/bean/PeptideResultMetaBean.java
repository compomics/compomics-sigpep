package com.compomics.sigpep.webapp.bean;

import com.compomics.sigpep.report.MetaNamesEnumeration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is a
 */
public class PeptideResultMetaBean {
    private static final Logger logger = Logger.getLogger(PeptideResultMetaBean.class);

    // Instance variables for the expected fields around the meta properties files.
    private ArrayList iProteins = null;
    private String iPeptide = null;
    private ArrayList iBarcodeMasses = null;
    private ArrayList iBarcodeIonTypes = null;
    private ArrayList iBarcodeIonNumbers = null;

    /**
     * Construct a new meta information instance for a Peptide results file
     *
     * @param aFile
     */
    public PeptideResultMetaBean(File aFile) {
        try {
            PropertiesConfiguration lConfiguration = new PropertiesConfiguration();
            lConfiguration.load(aFile);

            Object lProperty = lConfiguration.getProperty(MetaNamesEnumeration.PROTEIN.NAME);
            if (lProperty != null) {
                if (lProperty instanceof String) {
                    iProteins = new ArrayList();
                    iProteins.add(lProperty);
                } else {
                    iProteins = (ArrayList) lProperty;
                }
            }

            lProperty = lConfiguration.getProperty(MetaNamesEnumeration.PEPTIDE.NAME);
            if (lProperty != null) {
                iPeptide = (String) lProperty;
            }

            lProperty = lConfiguration.getProperty(MetaNamesEnumeration.BARCODE_MASSES.NAME);
            if (lProperty != null) {
                iBarcodeMasses = (ArrayList) lProperty;
            }

            lProperty = lConfiguration.getProperty(MetaNamesEnumeration.BARCODE_IONNUMBER.NAME);
            if (lProperty != null) {
                iBarcodeIonNumbers = (ArrayList) lProperty;
            }

            lProperty = lConfiguration.getProperty(MetaNamesEnumeration.BARCODE_IONTYPE.NAME);
            if (lProperty != null) {
                iBarcodeIonTypes = (ArrayList) lProperty;
            }

        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Returns the number of transitions in this peptide signature barcode.
     *
     * @return
     */
    public int getBarcodeCount() {
        if (iBarcodeMasses.size() == iBarcodeIonTypes.size() && iBarcodeIonTypes.size() == iBarcodeIonNumbers.size()) {
            return iBarcodeIonNumbers.size();
        } else {
            return -1;
        }
    }

    /**
     * Returns the number of parent proteins that this peptide was associated to.
     *
     * @return
     */
    public int getParentCount() {
        return iProteins.size();
    }

    /**
     * Returns the peptide sequence.
     *
     * @return
     */
    public String getPeptide() {
        return iPeptide;
    }

    /**
     * Returns the protein at the specified index. (getParentCount()  for the number of parents.)
     *
     * @param aIndex
     * @return
     */
    public String getProtein(int aIndex) {
        return iProteins.get(aIndex).toString();
    }

    /**
     * Returns the proteins. (getParentCount()  for the number of parents.)
     *
     * @return
     */
    public ArrayList getProteins() {
        return iProteins;
    }

    /**
     * Returns the product ion m/Z (charge 1) at the specified index of the barcode
     *
     * @param aIndex
     * @return
     */
    public double getBarcodeMass(int aIndex) {
        return Double.parseDouble(iBarcodeMasses.get(aIndex).toString());
    }

    /**
     * Returns the barcode index of the specified mass
     *
     * @param aMass
     * @return
     */
    public int getMassMatchIndex(double aMass) {
        int lMax = getBarcodeCount();

        for (int i = 0; i < lMax; i++) {
            if (Math.abs(getBarcodeMass(i) - aMass) < 0.01) {
                return i;
            }
        }
        // No match found!!
        return -1;
    }

    /**
     * Returns the ionType (b-y) at the specified index of the barcode
     *
     * @param aIndex
     * @return
     */
    public char getBarcodeIonType(int aIndex) {
        return iBarcodeIonTypes.get(aIndex).toString().charAt(0);
    }

    /**
     * Returns the ionNumber (1,2,3 ... ) at the specified index of the barcode
     *
     * @param aIndex
     * @return
     */
    public int getBarcodeIonNumber(int aIndex) {
        return Integer.parseInt(iBarcodeIonNumbers.get(aIndex).toString());
    }

}
