package com.compomics.sigpep.persistence.dao.impl;

import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.impl.ProteaseImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.compomics.dbtools.SqlUtil;
import com.compomics.sigpep.persistence.dao.NamedQueryAccess;
import com.compomics.sigpep.persistence.dao.SimpleQueryDao;
import com.compomics.sigpep.persistence.dao.SimpleQueryDaoFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 29-May-2008<br/>
 * Time: 18:00:30<br/>
 */
public class SpringJdbcSimpleQueryDao extends JdbcDaoSupport implements SimpleQueryDao {

    protected static NamedQueryAccess namedQueries = NamedQueryAccess.getInstance();
    private static final String SQL_SELECT_PROTEIN_COUNT = namedQueries.getString("query.proteinCount");
    private static final String SQL_SELECT_GENE_COUNT = namedQueries.getString("query.geneCount");
    private static final String SQL_SELECT_SEQUENCE_COUNT = namedQueries.getString("query.sequenceCount");
    private static final String SQL_SELECT_PROTEASE_COUNT = namedQueries.getString("query.proteaseCount");
    private static final String SQL_SELECT_USED_PROTEASENAMES = namedQueries.getString("query.usedProteaseNames");
    private static final String SQL_SELECT_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORT_NAMES = namedQueries.getString("peptideSequencesByProteaseShortNames");
    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORT_NAMES = namedQueries.getString("peptideSequencesByProteaseShortNames");
    private static final String SQL_SELECT_SEQUENCE_IDS_AND_STRINGS = namedQueries.getString("query.sequenceIdsAndStrings");
    private static final String SQL_SELECT_SEQUENCE_ID_TO_PROTEIN_ACCESSION = namedQueries.getString("query.sequenceIdToProteinAccession");
    private static final String SQL_SELECT_PROTEIN_ACCESSION_TO_GENE_ACCESSION = namedQueries.getString("query.proteinAccessionToGeneAccession");
    private static final String SQL_SELECT_PEPTIDE_FEATURES_BY_PROTEASE_SHORT_NAME = namedQueries.getString("query.peptideFeaturesByProteaseShortName");
    private static final String SQL_SELECT_PEPTIDE_SEQUENCE_AND_SEQUENCE_ID_BY_PEPTIDE_SEQUENCE_AND_PROTEASE_SHORTNAME = namedQueries.getString("query.peptideSequenceAndSequenceIdByPeptideSequenceAndProteaseShortName");
    private static final String SQL_SELECT_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORTNAME = namedQueries.getString("query.peptideSequencesByProteaseShortName");
    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORTNAME = namedQueries.getString("query.signaturePeptideSequencesByProteaseShortName");
    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_IDS_BY_PROTEASE_SHORTNAME_PROTEIN_LEVEL = namedQueries.getString("query.signaturePeptideIdsByProteaseShortNameProteinLevel");
    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_IDS_BY_PROTEASE_SHORTNAME_GENE_LEVEL = namedQueries.getString("query.signaturePeptideIdsByProteaseShortNameGeneLevel");
    private static final String SQL_SELECT_LAST_PROTEIN_ID = namedQueries.getString("query.lastProteinId");
    private static final String SQL_SELECT_LAST_GENE_ID = namedQueries.getString("query.lastGeneId");
    private static final String SQL_SELECT_PROTEASE_IDS_AND_SHORTNAMES = namedQueries.getString("query.proteaseIds");
    private static final String SQL_SELECT_PROTEASE_NAMES_AND_IDS = namedQueries.getString("query.proteaseNameToIds");

    private static final String SQL_SELECT_GENE_IDS_AND_ACCESSIONS = namedQueries.getString("query.geneIds");
    private static final String SQL_SELECT_PROTEIN_IDS_AND_ACCESSIONS = namedQueries.getString("query.proteinIds");

