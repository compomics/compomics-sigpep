package com.compomics.sigpep.impl;

import org.apache.log4j.Logger;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.PeptideGenerator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Mar-2008<br/>
 * Time: 09:56:42<br/>
 */
public class PeptideGeneratorImpl implements PeptideGenerator {

    /** the logger  */
    protected static Logger logger = Logger.getLogger(PeptideGeneratorImpl.class);
    /** the protein sequences of the organism */
    private Map<Integer, String> proteinSequences;
    /** the peptide features  */
    private Map<Integer, List<int[]>> peptideFeatures;
    /** the names of the proteases this peptide generator is for  */
    private Set<String> proteaseNames;
    /** the sequence ID to gene accession mapping  */
    private Map<Integer, Set<String>> sequenceIdToGeneAccessionMap;
    /** the sequence ID to protein accession mapping  */
    private Map<Integer, Set<String>> sequenceIdToProteinAccessionMap;
    /** the modifications that will be applied to peptide sequences  */
    private Set<Modification> modifications;

    /**
     * Constructs a PeptideGenerator instance for the specified set of proteases.
     *
     * @param proteaseNames the protease names
     */
    PeptideGeneratorImpl(Set<String> proteaseNames) {
        this.proteinSequences = new HashMap<Integer, String>();
        this.peptideFeatures = new HashMap<Integer, List<int[]>>();
        this.proteaseNames = proteaseNames;
        this.modifications = new HashSet<Modification>();
    }

    /**
     * Sets the protein sequence ID to protein accession mapping.
     *
     * @param sequenceIdToProteinAccessionMap the sequence ID to protein accession mapping
     */
    void setSequenceIdToProteinAccessionMap(Map<Integer, Set<String>> sequenceIdToProteinAccessionMap) {
        this.sequenceIdToProteinAccessionMap = sequenceIdToProteinAccessionMap;
    }

    /**
     * Sets the protein sequence ID to gene accession mapping.
     *
     * @param sequenceIdToGeneAccessionMap the protein sequence ID to gene accession mapping
     */
    void setSequenceIdToGeneAccessionMap(Map<Integer, Set<String>> sequenceIdToGeneAccessionMap) {
        this.sequenceIdToGeneAccessionMap = sequenceIdToGeneAccessionMap;
    }

    /**
     * Sets the protein sequences.
     *
     * @param proteinSequences a map of protein sequence IDs and protein sequences
     */
    void setProteinSequences(Map<Integer, String> proteinSequences) {
        this.proteinSequences = proteinSequences;
    }

    /**
     * Sets the peptide feature locations.
     *
     * @param peptideFeatures a map of protein sequence IDs and lists of 2 dimensional
     *                        arrays containing start and end positions of the peptide
     *                        features
     */
    void setPeptideFeatures(Map<Integer, List<int[]>> peptideFeatures) {
        this.peptideFeatures = peptideFeatures;
    }

    /**
     * Sets the proteases peptides are generated for.
     *
     * @param proteaseNames the protease names
     */
    public void setProteaseNames(Set<String> proteaseNames) {
        this.proteaseNames = proteaseNames;
    }

    /**
     * Adds a protein sequence.
     *
     * @param id        the protein sequence ID
     * @param sequence  the protein sequence string
     */
    void addProteinSequence(int id, String sequence) {
        this.proteinSequences.put(id, sequence);
    }

    /**
     * Adds a peptide feature location.
     *
     * @param proteinSequenceId the ID of the protein sequence containing the feature
     * @param start             the feature start position
     * @param end               the feature end position
     */
    void addPeptideFeature(int proteinSequenceId, int start, int end) {

        //create an empty list if none exists for the protein sequence yet
        if (!this.peptideFeatures.containsKey(proteinSequenceId)) {
            this.peptideFeatures.put(proteinSequenceId, new ArrayList<int[]>());
        }

        //create array new array of ints for start and end position
        int[] coordinates = new int[2];
        coordinates[0] = start;
        coordinates[1] = end;

        //add array to list
        this.peptideFeatures.get(proteinSequenceId).add(coordinates);

    }


