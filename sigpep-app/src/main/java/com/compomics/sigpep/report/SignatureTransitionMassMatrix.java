package com.compomics.sigpep.report;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIon;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.SignatureTransition;
import com.compomics.sigpep.util.DelimitedTableWriter;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 06-Aug-2008<br/>
 * Time: 11:21:24<br/>
 */
public class SignatureTransitionMassMatrix implements Writable {

    protected Configuration config = Configuration.getInstance();
    protected int massPrecission = config.getInt("sigpep.app.monoisotopic.mass.precision");

    private SignatureTransition signatureTransition;

    public SignatureTransitionMassMatrix(SignatureTransition signatureTransition) {
        this.signatureTransition = signatureTransition;
    }

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
}
