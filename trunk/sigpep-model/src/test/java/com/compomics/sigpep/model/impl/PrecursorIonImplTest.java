package com.compomics.sigpep.model.impl;

import org.junit.*;
import static org.junit.Assert.*;
import com.compomics.sigpep.model.constants.MonoElementMasses;
import com.compomics.sigpep.model.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 15:46:41<br/>
 */
public class PrecursorIonImplTest {


    private Peptide aPeptide;
    private Peptide anotherPeptide;
    private PrecursorIon aPrecursorIon;
    private PrecursorIon anotherPrecursorIon;

    @BeforeClass
    public static void unitSetup() {
        
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {

    } // unitCleanup()

    @Before
    public void methodSetup() {

        aPeptide = PeptideFactory.createPeptide("LTWLTPLIPSTLLSLGGLPPLTGFLPK");
        anotherPeptide = PeptideFactory.createPeptide("YDQLMHLLWK");

        aPrecursorIon = aPeptide.getPrecursorIon();
        anotherPrecursorIon = aPeptide.getPrecursorIon();


    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testGetPeptide(){

        assertEquals(aPeptide, aPrecursorIon.getPeptide());

    }

    @Test
    public void testGetProductIon(){

        for(ProductIonType type : ProductIonType.values()){

            int length = 3;
            ProductIon product = aPrecursorIon.getProductIon(type, length);

            //make sure we have the right product ion type...
            assertTrue(product.getType() == type);

            //...of the correct length
            assertTrue(product.getSequenceLength()==length);
       
        }

        //make sure an illegal argument exception is thrown for precursor lengths greater peptide length
        try{

            aPrecursorIon.getProductIon(ProductIonType.B, aPrecursorIon.getSequenceLength() + 1);

            fail("IllegalArgumentException - used a product ion length greater than the peptide length");

        } catch (IllegalArgumentException e) {
            // Expected - intentional
        }

        //make sure the product ions have the correct sequence

        for(int length = 1; length <= aPrecursorIon.getSequenceLength(); length++){

            ProductIon product = aPrecursorIon.getProductIon(ProductIonType.B, length);

            String value = product.getSequenceString();
            String expected = aPrecursorIon.getPeptide().getSequenceString().substring(0,length);

            assertEquals(value,expected);

        }

        for(int length = 1; length <= aPrecursorIon.getSequenceLength(); length++){

            ProductIon product = aPrecursorIon.getProductIon(ProductIonType.Y, length);

            String value = product.getSequenceString();
            int peptideLength = aPrecursorIon.getPeptide().getSequenceLength();
            String expected = aPrecursorIon.getPeptide().getSequenceString().substring(peptideLength - length, peptideLength);

            assertEquals(value,expected);

        }

    }

    @Test
    public void testGetProductIons(){


        List<ProductIon> products = aPrecursorIon.getProductIons(ProductIonType.Y);

        //make sure there are as many product ions as residues in the sequence
        assertTrue(products.size() == aPrecursorIon.getSequenceLength());

        //make sure the product ions are of the correct type
        for(ProductIon product : products){
            assertTrue(product.getType()==ProductIonType.Y);
        }

    }

    @Test
    public void testGetNeutralMassNTerminalGroup() {

        //should be monoisotopic mass of H atom
        double expected = MonoElementMasses.getInstance().getDouble("H");

        assertTrue(aPrecursorIon.getNeutralMassNTerminalGroup()==expected);

    }

    @Test
    public void testGetNeutralMassCTerminalGroup() {

        //should be monoisotopic mass of OH group
        double expected = MonoElementMasses.getInstance().getDouble("H") +
                MonoElementMasses.getInstance().getDouble("O");

        assertTrue(aPrecursorIon.getNeutralMassCTerminalGroup()==expected);

    }

    @Test
    public void testSetNeutralMassNTerminalGroup() {

        double originalValue = aPrecursorIon.getNeutralMassNTerminalGroup();
        double newValue = 145.32456;

        aPrecursorIon.setNeutralMassNTerminalGroup(newValue);

        assertTrue(aPrecursorIon.getNeutralMassNTerminalGroup()==newValue);

        aPrecursorIon.setNeutralMassNTerminalGroup(originalValue);

    }

    @Test
    public void testSetNeutralMassCTerminalGroup() {

        double originalValue = aPrecursorIon.getNeutralMassCTerminalGroup();
        double newValue = 145.32456;

        aPrecursorIon.setNeutralMassCTerminalGroup(newValue);

        assertTrue(aPrecursorIon.getNeutralMassCTerminalGroup()==newValue);

        aPrecursorIon.setNeutralMassCTerminalGroup(originalValue);



    }

}