    private static final String SQL_CREATE_TEMPORARY_TABLE_GENES_ALTERNATIVELY_SPLICED = namedQueries.getString("query.createTemporaryTableGenesAlternativelySpliced");
    private static final String SQL_INSERT_INTO_TEMPORARY_TABLE_GENES_ALTERNATIVELY_SPLICED = namedQueries.getString("query.insertIntoTemporaryTableGenesAlternativelySpliced");
    private static final String SQL_SELECT_PROTEIN_IDS_ALTERNATIVELY_SPLICED_GENES_TRANSCRIPT_LEVEL = namedQueries.getString("query.proteinIdsAlternativelySplicedGenesTranscriptLevel");

    private static final String SQL_SELECT_PEPTIDE_FEATURE_IDS_BY_PEPTIDE_ID_AND_SEQUENCE_ID = namedQueries.getString("query.peptideFeatureIdsByPeptideIdAndSequenceId");

    private static final String SQL_SELECT_ACCESSIONS_ALTERNATIVELY_SPLICED_GENES_TRANSCRIPT_LEVEL = namedQueries.getString("query.accessionsAlternativelySplicedGenesTranscriptLevel");
    private static final String SQL_SELECT_ACCESSIONS_ALTERNATIVELY_SPLICED_GENES_TRANSLATION_LEVEL = namedQueries.getString("query.accessionsAlternativelySplicedGenesTranslationLevel");
    private static final String SQL_SELECT_PEPTIDE_LENGTH_FREQUENCY_BY_PROTEASE_SHORTNAME = namedQueries.getString("query.peptideLengthFrequencyByProteaseShortNames");

    private static final String SQL_SELECT_GENE_ACCESSIONS = namedQueries.getString("query.geneAccessions");
    private static final String SQL_SELECT_PROTEIN_ACCESSIONS = namedQueries.getString("query.proteinAccessions");
    private static final String SQL_SELECT_PROTEIN_SEQUENCES = namedQueries.getString("query.proteinSequences");

    /**
     * Creates a new SpringJdbcSimpleQueryDao.
     */
    public SpringJdbcSimpleQueryDao() {
    }