    /**
     * Returns the names of the proteases peptides are generate for.
     *
     * @return the protease names
     */
    public Set<String> getProteaseNames() {
        return proteaseNames;
    }

    /**
     * Returns the post-translational modifications applied to
     * the peptide sequences.
     *
     * @return set of modifications
     */
    public Set<Modification> getPostTranslationalModifications() {
        return modifications;
    }

    /**
     * Sets the post-translational modifications applied to
     * the peptide sequences.
     *
     * @param modifications set of modifications
     */
    public void setPostTranslationalModifications(Set<Modification> modifications) {
        this.modifications = modifications;
    }

    /**
     * Sets the post-translational modifications applied to
     * the peptide sequences.
     *
     * @param modification one or more modifications
     */
    public void setPostTranslationalModification(Modification... modification) {

        Set<Modification> ptms = new LinkedHashSet<Modification>();
        Collections.addAll(ptms, modification);
        this.setPostTranslationalModifications(ptms);

    }

    /**
     * Returns a map of peptide sequence strings and their degeneracy
     * across the protein sequence space of the organism.
     *
     * @return a map of peptide sequences and their degeneracy
     */
    public Map<String, Integer> getPeptideSequenceDegeneracy() {

        //generate peptides and store degree
        Map<String, Integer> peptide2Degree = new HashMap<String, Integer>();
        for (Integer sequenceId : peptideFeatures.keySet()) {

            String sequence = proteinSequences.get(sequenceId);
            for (int[] coordinates : peptideFeatures.get(sequenceId)) {

                int start = coordinates[0];
                int end = coordinates[1];
                String peptideSequence = sequence.substring(start - 1, end);
                if (!peptide2Degree.containsKey(peptideSequence)) {
                    peptide2Degree.put(peptideSequence, 0);
                }
                int peptideDegree = peptide2Degree.get(peptideSequence);
                peptideDegree++;
                peptide2Degree.put(peptideSequence, peptideDegree);

            }

        }

        return peptide2Degree;

    }

    /**
     * Returns peptide sequences with the specified degeneracy across all protein
     * sequences of the organism.
     *
     * @param degeneracy peptide sequence degeneracy
     * @return set of peptide sequence strings
     */
    public Set<String> getPeptideSequencesByProteinSequenceLevelDegeneracy(int degeneracy) {

        Map<String, Integer> peptide2Degree = this.getPeptideSequenceDegeneracy();
        Set<String> retVal = new HashSet<String>();
        for (String peptide : peptide2Degree.keySet()) {
            int peptideDegree = peptide2Degree.get(peptide);
            if (peptideDegree == degeneracy) {
                retVal.add(peptide);
            }
        }
        return retVal;

    }

    /**
     * Returns peptide objects for peptide sequences with the specified degeneracy
     * across all protein sequences of the organism.
     *
     * @param degeneracy peptide sequence degeneracy
     * @return set of peptide objects
     */
    public Set<Peptide> getPeptidesByProteinLevelDegeneracy(int degeneracy) {

        return this.getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(null, degeneracy);

    }


    /**
     * Returns a map of protein accessions and sets of proteolytic peptides emitted by
     * the respective protein sequence that have the specified degeneracy.
     *
     * @param proteinAccessions the protein accessions
     * @param degeneracy the peptide sequence degeneracy
     * @return a map of protein accessions and sets of peptide sequence strings
     */
    public Map<String, Set<String>> getPeptideSequencesByProteinAccessionAndProteinSequenceLevelDegeneracy(Set<String> proteinAccessions, int degeneracy) {

        return getProteinAccessionToPeptideSequenceMap(proteinAccessions, degeneracy);

    }

    /**
     * Returns a map of all protein accessions for the organism and sets of proteolytic peptides emitted by
     * the respective protein sequence that have the specified degeneracy.
     *
     * @param degeneracy the peptide sequence degeneracy
     * @return a map of all protein accessions and sets of peptide sequence strings
     */
    public Map<String, Set<String>> getPeptideSequencesByProteinAccessionAndProteinSequenceLevelDegeneracy(int degeneracy) {

        return getProteinAccessionToPeptideSequenceMap(null, degeneracy);

    }

