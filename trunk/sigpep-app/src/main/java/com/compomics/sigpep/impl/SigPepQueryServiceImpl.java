package com.compomics.sigpep.impl;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import org.apache.log4j.Logger;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.persistence.dao.ObjectDao;
import com.compomics.sigpep.persistence.dao.SimpleQueryDao;

import java.util.*;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 31-Aug-2007<br/>
 * Time: 09:53:18<br/>
 */
public class SigPepQueryServiceImpl implements SigPepQueryService {

    /** the log4j logger   */
    protected static Logger logger = Logger.getLogger(SigPepQueryServiceImpl.class);

    /** Field simpleQueryDao  */
    protected SimpleQueryDao simpleQueryDao;

    /** Field objectDao  */
    protected ObjectDao objectDao;

    /** Field sigPepSession  */
    protected SigPepSession sigPepSession;

    /**
     * Constructs a SigPep query service instance attached to the SigPep session
     * passed as an argument.
     *
     * @param sigPepSession the SigPep session the SigPep query service is attached to
     */
    public SigPepQueryServiceImpl(SigPepSession sigPepSession) {
        this.setSigPepSession(sigPepSession);
    }

    /**
     * Returns the SigPep session this service is attached to.
     *
     * @return the SigPep session
     */
    public SigPepSession getSigPepSession() {
        return sigPepSession;
    }

    /**
     * Sets the SigPep session this service is attached to.
     *
     * @param sigPepSession the SigPep session
     */
    public void setSigPepSession(SigPepSession sigPepSession) {
        this.sigPepSession = sigPepSession;
        this.simpleQueryDao = sigPepSession.getSimpleQueryDao();
        this.objectDao = sigPepSession.getObjectDao();
    }

    //////////////////
    //Organism queries
    //////////////////

    /**
     * Returns the organism the query service is for.
     *
     * @return the organism
     */
    public Organism getOrganism() {
        return sigPepSession.getOrganism();
    }

    //////////////////
    //Protease queries
    //////////////////

    /**
     * Returns all proteases available for queries.
     *
     * @return a set of proteases
     */
    public Set<Protease> getAllProteases() {
        return objectDao.getAllProteases();
    }

    /**
     * Returns the protease specified by short name.
     *
     * @param shortName the protease short name
     * @return the protease
     */
    public Protease getProteaseByShortName(String shortName) {
        return objectDao.getProteaseByShortName(shortName);
    }

    /**
     * Returns the protease specified by short name.
     *
     * @param fullName the protease short name
     * @return the protease
     */
    public Protease getProteaseByFullName(String fullName) {
        return objectDao.getProteaseByFullName(fullName);
    }


    /**
     * Returns a set of one or more proteases specified by its/their shortname(s).
     *
     * @param shortName the protease short name(s)
     * @return a set of proteases
     */
    public Set<Protease> getProteaseSetByShortName(String... shortName) {
        Set<String> shortNames = new HashSet<String>();
        Collections.addAll(shortNames, shortName);
        return objectDao.getProteaseSetByShortName(shortNames);
    }

    /**
     * Returns a set of one or more proteases specified by its/their shortname(s).
     *
     * @param shortName a set of protease short name(s)
     * @return a set of proteases
     */
    public Set<Protease> getProteaseSetByShortName(Set<String> shortName) {
        return objectDao.getProteaseSetByShortName(shortName);
    }

    //////////////
    //Gene queries
    //////////////

    /**
     * Returns the number of gene entries in the database
     *
     * @return the gene entry count
     */
    public int getGeneCount() {
        return simpleQueryDao.getGeneCount();
    }

    /**
     * Returns the accessions of all gene entries in the database.
     *
     * @return the accessions
     */
    public Set<String> getGeneAccessions() {
        return simpleQueryDao.getGeneAccessions();
    }

    /**
     * Returns all gene entries in the database.
     *
     * @return the gene entries
     */
    public Set<Gene> getAllGenes() {
        return objectDao.getAllGenes();
    }

    /**
     * Returns the gene specified by the accession number.
     *
     * @param accession the accession number
     * @return the gene
     */
    public Gene getGeneByAccession(String accession) {
        return objectDao.getGeneByAccession(accession);
    }

    /**
     * Returns the genes specified by one or more accession numbers.
     *
     * @param accession the accession numbers
     * @return the gene
     */
    public Set<Gene> getGeneSetByAccession(String... accession) {
        Set<String> accessions = new HashSet<String>();
        Collections.addAll(accessions, accession);
        return this.getGeneSetByAccession(accessions);
    }

    /**
     * Returns the genes specified by a set of accession numbers.
     *
     * @param accessions the accession numbers
     * @return the gene
     */
    public Set<Gene> getGeneSetByAccession(Set<String> accessions) {
        return objectDao.getGeneSetByAccession(accessions);
    }

