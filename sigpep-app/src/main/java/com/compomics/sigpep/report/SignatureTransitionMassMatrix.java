package com.compomics.sigpep.report;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.SignatureTransition;
import com.compomics.sigpep.util.DelimitedTableWriter;
import com.compomics.sigpep.util.SigPepUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 06-Aug-2008<br/>
 * Time: 11:21:24<br/>
 */
public class SignatureTransitionMassMatrix implements Writable {
    private static Logger logger = Logger.getLogger(SignatureTransitionMassMatrix.class);

    protected Configuration config = Configuration.getInstance();
    protected int massPrecission = config.getInt("sigpep.app.monoisotopic.mass.precision");

    private SignatureTransition signatureTransition;

    /**
     * @param signatureTransition
     * @TODO: JavaDoc missing.
     */
    public SignatureTransitionMassMatrix(SignatureTransition signatureTransition) {
        this.signatureTransition = signatureTransition;
    }

    /**
     * @param outputStream
     * @TODO: JavaDoc missing.
     */
    public void write(OutputStream outputStream) {

        Peptide target = signatureTransition.getPeptide();
        Set<Peptide> backgroundPeptides = signatureTransition.getBackgroundPeptides();
        List<ProductIon> barcode = signatureTransition.getProductIons();
        Set<ProductIonType> backgroundProductIonTypes = signatureTransition.getBackgroundProductIonTypes();
        Set<ProductIonType> targetProductIonTypes = signatureTransition.getTargetProductIonTypes();
        Set<Integer> productIonChargeStates = signatureTransition.getProductIonChargeStates();

        DelimitedTableWriter dtw = new DelimitedTableWriter(outputStream, "\t", false);

        //write barcode m/z
        List<String> barcodeMz = new ArrayList<String>();

        barcodeMz.add("bc");

        barcodeMz.add("");

        for (ProductIon pi : barcode) {
            for (Integer z : productIonChargeStates) {
                barcodeMz.add("" + SigPepUtil.round(pi.getMassOverCharge(z), massPrecission));
            }
        }

        dtw.writeRow(barcodeMz.toArray());

        for (ProductIonType type : targetProductIonTypes) {
            //write target product ion m/z
            List<String> targetMz = new ArrayList<String>();

            targetMz.add("tg");

            targetMz.add(type.getName());

            for (ProductIon pi : target.getPrecursorIon().getProductIons(type)) {
                for (Integer z : productIonChargeStates) {
                    targetMz.add("" + SigPepUtil.round(pi.getMassOverCharge(z), massPrecission));
                }
            }

            dtw.writeRow(targetMz.toArray());
        }

        //write background product ion m/z
        for (Peptide backgroundPetpide : backgroundPeptides) {

            for (ProductIonType type : backgroundProductIonTypes) {

                List<String> backgroundMz = new ArrayList<String>();

                backgroundMz.add("bg");

                backgroundMz.add(type.getName());

                for (ProductIon pi : backgroundPetpide.getPrecursorIon().getProductIons(type)) {
                    for (Integer z : productIonChargeStates) {
                        backgroundMz.add("" + SigPepUtil.round(pi.getMassOverCharge(z), massPrecission));
                    }
                }

                dtw.writeRow(backgroundMz.toArray());
            }
        }
    }


    /**
     * Write the meta data for a transition set (e.g. barcode iontypes and parent protein)
     *
     * @param outputStream      - the target stream
     * @param aProteinAccession - a single parent protein accession
     */
    public void writeMetaData(OutputStream outputStream, String aProteinAccession) {
        HashSet<String> lSet = new HashSet<String>();
        lSet.add(aProteinAccession);
        writeMetaData(outputStream, lSet);
    }

    /**
     * Write the meta data for a transition set (e.g. barcode iontypes and parent protein)
     *
     * @param outputStream       - the target stream
     * @param aProteinAccessions - a set of parent protein accessions
     */
    public void writeMetaData(OutputStream outputStream, Set<String> aProteinAccessions) {

        try {
            PropertiesConfiguration lConfiguration = new PropertiesConfiguration();

            // Add the parent protein accession
            lConfiguration.addProperty(MetaNamesEnumeration.PROTEIN.NAME, aProteinAccessions);

            // Add the peptide sequence
            lConfiguration.addProperty(MetaNamesEnumeration.PEPTIDE.NAME, signatureTransition.getPeptide().getSequenceString());

            // Add the peptide charge state
            lConfiguration.addProperty(MetaNamesEnumeration.PEPTIDE_CHARGE.NAME, signatureTransition.getTargetPeptideChargeState());

            // Add the barcode information
            List<ProductIon> barcode = signatureTransition.getProductIons();

            ArrayList lIonTypes = new ArrayList();
            ArrayList lIonMasses = new ArrayList();
            ArrayList lIonNumbers = new ArrayList();

            logger.debug("writing metadata for peptide " + signatureTransition.getPeptide().getSequenceString());
            for (ProductIon lProductIon : barcode) {
                double lBarcodeMassOverCharge = lProductIon.getMassOverCharge(1);
                ProductIonType lProductIonType = lProductIon.getType();
                String lBarcodeIonType = lProductIonType.getName();
                int lBarcodeIonNumber = 0;

                logger.debug("production mz\t" + lBarcodeMassOverCharge);
                logger.debug("production type\t" + lBarcodeIonType);
                logger.debug("production seq length\t" + lProductIon.getSequenceLength());

                for (int i = 1; i < signatureTransition.getPeptide().getSequenceLength(); i++) {
                    ProductIon lRunningIon = lProductIon.getPrecursorIon().getProductIon(lProductIonType, i);
                    logger.debug("parent to product " + i + " " + lRunningIon.getMassOverCharge(1));
                    if (Math.abs(lRunningIon.getMassOverCharge(1) - lBarcodeMassOverCharge) <= 0.01) {
                        // i equals the ion number!
                        lBarcodeIonNumber = i;
                        break;
                    }
                }

                lIonTypes.add(lBarcodeIonType);
                lIonMasses.add(lBarcodeMassOverCharge);
                lIonNumbers.add(lBarcodeIonNumber);
            }

            lConfiguration.addProperty(MetaNamesEnumeration.BARCODE_IONNUMBER.NAME, lIonNumbers);
            lConfiguration.addProperty(MetaNamesEnumeration.BARCODE_IONTYPE.NAME, lIonTypes);
            lConfiguration.addProperty(MetaNamesEnumeration.BARCODE_MASSES.NAME, lIonMasses);


            lConfiguration.save(outputStream);
        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);

        }
    }
}
