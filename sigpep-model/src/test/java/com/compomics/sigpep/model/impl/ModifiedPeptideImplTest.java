package com.compomics.sigpep.model.impl;

import org.junit.*;

import static org.junit.Assert.*;

import com.compomics.sigpep.model.*;
import com.compomics.sigpep.model.ModificationFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 12:04:40<br/>
 */
public class ModifiedPeptideImplTest {

    private Peptide peptide;
    private ModifiedPeptide aModifiedPeptide;
    private Set<Peptide> modifiedPeptides;
    private String sequenceString;
    private Modification methionineOxidation;
    private Set<Protease> proteases;

    @BeforeClass
    public static void unitSetup() {

    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {

    } // unitCleanup()

    @Before
    public void methodSetup() {

        proteases = new HashSet<Protease>();
        proteases.add(new ProteaseImpl("tryp"));
        sequenceString = "WQFEHTKPTPFLPTLIALTTLLLPISPFMLMIL";
        peptide = PeptideFactory.createPeptide(sequenceString);
        peptide.setProteases(proteases);
        methionineOxidation = ModificationFactory.createPostTranslationalModification("metox");
        modifiedPeptides = peptide.applyModification(methionineOxidation);
        aModifiedPeptide = (ModifiedPeptide) modifiedPeptides.iterator().next();

    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testGetUnmodifiedPeptide() {

        Peptide unmodifiedPeptide = aModifiedPeptide.getUnmodifiedPeptide();
        assertEquals(unmodifiedPeptide, peptide);
        assertTrue(unmodifiedPeptide == peptide);

    }

    @Test
    public void testGetPostTranslationalModifications() {

        for (Peptide pep : modifiedPeptides) {

            ModifiedPeptide modPep = (ModifiedPeptide) pep;
            Map<Integer, Modification> ptms = modPep.getPostTranslationalModifications();

            //check if we get something back
            assertNotNull(ptms);

            //checkt that modifications habe been applied
            assertTrue(ptms.size() > 0);

            //check that the modification positions are
            //equal to the modified residue position
            for (Integer ptmPosition : ptms.keySet()) {
                assertTrue(peptide.getResiduePositions(methionineOxidation.getResidue()).contains(ptmPosition));
            }

        }

    }

    @Test
    public void testGetProteases() {
        assertEquals(proteases, aModifiedPeptide.getProteases());
    }

    @Test
    public void testSetProteases() {

        Set<Protease> expected = new HashSet<Protease>();
        expected.add(new ProteaseImpl("argc"));
        expected.add(new ProteaseImpl("lysc"));
        expected.add(new ProteaseImpl("pepa"));

        Set<Protease> originalValue = aModifiedPeptide.getProteases();
        aModifiedPeptide.setProteases(expected);

        //make sure that the protease set has changed
        assertTrue(aModifiedPeptide.getProteases() == expected);

        //make sure that the setting is applied to the unmodified peptide
        assertTrue(aModifiedPeptide.getProteases() == aModifiedPeptide.getUnmodifiedPeptide().getProteases());

        //reset proteases
        aModifiedPeptide.setProteases(originalValue);

    }

    @Test
    public void testGetSequenceString() {

        assertEquals(aModifiedPeptide.getSequenceString(), sequenceString);

        assertEquals(aModifiedPeptide.getUnmodifiedPeptide().getSequenceString(), sequenceString);

    }

    @Test
    public void testGetSequenceLength() {

        assertEquals(aModifiedPeptide.getSequenceLength(), sequenceString.length());

        assertEquals(aModifiedPeptide.getUnmodifiedPeptide().getSequenceLength(), sequenceString.length());

    }

    @Test
    public void testIsModified() {

        assertTrue(aModifiedPeptide.isModified());

        assertFalse(aModifiedPeptide.getUnmodifiedPeptide().isModified());

    }

    @Test
    public void testGetPrecursorIon() {

        PrecursorIon modifiedPrecursor = aModifiedPeptide.getPrecursorIon();
        PrecursorIon unmodifiedPrecursor = aModifiedPeptide.getUnmodifiedPeptide().getPrecursorIon();

        //make sure we get precursor ions
        assertNotNull(modifiedPrecursor);
        assertNotNull(unmodifiedPrecursor);

        //make sure the modified precursor ion is not equal to the modified precursor ion
        assertFalse(modifiedPrecursor.equals(unmodifiedPrecursor));

        //make sure the modified precursor is heavier than the unmodified precursor ion
        assertTrue(modifiedPrecursor.getNeutralMassPeptide() > unmodifiedPrecursor.getNeutralMassPeptide());

    }

    @Test
    public void testGetResiduePositions() {

        assertEquals(aModifiedPeptide.getResiduePositions("M"), peptide.getResiduePositions("M"));

    }
}