    /**
     * Returns a map of all protein acccessions for the organism and the proteolytic peptides emitted by
     * the respective protein sequences.
     *
     * @return a map of protein accessions and peptide sequence strings
     */
    public Map<String, Set<String>> getProteinAccessionToPeptideSequenceMap() {

        return getProteinAccessionToPeptideSequenceMap(null, -1);

    }

    /**
     * Returns the set of distinct proteolytic peptide sequences generated by a
     * digest of all protein sequences of the organism.
     *
     * @return set of peptide sequence strings
     */
    public Set<String> getPeptideSequences() {

        return this.getPeptideSequenceDegeneracy().keySet();

    }

    /**
     * Returns the set of distinct proteolytic peptide object (including post-translational
     * modifications) generated by a digest of all protein sequences of the organism.
     *
     * @return set of peptide objects
     */
    public Set<Peptide> getPeptides() {

        Map<String, Set<Peptide>> accession2PeptideMap = this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(null, -1);
        Set<Peptide> retVal = new HashSet<Peptide>();
        for (Set<Peptide> peptideSet : accession2PeptideMap.values()) {
            retVal.addAll(peptideSet);
        }
        return retVal;
    }

    /**
     * Returns all peptides generated by the set of proteaseFilter from a set proteins
     * which occur n = degree times in the digested proteome.
     *
     * @param proteinAccessions a set of proteins
     * @param degree            the number of proteome occurences of the returned peptides
     * @return a map of protein accessions and peptide sequences
     */
    private Map<String, Set<String>> getProteinAccessionToPeptideSequenceMap(Set<String> proteinAccessions, int degree) {

        Map<String, Set<Integer>> peptide2SequenceId = getPeptideSequenceToSequenceIdMap();
        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();
        for (String peptide : peptide2SequenceId.keySet()) {
            Set<Integer> sequenceIds = peptide2SequenceId.get(peptide);
            if (degree == -1 || sequenceIds.size() == degree) {

                for (Integer sequenceId : sequenceIds) {

                    Set<String> accessions = sequenceIdToProteinAccessionMap.get(sequenceId);

                    for (String accession : accessions) {

                        if (proteinAccessions == null || proteinAccessions.contains(accession)) {

                            if (!retVal.containsKey(accession)) {
                                retVal.put(accession, new HashSet<String>());
                            }
                            retVal.get(accession).add(peptide);

                        }
                    }
                }
            }

        }
        return retVal;

    }

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of all
     * protein sequences of the organism.
     *
     * @return a map of protein accessions and sets of peptide objects
     */
    public Map<String, Set<Peptide>> getProteinAccessionToPeptideMap() {
        return this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(null, -1);
    }

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of all
     * protein sequences of the organism with the specified degeneracy
     * across the protein sequence space of the organism.
     *
     * @param degeneracy peptide sequence degeneracy
     * @return a map of protein accessions and sets of peptide objects
     */
    public Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(int degeneracy) {
        return this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(null, degeneracy);
    }

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
    public Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(Set<String> proteinAccessions, int degeneracy) {

        if (proteinAccessions.size() == 0) {
            proteinAccessions = null;
        }

        return this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(proteinAccessions, degeneracy);

    }

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
    public Set<Peptide> getPeptidesByProteinAccessionAndProteinSequenceLevelDegeneracy(String proteinAccession, int degeneracy) {

        Set<String> accessions = new HashSet<String>();
        if (proteinAccession != null) {
            accessions.add(proteinAccession);
        } else {
            accessions = null;
        }

        Map<String, Set<Peptide>> accession2PeptideMap = this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(accessions, degeneracy);
        Set<Peptide> retVal = new HashSet<Peptide>();
        for (Set<Peptide> peptideSet : accession2PeptideMap.values()) {
            retVal.addAll(peptideSet);
        }
        return retVal;

    }