    /**
     * Creates a new SpringJdbcSimpleQueryDao using the given data source.
     *
     * @param dataSource the data source to use
     */
    public SpringJdbcSimpleQueryDao(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * Returns the number of protein entries in the database.
     *
     * @return protein count
     */
    public int getProteinCount() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_PROTEIN_COUNT);
    }

    /**
     * Returns the number of gene entries in the database.
     *
     * @return gene count
     */
    public int getGeneCount() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_GENE_COUNT);
    }

    /**
     * Returns the number of protein sequence entries in the database.
     *
     * @return protein sequence count
     */
    public int getSequenceCount() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_SEQUENCE_COUNT);
    }

    /**
     * Returns the number of protease entries in the database.
     *
     * @return protease count
     */
    public int getProteaseCount() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_PROTEASE_COUNT);
    }

    /**
     * Returns the protease entries that have a protease2petide relation in the database
     *
     * @return protease name set
     */
    public Set<String> getUsedProteaseNames() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_USED_PROTEASENAMES, String.class));
        return retVal;
    }

    public Map<Integer, String> getSequenceIdsAndStrings() {

        return (Map<Integer, String>) this.getJdbcTemplate().query(
                SQL_SELECT_SEQUENCE_IDS_AND_STRINGS,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        Map<Integer, String> retVal = new HashMap<Integer, String>();
                        while (resultSet.next()) {
                            int sequenceId = resultSet.getInt(1);
                            String sequenceString = resultSet.getString(2);
                            retVal.put(sequenceId, sequenceString);
                        }
                        return retVal;
                    }
                }
        );
    }

    public Set<String> getPeptideSequencesByProteaseShortNames(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORT_NAMES, "proteaseNames", proteaseShortNames);
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, String.class));
        return retVal;
    }

    public Set<Integer> getSignaturePeptideIdsByProteaseShortNamesProteinLevel(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGNATURE_PEPTIDE_IDS_BY_PROTEASE_SHORTNAME_PROTEIN_LEVEL, "proteaseNames", proteaseShortNames);
        Set<Integer> retVal = new HashSet<Integer>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, Integer.class));
        return retVal;
    }

    public Set<Integer> getSignaturePeptideIdsByProteaseShortNamesGeneLevel(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGNATURE_PEPTIDE_IDS_BY_PROTEASE_SHORTNAME_GENE_LEVEL, "proteaseNames", proteaseShortNames);
        Set<Integer> retVal = new HashSet<Integer>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, Integer.class));
        return retVal;
    }

    public Set<String> getSignaturePeptideSequencesByProteaseShortNames(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGNATURE_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORT_NAMES, "proteaseNames", proteaseShortNames);
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, String.class));
        return retVal;
    }

    public Map<Integer, String> getSpeciesTaxonIdsAndNames() {
        return null;
    }

    /**
     * Returns a map of protein accessions to gene accessions.
     *
     * @return a map with protein accessions as key and gene accessions as value
     */
    public Map<String, String> getProteinAccessionToGeneAccessionMap() {

        return (Map<String, String>) this.getJdbcTemplate().query(
                SQL_SELECT_PROTEIN_ACCESSION_TO_GENE_ACCESSION,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        Map<String, String> retVal = new HashMap<String, String>();
                        while (resultSet.next()) {

                            String proteinAccession = resultSet.getString(1);
                            String geneAccession = resultSet.getString(2);
                            retVal.put(proteinAccession, geneAccession);

                        }
                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns a map of protein sequence IDs to protein accessions.
     *
     * @return a map of sequence IDS and protein accessions
     */
    public Map<Integer, Set<String>> getSequenceIdToProteinAccessionMap() {

        return (Map<Integer, Set<String>>) this.getJdbcTemplate().query(
                SQL_SELECT_SEQUENCE_ID_TO_PROTEIN_ACCESSION,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        Map<Integer, Set<String>> retVal = new HashMap<Integer, Set<String>>();
                        while (resultSet.next()) {

                            Integer sequenceId = resultSet.getInt(1);
                            String proteinAccession = resultSet.getString(2);
                            if (!retVal.containsKey(sequenceId)) {
                                retVal.put(sequenceId, new HashSet<String>());
                            }
                            retVal.get(sequenceId).add(proteinAccession);

                        }
                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns a map of sequence IDs and peptide feature coordinates for a set of proteases.
     *
     * @param proteaseShortNames a set of protease short names
     * @return map with sequence ID as key and array of coordinates (first element := start coordinate;
     *         second element := end coordinate) as value
     */
    public Map<Integer, List<int[]>> getPeptideFeatureCoordinatesByProteaseShortNames(Set<String> proteaseShortNames) {

        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_FEATURES_BY_PROTEASE_SHORT_NAME, "proteaseNames", proteaseShortNames);

        return (Map<Integer, List<int[]>>) this.getJdbcTemplate().query(
                sql,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        Map<Integer, List<int[]>> retVal = new HashMap<Integer, List<int[]>>();
                        while (resultSet.next()) {

                            Integer sequenceId = resultSet.getInt(1);
                            Integer start = resultSet.getInt(2);
                            Integer end = resultSet.getInt(3);


                            if (!retVal.containsKey(sequenceId)) {
                                retVal.put(sequenceId, new ArrayList<int[]>());
                            }
                            retVal.get(sequenceId).add(new int[]{start, end});

                        }

                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns a map of peptide sequences and IDs of the protein sequences they are generated by using the protease set
     * specified.
     *
     * @param peptideSequences   the peptide sequences
     * @param proteaseShortNames the generating proteases
     * @return a map of peptide sequences and sets of protein sequence IDs
     */
    public Map<String, Set<Integer>> getSequenceIdsByPeptideSequenceAndProteaseShortName(Set<String> peptideSequences, Set<String> proteaseShortNames) {

        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_SEQUENCE_AND_SEQUENCE_ID_BY_PEPTIDE_SEQUENCE_AND_PROTEASE_SHORTNAME,
                "proteaseNames", proteaseShortNames);

        sql = SqlUtil.setParameterSet(sql,
                "peptideSequences", peptideSequences);

        return (Map<String, Set<Integer>>) this.getJdbcTemplate().query(
                sql,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        Map<String, Set<Integer>> retVal = new HashMap<String, Set<Integer>>();
                        while (resultSet.next()) {

                            String peptideSequence = resultSet.getString(1);
                            Integer sequenceId = resultSet.getInt(2);

                            if (!retVal.containsKey(peptideSequence)) {
                                retVal.put(peptideSequence, new HashSet<Integer>());
                            }
                            retVal.get(peptideSequence).add(sequenceId);
                        }

                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns the sequences of peptides generated by a set of proteases.
     *
     * @param proteaseShortNames the protease shortnames
     * @return the peptide sequences
     */
    public Set<String> getPeptideSequencesByProteaseShortName(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORTNAME, "proteaseShortNames", proteaseShortNames);
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, String.class));
        return retVal;
    }


    /**
     * Returns the sequences of signature peptides generated by a set of proteases.
     *
     * @param proteaseShortNames the protease shortnames
     * @return the peptide sequences
     */
    public Set<String> getSignaturePeptideSequencesByProteaseShortName(Set<String> proteaseShortNames) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGNATURE_PEPTIDE_SEQUENCES_BY_PROTEASE_SHORTNAME, "proteaseNames", proteaseShortNames);
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, String.class));
        return retVal;
    }

    /**
     * Returns proteins that are the product of genes alternatively spliced on transcript level.
     *
     * @return a set of protein accessions
     */
    public Set<String> fetchAlternativelySplicedProteinsTranscriptLevel() {

        Set<String> retVal = new HashSet<String>();

        Connection con = null;
        Statement s = null;
        ResultSet rs = null;

        try {

            //get connection and statement
            con = this.getDataSource().getConnection();
            s = con.createStatement();

            //create temporary table
            s.execute(SQL_CREATE_TEMPORARY_TABLE_GENES_ALTERNATIVELY_SPLICED);

            //populate temporary table
            s.execute(SQL_INSERT_INTO_TEMPORARY_TABLE_GENES_ALTERNATIVELY_SPLICED);

            //execute query
            rs = s.executeQuery(SQL_SELECT_PROTEIN_IDS_ALTERNATIVELY_SPLICED_GENES_TRANSCRIPT_LEVEL);

            while (rs.next()) {
                String accession = rs.getString("protein_accession");
                retVal.add(accession);
            }

        } catch (SQLException e) {
            throw new DataRetrievalFailureException("Exception while querying SigPep database.", e);
        } finally {

            //clean up
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException se) {
                    //can't do anything useful here
                }
            }

            if (s != null) {
                try {
                    s.close();
                } catch (SQLException se) {
                    //can't do anything useful here
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException se) {
                    //can't do anything useful here
                }
            }
        }

        return retVal;

    }

    /**
     * Returns the primary key of the last protein entry.
     *
     * @return the entry ID
     */
    public int getLastProteinId() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_LAST_PROTEIN_ID);
    }

    /**
     * Returns the primary key of the last gene entry.
     *
     * @return the entry ID
     */
    public int getLastGeneId() {
        return this.getJdbcTemplate().queryForInt(SQL_SELECT_LAST_GENE_ID);
    }

    /**
     * Returns a map of protease IDs and protease shortnames.
     *
     * @return a map with protease IDs as key and protease short names as value
     */
    public Map<Integer, String> getProteaseIdToProteaseShortNameMap() {

        return (Map<Integer, String>) this.getJdbcTemplate().query(
                SQL_SELECT_PROTEASE_IDS_AND_SHORTNAMES,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        Map<Integer, String> retVal = new HashMap<Integer, String>();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("protease_id");
                            String name = resultSet.getString("name");

                            retVal.put(id, name);
                        }

                        return retVal;
                    }
                }
        );
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> getProteaseNameToProteaseIDMap() {
        return (Map<String, Integer>) this.getJdbcTemplate().query(
                SQL_SELECT_PROTEASE_NAMES_AND_IDS,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        Map<String, Integer> retVal = new HashMap<String, Integer>();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("protease_id");
                            String name = resultSet.getString("name");
                            String fullname = resultSet.getString("full_name");

                            retVal.put(name, id);
                            retVal.put(fullname, id);
                        }

                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns a map of gene IDs and gene accessions.
     *
     * @return a map with gene IDs as key and gene accessions as value
     */
    public Map<Integer, String> getGeneIdToGeneAccessionMap() {

        return (Map<Integer, String>) this.getJdbcTemplate().query(
                SQL_SELECT_GENE_IDS_AND_ACCESSIONS,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        Map<Integer, String> retVal = new HashMap<Integer, String>();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("gene_id");
                            String accession = resultSet.getString("gene_accession");
                            retVal.put(id, accession);
                        }

                        return retVal;
                    }
                }
        );
    }

    /**
     * Returns a map of protein IDs and protein accessions.
     *
     * @return a map with protein IDs as key and protein accessions value
     */
    public Map<Integer, String> getProteinIdsToProteinAccessionMap() {

        return (Map<Integer, String>) this.getJdbcTemplate().query(
                SQL_SELECT_PROTEIN_IDS_AND_ACCESSIONS,
                new ResultSetExtractor() {

                    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        Map<Integer, String> retVal = new HashMap<Integer, String>();
                        while (resultSet.next()) {

                            int id = resultSet.getInt("protein_id");
                            String name = resultSet.getString("protein_accession");
                            retVal.put(id, name);

                        }

                        return retVal;
                    }
                }
        );
    }

    public Set<Integer> getPeptideFeatureIdsByPeptideIdAndSequenceId(Set<Integer> peptideId, Set<Integer> sequenceId) {
        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_FEATURE_IDS_BY_PEPTIDE_ID_AND_SEQUENCE_ID, "peptideIds", peptideId);
        sql = SqlUtil.setParameterSet(sql, "sequenceIds", sequenceId);
        Set<Integer> retVal = new HashSet<Integer>();
        retVal.addAll(this.getJdbcTemplate().queryForList(sql, Integer.class));
        return retVal;
    }

    /**
     * Returns accessions of genes that are alternatively spliced on transcript level.
     *
     * @return a set of gene accessions
     */
    public Set<String> getAccessionsAlternativelySplicedGenesTranscriptLevel() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_ACCESSIONS_ALTERNATIVELY_SPLICED_GENES_TRANSCRIPT_LEVEL, String.class));
        return retVal;
    }

    /**
     * Returns accessions of genes that are alternatively spliced on transcript level.
     *
     * @return a set of gene accessions
     */
    public Set<String> getAccessionsAlternativelySplicedGenesTranslationLevel() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_ACCESSIONS_ALTERNATIVELY_SPLICED_GENES_TRANSLATION_LEVEL, String.class));
        return retVal;
    }

    /**
     * Returns the peptide length frequency for a set of proteases.
     *
     * @param proteaseShortNames the protease short names
     * @return a map of peptide lengths and absolute frequencies
     */
    public Map<Integer, Integer> getPeptideLengthFrequencyByProteaseShortName(Set<String> proteaseShortNames) {

        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_LENGTH_FREQUENCY_BY_PROTEASE_SHORTNAME, "proteaseShortNames", proteaseShortNames);
        return (Map<Integer, Integer>) this.getJdbcTemplate().query(sql, new ResultSetExtractor() {

            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                Map<Integer, Integer> retVal = new TreeMap<Integer, Integer>();

                while (resultSet.next()) {
                    int length = resultSet.getInt(1);
                    int frequency = resultSet.getInt(2);
                    retVal.put(length, frequency);
                }

                return retVal;
            }
        });
    }

    /**
     * @return
     */
    public Set<String> getGeneAccessions() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_GENE_ACCESSIONS, String.class));
        return retVal;
    }

    /**
     * @return
     */
    public Set<String> getProteinSequenceStrings() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_PROTEIN_SEQUENCES, String.class));
        return retVal;
    }

    /**
     * @return
     */
    public Set<String> getProteinAccessions() {
        Set<String> retVal = new HashSet<String>();
        retVal.addAll(this.getJdbcTemplate().queryForList(SQL_SELECT_PROTEIN_ACCESSIONS, String.class));
        return retVal;
    }

    //    public Map<String, Set<String>> getIdentifiableProteome(Set<String> peptideSequences, Set<String> proteaseNames) {
