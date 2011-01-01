package com.compomics.sigpep;

import com.opensymphony.oscache.base.Cache;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.ProductIonType;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.persistence.dao.ObjectDao;
import com.compomics.sigpep.persistence.dao.SimpleQueryDao;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 15:02:43<br/>
 */
public interface SigPepSession {

    /**
     * Returns the session organism.
     *
     * @return the organism
     */
    Organism getOrganism();

    /**
     * Returns the object DAO used by this session.
     *
     * @return the object DAO
     */
    ObjectDao getObjectDao();

    /**
     * Returns the simple query DAO used by this session.
     *
     * @return the simple query DAO
     */
    SimpleQueryDao getSimpleQueryDao();

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param protease one or more proteases
     * @return the peptide generator
     */
    PeptideGenerator createPeptideGenerator(Protease... protease);

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteaseShortNames one or more protease short names
     * @return the peptide generator
     */
    PeptideGenerator createPeptideGenerator(String... proteaseShortNames);

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteases the proteases
     * @return the peptide generator
     */
    PeptideGenerator createPeptideGeneratorForProteaseSet(Set<Protease> proteases);

    /**
     * Creates a peptide generator for a specified set of proteases.
     *
     * @param proteaseShortNames the protease short names
     * @return the peptide generator
     */
    PeptideGenerator createPeptideGenerator(Set<String> proteaseShortNames);

    /**
     * Creates a service to query the SigPep database.
     *
     * @return a SigPep query service
     */
    SigPepQueryService createSigPepQueryService();

    /**
     * Returns the session cache.
     *
     * @return the cache
     */
    Cache getCache();

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
    SignatureTransitionFinder createSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                              Set<ProductIonType> targetProductIonTypes,
                                                              Set<ProductIonType> backgroundProductIonTypes,
                                                              Set<Integer> precursorIonChargeStates,
                                                              Set<Integer> productIonChargeStates,
                                                              double massAccuracy,
                                                              int minimumCombinationSize,
                                                              int maximumCombinationSize,
                                                              SignatureTransitionFinderType type);

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
    SignatureTransitionFinder createSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                              Set<ProductIonType> targetProductIonTypes,
                                                              Set<ProductIonType> backgroundProductIonTypes,
                                                              List<Map<Double, Integer>> observedPrecursorIonChargeStates,
                                                              Set<Integer> productIonChargeStates,
                                                              double massAccuracy,
                                                              int minimumCombinationSize,
                                                              int maximumCombinationSize,
                                                              SignatureTransitionFinderType type);

    /**
     * Returns a signature transition finder with default settings.
     *
     * @param backgroundPeptides the background to search the signature transition against
     * @param type               the type of signature transition finder
     * @return a signature transition finder
     */
    SignatureTransitionFinder createSignatureTransitionFinder(Set<Peptide> backgroundPeptides,
                                                              SignatureTransitionFinderType type);
}