    /**
     * Returns the set of all proteolytic peptide object (including post-translational
     * modifications) generated by a digest of the protein sequence identified
     * by the accession.
     *
     * @param proteinAccession the protein accession
     * @return a set of peptides
     */
    public Set<Peptide> getPeptidesByProteinAccession(String proteinAccession) {

        Set<String> accessions = new HashSet<String>();
        accessions.add(proteinAccession);
        Map<String, Set<Peptide>> accession2PeptideMap = this.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(accessions, -1);
        Set<Peptide> retVal = new HashSet<Peptide>();
        for (Set<Peptide> peptideSet : accession2PeptideMap.values()) {
            retVal.addAll(peptideSet);
        }
        return retVal;

    }

    /**
     * Returns a map of protein accessions and sets of proteolytic peptide object
     * (including post-translational modifications) generated by a digest of the
     * protein sequences identified by the protein accessions.
     *
     * @param proteinAccessions the protein accessions
     * @return a map of protein accessions and sets of peptide objects
     */
    public Map<String, Set<Peptide>> getProteinAccessionToPeptideMap(Set<String> proteinAccessions) {
        return getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(proteinAccessions, -1);
    }




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
    public Map<String, Set<Peptide>> getPeptidesByGeneAccessionAndGeneLevelDegeneracy(Set<String> geneAccessions, int degeneracy) {

//        System.out.println("PeptideGeneratorImpl.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy");
//        System.out.println("proteinAccessions = " + proteinAccessions);
//        System.out.println("degree = " + degree);

        Map<String, Set<Peptide>> retVal = new HashMap<String, Set<Peptide>>();

        //get peptides generated by protease combination
        Map<String, Set<Integer>> peptide2SequenceId = getPeptideSequenceToSequenceIdMap();

        //for each peptide...
        for (String peptide : peptide2SequenceId.keySet()) {

            //...get the sequences that emit the peptide
            Set<Integer> sequenceIds = peptide2SequenceId.get(peptide);

            //get the genes that encode the sequences
            Set<String> genes = new HashSet<String>();
            for (Integer id : sequenceIds) {
                genes.addAll(this.sequenceIdToGeneAccessionMap.get(id));
            }

            //if we are interested in all genes or
            // the gene set encoding the peptide
            // emmitting sequence(s)...
            if (geneAccessions == null || genes.removeAll(geneAccessions)) {

                //...first restore the gene set changed by the if statement
                // again...
                genes = new HashSet<String>();
                for (Integer id : sequenceIds) {
                    genes.addAll(this.sequenceIdToGeneAccessionMap.get(id));
                }

                //if we don't care about the degree or
                // the degree matches the degree value...
                if (degeneracy == -1 || genes.size() == degeneracy) {

//                    System.out.println(peptide +  " emitted by " + genes + " has degree " + degree);

                    //...create the isoforms according to the post translational modifications
                    Set<Peptide> peptideIsoforms = createPeptideIsoforms(peptide, sequenceIds);
//                System.out.println("peptideIsoforms = " + peptideIsoforms);

                    for (Integer sequenceId : sequenceIds) {

                        for (String accession : sequenceIdToGeneAccessionMap.get(sequenceId)) {

                            if (geneAccessions == null || geneAccessions.contains(accession)) {

                                if (!retVal.containsKey(accession)) {
                                    retVal.put(accession, new HashSet<Peptide>());
                                }
                                retVal.get(accession).addAll(peptideIsoforms);
                            }
                        }
                    }

                }
            }
        }

        return retVal;

    }

