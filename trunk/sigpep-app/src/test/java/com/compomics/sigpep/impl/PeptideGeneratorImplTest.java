package com.compomics.sigpep.impl;

import org.junit.*;

import static org.junit.Assert.assertTrue;

import com.compomics.sigpep.*;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.util.SigPepUtil;

import java.util.*;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 25-Mar-2008<br/>
 * Time: 10:25:16<br/>
 */
public class PeptideGeneratorImplTest {

    private static ApplicationLocator applicationLocator = ApplicationLocator.getInstance();
    private static SigPepApplication application;
    private static SigPepSessionFactory sessionFactory;
    private static PeptideGenerator peptideGenerator;
    private static Set<Modification> ptms;
    private static Set<Modification> staticPtms;
    private static Set<Modification> nonStaticPtms;
    private static Map<String, Set<Modification>> staticPtmsByResidue;
    private static Map<String, Set<Modification>> nonStaticPtmsByResidue;


    @BeforeClass
    public static void unitSetup() {

        application = applicationLocator.getApplication();
        sessionFactory = application.getSigPepSessionFactory();
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);

        ptms = ModificationFactory.createPostTranslationalModifications();
        staticPtms = new LinkedHashSet<Modification>();
        nonStaticPtms = new LinkedHashSet<Modification>();
        staticPtmsByResidue = new HashMap<String, Set<Modification>>();
        nonStaticPtmsByResidue = new HashMap<String, Set<Modification>>();

        for (Modification ptm : ptms) {
            String residue = ptm.getResidue();
            if (ptm.isStatic()) {
                staticPtms.add(ptm);
                if (!staticPtmsByResidue.containsKey(residue)) {
                    staticPtmsByResidue.put(residue, new HashSet<Modification>());
                }
                staticPtmsByResidue.get(residue).add(ptm);
            } else {
                nonStaticPtms.add(ptm);
                if (!nonStaticPtmsByResidue.containsKey(residue)) {
                    nonStaticPtmsByResidue.put(residue, new HashSet<Modification>());
                }
                nonStaticPtmsByResidue.get(residue).add(ptm);
            }
        }

        peptideGenerator = session.createPeptideGenerator("tryp");

    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {

    } // unitCleanup()

