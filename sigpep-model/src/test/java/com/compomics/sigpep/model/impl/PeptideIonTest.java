package com.compomics.sigpep.model.impl;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import com.compomics.sigpep.model.ProductIonType;

/**
 * @TODO: JavaDoc missing
 * @TODO: most of the code in this test is commented out...
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 27-Nov-2007<br/>
 * Time: 13:27:08<br/>
 */
public class PeptideIonTest {

    /**
     * A =  71.03711
     * R = 156.10111
     * N = 114.04293
     * D = 115.02694
     * C = 103.00919
     * Q = 128.05858
     * E = 129.04259
     * G =  57.02146
     * H = 137.05891
     * I = 113.08406
     * L = 113.08406
     * K = 128.09496
     * M = 131.04049
     * F = 147.06841
     * P =  97.05276
     * S =  87.03203
     * T = 101.04768
     * W = 186.07931
     * Y = 163.06333
     * V =  99.06841
     * B = 114.04293
     * Z = 128.05858
     * X = 118.80450
     * U = 168.96420
     */

    //the expected masses used as expected values obtained from ProteinProspector
    //seem to use a mono isotopic mass for Oxygen which differs from the
    //one used here by 0.0005
    private static final double massDelta = 0.0006;

    private static final String sequence = "RNDCQEGHILKMFPSTWYV";

    private static final double expectedA02 = 243.1564;
    private static final double expectedA03 = 358.1833;
    private static final double expectedA04 = 461.1925;
    private static final double expectedA05 = 589.2511;
    private static final double expectedA06 = 718.2937;
    private static final double expectedA07 = 775.3152;
    private static final double expectedA08 = 912.3741;
    private static final double expectedA09 = 1025.4581;
    private static final double expectedA10 = 1138.5422;
    private static final double expectedA11 = 1266.6372;
    private static final double expectedA12 = 1397.6776;
    private static final double expectedA13 = 1544.7461;
    private static final double expectedA14 = 1641.7988;
    private static final double expectedA15 = 1728.8309;
    private static final double expectedA16 = 1829.8785;
    private static final double expectedA17 = 2015.9578;
    private static final double expectedA18 = 2179.0212;

    private static final double expectedB02 = 271.1513;
    private static final double expectedB03 = 386.1783;
    private static final double expectedB04 = 489.1874;
    private static final double expectedB05 = 617.2460;
    private static final double expectedB06 = 746.2886;
    private static final double expectedB07 = 803.3101;
    private static final double expectedB08 = 940.3690;
    private static final double expectedB09 = 1053.4531;
    private static final double expectedB10 = 1166.5371;
    private static final double expectedB11 = 1294.6321;
    private static final double expectedB12 = 1425.6726;
    private static final double expectedB13 = 1572.7410;
    private static final double expectedB14 = 1669.7937;
    private static final double expectedB15 = 1756.8258;
    private static final double expectedB16 = 1857.8734;
    private static final double expectedB17 = 2043.9528;
    private static final double expectedB18 = 2207.0161;

    private static final double expectedY01 = 118.0863;
    private static final double expectedY02 = 281.1496;
    private static final double expectedY03 = 467.2289;
    private static final double expectedY04 = 568.2766;
    private static final double expectedY05 = 655.3086;
    private static final double expectedY06 = 752.3614;
    private static final double expectedY07 = 899.4298;
    private static final double expectedY08 = 1030.4703;
    private static final double expectedY09 = 1158.5652;
    private static final double expectedY10 = 1271.6493;
    private static final double expectedY11 = 1384.7334;
    private static final double expectedY12 = 1521.7923;
    private static final double expectedY13 = 1578.8137;
    private static final double expectedY14 = 1707.8563;
    private static final double expectedY15 = 1835.9149;
    private static final double expectedY16 = 1938.9241;
    private static final double expectedY17 = 2053.9510;
    private static final double expectedY18 = 2167.9940;

//    @BeforeClass
//    public static void unitSetup() {
//
//    } // unitSetup()
//
//    @AfterClass
//    public static void unitCleanup() {
//
//    } // unitCleanup()
//
//    @Before
//    public void methodSetup() {
//    } // methodSetup()
//
//    @After
//    public void methodCleanup() {
//    } // methodCleanup()
//
//    @Test
//    public void testCalculatePeptideMass() {
//
//
//    } // testGetMarker()
//
//    @Test
//    public void testCalculateMassOverCharge() {
//
//    }
//
//    @Test
//    public void testCalculatePeptideMassFromMassOverCharge() {
//
//    }
//
//    @Test
//    public void testGetPrecursorIonMassOverCharge() {
//
//        PrecursorIonImpl pi = new PrecursorIonImpl(PeptideFactory.createPeptide(sequence));
//        double expected = 2324.0951;
//
//        assertEquals(expected, pi.getMassOverCharge(1), massDelta);
//
//    }
//
//    @Test
//    public void testGetLength() {
//
//        PrecursorIonImpl pi = new PrecursorIonImpl(PeptideFactory.createPeptide(sequence));
//
//        assertEquals(sequence.length(), pi.getSequenceLength());
//
//    }
//
//    @Test
//    public void testGetPeptideMass() {
//
//        PrecursorIonImpl pi = new PrecursorIonImpl(PeptideFactory.createPeptide(sequence));
//
//        double expected = 2323.0878;
//
//        assertEquals(expected, pi.getNeutralMassPeptide(), massDelta);
//
//    }
//
//    @Test
//    public void testGetResidueMass() {
//
//    }
//
//    @Test
//    public void testGetSequence() {
//
//        PrecursorIonImpl pi = new PrecursorIonImpl(PeptideFactory.createPeptide(sequence));
//
//        assertEquals(sequence, pi.getSequenceString());
//
//    }
//
//    @Test
//    public void testGetMassNTerminalGroup() {
//
//    }
//
//    @Test
//    public void testSetMassNTerminalGroup() {
//
//    }
//
//    @Test
//    public void testGetMassCTerminalGroup() {
//
//    }
//
//    @Test
//    public void testSetMassCTerminalGroup() {
//
//    }
//
//    @Test
//    public void testGetAllProductIonMassOverCharges() {
//
//    }
//
//    @Test
//    public void testGetAllProductIonMasses() {
//
//    }
//
//    @Test
//    public void testGetProductIonMass() {
//
//
//    }


}