    /**
     * Returns a map of peptide sequence strings and sets of accessions
     * of the proteins that emit the peptide sequence for all proteins
     * of the organism.
     *
     * @return a map of peptide sequences and sets protein accessions
     */
    public Map<String, Set<String>> getPeptideSequenceToProteinAccessionMap() {

        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();

        Map<String, Set<Integer>> peptideSequenceToSequenceIdMap = this.getPeptideSequenceToSequenceIdMap();
        for (String peptideSequence : peptideSequenceToSequenceIdMap.keySet()) {

            for (Integer sequenceId : peptideSequenceToSequenceIdMap.get(peptideSequence)) {
                Set<String> proteinAccessions = this.sequenceIdToProteinAccessionMap.get(sequenceId);
                if (!retVal.containsKey(peptideSequence)) {
                    retVal.put(peptideSequence, new HashSet<String>());
                }
                retVal.get(peptideSequence).addAll(proteinAccessions);
            }

        }

        return retVal;

    }

//    public Map<String, Set<Peptide>> getPeptidesByGeneAccessionSetAndGeneLevelDegreeOld(Set<String> geneAccessions, int degree) {
//
////        System.out.println("PeptideGeneratorImpl.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy");
////        System.out.println("proteinAccessions = " + proteinAccessions);
////        System.out.println("degree = " + degree);
//
//        Map<String, Set<Peptide>> retVal = new HashMap<String, Set<Peptide>>();
//
//        //get peptides generated by protease combination
//        Map<String, Set<Integer>> peptide2SequenceId = getPeptideSequenceToSequenceIdMap();
//
//        //for each peptide...
//        for (String peptide : peptide2SequenceId.keySet()) {
//
//            //...get the sequences that emit the peptide
//            Set<Integer> sequenceIds = peptide2SequenceId.get(peptide);
//
//            //get the genes that encode the sequences
//            Set<String> genes = new HashSet<String>();
//            for (Integer id : sequenceIds) {
//                genes.addAll(this.sequenceIdToGeneAccessionMap.get(id));
//            }
//
//            //if we don't care about the degree or
//            // the degree matches the degree value...
//            if (degree == -1 || genes.size() == degree) {
//
//                //...create the isoforms according to the post translational modifications
//                Set<Peptide> peptideIsoforms = createPeptideIsoforms(peptide, sequenceIds);
////                System.out.println("peptideIsoforms = " + peptideIsoforms);
//
//                for (Integer sequenceId : sequenceIds) {
//
//                    for (String accession : sequenceIdToGeneAccessionMap.get(sequenceId)) {
//
//                        if (geneAccessions == null || geneAccessions.contains(accession)) {
//
//                            if (!retVal.containsKey(accession)) {
//                                retVal.put(accession, new HashSet<Peptide>());
//                            }
//                            retVal.get(accession).addAll(peptideIsoforms);
//                        }
//                    }
//                }
//
//            }
//        }
//
//        return retVal;
//
//    }


    /**
     * Returns the peptides of the specified degeneracy, generated from a set of protein sequences
     * identified by the protein acessions passed as a parameter.
     *
     * @param proteinAccessions the accessions of the proteins the peptides are generated from
     * @param degeneracy        the sequence level degeneracy of the peptide sequences
     *                          if -1 peptides of all degeneracy levels will be returned
     * @return a map of protein accessions and sets of peptide objects
     */
    private Map<String, Set<Peptide>> getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy(Set<String> proteinAccessions, int degeneracy) {

//        System.out.println("PeptideGeneratorImpl.getPeptidesByProteinAccessionSetAndProteinLevelDegeneracy");
//        System.out.println("proteinAccessions = " + proteinAccessions);
//        System.out.println("degree = " + degree);

        Map<String, Set<Peptide>> retVal = new HashMap<String, Set<Peptide>>();

        //get peptides generated by protease combination
        Map<String, Set<Integer>> peptide2SequenceId = getPeptideSequenceToSequenceIdMap();

        //for each peptide...
        for (String peptide : peptide2SequenceId.keySet()) {

            //...get the sequences that emit the peptide
            Set<Integer> sequenceIds = peptide2SequenceId.get(peptide);

            //if we don't care about the degree or
            // the degree matches the degree value...
            if (degeneracy == -1 || sequenceIds.size() == degeneracy) {

                //...create the isoforms according to the post translational modifications
                Set<Peptide> peptideIsoforms = createPeptideIsoforms(peptide, sequenceIds);
//                System.out.println("peptideIsoforms = " + peptideIsoforms);

                for (Integer sequenceId : sequenceIds) {

                    for (String accession : sequenceIdToProteinAccessionMap.get(sequenceId)) {

                        if (proteinAccessions == null || proteinAccessions.contains(accession)) {

                            if (!retVal.containsKey(accession)) {
                                retVal.put(accession, new HashSet<Peptide>());
                            }
                            retVal.get(accession).addAll(peptideIsoforms);
                        }
                    }
                }

            }
        }

        return retVal;

    }