    /**
     * Returns all genes with more than one transcript (a gene might have more than
     * one transcript but still only one translation as the transcripts only differ
     * in the UTRs).
     *
     * @return the genes
     */
    public Set<Gene> getGenesWithAlternativeTranscripts() {

        Set<Gene> retVal;
        Set<String> accessions = simpleQueryDao.getAccessionsAlternativelySplicedGenesTranscriptLevel();
        if (accessions.size() > 0) {
            retVal = objectDao.getGeneSetByAccession(accessions);
        } else {
            retVal = Collections.emptySet();
        }
        return retVal;
    }

    /**
     * Returns all genes with more than one translation (a gene might have more than
     * one transcript but still only one translation as the transcripts only differ
     * in the UTRs).
     *
     * @return the genes
     */
    public Set<Gene> getGenesWithAlternativeTranslations() {

        Set<Gene> retVal;
        Set<String> accessions = simpleQueryDao.getAccessionsAlternativelySplicedGenesTranslationLevel();
        if (accessions.size() > 0) {
            retVal = objectDao.getGeneSetByAccession(accessions);
        } else {
            retVal = Collections.emptySet();
        }

        return retVal;
    }

    /////////////////
    //Protein queries
    /////////////////

    /**
     * Returns the number of protein entries in the database.
     *
     * @return the protein entry count
     */
    public int getProteinCount() {
        return simpleQueryDao.getProteinCount();
    }

    /**
     * Returns the accessions of all protein entries in the database.
     *
     * @return the accessions
     */
    public Set<String> getProteinAccessions() {
        return simpleQueryDao.getProteinAccessions();
    }

    /**
     * Returns the protein specified by the accession number.
     *
     * @param accession the accession number
     * @return the protein
     */
    public Protein getProteinByAccession(String accession) {
        return objectDao.getProteinByAccession(accession);
    }

    /**
     * Returns the proteins specified by one or more accession numbers.
     *
     * @param accession the accession numbers
     * @return the gene
     */
    public Set<Protein> getProteinSetByAccession(String... accession) {
        Set<String> accessions = new HashSet<String>();
        Collections.addAll(accessions, accession);
        return this.getProteinSetByAccession(accessions);
    }

    /**
     * Returns the proteins specified by a set of accession numbers.
     *
     * @param accessions the accession numbers
     * @return the gene
     */
    public Set<Protein> getProteinSetByAccession(Set<String> accessions) {
        return objectDao.getProteinSetByAccession(accessions);
    }

    //////////////////
    //Sequence queries
    //////////////////

    /**
     * Returns all protein sequences in the database.
     *
     * @return the protein sequences
     */
    public Set<String> getProteinSequences() {
        return simpleQueryDao.getProteinSequenceStrings();
    }

    /////////////////
    //Peptide queries
    /////////////////

    /**
     * Returns all peptide sequences generated by one or more proteases
     * from all protein sequences of an organism.
     *
     * @param protease the protease(s)
     * @return a set of peptide sequence strings
     */
    public Set<String> getPeptideSequencesForProtease(Protease... protease) {

        Set<String> proteaseSet = new HashSet<String>();

        for (Protease p : protease) {
            proteaseSet.add(p.getShortName());
        }

        return simpleQueryDao.getPeptideSequencesByProteaseShortName(proteaseSet);
    }

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from all protein sequences of an organism.
     *
     * @param protease the protease(s)
     * @return a set of peptide sequence strings
     */
    public Set<String> getSignaturePeptideSequencesForProtease(Protease... protease) {

        Set<String> proteaseSet = new HashSet<String>();

        for (Protease p : protease) {
            proteaseSet.add(p.getShortName());
        }

        return simpleQueryDao.getSignaturePeptideSequencesByProteaseShortName(proteaseSet);
    }

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequences of a set of protein.
     *
     * @param proteins  the protein set
     * @param proteases the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptidesForProteinSet(Set<Protein> proteins, Set<Protease> proteases) {

        Cache sessionCache = this.getSigPepSession().getCache();

        Set<PeptideFeature> retVal = new HashSet<PeptideFeature>();

        Set<String> proteaseShortNames = new HashSet<String>();

        for (Protease prot : proteases) {
            proteaseShortNames.add(prot.getShortName());
        }

        String key = "signature_peptides_protein_level_" + proteaseShortNames;
        Set<Integer> signaturePeptideIds;

        try {
            signaturePeptideIds = (Set<Integer>) this.getSigPepSession().getCache().getFromCache(key);
        } catch (NeedsRefreshException e) {
            boolean updated = false;
            try {
                // Get the value (probably by calling an EJB)
                signaturePeptideIds = simpleQueryDao.getSignaturePeptideIdsByProteaseShortNamesProteinLevel(proteaseShortNames);

                // Store in the cache
                sessionCache.putInCache(key, signaturePeptideIds);
                updated = true;
            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    sessionCache.cancelUpdate(key);
                }
            }
        }

