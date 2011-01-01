package com.compomics.sigpep.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @TODO: JavaDoc missing.
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jul-2008<br/>
 * Time: 13:37:23<br/>
 */
public class SigPepQueryServiceImplTest {

    private static SigPepQueryService service;

    @BeforeClass
    public static void unitSetup() {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);
        service = session.createSigPepQueryService();

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
    public void testGetOrganism() {
        //System.out.println(service.getOrganism());
        assertNotNull("getOrganism returns null", service.getOrganism());
    }

    @Test
    public void testGetAllProteases() {
        //System.out.println(service.getAllProteases());
        assertTrue("getAllProteases returns empty set", service.getAllProteases().size() > 0);
    }

    @Test
    public void testGetProteasesByShortName() {
        String expected = "tryp";
        //System.out.println(service.getProteaseByShortName(expected));
        assertEquals("getOrganism returns null", service.getProteaseByShortName(expected).getShortName(), expected);
    }

    @Test
    public void testGetProteaseSetByShortName() {

        Set<String> expected = new TreeSet<String>();
        expected.add("argc");
        expected.add("lysc");
        expected.add("tryp");

        Set<String> returned = new TreeSet<String>();
        for (Protease protease : service.getProteaseSetByShortName(expected)) {
            //System.out.println(protease);
            returned.add(protease.getShortName());
        }

        assertEquals("getProteaseSetByShortName returns wrong protease(s)", returned, expected);
    }

    @Test
    public void testGetGeneCount() {
        //System.out.println(service.getGeneCount());
        assertTrue("getGeneCount returns number < 1", service.getGeneCount() > 0);
    }

    @Test
    public void testGetAllGenes() {

//        for (Gene gene : service.getAllGenes()) {
//            System.out.println(gene);
//        }
        assertEquals("getAllGenes returns wrong number of genes", service.getAllGenes().size(), service.getGeneCount());
    }

//    @Test
//    public void testGetGeneSetByAccession() {
//
//        Set<String> expected = new TreeSet<String>();
//        expected.addAll(service.getGeneAccessions());
//
//        Set<String> returned = new TreeSet<String>();
//        for (Gene gene : service.getGeneSetByAccession(expected)) {
//            //System.out.println(gene);
//            returned.add(gene.getPrimaryDbXref().getAccession());
//        }
//
//        assertEquals("getGeneSetByAccession returns wrong gene(s)", returned, expected);
//
//    }


//    @Test
//    public void testGetProteinSetByAccession() {
//
//        Set<String> expected = new TreeSet<String>();
//        expected.addAll(service.getProteinAccessions());
//
//        Set<String> returned = new TreeSet<String>();
//        for (Protein protein : service.getProteinSetByAccession(expected)) {
//            //System.out.println(protein);
//            returned.add(protein.getPrimaryDbXref().getAccession());
//        }
//
//        assertEquals("getProteinSetByAccession returns wrong proteins(s)", returned, expected);
//
//    }

//    @Test
//    public void testGetGenesWithAlternativeTranscripts() {
//
//        Set<Gene> result = service.getGenesWithAlternativeTranscripts();
//
//        for (Gene gene : result) {
//            //System.out.println(gene.getPrimaryDbXref().getAccession() + " " + gene.getProteins().size());
//            assertTrue("getGenesWithAlternativeTranscripts returns gene with only on transcript", gene.getProteins().size() > 1);
//        }
//
//    }
//
//    @Test
//    public void testGetGenesWithAlternativeTranslations() {
//
//        Set<Gene> result = service.getGenesWithAlternativeTranslations();
//
//        for (Gene gene : result) {
//
//            Set<ProteinSequence> translations = new HashSet<ProteinSequence>();
//            for (Protein p : gene.getProteins()) {
//                translations.add(p.getSequence());
//            }
//            //System.out.println(gene.getPrimaryDbXref().getAccession() + " " + gene.getProteins().size());
//            assertTrue("testGetGenesWithAlternativeTranslations returns gene with only on translation", translations.size() > 1);
//
//        }
//
//    }

    @Test
    public void testGetSignaturePeptidesForProteinSet() {


        ArrayList<String> accessionList = new ArrayList<String>();
        accessionList.addAll(service.getProteinAccessions());
        Set<String> subSet = new HashSet<String>();
        subSet.addAll(accessionList.subList(1, 10));

        Set<Protein> targets = service.getProteinSetByAccession(subSet);
        Protease protease = service.getProteaseByShortName("tryp");

        for (PeptideFeature peptideFeature : service.getSignaturePeptidesForProteinSet(targets, protease)) {

            FeaturePeptide peptide = peptideFeature.getFeatureObject();
            Set<ProteinSequence> sequences = peptide.getParentSequences(protease);
            ProteinSequence sequence = sequences.iterator().next();

            for(Protein p : sequence.getProteins()){
                System.out.println(((Persistable)sequence).getId() + " " + p.getPrimaryDbXref().getAccession() + " " + peptide.getSequenceString());
            }

            assertTrue("getSignaturePeptidesForProteinSet returns gene with only on translation", sequences.size() == 1);
        }
    }

    @Test
    public void testGetSignaturePeptidesForGeneSet() {

        ArrayList<String> accessionList = new ArrayList<String>();
        accessionList.addAll(service.getGeneAccessions());
        Set<String> subSet = new HashSet<String>();
        subSet.addAll(accessionList.subList(1, 10));

        Set<Gene> targets = service.getGeneSetByAccession(subSet);
        Protease protease = service.getProteaseByShortName("tryp");

        for (PeptideFeature peptideFeature : service.getSignaturePeptidesForGeneSet(targets, protease)) {

            FeaturePeptide peptide = peptideFeature.getFeatureObject();
            Set<Gene> genes = new HashSet<Gene>();
            for (ProteinSequence sequence : peptide.getParentSequences(protease)) {
                for (Protein protein : sequence.getProteins()) {
                    System.out.println(((Persistable)sequence).getId() + " " + protein.getPrimaryDbXref().getAccession() + " "
                            + protein.getGene().getPrimaryDbXref().getAccession() + " " + peptide.getSequenceString());
                    genes.add(protein.getGene());
                }
            }

            //System.out.println(gene.getPrimaryDbXref().getAccession() + " " + gene.getProteins().size());
            assertTrue("getSignaturePeptidesForGeneSet returns gene with only on translation", genes.size() == 1);
        }
    }
}