    /**
     * Applies post-translational modifications to the peptide sequence. As the context of
     * the peptide sequence is important for some modifications (e.g. modifications
     * occuring at the protein termini) the IDs of the protein sequences the peptide is
     * generated from have to be passed as a parameter as well.
     *
     * @param peptideSequence    the peptide sequences to modify
     * @param proteinSequenceIds the IDs of the protein sequences the peptide has been generated from
     * @return the modified peptides
     */
    private Set<Peptide> createPeptideIsoforms(String peptideSequence, Set<Integer> proteinSequenceIds) {

//        System.out.println("PeptideGeneratorImpl.createPeptideIsoforms");

        Set<Peptide> retVal = new HashSet<Peptide>();

        //create peptide object
        Peptide peptide = PeptideFactory.createPeptide(peptideSequence);

        //get peptide origins
        Set<PeptideOrigin> peptideOrigins = new HashSet<PeptideOrigin>();
        for (Integer sequenceId : proteinSequenceIds) {

            String sequence = this.proteinSequences.get(sequenceId);
            if (sequence.startsWith(peptideSequence)) {
                peptideOrigins.add(PeptideOrigin.N_TERMINAL);
            } else if (sequence.endsWith(sequence)) {
                peptideOrigins.add(PeptideOrigin.C_TERMINAL);
            } else {
                peptideOrigins.add(PeptideOrigin.INTERNAL);
            }

        }
        peptide.setOrigins(peptideOrigins);

        //apply post translational modifications
        Set<Peptide> modifiedPeptides = peptide.applyModifications(modifications);
//        System.out.println("modifications=" + modifications);

        retVal.addAll(modifiedPeptides);

        return retVal;

    }



    /**
     * Returns a map of peptide sequence strings and sets of accessions
     * of the genes that encode protein sequences that emit the respective
     * peptide sequence.
     *
     * @return a map of peptide sequences and sets of gene accessions
     */
    public Map<String, Set<String>> getPeptideSequenceToGeneAccessionMap() {

        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();

        Map<String, Set<Integer>> peptideSequenceToSequenceIdMap = this.getPeptideSequenceToSequenceIdMap();
        for (String peptideSequence : peptideSequenceToSequenceIdMap.keySet()) {

            for (Integer sequenceId : peptideSequenceToSequenceIdMap.get(peptideSequence)) {
                Set<String> geneAccessions = this.sequenceIdToGeneAccessionMap.get(sequenceId);
                retVal.put(peptideSequence, geneAccessions);
            }

        }

        return retVal;

    }

    /**
     * Returns the peptide sequence to protein sequence mapping.
     *
     * @return a map of peptide sequences and sets of protein sequence IDs
     */
    private Map<String, Set<Integer>> getPeptideSequenceToSequenceIdMap() {

        //generate peptides and store
        Map<String, Set<Integer>> peptide2SequenceId = new HashMap<String, Set<Integer>>();
        for (Integer sequenceId : peptideFeatures.keySet()) {

            String sequence = proteinSequences.get(sequenceId);
            for (int[] coordinates : peptideFeatures.get(sequenceId)) {

                int start = coordinates[0];
                int end = coordinates[1];
                String peptideSequence = sequence.substring(start - 1, end);
                if (!peptide2SequenceId.containsKey(peptideSequence)) {
                    peptide2SequenceId.put(peptideSequence, new HashSet<Integer>());
                }
                peptide2SequenceId.get(peptideSequence).add(sequenceId);

            }

        }

        return peptide2SequenceId;

    }


}