        for (Protein protein : proteins) {
            ProteinSequence s = protein.getSequence();

            for (PeptideFeature pf : s.getPeptides()) {

                FeaturePeptide peptide = pf.getFeatureObject();
                int id = ((Persistable) peptide).getId();
                if (signaturePeptideIds.contains(new Integer(id))) {
                    retVal.add(pf);
                }
            }
        }

        return retVal;
    }

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequences of a set of protein.
     *
     * @param proteins the protein set
     * @param protease the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptidesForProteinSet(Set<Protein> proteins, Protease... protease) {
        Set<Protease> proteases = new HashSet<Protease>();
        Collections.addAll(proteases, protease);
        return getSignaturePeptidesForProteinSet(proteins, proteases);
    }

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequence of a protein.
     *
     * @param protein  the protein
     * @param protease the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptides(Protein protein, Protease... protease) {
        Set<Protein> proteins = new HashSet<Protein>();
        proteins.add(protein);
        return this.getSignaturePeptidesForProteinSet(proteins, protease);
    }

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a set of genes.
     *
     * @param genes    the genes
     * @param protease the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptidesForGeneSet(Set<Gene> genes, Protease... protease) {
        Set<Protease> proteases = new HashSet<Protease>();
        Collections.addAll(proteases, protease);
        return getSignaturePeptidesForGeneSet(genes, proteases);
    }

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a gene.
     *
     * @param gene     the gene
     * @param protease the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptides(Gene gene, Protease... protease) {
        Set<Gene> genes = new HashSet<Gene>();
        genes.add(gene);
        return getSignaturePeptidesForGeneSet(genes, protease);
    }

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a set of genes.
     *
     * @param genes    the genes
     * @param protease the protease(s)
     * @return the peptides
     */
    public Set<PeptideFeature> getSignaturePeptidesForGeneSet(Set<Gene> genes, Set<Protease> protease) {

        Cache sessionCache = this.getSigPepSession().getCache();

        Set<PeptideFeature> retVal = new HashSet<PeptideFeature>();

        Set<String> proteaseShortNames = new HashSet<String>();

        for (Protease prot : protease) {
            proteaseShortNames.add(prot.getShortName());
        }

        String key = "signature_peptides_gene_level_" + proteaseShortNames;
        Set<Integer> signaturePeptideIds;

        try {
            signaturePeptideIds = (Set<Integer>) this.getSigPepSession().getCache().getFromCache(key);
        } catch (NeedsRefreshException e) {
            boolean updated = false;
            try {
                // Get the value (probably by calling an EJB)
                System.out.println("fetching signature peptide ids");
                signaturePeptideIds = simpleQueryDao.getSignaturePeptideIdsByProteaseShortNamesGeneLevel(proteaseShortNames);
                System.out.println("done");
                // Store in the cache
                sessionCache.putInCache(key, signaturePeptideIds);
                updated = true;
            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    sessionCache.cancelUpdate(key);
                }
            }
        }

        for (Gene gene : genes) {
            for (Protein protein : gene.getProteins()) {

                ProteinSequence s = protein.getSequence();

                for (PeptideFeature pf : s.getPeptides()) {

                    FeaturePeptide peptide = pf.getFeatureObject();
                    int id = ((Persistable) peptide).getId();
                    if (signaturePeptideIds.contains(new Integer(id))) {
                        retVal.add(pf);
                    }
                }
            }
        }

        return retVal;
    }


    /**
     * Returns a map of protease names and shortnames to protease IDs.
     *
     * @return a map of protease names and shortnames to protease IDs
     */
    public Map<String, Integer> getProteaseNameToIDMap(){
        return simpleQueryDao.getProteaseNameToProteaseIDMap();
    }

    /**
     * Returns the peptide length distribution of peptides generated by one or more proteases
     * from the protein sequences of an organism.
     *
     * @param protease the protease(s)
     * @return a map of sequence lengths and peptide frequencies
     */
    public Map<Integer, Integer> getPeptideLengthFrequencyForProteaseCombination(Protease... protease) {

        Set<String> proteaseShortNames = new HashSet<String>();
        
        for (Protease p : protease) {
            proteaseShortNames.add(p.getShortName());
        }

        return simpleQueryDao.getPeptideLengthFrequencyByProteaseShortName(proteaseShortNames);
    }
}