    @Before
    public void methodSetup() {


    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testGetPeptides() {

        //get unmodified peptides
        Set<Peptide> peptides = peptideGenerator.getPeptides();
        int unmodifiedPeptideSetSize = peptides.size();
        assertTrue("Empty peptide set returned.", unmodifiedPeptideSetSize > 0);

        //get statically modified peptides
        peptideGenerator.setPostTranslationalModifications(staticPtms);
        peptides = peptideGenerator.getPeptides();
        int staticallyModifiedPeptideSetSize = peptides.size();
        int modifiedPeptideCount = 0;
        for (Peptide peptide : peptides) {
            if (peptide.isModified()) {
                modifiedPeptideCount++;
            }
        }
        assertTrue("Application of modifications does not result in any modified peptides.", modifiedPeptideCount > 0);
        assertTrue("Peptide set size increased after setting PeptideGenerator modifications to set containing only static modifications", unmodifiedPeptideSetSize == staticallyModifiedPeptideSetSize);

        //check the number of modified peptides for static PTMs
        for (Modification ptm : staticPtms) {

            peptideGenerator.setPostTranslationalModification(ptm);
            peptides = peptideGenerator.getPeptides();

            String residue = ptm.getResidue();
            ModificationPosition position = ptm.getPosition();

            modifiedPeptideCount = 0;
            int expectedModifiedPeptideCount = 0;
            for (Peptide peptide : peptides) {

                if (peptide.isModified()) {
                    modifiedPeptideCount++;
                }

                //protein modification
                //position: protein N-terminal
                if (residue.equals("protein") && position == ModificationPosition.N_TERMINAL &&
                        peptide.getOrigins().contains(PeptideOrigin.N_TERMINAL)) {
                    expectedModifiedPeptideCount++;
                }

                //protein modification
                //position: protein C-terminal
                else if (residue.equals("protein") && position == ModificationPosition.C_TERMINAL &&
                        peptide.getOrigins().contains(PeptideOrigin.C_TERMINAL)) {
                    expectedModifiedPeptideCount++;
                }

                //protein modification
                //position: protein internal
                else if (residue.equals("protein") && position == ModificationPosition.INTERNAL &&
                        peptide.getOrigins().contains(PeptideOrigin.INTERNAL)) {
                    expectedModifiedPeptideCount++;
                }

                //residue modification
                //position: peptide N-terminal
                else if (peptide.getResiduePositions(residue).contains(1) && position == ModificationPosition.N_TERMINAL) {
                    expectedModifiedPeptideCount++;
                }

                //residue modification
                //position: peptide C-terminal
                else if (peptide.getResiduePositions(residue).contains(peptide.getSequenceLength()) && position == ModificationPosition.C_TERMINAL) {
                    expectedModifiedPeptideCount++;
                }

                //residue modification
                //position: non-positional
                else if (peptide.getResidueCount(residue) > 0 && position == ModificationPosition.NON_POSITIONAL) {
                    expectedModifiedPeptideCount++;
                }

            }

            assertTrue("Modified peptide count (" + modifiedPeptideCount + ") is not equal expected modified peptide count ("
                    + expectedModifiedPeptideCount + ") for modification " + ptm + ".", modifiedPeptideCount == expectedModifiedPeptideCount);
            System.out.println("Modified peptide count (" + modifiedPeptideCount + "); expected modified peptide count ("
                    + expectedModifiedPeptideCount + ") for modification " + ptm + ".");
        }

        //check the number of modified peptides for non-static PTMs

        for (Modification ptm : nonStaticPtms) {

            Map<String, Integer> peptideSequence2ExpectedModifiedPeptideCount = new HashMap<String, Integer>();

            peptideGenerator.setPostTranslationalModification(ptm);
            peptides = peptideGenerator.getPeptides();

            String residue = ptm.getResidue();
            ModificationPosition position = ptm.getPosition();

            modifiedPeptideCount = 0;

            for (Peptide peptide : peptides) {

                if (peptide.isModified()) {
                    modifiedPeptideCount++;
                }

                if (!peptideSequence2ExpectedModifiedPeptideCount.containsKey(peptide.getSequenceString())) {

                    String sequenceString = peptide.getSequenceString();
                    int expectedModifiedPeptideCount = 0;

                    //protein modification
                    //position: protein N-terminal
                    if (residue.equals("protein") &&
                            position == ModificationPosition.N_TERMINAL &&
                            peptide.getOrigins().contains(PeptideOrigin.N_TERMINAL)) {
                        expectedModifiedPeptideCount++;

                    }

                    //protein modification
                    //position: protein C-terminal
                    else if (residue.equals("protein") &&
                            position == ModificationPosition.C_TERMINAL &&
                            peptide.getOrigins().contains(PeptideOrigin.C_TERMINAL)) {
                        expectedModifiedPeptideCount++;

                    }

                    //protein modification
                    //position: protein internal
                    else if (residue.equals("protein") &&
                            position == ModificationPosition.INTERNAL &&
                            peptide.getOrigins().contains(PeptideOrigin.INTERNAL)) {
                        expectedModifiedPeptideCount++;

                    }

                    //residue modification
                    //position: peptide N-terminal
                    else if (peptide.getResiduePositions(residue).contains(1) && position == ModificationPosition.N_TERMINAL) {
                        expectedModifiedPeptideCount++;

                    }

                    //residue modification
                    //position: peptide C-terminal
                    else if (peptide.getResiduePositions(residue).contains(peptide.getSequenceLength()) && position == ModificationPosition.C_TERMINAL) {
                        expectedModifiedPeptideCount++;

                    }

                    //residue modification
                    //position: non-positional
                    else if (peptide.getResidueCount(residue) > 0 && position == ModificationPosition.NON_POSITIONAL) {
                        int residueCount = peptide.getResidueCount(residue);
                        for (int combinationSize = 1; combinationSize <= residueCount; combinationSize++) {
                            expectedModifiedPeptideCount = expectedModifiedPeptideCount + (int) SigPepUtil.combinationsWithoutRepitition(residueCount, combinationSize);
                        }

                    }

                    peptideSequence2ExpectedModifiedPeptideCount.put(sequenceString, expectedModifiedPeptideCount);

                }
            }

            int expectedModifiedPeptideCountTotal = 0;
            for (Integer count : peptideSequence2ExpectedModifiedPeptideCount.values()) {
                expectedModifiedPeptideCountTotal = expectedModifiedPeptideCountTotal + count;
            }

            assertTrue("Modified peptide count (" + modifiedPeptideCount + ") is not equal expected modified peptide count ("
                    + expectedModifiedPeptideCountTotal + ") for modification " + ptm + ".", modifiedPeptideCount == expectedModifiedPeptideCountTotal);
            System.out.println("Modified peptide count (" + modifiedPeptideCount + "); expected modified peptide count ("
                    + expectedModifiedPeptideCountTotal + ") for modification " + ptm + ".");

        }

        //apply a static and a non-static modification to different residues and check the number of modified peptides
        ///////////////////////

        Modification aStaticPtm = null;
        Modification aNonStaticPtm = null;

        for (Modification ptm : staticPtms) {
            if (!ptm.getResidue().equals("protein")) {
                aStaticPtm = ptm;
                break;
            }
        }

        for (Modification ptm : nonStaticPtms) {
            if (!ptm.getResidue().equals("protein") && !ptm.getResidue().equals(aStaticPtm.getResidue())) {
                aNonStaticPtm = ptm;
            }
        }

        peptideGenerator.setPostTranslationalModification(aStaticPtm, aNonStaticPtm);

        Map<String, Integer> peptideSequence2ExpectedNonStaticModifiedPeptideCount = new HashMap<String, Integer>();
        Set<String> expectedStaticallyModifiedPeptides = new HashSet<String>();
        Set<String> expectedStaticallyAndNonStaticallyModifiedPeptides = new HashSet<String>();


        //reset modified peptide count
        modifiedPeptideCount = 0;
        int staticModifiedPeptideCount = 0;
        int nonStaticModifiedPeptideCount = 0;
        int staticAndNonStaticModifiedPeptideCount = 0;
        Set<String> peptidesThatShouldBeNonStaticallyModified = new HashSet<String>();

        for (Peptide peptide : peptideGenerator.getPeptides()) {

            if (peptide.isModified()) {
                modifiedPeptideCount++;
                ModifiedPeptide modPep = (ModifiedPeptide) peptide;
                if (modPep.getPostTranslationalModifications().values().contains(aStaticPtm)) {
                    staticModifiedPeptideCount++;
                }
                if (modPep.getPostTranslationalModifications().values().contains(aNonStaticPtm)) {
                    nonStaticModifiedPeptideCount++;
                }
                if (modPep.getPostTranslationalModifications().values().contains(aNonStaticPtm) &&
                        modPep.getPostTranslationalModifications().values().contains(aStaticPtm)) {
                    staticAndNonStaticModifiedPeptideCount++;
                }


            }

            if (!peptide.isModified() &&
                    peptide.getResidueCount(aNonStaticPtm.getResidue()) > 0) {
                peptidesThatShouldBeNonStaticallyModified.add(peptide.getSequenceString());
            }

            //calculate expected non-statically modified peptide count
            String sequenceString = peptide.getSequenceString();
            if (!peptideSequence2ExpectedNonStaticModifiedPeptideCount.containsKey(sequenceString)) {

                int expectedModifiedPeptideCount = 0;

                if (peptide.getResiduePositions(aNonStaticPtm.getResidue()).contains(1) && aNonStaticPtm.getPosition() == ModificationPosition.N_TERMINAL) {
                    expectedModifiedPeptideCount++;
                }

                //residue modification
                //position: peptide C-terminal
                else if (peptide.getResiduePositions(aNonStaticPtm.getResidue()).contains(peptide.getSequenceLength()) && aNonStaticPtm.getPosition() == ModificationPosition.C_TERMINAL) {
                    expectedModifiedPeptideCount++;

                }

                //residue modification
                //position: non-positional
                else if (peptide.getResidueCount(aNonStaticPtm.getResidue()) > 0 && aNonStaticPtm.getPosition() == ModificationPosition.NON_POSITIONAL) {
                    int residueCount = peptide.getResidueCount(aNonStaticPtm.getResidue());
                    for (int combinationSize = 1; combinationSize <= residueCount; combinationSize++) {
                        expectedModifiedPeptideCount = expectedModifiedPeptideCount + (int) SigPepUtil.combinationsWithoutRepitition(residueCount, combinationSize);
                    }

                }

                peptideSequence2ExpectedNonStaticModifiedPeptideCount.put(sequenceString, expectedModifiedPeptideCount);
            }

            //calculate expected statically modified peptide count

            if (peptide.getResiduePositions(aStaticPtm.getResidue()).contains(1) && aStaticPtm.getPosition() == ModificationPosition.N_TERMINAL) {
                expectedStaticallyModifiedPeptides.add(sequenceString);
                if (peptide.getResiduePositions(aStaticPtm.getResidue()).size() > 0) {
                    expectedStaticallyAndNonStaticallyModifiedPeptides.add(peptide.getSequenceString());
                }
            }

            //residue modification
            //position: peptide C-terminal
            else if (peptide.getResiduePositions(aStaticPtm.getResidue()).contains(peptide.getSequenceLength()) && aStaticPtm.getPosition() == ModificationPosition.C_TERMINAL) {
                expectedStaticallyModifiedPeptides.add(sequenceString);
                if (peptide.getResiduePositions(aStaticPtm.getResidue()).size() > 0) {
                    expectedStaticallyAndNonStaticallyModifiedPeptides.add(peptide.getSequenceString());
                }
            }

            //residue modification
            //position: non-positional
            else if (peptide.getResidueCount(aStaticPtm.getResidue()) > 0 && aStaticPtm.getPosition() == ModificationPosition.NON_POSITIONAL) {
                expectedStaticallyModifiedPeptides.add(sequenceString);
                if (peptide.getResiduePositions(aStaticPtm.getResidue()).size() > 0) {
                    expectedStaticallyAndNonStaticallyModifiedPeptides.add(peptide.getSequenceString());
                }
            }
        }

        int expectedStaticModificationOnlyCount = expectedStaticallyModifiedPeptides.size();

        int expectedModifiedPeptideCountTotal = 0;
        for (String peptideSequence : peptideSequence2ExpectedNonStaticModifiedPeptideCount.keySet()) {
            Integer count = peptideSequence2ExpectedNonStaticModifiedPeptideCount.get(peptideSequence);
            expectedModifiedPeptideCountTotal = expectedModifiedPeptideCountTotal + count;
        }

        int expectedNonStaticModificationOnlyCount = expectedModifiedPeptideCountTotal;
        int expectedStaticModificationOnlyAfterNonStaticModification = expectedStaticallyModifiedPeptides.size();

        expectedModifiedPeptideCountTotal = expectedModifiedPeptideCountTotal + expectedStaticallyModifiedPeptides.size();
        System.out.println("expectedStaticModificationOnlyCount = " + expectedStaticModificationOnlyCount);
        System.out.println("expectedNonStaticModificationOnlyCount = " + expectedNonStaticModificationOnlyCount);
        System.out.println("expectedStaticModificationOnlyAfterNonStaticModification = " + expectedStaticModificationOnlyAfterNonStaticModification);


        /**
         *  int staticModifiedPeptideCount = 0;
         int nonStaticModifiedPeptideCount = 0;
         int staticAndNonStaticModifiedPeptideCount = 0;
         */
        System.out.println("");
        System.out.println("staticModifiedPeptideCount = " + staticModifiedPeptideCount);
        System.out.println("nonStaticModifiedPeptideCount = " + nonStaticModifiedPeptideCount);
        System.out.println("staticAndNonStaticModifiedPeptideCount = " + staticAndNonStaticModifiedPeptideCount);
        System.out.println("peptidesThatShouldBeNonStaticallyModified = " + peptidesThatShouldBeNonStaticallyModified);

        assertTrue("Modified peptide count (" + modifiedPeptideCount + ") is not equal expected modified peptide count ("
                + expectedModifiedPeptideCountTotal + ") for modification combination " + aStaticPtm + " AND " + aNonStaticPtm + ".", modifiedPeptideCount == expectedModifiedPeptideCountTotal);

    }
}