//
//        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();
//
//        String sql = namedQueries.getString("query.peptidesForProteaseSet");
//        sql = SqlUtil.setParameterSet(sql, "proteaseNames", proteaseNames);
//
//        Connection con = null;
//        Statement s = null;
//        ResultSet rs = null;
//
//        try {
//
//            //get connection and statement
//            if(connection == null){
//                con = sigPepDatabase.getConnection();
//            } else {
//                con = connection;
//            }
//            s = con.createStatement();
//
//            //execute query
//            rs = s.executeQuery(sql);
//            while (rs.next()) {
//                String peptideSequence = rs.getString("aa_sequence");
//                retVal.add(peptideSequence);
//            }
//
//
//        } catch (SQLException e) {
//            logger.error("Exception while fetching signautre proteins for proteases " + proteaseSet.toString() + " form SigPep database.", e);
//        } finally {
//
//            //clean up
//
//            if (con != null && connection == null) {
//                try {
//                    con.close();
//                } catch (SQLException se) {
//                    //can't do anything useful here
//                }
//            }
//
//            if (s != null) {
//                try {
//                    s.close();
//                } catch (SQLException se) {
//                    //can't do anything useful here
//                }
//            }
//
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException se) {
//                    //can't do anything useful here
//                }
//            }
//
//        }
//
//
//        return retVal;
//
//    }

//    public Map<String, Set<String>> getIdentifiableGenome(Set<String> peptideSequences, Set<String> proteaseNames) {
//
//        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();
//
//
//        return retVal;
//
//    }


//    public Map<String, Set<String>> fetchProteinAccessionsByPeptideSequence(Set<String> peptideSequences, Set<String> proteaseNames){
//
//        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();
//
//        PeptideGeneratorImpl generator = this.createPeptideGenerator(proteaseNames);
//        Map<String, Set<String>> proteinAccession2PeptideMap = generator.getProteinAccessionToPeptideSequenceMap();
//        for(String proteinAccession : proteinAccession2PeptideMap.keySet()){
//
//            Set<String> peptides = proteinAccession2PeptideMap.get(proteinAccession);
//            for(String peptide : peptides){
//                if(peptideSequences.contains(peptide)){
//
//                    if(!retVal.containsKey(proteinAccession)){
//                        retVal.put(proteinAccession, new HashSet<String>());
//                    }
//                    retVal.get(proteinAccession).add(peptide);
//
//                }
//            }
//
//        }
//
//        return retVal;
//
//    }

    /**
     * @param args
     * @TODO: JavaDoc missing
     */
    public static void main(String[] args) {

        SimpleQueryDao dao = SimpleQueryDaoFactory.getInstance().createSimpleQueryDao(9606);
        Set<String> peptides = new HashSet<String>();
        peptides.add("MIMAHCSSLLGSSDPPASTSQVAG");
        Set<String> proteases = new HashSet<String>();
        proteases.add("tryp");

        Map<String, Set<Integer>> result = dao.getSequenceIdsByPeptideSequenceAndProteaseShortName(peptides, proteases);
        for (String peptide : result.keySet()) {

            for (Integer sequenceId : result.get(peptide)) {
                System.out.println(peptide + " " + sequenceId);
            }
        }
    }
}
