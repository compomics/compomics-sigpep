package com.compomics.sigpep;

import com.compomics.sigpep.model.Modification;
import com.compomics.sigpep.model.Peptide;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 23-Jun-2008<br/>
 * Time: 09:51:11<br/>
 */
public interface PeptideGenerator {

    /**
     * Sets the proteases peptides are generated for.
     *
     * @param proteaseNames the protease names
     */
    void setProteaseNames(Set<String> proteaseNames);

    /**
     * Returns the names of the proteases peptides are generate for.
     *
     * @return the protease names
     */
    Set<String> getProteaseNames();

    /**
     * Returns the post-translational modifications applied to
     * the peptide sequences.
     *
     * @return set of modifications
     */
    Set<Modification> getPostTranslationalModifications();

    /**
     * Sets the post-translational modifications applied to
     * the peptide sequences.
     *
     * @param modifications set of modifications
     */
    void setPostTranslationalModifications(Set<Modification> modifications);

    /**
     * Sets the post-translational modifications applied to
     * the peptide sequences.
     *
     * @param modification one or more modifications
     */
    void setPostTranslationalModification(Modification... modification);

    /**
     * Returns a map of peptide sequence strings and their degeneracy
     * across the protein sequence space of the organism.
     *
     * @return a map of peptide sequences and their degeneracy
     */
    Map<String, Integer> getPeptideSequenceDegeneracy();

    /**
     * Returns peptide sequences with the specified degeneracy across all protein
     * sequences of the organism.
     *
     * @param degeneracy peptide sequence degeneracy
     * @return set of peptide sequence strings
     */
    Set<String> getPeptideSequencesByProteinSequenceLevelDegeneracy(int degeneracy);

    /**
     * Returns peptide objects for peptide sequences with the specified degeneracy
     * across all protein sequences of the organism.
     * 
     * @param degeneracy peptide sequence degeneracy
     * @return set of peptide objects
     */
    Set<Peptide> getPeptidesByProteinLevelDegeneracy(int degeneracy);

    /**
     * Returns a map of protein accessions and sets of proteolytic peptides emitted by
     * the respective protein sequence that have the specified degeneracy.
     *
     * @param proteinAccessions the protein accessions
     * @param degeneracy the peptide sequence degeneracy
     * @return a map of protein accessions and sets of peptide sequence strings
     */
    Map<String, Set<String>> getPeptideSequencesByProteinAccessionAndProteinSequenceLevelDegeneracy(Set<String> proteinAccessions, int degeneracy);

    /**
     * Returns a map of all protein accessions for the organism and sets of proteolytic peptides emitted by
     * the respective protein sequence that have the specified degeneracy.
     * 
     * @param degeneracy the peptide sequence degeneracy
     * @return a map of all protein accessions and sets of peptide sequence strings
     */
    Map<String, Set<String>> getPeptideSequencesByProteinAccessionAndProteinSequenceLevelDegeneracy(int degeneracy);

    /**
     * Returns a map of all protein acccessions for the organism and the proteolytic peptides emitted by
     * the respective protein sequences.
     *
     * @return a map of protein accessions and peptide sequence strings
     */
    Map<String, Set<String>> getProteinAccessionToPeptideSequenceMap();

    /**
     * Returns the set of distinct proteolytic peptide sequences generated by a
     * digest of all protein sequences of the organism.
     *
     * @return set of peptide sequence strings
     */
    Set<String> getPeptideSequences();

    /**
     * Returns the set of distinct proteolytic peptide object (including post-translational
     * modifications) generated by a digest of all protein sequences of the organism.
     *
     * @return set of peptide objects
     */
    Set<Peptide> getPeptides();

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of all
     * protein sequences of the organism.
     *
     * @return a map of protein accessions and sets of peptide objects
     */
    Map<String, Set<Peptide>> getProteinAccessionToPeptideMap();

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of all
     * protein sequences of the organism with the specified degeneracy
     * across the protein sequence space of the organism.
     *
     * @param degeneracy peptide sequence degeneracy
     * @return a map of protein accessions and sets of peptide objects
     */
    Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(int degeneracy);

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of the
     * protein sequences identified by the protein accessions.
     *
     * @param proteinAccessions the protein accessions
     * @return a map of protein accessions and sets of peptide objects
     */
    Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(Set<String> proteinAccessions);

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of the
     * protein sequences identified by the protein accessions with the specified
     * degeneracy across the protein sequence space of the organism.
     *
     * @param proteinAccessions the protein accessions
     * @param degeneracy the peptide degeneracy
     * @return a map of protein accessions and sets of peptide objects
     */
    Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(Set<String> proteinAccessions, int degeneracy);

    /**
     * Returns a set of proteolytic peptide object  (including post-translational
     * modifications) generated by a digest of the protein sequence identified
     * by the accession with the specified degeneracy across the protein sequence
     * space of the organism.
     *
     * @param proteinAccession the protein accession
     * @param degeneracy the peptide degeneracy
     * @return a set of peptide objects
     */
    Set<Peptide> getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(String proteinAccession, int degeneracy);

    /**
     * Returns the set of all proteolytic peptide object (including post-translational
     * modifications) generated by a digest of the protein sequence identified
     * by the accession.
     *
     * @param proteinAccession the protein accession
     * @return a set of peptides
     */
    Set<Peptide> getPeptidesByProteinAccession(String proteinAccession);



    /**
     * Returns a map of peptide sequence strings and sets of accessions
     * of the proteins that emit the peptide sequence for all proteins
     * of the organism.
     *
     * @return a map of peptide sequences and sets protein accessions
     */
    Map<String, Set<String>> getPeptideSequenceToProteinAccessionMap();

    /**
     * Returns a map of peptide sequence strings and sets of accessions
     * of the genes that encode protein sequences that emit the respective
     * peptide sequence.
     *
     * @return a map of peptide sequences and sets of gene accessions
     */
    Map<String, Set<String>> getPeptideSequenceToGeneAccessionMap();

    /**
     * Returns a map of gene accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of the
     * protein sequences encoded by the genes identified by the gene accessions
     * with the specified degeneracy across the protein sequence space of the
     * organism.
     *
     * @param geneAccessions the gene accessions
     * @param degeneracy the peptide degeneracy
     * @return a map of gene accessions and sets of peptide sequence objects
     */
    Map<String, Set<Peptide>> getPeptidesByGeneAccessionAndGeneLevelDegeneracy(Set<String> geneAccessions, int degeneracy);

}
