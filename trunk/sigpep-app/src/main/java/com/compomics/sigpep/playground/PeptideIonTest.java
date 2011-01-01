package com.compomics.sigpep.playground;

import com.compomics.sigpep.model.*;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 11-Jan-2008<br/>
 * Time: 10:26:50<br/>
 */
public class PeptideIonTest {

    /**
     * @TODO: JavaDoc missing.
     *
     * @param args
     */
    public static void main(String[] args) {

        Peptide p = PeptideFactory.createPeptide("MYWFR");

        PrecursorIon pi = p.getPrecursorIon();

        System.out.println("y-ions");
        for(ProductIon prod : pi.getProductIons(ProductIonType.Y)){
            System.out.println(prod.getMassOverCharge(1));
        }

        System.out.println("b-ions");
        for(ProductIon prod : pi.getProductIons(ProductIonType.B)){
            System.out.println(prod.getMassOverCharge(1));
        }

//        PrecursorIonImpl precursor = new PrecursorIonImpl("AMQWEMWE");
//
//        System.out.println(precursor.toString());
//
//        System.out.println("m/z z = 1 = " + precursor.getMassOverCharge(1) );
//        System.out.println("m/z z = 2 = " + precursor.getMassOverCharge(2) );
//        System.out.println("m/z z = -1 = " + precursor.getMassOverCharge(-1) );
//
//        System.out.println("contains " + precursor.getResidueCount("A") + " A");
//
//        for (ProductIonType type : ProductIonType.values()) {
//
//            for (ProductIon product : precursor.getProductIons(type)) {
//
//                System.out.println(product.toString());
//                System.out.println("contains " + product.getResidueCount("A") + " A");
//
//            }
//
//            System.out.println("");
//        }
    }
}
