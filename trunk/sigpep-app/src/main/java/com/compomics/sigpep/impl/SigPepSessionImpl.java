package com.compomics.sigpep.impl;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.compomics.sigpep.*;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.analysis.SignatureTransitionFinderFactory;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.persistence.dao.ObjectDao;
import com.compomics.sigpep.persistence.dao.SimpleQueryDao;

import java.util.*;

/**
 * SigPep session bean implementation.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 15:12:35<br/>
 */
public class SigPepSessionImpl implements SigPepSession {

    /**
     * the organism the session is for
     */
    private Organism organism;
    /**
     * the object cache
     */
    private Cache cache;
    /**
     * the simple query DAO
     */
    private SimpleQueryDao simpleQueryDao;
    /**
     * the object DAO
     */
    private ObjectDao objectDao;
    /**
     * the factory for signature transition finders
     */
    private SignatureTransitionFinderFactory signatureTransitionFinderFactory = SignatureTransitionFinderFactory.getInstance();
    /**
     * the application instance this session belongs to
     */
    private static SigPepApplication application = ApplicationLocator.getInstance().getApplication();

    /**
     * Constructor SigPepSessionImpl creates a new SigPepSessionImpl instance.
     */
    public SigPepSessionImpl() {
    }

    /**
     * Constructs a new SigPep session bean with the DAOs passed as parameters.
     *
     * @param simpleQueryDao the simple query DAO
     * @param objectDao      the object DAO
     */
    public SigPepSessionImpl(SimpleQueryDao simpleQueryDao, ObjectDao objectDao) {

        try {
            this.simpleQueryDao = simpleQueryDao;
            this.objectDao = objectDao;
            this.organism = objectDao.getOrganism();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a new SigPep session bean with the DAOs and cache passed as parameters.
     *
     * @param simpleQueryDao the simple query DAO
     * @param objectDao      the simple object DAO
     * @param cache          the object cache
     */
    public SigPepSessionImpl(SimpleQueryDao simpleQueryDao, ObjectDao objectDao, Cache cache) {

        try {
            this.simpleQueryDao = simpleQueryDao;
            this.objectDao = objectDao;
            this.organism = objectDao.getOrganism();
            this.cache = cache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the session organism.
     *
     * @return the organism
     */
    public Organism getOrganism() {
        return organism;
    }

    /**
     * Returns the session cache.
     *
     * @return the cache
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets the simple query DAO of this SigPep session bean.
     *
     * @param simpleQueryDao the simpleQueryDao of this SigPepSessionImpl object.
     */
    public void setSimpleQueryDao(SimpleQueryDao simpleQueryDao) {
        this.simpleQueryDao = simpleQueryDao;
    }

    /**
     * Returns the simple query DAO used by this session.
     *
     * @return the simple query DAO
     */
    public SimpleQueryDao getSimpleQueryDao() {
        return simpleQueryDao;
    }

    /**
     * Returns the object DAO used by this session.
     *
     * @return the object DAO
     */
    public ObjectDao getObjectDao() {
        return objectDao;
    }

    /**
     * Sets the object DAO of this SigPep session bean.
     *
     * @param objectDao the object DAO
     */
    public void setObjectDao(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param protease one or more proteases
     * @return the peptide generator
     */
    public PeptideGenerator createPeptideGenerator(Protease... protease) {
        Set<String> proteaseShortName = new HashSet<String>();
        for (Protease prot : protease) {
            proteaseShortName.add(prot.getShortName());
        }
        return createPeptideGenerator(proteaseShortName);
    }

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteases the proteases
     * @return the peptide generator
     */
    public PeptideGenerator createPeptideGeneratorForProteaseSet(Set<Protease> proteases) {
        Set<String> proteaseShortName = new HashSet<String>();
        for (Protease protease : proteases) {
            proteaseShortName.add(protease.getShortName());
        }
        return createPeptideGenerator(proteaseShortName);
    }

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteaseShortNames one or more protease short names
     * @return the peptide generator
     */
    public PeptideGenerator createPeptideGenerator(String... proteaseShortNames) {

        Set<String> proteases = new HashSet<String>();
        Collections.addAll(proteases, proteaseShortNames);
        return createPeptideGenerator(proteases);
    }

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteaseShortNames the protease short names
     * @return the peptide generator
     */
    public PeptideGenerator createPeptideGenerator(Set<String> proteaseShortNames) {

        //create peptide generator
        PeptideGeneratorImpl retVal = new PeptideGeneratorImpl(proteaseShortNames);

        //get protein sequences
        String proteinSequencesKey = "protein_sequences";
        Map<Integer, String> proteinSequences;

        try {

            //check if protein sequences are in the cache
            proteinSequences = (Map<Integer, String>) this.getCache().getFromCache(proteinSequencesKey);

        } catch (NeedsRefreshException e) {

            //if not...
            boolean updated = false;
            try {
                // get them from the database
                proteinSequences = simpleQueryDao.getSequenceIdsAndStrings();

                // store in the cache
                this.getCache().putInCache(proteinSequencesKey, proteinSequences);
                updated = true;

            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    this.getCache().cancelUpdate(proteinSequencesKey);
                }
            }
        }

        //get sequence ID-to-protein-accession map
        String sequenceId2ProteinAccessionMapKey = "sequence_id_2_protein_accession_map";
        Map<Integer, Set<String>> sequenceId2ProteinAccessionMap;

        try {

            //check if map is in cache
            sequenceId2ProteinAccessionMap = (Map<Integer, Set<String>>) this.getCache().getFromCache(sequenceId2ProteinAccessionMapKey);
        } catch (NeedsRefreshException e) {

            //if not...
            boolean updated = false;
            try {
                // get it from database
                sequenceId2ProteinAccessionMap = simpleQueryDao.getSequenceIdToProteinAccessionMap();

                // store in the cache
                this.getCache().putInCache(sequenceId2ProteinAccessionMapKey, sequenceId2ProteinAccessionMap);
                updated = true;
            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    this.getCache().cancelUpdate(sequenceId2ProteinAccessionMapKey);
                }
            }
        }

        //create sequence ID-to-gene-accession map
        String sequenceId2GeneAccessionMapMapKey = "sequence_id_2_gene_accession_map";
        Map<Integer, Set<String>> sequenceId2GeneAccessionMap;

        try {
            //check if map is in cache
            sequenceId2GeneAccessionMap = (Map<Integer, Set<String>>) this.getCache().getFromCache(sequenceId2GeneAccessionMapMapKey);
        } catch (NeedsRefreshException e) {

            //if not get
            boolean updated = false;
            try {
                // Get the value (probably by calling an EJB)
                sequenceId2GeneAccessionMap = new HashMap<Integer, Set<String>>();

                Map<String, String> proteinAccessionToGeneAccesssionMap = simpleQueryDao.getProteinAccessionToGeneAccessionMap();
                for (Integer sequenceId : sequenceId2ProteinAccessionMap.keySet()) {

                    Set<String> geneAccessions = new HashSet<String>();
                    for (String proteinAccession : sequenceId2ProteinAccessionMap.get(sequenceId)) {

                        String geneAccession = proteinAccessionToGeneAccesssionMap.get(proteinAccession);
                        geneAccessions.add(geneAccession);

                    }
                    sequenceId2GeneAccessionMap.put(sequenceId, geneAccessions);
                }

                // Store in the cache
                this.getCache().putInCache(sequenceId2GeneAccessionMapMapKey, sequenceId2GeneAccessionMap);
                updated = true;
            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    this.getCache().cancelUpdate(sequenceId2GeneAccessionMapMapKey);
                }
            }
        }

        //get peptide featureCoordinates

        String featureCoordinatesKey = "feature_Coordinates_" + proteaseShortNames;
        Map<Integer, List<int[]>> featureCoordinates;

        try {
            featureCoordinates = (Map<Integer, List<int[]>>) this.getCache().getFromCache(featureCoordinatesKey);
        } catch (NeedsRefreshException e) {
            boolean updated = false;
            try {
                // Get the value (probably by calling an EJB)
                featureCoordinates = simpleQueryDao.getPeptideFeatureCoordinatesByProteaseShortNames(proteaseShortNames);
//                featureCoordinates = simpleQueryDao.getPeptideFeatureCoordinatesByProteaseShortNames(proteaseShortNames);

                // Store in the cache
                this.getCache().putInCache(featureCoordinatesKey, featureCoordinates);
                updated = true;

            } finally {
                if (!updated) {
                    // It is essential that cancelUpdate is called if the
                    // cached content could not be rebuilt
                    this.getCache().cancelUpdate(featureCoordinatesKey);
                }
            }
        }

        //set retVal properties
        retVal.setSequenceIdToProteinAccessionMap(sequenceId2ProteinAccessionMap);
        retVal.setProteinSequences(proteinSequences);
        retVal.setSequenceIdToGeneAccessionMap(sequenceId2GeneAccessionMap);
        retVal.setPeptideFeatures(featureCoordinates);

        return retVal;
    }

    /**
     * Creates a service to query the SigPep database.
     *
     * @return a SigPep query service
     */
    public SigPepQueryService createSigPepQueryService() {
        return new SigPepQueryServiceImpl(this);
    }

    /**
     * Returns a signature transition finder.
     *
     * @param backgroundPeptides        the background to search the signature transition against
     * @param targetProductIonTypes     the product ion types of the target peptide to take into account
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account
     * @param precursorIonChargeStates  the allowed precursor ion charges
     * @param productIonChargeStates    the allowed product ion charges
     * @param massAccuracy              the mass accuracy
     * @param minimumCombinationSize    the minimum number of product ions a signature transition has to consist of
     * @param maximumCombinationSize    the minimum number of product ions a signature transition is allowed to consist of
     * @param type                      the type of signature transition finder
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createSignatureTransitionFinder(
            Set<Peptide> backgroundPeptides,
            Set<ProductIonType> targetProductIonTypes,
            Set<ProductIonType> backgroundProductIonTypes,
            Set<Integer> precursorIonChargeStates,
            Set<Integer> productIonChargeStates,
            double massAccuracy,
            int minimumCombinationSize,
            int maximumCombinationSize,
            SignatureTransitionFinderType type) {

        SignatureTransitionFinder retVal = null;

        switch (type) {

            case FIRST:
                retVal = signatureTransitionFinderFactory.createFindFirstSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        precursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;

            case MINIMAL:
                retVal = signatureTransitionFinderFactory.createFindMinimalSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        precursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;

            case ALL:
                retVal = signatureTransitionFinderFactory.createFindAllSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        precursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;
        }

        return retVal;
    }

    /**
     * Returns a signature transition finder.
     *
     * @param backgroundPeptides        the background to search the signature transition against
     * @param targetProductIonTypes     the product ion types of the target peptide to take into account
     * @param backgroundProductIonTypes the product ion types of the background peptides to take into account
     * @param observedPrecursorIonChargeStates
     *                                  observed pairs of peptide length and ion charges
     * @param productIonChargeStates    the allowed product ion charges
     * @param massAccuracy              the mass accuracy
     * @param minimumCombinationSize    the minimum number of product ions a signature transition has to consist of
     * @param maximumCombinationSize    the minimum number of product ions a signature transition is allowed to consist of
     * @param type                      the type of signature transition finder
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                     Set<ProductIonType> targetProductIonTypes,
                                                                     Set<ProductIonType> backgroundProductIonTypes,
                                                                     List<Map<Double, Integer>> observedPrecursorIonChargeStates,
                                                                     Set<Integer> productIonChargeStates,
                                                                     double massAccuracy,
                                                                     int minimumCombinationSize,
                                                                     int maximumCombinationSize,
                                                                     SignatureTransitionFinderType type) {

        SignatureTransitionFinder retVal = null;

        switch (type) {

            case FIRST:
                retVal = signatureTransitionFinderFactory.createFindFirstSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        observedPrecursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;

            case MINIMAL:
                retVal = signatureTransitionFinderFactory.createFindMinimalSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        observedPrecursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;

            case ALL:
                retVal = signatureTransitionFinderFactory.createFindAllSignatureTransitionFinder(backgroundPeptides,
                        targetProductIonTypes,
                        backgroundProductIonTypes,
                        observedPrecursorIonChargeStates,
                        productIonChargeStates,
                        massAccuracy,
                        minimumCombinationSize,
                        maximumCombinationSize);
                break;
        }

        return retVal;
    }

    /**
     * Returns a signature transition finder with default settings.
     *
     * @param backgroundPeptides the background to search the signature transition against
     * @param type               the type of signature transition finder
     * @return a signature transition finder
     */
    public SignatureTransitionFinder createSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                                     SignatureTransitionFinderType type) {

        return createSignatureTransitionFinder(backgroundPeptides,
                application.getDefaultTargetProductIonTypes(),
                application.getDefaultBackgroundProductIonTypes(),
                application.getDefaultPrecursorIonChargeStates(),
                application.getDefaultProductIonChargeStates(),
                application.getDefaultMassAccuracy(),
                application.getDefaultMinimumSignatureTransitionSize(),
                application.getDefaultMaximumSignatureTransitionSize(),
                type);
    }
}
