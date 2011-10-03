package com.compomics.sigpep.model.impl;

import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.compomics.sigpep.model.*;
import com.compomics.sigpep.util.DelimitedTableReader;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 17:59:36<br/>
 */
public class PeptideImplTest {

    private Peptide aPeptide;
    private Peptide aPeptideWithTheSameSequence;
    private Peptide anotherPeptide;
    private Peptide anNterminalPeptide;
    private Peptide aCTerminalPeptide;

    private PrecursorIon aPrecursorIon;
    private PrecursorIon anotherPrecursorIon;
    private Modification methionineOxidation;
    private Modification tyrptophanDiOxidation;
    private Set<Peptide> methionineOxidisedPeptides;
    private ModifiedPeptide aMethionineOxidisedPeptide;
    private ModifiedPeptide anotherMethionineOxidisedPeptide;
    private static Set<String> peptidesContainingMetAndCysRedsidues;


    @BeforeClass
    public static void unitSetup() {


    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {

    } // unitCleanup()

    @Before
    public void methodSetup() {

        aPeptide = PeptideFactory.createPeptide("WQFEHTKPTPFLPTLIALTTLLLPISPFMLMIL");

        anNterminalPeptide = PeptideFactory.createPeptide("MELERPGGNEITR");
        HashSet<PeptideOrigin> origin = new HashSet<PeptideOrigin>();
        origin.add(PeptideOrigin.N_TERMINAL);
        anNterminalPeptide.setOrigins(origin);

        aCTerminalPeptide = PeptideFactory.createPeptide("EQNSLDSWGPK");
        HashSet<PeptideOrigin> originc = new HashSet<PeptideOrigin>();
        origin.add(PeptideOrigin.C_TERMINAL);
        aCTerminalPeptide.setOrigins(originc);

        aPeptideWithTheSameSequence = PeptideFactory.createPeptide("WQFEHTKPTPFLPTLIALTTLLLPISPFMLMIL");

        anotherPeptide = PeptideFactory.createPeptide("LTWLTPLIPSTLLSLGGLPPLTGFLPK");

        aPrecursorIon = aPeptide.getPrecursorIon();
        anotherPrecursorIon = aPeptide.getPrecursorIon();

        methionineOxidation = ModificationFactory.createPostTranslationalModification("metox");

        tyrptophanDiOxidation = ModificationFactory.createPostTranslationalModification("trpdiox");

        methionineOxidisedPeptides = aPeptide.applyModification(methionineOxidation);
        aMethionineOxidisedPeptide = methionineOxidisedPeptides.toArray(new ModifiedPeptide[0])[0];
        anotherMethionineOxidisedPeptide = methionineOxidisedPeptides.toArray(new ModifiedPeptide[0])[1];

        peptidesContainingMetAndCysRedsidues = new HashSet<String>();

        try {
            DelimitedTableReader dtr = new DelimitedTableReader(new FileInputStream("/home/mmueller/dev/java/sigpep/sigpep-model/src/test/resources/metAndCysContainingPeptides.tsv"), "\t");
            for (Iterator<String[]> peptides = dtr.read(); peptides.hasNext(); ) {
                peptidesContainingMetAndCysRedsidues.add(peptides.next()[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testEquals() {

        assertTrue(aPeptide.equals(aPeptide));

        assertTrue(aPeptide.equals(aPeptideWithTheSameSequence));

        assertFalse(aPeptide.equals(anotherPeptide));

        assertFalse(aPeptide.equals(aMethionineOxidisedPeptide));

        assertTrue(aPeptide.equals(aMethionineOxidisedPeptide.getUnmodifiedPeptide()));

        assertFalse(aMethionineOxidisedPeptide.equals(anotherMethionineOxidisedPeptide));

        assertTrue(aMethionineOxidisedPeptide.equals(aMethionineOxidisedPeptide));

        //check for equality for two different object that have the same
        //sequence and PTMs
        aMethionineOxidisedPeptide.getPostTranslationalModifications().clear();
        aMethionineOxidisedPeptide.getPostTranslationalModifications().putAll(anotherMethionineOxidisedPeptide.getPostTranslationalModifications());

        assertTrue(aMethionineOxidisedPeptide.equals(anotherMethionineOxidisedPeptide));

    }

    @Test
    public void testApplyModification() {

        //apply protein acetylation to N-terminal peptide
        Modification protacetyl = ModificationFactory.createPostTranslationalModification("protacetyl");
        Set<Peptide> modifiedPeptides = anNterminalPeptide.applyModification(protacetyl);

        assertTrue("More than one modified peptide returned when N-terminal protein modification was applied.", modifiedPeptides.size() == 1);

        //apply protein acetylation to non-N-terminal peptide
        modifiedPeptides = aPeptide.applyModification(protacetyl);

        assertTrue("Modified peptide returned when N-terminal protein modification was applied to non-N-terminal protein.", modifiedPeptides.size() == 0);

        //apply modificication to C-terminal peptide
        modifiedPeptides = aCTerminalPeptide.applyModification(protacetyl);

        assertTrue("Modified peptide returned when N-terminal protein modification was applied to C-terminal protein.", modifiedPeptides.size() == 0);

        //apply Glu->pyro-Glu to peptide with N-terminal Glu
        Modification glupyroglu = ModificationFactory.createPostTranslationalModification("glupyroglu");
        modifiedPeptides = aCTerminalPeptide.applyModification(glupyroglu);
        assertTrue("More than one modified peptide returned when Glu->pyro-Glu modification was applied to EQNSLDSWGPK.", modifiedPeptides.size() == 1);

        //apply Glu->pyro-Glu to peptide without N-terminal Glu
        modifiedPeptides = anNterminalPeptide.applyModification(glupyroglu);
        assertTrue("Modified peptide returned when Glu->pyro-Glu modification was applied to " + anNterminalPeptide.getSequenceString() + ".", modifiedPeptides.size() == 0);

        //apply non-static modification

        for (String peptideSequence : peptidesContainingMetAndCysRedsidues) {

            Peptide peptide = PeptideFactory.createPeptide(peptideSequence);
            Set<Peptide> modifiedPeptids = peptide.applyModification(ModificationFactory.createPostTranslationalModification("metox"));

            int expectedModifiedPeptideCount = 0;
            if (peptide.getResidueCount("M") > 0) {
                int residueCount = peptide.getResidueCount("M");
                for (int combinationSize = 1; combinationSize <= residueCount; combinationSize++) {
                    expectedModifiedPeptideCount = expectedModifiedPeptideCount + (int) SigPepUtil.combinationsWithoutRepitition(residueCount, combinationSize);
                }

            }

            assertTrue("Number of modified peptides (" + modifiedPeptids.size() + ") not equal number of expected modified peptides (" + expectedModifiedPeptideCount + ") for peptide " + peptideSequence + ".", expectedModifiedPeptideCount == modifiedPeptids.size());
            //System.out.println("Number of modified peptides (" + modifiedPeptids.size() + "); expected modified peptides (" + expectedModifiedPeptideCount + ")");
        }

        //apply multiple modifications
        Set<Modification> staticAndNonStaticModification = ModificationFactory.createPostTranslationalModifications("cyscarbamidmeth", "metox");
        for (String peptideSequence : peptidesContainingMetAndCysRedsidues) {
            //String peptideSequence = "TFGCGSAIASSSYMTELVQGMTLDDAAK";
            Peptide peptide = PeptideFactory.createPeptide(peptideSequence);
            Set<Peptide> modifiedPeptids = peptide.applyModifications(staticAndNonStaticModification);

            int expectedModifiedPeptideCount = 0;
            if (peptide.getResidueCount("M") > 0) {
                int residueCount = peptide.getResidueCount("M");
                for (int combinationSize = 1; combinationSize <= residueCount; combinationSize++) {
                    expectedModifiedPeptideCount = expectedModifiedPeptideCount + (int) SigPepUtil.combinationsWithoutRepitition(residueCount, combinationSize);
                }

            }
            if (peptide.getResidueCount("C") > 0) {
                expectedModifiedPeptideCount++;
            }

            assertTrue("Number of modified peptides (" + modifiedPeptids.size() + ") not equal number of expected modified peptides (" + expectedModifiedPeptideCount + ") for peptide " + peptideSequence + ".", expectedModifiedPeptideCount == modifiedPeptids.size());
            System.out.println("Number of modified peptides (" + modifiedPeptids.size() + "); expected modified peptides (" + expectedModifiedPeptideCount + ")");


        }

    }

}
