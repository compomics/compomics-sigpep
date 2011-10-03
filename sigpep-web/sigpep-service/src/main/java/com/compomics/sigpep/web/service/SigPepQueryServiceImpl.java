package com.compomics.sigpep.web.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.persistence.util.HibernateUtil;

import javax.jws.WebService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Exposes namedQueries of the SigPep database.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Feb-2008<br/>
 * Time: 14:47:55<br/>
 */

@WebService(endpointInterface = "com.compomics.sigpep.web.service.SigPepQueryServiceImpl")
public class SigPepQueryServiceImpl implements SigPepQueryService {


    /**
     * Returns the signature peptides emitted by a protein when digested with a protease.
     *
     * @param ncbiTaxonId      the NCBI taxon ID identifying the species
     * @param proteinAccession the accession of the protein
     * @param proteaseName     the protease generating the peptides
     * @return a set of peptide sequences
     */
    public HashSet<String> getPeptidesForProtein(int ncbiTaxonId, String proteinAccession, String... proteaseName) {

        HashSet<String> proteinAccessions = new HashSet<String>();
        proteinAccessions.add(proteinAccession);

        HashSet<String> proteaseNames = new HashSet<String>();
        Collections.addAll(proteaseNames, proteaseName);

        HashMap<String, HashSet<String>> protein2Peptide = getPeptidesForProteinSet(ncbiTaxonId, proteinAccessions, proteaseNames);

        return protein2Peptide.get(proteinAccession);

    }

    /**
     * Returns the signature peptides emitted by a protein when digested with a protease.
     *
     * @param ncbiTaxonId       the NCBI taxon ID identifying the species
     * @param proteinAccessions the accession of the protein
     * @param proteaseName      the protease generating the peptides
     * @return a set of peptide sequences
     */
    public HashMap<String, HashSet<String>> getPeptidesForProteinSet(int ncbiTaxonId, HashSet<String> proteinAccessions, String... proteaseName) {

        HashSet<String> proteaseNames = new HashSet<String>();
        Collections.addAll(proteaseNames, proteaseName);
        return getPeptidesForProteinSet(ncbiTaxonId, proteinAccessions, proteaseNames);

    }

    /**
     * Returns the peptides emitted by a set of proteins when digested with a set of proteaseFilter.
     *
     * @param ncbiTaxonId       the NCBI taxon ID identifying the species
     * @param proteinAccessions the protein accessions
     * @param proteaseNames     the names of the proteaseFilter generating the peptides
     * @return a map of protein accessions and peptide sequences
     */
    private HashMap<String, HashSet<String>> getPeptidesForProteinSet(int ncbiTaxonId, HashSet<String> proteinAccessions, HashSet<String> proteaseNames) {

        HashMap<String, HashSet<String>> retVal = new HashMap<String, HashSet<String>>();

        SessionFactory sf = HibernateUtil.getSessionFactory(ncbiTaxonId);
        Session s = sf.openSession();

        //fetch protease
        Query q = s.createQuery("from Protease where name in (:name)");
        q.setParameter("name", proteaseNames);
        Protease pt = (Protease) q.uniqueResult();

        //fetch protein
        q = s.createQuery("from Protein where sequence.primaryDbXref.accession in (:accessions)");
        q.setParameterList("accessions", proteinAccessions);
        for (Iterator<Protein> proteins = q.iterate(); proteins.hasNext(); ) {

            Protein p = proteins.next();

            //get protein sequence
            ProteinSequence ps = p.getSequence();
            for (PeptideFeature pf : ps.getPeptides()) {
                FeaturePeptide fp = pf.getFeatureObject();
                if (pf.getProteases().contains(pt)) {
                    String peptideSequence = fp.getSequenceString();
                    String proteinAccession = p.getPrimaryDbXref().getAccession();
                    if (!retVal.containsKey(proteinAccession)) {
                        retVal.put(proteinAccession, new HashSet<String>());
                    }
                    retVal.get(proteinAccession).add(peptideSequence);
                }
            }

        }
        return retVal;
    }

    /**
     * Returns the peptides emitted by a protein when digested with a protease.
     *
     * @param ncbiTaxonId      the NCBI taxon ID identifying the species
     * @param proteinAccession the accession of the protein
     * @param proteaseName     the protease generating the peptides
     * @return a set of peptide sequences
     */
    public HashSet<String> getSignaturePeptidesForProtein(int ncbiTaxonId, String proteinAccession, String... proteaseName) {
        return null;
    }

    /**
     * Returns the signature peptides emitted by a set of proteins when digested with a set of proteaseFilter.
     *
     * @param ncbiTaxonId       the NCBI taxon ID identifying the species
     * @param proteinAccessions the protein accessions
     * @param proteaseNames     the names of the proteaseFilter generating the peptides
     * @return a map of protein accessions and peptide sequences
     */
    public HashMap<String, HashSet<String>> getSignaturePeptidesForProteinSet(int ncbiTaxonId, HashSet<String> proteinAccessions, String... proteaseNames) {
        return null;
    }

    /**
     * Returns the product ions discriminating a target peptide from the background peptides
     * generated by a protease.
     *
     * @param peptide                        the target peptide
     * @param postTranslationalModifications the post translational modifications to take into account
     * @param proteaseName                   the name(s) of the peptide generating protease(s)
     * @return a map of product ion names and masses in Da
     */
    public HashMap<String, Double> getSignatureTransitionForPeptideWithPTM(String peptide, HashSet<String> postTranslationalModifications, String... proteaseName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns the product ions discriminating a target peptide from the background peptides
     * generated by a protease.
     *
     * @param peptide      the target peptide
     * @param proteaseName the name(s) of the peptide generating protease(s)
     * @return a map of product ion names and masses in Da
     */
    public HashMap<String, Double> getSignatureTransitionForPeptide(String peptide, String... proteaseName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String sayHello(String name) {
        return "Hello " + name;
    }

}
