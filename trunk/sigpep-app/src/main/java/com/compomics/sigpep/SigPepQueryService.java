package com.compomics.sigpep;

import com.compomics.sigpep.model.*;

import java.util.Map;
import java.util.Set;

/**
 * Provides methods to query the SigPep database.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 04-Jul-2008<br/>
 * Time: 18:09:08<br/>
 */
public interface SigPepQueryService {

    /**
     * Returns the SigPep session this service is attached to.
     *
     * @return the SigPep session
     */
    SigPepSession getSigPepSession();

    /**
     * Sets the SigPep session this service is attached to.
     *
     * @param sigPepSession the SigPep session
     */
    void setSigPepSession(SigPepSession sigPepSession);

    ///////////////////
    // Organism queries
    ///////////////////

    /**
     * Returns the organism the query service is for.
     *
     * @return the organism
     */
    Organism getOrganism();


    ///////////////////
    // Protease queries
    ///////////////////

    /**
     * Returns all proteases available for queries.
     *
     * @return a set of proteases
     */
    Set<Protease> getAllProteases();

    /**
     * Returns the protease specified by short name.
     *
     * @param shortName the protease short name
     * @return the protease
     */
    Protease getProteaseByShortName(String shortName);

    /**
     * Returns the protease specified by full name.
     *
     * @param fullName the protease full name
     * @return the protease
     */
    Protease getProteaseByFullName(String fullName);

    /**
     * Returns a set of one or more proteases specified by its/their shortname(s).
     *
     * @param shortName the protease short name(s)
     * @return a set of proteases
     */
    Set<Protease> getProteaseSetByShortName(String... shortName);

    /**
     * Returns a set of one or more proteases specified by its/their shortname(s).
     *
     * @param shortName a set of protease short name(s)
     * @return a set of proteases
     */
    Set<Protease> getProteaseSetByShortName(Set<String> shortName);


    ///////////////
    // Gene queries
    ///////////////

    /**
     * Returns the number of gene entries in the database
     *
     * @return the gene entry count
     */
    int getGeneCount();

    /**
     * Returns all gene entries in the database.
     *
     * @return the gene entries
     */
    Set<Gene> getAllGenes();

    /**
     * Returns the accessions of all gene entries in the database.
     *
     * @return the accessions
     */
    Set<String> getGeneAccessions();

    /**
     * Returns the gene specified by the accession number.
     *
     * @param accession the accession number
     * @return the gene
     */
    Gene getGeneByAccession(String accession);

    /**
     * Returns the genes specified by one or more accession numbers.
     *
     * @param accession the accession numbers
     * @return the gene
     */
    Set<Gene> getGeneSetByAccession(String... accession);

    /**
     * Returns the genes specified by a set of accession numbers.
     *
     * @param accessions the accession numbers
     * @return the gene
     */
    Set<Gene> getGeneSetByAccession(Set<String> accessions);

    /**
     * Returns all genes with more than one transcript (a gene might have more than
     * one transcript but still only one translation as the transcripts only differ
     * in the UTRs).
     *
     * @return the genes
     */
    Set<Gene> getGenesWithAlternativeTranscripts();

    /**
     * Returns all genes with more than one translation (a gene might have more than
     * one transcript but still only one translation as the transcripts only differ
     * in the UTRs).
     *
     * @return the genes
     */
    Set<Gene> getGenesWithAlternativeTranslations();


    /////////////////
    //Protein queries
    /////////////////

    /**
     * Returns the number of protein entries in the database.
     *
     * @return the protein entry count
     */
    int getProteinCount();

    /**
     * Returns the accessions of all protein entries in the database.
     *
     * @return the accessions
     */
    Set<String> getProteinAccessions();

    /**
     * Returns the protein specified by the accession number.
     *
     * @param accession the accession number
     * @return the protein
     */
    Protein getProteinByAccession(String accession);

    /**
     * Returns the proteins specified by one or more accession numbers.
     *
     * @param accession the accession numbers
     * @return the gene
     */
    Set<Protein> getProteinSetByAccession(String... accession);

    /**
     * Returns the proteins specified by a set of accession numbers.
     *
     * @param accessions the accession numbers
     * @return the gene
     */
    Set<Protein> getProteinSetByAccession(Set<String> accessions);


    //////////////////
    //Sequence queries
    //////////////////

    /**
     * Returns all protein sequences in the database.
     *
     * @return the protein sequences
     */
    Set<String> getProteinSequences();


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
    Set<String> getPeptideSequencesForProtease(Protease... protease);

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from all protein sequences of an organism.
     *
     * @param protease the protease(s)
     * @return a set of peptide sequence strings
     */
    Set<String> getSignaturePeptideSequencesForProtease(Protease... protease);

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequence of a protein.
     *
     * @param protein  the protein
     * @param protease the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptides(Protein protein, Protease... protease);

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequences of a set of protein.
     *
     * @param proteins  the protein set
     * @param proteases the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptidesForProteinSet(Set<Protein> proteins, Set<Protease> proteases);

    /**
     * Returns all non-degenerate peptides generated by one or more proteases
     * from the sequences of a set of protein.
     *
     * @param proteins the protein set
     * @param protease the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptidesForProteinSet(Set<Protein> proteins, Protease... protease);

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a gene.
     *
     * @param gene     the gene
     * @param protease the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptides(Gene gene, Protease... protease);

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a set of genes.
     *
     * @param genes    the genes
     * @param protease the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptidesForGeneSet(Set<Gene> genes, Set<Protease> protease);

    /**
     * Returns all peptides non-degenerate on gene level generated by one or more proteases
     * from the protein sequences encoded by a set of genes.
     *
     * @param genes    the genes
     * @param protease the protease(s)
     * @return the peptides
     */
    Set<PeptideFeature> getSignaturePeptidesForGeneSet(Set<Gene> genes, Protease... protease);

    /**
     * Returns the peptide length distribution of peptides generated by one or more proteases
     * from the protein sequences of an organism.
     *
     * @param protease the protease(s)
     * @return a map of sequence lengths and peptide frequencies
     */
    Map<Integer, Integer> getPeptideLengthFrequencyForProteaseCombination(Protease... protease);

    /**
     * Returns a set of protein accession strings for a given sequenceId set
     *
     * @param sequenceIds
     * @return a set of protein accession strings
     */
    Set<String> getProteinAccessionsBySequenceIds(Set<Integer> sequenceIds);
}
