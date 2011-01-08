package com.compomics.sigpep.persistence.rdbms;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import com.compomics.dbtools.*;
import com.compomics.ensh.Ensh;
import com.compomics.ensh.core.model.Exon;
import com.compomics.ensh.core.model.Translation;
import com.compomics.ensh.exception.EnshException;
import com.compomics.sigpep.model.constants.Organisms;
import com.compomics.sigpep.persistence.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Provides access to the SigPep database schema for a specified species.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 07-Sep-2007
 * Time: 16:18:42
 */
public class SigPepDatabase extends MySqlDatabase {
    private static Configuration configuration = Configuration.getInstance();
    private static Logger logger = Logger.getLogger(SigPepDatabase.class);
    private int ncbiTaxonId;
    private static Organisms organisms = Organisms.getInstance();
    private static final int ERROR_CODE_TOO_MANY_CONNECTIONS = 1040;

    /**
     * Creates a Database object to access the SigPep schema for the species specified by the NCBI Taxon ID.
     *
     * @param ncbiTaxonId NCBI taxon ID of the species
     * @throws DatabaseException if an exception occurs while communicating with the database
     */
    public SigPepDatabase(int ncbiTaxonId) throws DatabaseException {
        super(configuration.getString("sigpep.db.host"),
                configuration.getInt("sigpep.db.port"),
                SigPepDatabase.getSchemaName(ncbiTaxonId),
                configuration.getString("sigpep.db.username"),
                configuration.getString("sigpep.db.password").toCharArray());
        this.ncbiTaxonId = ncbiTaxonId;
    }

    /**
     * Returns the SigPep schema name for the species identified by the NCBI taxon ID.
     *
     * @param ncbiTaxonId the NCBI taxon ID
     * @return the SigPep schema name
     */
    public static String getSchemaName(int ncbiTaxonId) {
        if (organisms.contains(ncbiTaxonId) && ncbiTaxonId != 0) {
            return configuration.getString("sigpep.db.schema.prefix") + "_" + getSpeciesSuffix(ncbiTaxonId);
        } else if (ncbiTaxonId == 0) {
            return Configuration.getInstance().getString("sigpep.db.default.schema");
        } else {
            throw new NoSuchElementException("Species identified by NCBI taxon ID " + ncbiTaxonId + " not in SigPep database.");
        }
    }

    /**
     * Returns the SigPep schema postfix for the species identified by the NCBI taxon ID.
     *
     * @param ncbiTaxonId NCBI taxon ID of the SigPep species
     * @return the SigPep schema postfix
     */
    public static String getSpeciesSuffix(int ncbiTaxonId) {
        if (organisms.contains((ncbiTaxonId))) {
            return organisms.getSpeciesName(ncbiTaxonId).replace(" ", "_");
        } else {
            return "";
        }
    }

    /**
     * Creates a Database object to access the SigPep schema for the species specified by the NCBI Taxon ID.
     *
     * @param username    username for the database connection
     * @param password    password for the database connection
     * @param ncbiTaxonId NCBI taxon ID of the species
     * @throws DatabaseException if an exception occurs while communicating with the database
     */
    public SigPepDatabase(String username, char[] password, int ncbiTaxonId) throws DatabaseException {
        super(configuration.getString("sigpep.db.host"),
                configuration.getInt("sigpep.db.port"),
                SigPepDatabase.getSchemaName(ncbiTaxonId),
                username,
                password);
        this.ncbiTaxonId = ncbiTaxonId;
    }

    /**
     * Creates database schema for the species of this instance of SigPepDatabase. Any existing schema will be droped.
     *
     * @throws SQLException if an error occurs while communicating with the database
     * @throws IOException  if the SQL script required to create the database cannot be read
     */
    public void createSchema() throws SQLException, IOException {
        createSchema(ncbiTaxonId);
    }

    /**
     * Creates SigPep database schema for the specified species.
     *
     * @param ncbiTaxonId the NCBI taxon ID of the species
     * @throws SQLException if an error occurs while communicating with the database
     * @throws IOException  if the SQL script required to create the database cannot be read
     */
    public void createSchema(int ncbiTaxonId) throws SQLException, IOException {
        String scriptFilePath = Configuration.getInstance().getString("sigpep.db.create.schema.sql");

        URL urlSqlScript = ConfigurationUtils.locate(scriptFilePath);

        SqlScript script = new SqlScript(urlSqlScript);
        String schemaName = SigPepDatabase.getSchemaName(ncbiTaxonId);

        Connection con = this.getConnectionWithoutSchema();


        logger.info("Creating schema '" + schemaName + "' from SQL script '" + urlSqlScript.getPath() + "'...");

        Statement s = con.createStatement();

        for (Iterator<String> statements = script.getStatementIterator(); statements.hasNext();) {
            String statement = statements.next();
            statement = SqlUtil.setParameter(statement, "schemaName", schemaName, false);
            logger.info(statement);
            s.execute(statement);
        }

        //close statement
        s.close();

        //close connection
        con.close();
    }

    /**
     * Creates indices on the SigPep schema.
     * <p/>
     * Indeces are specified in the script the value of  the <code>sigpep.db.create.indices.sql</code>
     * property in the sigpep.properties file is pointing to.
     *
     * @throws SQLException if a database access error occurs
     * @throws IOException  if an exceptino occurs while reading the SQL script
     */
    public void createIndices() throws SQLException, IOException {
        createIndices(ncbiTaxonId);
    }

    /**
     * Creates indices on the SigPep schema of the specified species.
     * <p/>
     * Indeces are specified in the script the value of  the <code>sigpep.db.create.indices.sql</code>
     * property in the sigpep.properties file is pointing to.
     *
     * @param ncbiTaxonId the NCBI taxon ID of the species
     * @throws SQLException if an exception occurs while executing the SQL script
     * @throws IOException  if an exceptino occurs while reading the SQL script
     */
    public void createIndices(int ncbiTaxonId) throws SQLException, IOException {
        String scriptFilePath = com.compomics.sigpep.persistence.config.Configuration.getInstance().getString("sigpep.db.create.indices.sql");

        URL urlSqlScript = ConfigurationUtils.locate(scriptFilePath);

        Connection con = this.getConnection();
        SqlScript script = new SqlScript(urlSqlScript);

        String schemaName = SigPepDatabase.getSchemaName(ncbiTaxonId);

        logger.info("Creating indices on schema '" + schemaName + "' using SQL script '" + urlSqlScript.getPath() + "'...");

        Statement s = con.createStatement();

        for (Iterator<String> statements = script.getStatementIterator(); statements.hasNext();) {
            String statement = statements.next();
            statement = SqlUtil.setParameter(statement, "schemaName", schemaName, false);
            logger.info(statement);
            s.execute(statement);
        }

        //close statement
        s.close();

        //close connection
        con.close();
    }

    /**
     * Persists in silico digest created by DBToolkit to the SigPep database.
     * <p/>
     * Before digests can be persisted to the database they have to be processed
     * first using the {@link com.compomics.sigpep.persistence.rdbms.helper.impl.EnsemblDBToolkitDigestProcessor} class. The files created by the
     * <code>processFiles()</code> method of {@link com.compomics.sigpep.persistence.rdbms.helper.impl.EnsemblDBToolkitDigestProcessor} will be imported
     * using MySQL's LOAD DATA INFILE statement.
     *
     * @param inputDirectory the directory containing the processed in silico digests.
     */
    public void persistDigest(String inputDirectory) {
        try {
            logger.info("loading table data from files...");
            loadTableDataFromFiles(inputDirectory);

            logger.info("populating table 'signature_peptide'...");
            populateTableSignaturePeptide();

            logger.info("populating table 'sequence2signature_protease'...");
            populateTableSequence2SignatureProtease();
        } catch (SQLException e) {
            logger.error("Exception while persisting digest to database.", e);
        }
    }

    /**
     * Loads data files created by the {@link com.compomics.sigpep.persistence.rdbms.helper.impl.EnsemblDBToolkitDigestProcessor} class into the respective
     * database tables.
     *
     * @param inputDirectory directory containing the data files
     * @throws SQLException if a database access error occurs
     */
    private void loadTableDataFromFiles(String inputDirectory) throws SQLException {
        Connection con = this.getConnection();

        for (String file : new File(inputDirectory).list()) {
            if (file.endsWith(".tsv")) {
                logger.info("loading data from file " + file + "...");

                Statement s = con.createStatement();

                //enable local input files
                s.execute("SET GLOBAL local_infile = 1");

                String targetTable = file.replace(".tsv", "");
                String infile = inputDirectory + "/" + file;

                s.execute("LOAD DATA LOCAL INFILE '" + infile + "' INTO TABLE " + targetTable);
                s.close();
            }
        }

        con.close();
    }

    /**
     * Populates the database table 'signature_peptide' with the IDs of peptides qualifying as
     * a signature peptide.
     *
     * @throws SQLException if a database access error occurs
     */
    private void populateTableSignaturePeptide() throws SQLException {
        Connection con = this.getConnection();
        Statement s = con.createStatement();
        s.execute("INSERT INTO signature_peptide SELECT peptide_id FROM peptide GROUP BY peptide_id HAVING count(distinct sequence_id) = 1");
        s.close();

        con.close();
    }

    /**
     * Populates the database table 'sequence2signature_peptide' with the IDs of signature peptides and
     * the IDs of the respective sequence the originate from.
     *
     * @throws SQLException if a database access error occurs
     */
    private void populateTableSequence2SignatureProtease() throws SQLException {
        Connection con = this.getConnection();
        Statement s = con.createStatement();
        s.execute("INSERT INTO sequence2signature_protease(sequence_id,protease_id,signature_peptide_count)\n"
                + "SELECT pep2pro.sequence_id,\n"
                + "       pep2prot.protease_id,\n"
                + "       count(sp.peptide_id)\n"
                + "FROM signature_peptide sp,\n"
                + "         peptide2protease pep2prot,\n"
                + "         peptide pep2pro\n"
                + "WHERE sp.peptide_id = pep2pro.peptide_id\n"
                + "    AND sp.peptide_id = pep2prot.peptide_id\n"
                + "GROUP BY pep2pro.sequence_id,\n"
                + "         pep2prot.protease_id");
        s.close();
        con.close();
    }

    /**
     * @param ensemblVersion
     * @throws SQLException
     * @throws EnshException
     * @TODO: JavaDoc missing
     */
    public void importSpliceEvents(int ensemblVersion) throws SQLException, EnshException {
        logger.info("fetching protein accession -> sequence ID map...");
        //fetch protein accession to sequence ID map
        Map<String, Integer> proteinAccession2SequenceId = fetchProteinAccession2SequenceIdMap();

        logger.info("fetching splice event from Ensembl version " + ensemblVersion + "...");
        //fetch Ensembl splice events
        Map<String, Set<String>> spliceEvent2Location = fetchEnsemblSpliceEvents(ensemblVersion, proteinAccession2SequenceId.keySet());

        logger.info("creating exon set...");
        //extract exon accessions
        Set<String> exonAccessions = new HashSet<String>();
        for (String event : spliceEvent2Location.keySet()) {
            exonAccessions.add(event.split(":")[0]);
            exonAccessions.add(event.split(":")[1]);
        }

        logger.info("populating table 'exon'...");
        Map<String, Integer> exonAccession2Id = insertExons(exonAccessions);

        logger.info("populating table 'splice_event'...");
        Map<String, Integer> spliceEvent2Id = insertSpliceEvents(exonAccession2Id, spliceEvent2Location.keySet());

        logger.info("populating table 'splice_event_location'...");
        insertSpliceEventLocations(proteinAccession2SequenceId, spliceEvent2Id, spliceEvent2Location);

        logger.info("populating table 'peptide2splice_event'...");
        int rowsInserted = populateTablePeptide2SpliceEvent();
        logger.info(+rowsInserted + " rows inserted");
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     * @throws SQLException
     */
    private Map<String, Integer> fetchProteinAccession2SequenceIdMap() throws SQLException {
        try {
            Map<String, Integer> retVal = new HashMap<String, Integer>();

            Connection con = this.getConnection();
            Statement s = con.createStatement();

            ResultSet rs = s.executeQuery(
                    "SELECT prot.protein_accession, prot2seq.sequence_id "
                            + "FROM protein prot, protein2sequence prot2seq "
                            + "WHERE prot.protein_id=prot2seq.protein_id");

            while (rs.next()) {
                retVal.put(rs.getString(1), rs.getInt(2));
            }

            return retVal;
        } catch (SQLException e) {
            throw new SQLException("Exception while fetching data from SigPep database.", e);
        }
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param ensemblVersion
     * @param ensemblIds
     * @return
     * @throws EnshException
     */
    public Map<String, Set<String>> fetchEnsemblSpliceEvents(int ensemblVersion, Set<String> ensemblIds) throws EnshException {
        Map<String, Set<String>> retVal = new HashMap<String, Set<String>>();
        Set<String> processedIds = new HashSet<String>();

        SessionFactory sessionFactory = Ensh.getSessionFactory(this.getNcbiTaxonId(), ensemblVersion);

        ArrayList ensemblIdList = Collections.list(Collections.enumeration(ensemblIds));

        int intervalSize = 100;

        int counter = 0;
        for (int from = 0; from < ensemblIdList.size(); from += intervalSize) {
            int to = from + intervalSize;
            if (to >= ensemblIdList.size()) {
                to = ensemblIdList.size() - 1;
            }

            if (from % 1000 == 0) {
                logger.info(counter + " translations of " + ensemblIds.size() + " processed...");
            }

            List ensemblIdSubList = ensemblIdList.subList(from, to);

            Session session = sessionFactory.openSession();

            Criteria criteria = session.createCriteria("Translation").setFetchMode(
                    "stableId", FetchMode.JOIN).setFetchMode("transcript", FetchMode.JOIN).setFetchMode(
                    "transcript.exons", FetchMode.JOIN).createAlias("stableId", "stId").add(Restrictions.in("stId.stableId", ensemblIdSubList));

            for (Translation translation : (Iterable<Translation>) criteria.list()) {
                String ensemblId = translation.getStableId().getStableId();

                if (processedIds.contains(ensemblId)) {
                    continue;
                }

                Map<Integer, List<Exon>> exonBoundaries = translation.getExonBoundaries();

                for (Integer spliceSite : exonBoundaries.keySet()) {
                    ArrayList<Exon> exons = (ArrayList) exonBoundaries.get(spliceSite);
                    String exonAccession1 = exons.get(0).getStableId().getStableId();
                    String exonAccession2 = exons.get(1).getStableId().getStableId();

                    String spliceEvent = exonAccession1 + ":" + exonAccession2;

                    if (!retVal.containsKey(spliceEvent)) {
                        retVal.put(spliceEvent, new HashSet<String>());
                    }

                    retVal.get(spliceEvent).add(ensemblId + ":" + spliceSite);
                }

                processedIds.add(ensemblId);
            }

            session.flush();
            session.clear();
            session.close();
        }

        logger.info(counter + " translations of " + ensemblIds.size() + " processed...");

        return retVal;
    }

    ///////////////////
    //getters & setters
    ///////////////////

    /**
     * Returns the NCBI taxon ID of the species of this Database object.
     *
     * @return the NCBI taxon ID
     */
    public int getNcbiTaxonId() {
        return ncbiTaxonId;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param exonAccessions
     * @return
     * @throws SQLException
     */
    private Map<String, Integer> insertExons(Set<String> exonAccessions) throws SQLException {
        Connection con = null;
        PreparedStatement s = null;

        try {
            Map<String, Integer> retVal = new HashMap<String, Integer>();

            con = this.getConnection();
            s = con.prepareStatement("INSERT INTO exon (exon_accession) values (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            SortedSet<String> sortedExonAccessions = new TreeSet<String>(exonAccessions);

            int counter = 0;
            for (String accession : sortedExonAccessions) {
                s.setString(1, accession);
                s.addBatch();

                if (++counter % 1000 == 0) {
                    logger.info(counter + " exons of " + exonAccessions.size() + " processed...");
                }
            }

            logger.info(counter + " exons of " + exonAccessions.size() + " processed...");

            s.executeBatch();

            //fetch generated keys
            ResultSet generatedKeys = s.getGeneratedKeys();
            for (String accession : sortedExonAccessions) {
                generatedKeys.next();
                int key = generatedKeys.getInt(1);
                retVal.put(accession, key);
            }

            s.close();
            con.close();

            return retVal;
        } catch (SQLException e) {
            throw new SQLException("Exception while inserting into table exon.", e);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException e) { //do nothing
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) { //do nothing
            }
        }
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param exonAccession2Id
     * @param spliceEvents
     * @return
     * @throws SQLException
     */
    private Map<String, Integer> insertSpliceEvents(Map<String, Integer> exonAccession2Id, Set<String> spliceEvents) throws SQLException {
        Connection con = null;
        PreparedStatement s = null;

        try {
            Map<String, Integer> retVal = new HashMap<String, Integer>();

            con = this.getConnection();
            s = con.prepareStatement("INSERT INTO splice_event (exon_id_1, exon_id_2) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            SortedSet<String> sortedSpliceEvents = new TreeSet<String>(spliceEvents);

            int counter = 0;
            for (String event : sortedSpliceEvents) {
                String exonAccession1 = event.split(":")[0];
                String exonAccession2 = event.split(":")[1];
                int exonId1 = exonAccession2Id.get(exonAccession1);
                int exonId2 = exonAccession2Id.get(exonAccession2);

                s.setInt(1, exonId1);
                s.setInt(2, exonId2);
                s.addBatch();

                if (++counter % 1000 == 0) {
                    logger.info(counter + " splice events of " + spliceEvents.size() + " processed...");
                }
            }

            logger.info(counter + " splice events of " + spliceEvents.size() + " processed...");

            s.executeBatch();

            ResultSet generatedKeys = s.getGeneratedKeys();

            for (String event : sortedSpliceEvents) {
                generatedKeys.next();
                int key = generatedKeys.getInt(1);
                retVal.put(event, key);
            }

            s.close();
            con.close();

            return retVal;
        } catch (SQLException e) {
            throw new SQLException("Exception while inserting into table splice_event.", e);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException e) { //do nothing
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) { //do nothing
            }
        }
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param proteinAccession2SequenceId
     * @param spliceEvent2Id
     * @param spliceEvent2SequenceLocation
     * @throws SQLException
     */
    private void insertSpliceEventLocations(Map<String, Integer> proteinAccession2SequenceId,
                                            Map<String, Integer> spliceEvent2Id,
                                            Map<String, Set<String>> spliceEvent2SequenceLocation)
            throws SQLException {
        Connection con = null;
        PreparedStatement s = null;

        try {
            Set<String> uniqueRows = new HashSet<String>();

            con = this.getConnection();
            s = con.prepareStatement("INSERT INTO splice_event_location (splice_event_id, sequence_id, pos_start, pos_end) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);

            int counter = 0;
            for (String event : spliceEvent2SequenceLocation.keySet()) {
                int eventId = spliceEvent2Id.get(event);

                for (String sequenceLocation : spliceEvent2SequenceLocation.get(event)) {
                    String proteinAccession = sequenceLocation.split(":")[0];

                    if (proteinAccession2SequenceId.containsKey(proteinAccession)) {
                        int proteinId = proteinAccession2SequenceId.get(proteinAccession);
                        int location = new Integer(sequenceLocation.split(":")[1]);

                        String row = eventId + ":" + proteinId + ":" + location + ":" + location;

                        if (!uniqueRows.contains(row)) {
                            s.setInt(1, eventId);
                            s.setInt(2, proteinId);
                            s.setInt(3, location);
                            s.setInt(4, location);
                            s.addBatch();

                            uniqueRows.add(row);
                        }
                    } else {
                        logger.error("No SigPep entry for protein " + proteinAccession + "...");
                    }
                }

                if (++counter % 1000 == 0) {
                    logger.info(counter + " splice events of " + spliceEvent2SequenceLocation.size() + " processed...");
                }
            }

            logger.info(counter + " splice events of " + spliceEvent2SequenceLocation.size() + " processed...");

            s.executeBatch();
            s.close();
            con.close();
        } catch (SQLException e) {
            throw new SQLException("Exception while inserting data into table splice_event_location.", e);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException e) { //do nothing
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) { //do nothing
            }
        }
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     * @throws SQLException
     */
    private int populateTablePeptide2SpliceEvent() throws SQLException {
        String sql =
                "    INSERT INTO peptide2splice_event (peptide_id, splice_event_id) "
                        + "SELECT DISTINCT pep.peptide_id, "
                        + "                spel.splice_event_id "
                        + "           FROM protein_sequence seq, "
                        + "                splice_event_location spel, "
                        + "                peptide pep "
                        + "          WHERE seq.sequence_id=spel.sequence_id "
                        + "            AND (seq.sequence_id=pep.sequence_id AND pep.pos_start < spel.pos_start AND pep.pos_end > spel.pos_end)  "
                        + "       ORDER BY seq.sequence_id, spel.pos_start";

        Connection con = null;
        Statement s = null;

        try {
            con = this.getConnection();
            s = con.createStatement();
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Exception while inserting into table exon.", e);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException e) { //do nothing
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) { //do nothing
            }
        }
    }

    /**
     * Returns the NCBI taxon ID for SigPep schema name species suffix.
     *
     * @param speciesSuffix the schema name species suffix
     * @return the NCBI taxon ID
     */
    public static int getNcbiTaxonId(String speciesSuffix) {
        String speciesName = speciesSuffix.replace("_", " ");
        if (organisms.contains(speciesName)) {
            return organisms.getNcbiTaxonId(speciesName);
        }

        return 0;
    }

    /**
     * Removes all entries from the database which are related to proteins that are
     * not of Ensembl biotye 'protein_coding' (e.g. proteins of biotype 'v_segment')
     * <p/>
     * Will delete entries from tables <code>protein</code>, <code>protein2gene</code>,
     * <code>protein2organism</code>, <code>protein2sequence</code>,
     * <code>protein_sequence</code>, <code>peptide</code> and
     * <code>sequence2signature_protease</code>.
     *
     *
     * @param ensemblVersion the major version number of the Ensembl release to use
     * @return a map with the table name as key and the number of entries deleted from the table as value
     * @throws DatabaseException if an exception occurs during access to Ensembl Mart or SigPep
     */
    public Map<String, Integer> cleanupTables(String ensemblVersion) throws DatabaseException {
        Map<String, Integer> retVal = new TreeMap<String, Integer>();
        retVal.put("protein", 0);
        retVal.put("protein2gene", 0);
        retVal.put("protein2organism", 0);
        retVal.put("protein2seqeuence", 0);
        retVal.put("sequence", 0);
        retVal.put("peptide", 0);
        retVal.put("sequence2signature_protease", 0);
        retVal.put("signature_peptide", 0);

        //fetch IDs of Ensembl translations of biotype 'protein_coding'
        Set<String> ensemblProteinCodingTranslations = fetchEnsemblProteinCodingTranslationIds(ensemblVersion);
        if (ensemblProteinCodingTranslations.isEmpty()) {
            return retVal;
        }

        //fetch protein_ids of SigPep proteins not in the above set of Ensembl transations
        Set<Integer> proteinIds = fetchNonProteinCodingProteinIds(ensemblProteinCodingTranslations);
        if (proteinIds.isEmpty()) {
            return retVal;
        }

        retVal = deleteRelatedDatbaseEntries(proteinIds);

        //delete related database entries
        return retVal;
    }

    /**
     * Fetches stable IDs of Ensembl translations of biotype 'protein_coding' from the Ensembl Mart database.
     * <p/>
     * The table <code><organism_prefix>_gene_ensembl__transcript__main</code> of
     * the <code>ensembl_mart_<version></code> is queried to fetch the respective
     * Ensembl translation IDs.
     *
     *
     * @param ensemblVersion the major version number of the Ensembl release to use
     * @return set of Ensembl translation IDs
     * @throws DatabaseException if an exception occurs during the database transaction
     */
    private Set<String> fetchEnsemblProteinCodingTranslationIds(String ensemblVersion) throws DatabaseException {
        Set<String> retVal = new HashSet<String>();

        //create Database object for Ensembl Mart database
        String host = "martdb.ensembl.org";
        // ensembl version < 48
        //int port = 3316;

        // ensembl version >48
        int port = 5316;
        String database = "ensembl_mart_" + ensemblVersion;
        Database ensemblMart = SimpleDatabaseFactory.createMySQLDatabase(host, port, database);

        //create Ensembl Mart table name for species
        String sigPepSchemaName = getSchemaName(ncbiTaxonId);
        String ensemblMartTablePrefix = sigPepSchemaName.split("_")[1].substring(0, 1) + //the first character of the species name
                sigPepSchemaName.split("_")[2];
//        String ensemblMartTableSuffix = "gene_ensembl__transcript__main";
        String ensemblMartTableSuffix = "gene_ensembl__translation__main";
        String ensemblMartTableName = ensemblMartTablePrefix + "_" + ensemblMartTableSuffix;

        //fetch ensembl translation ids of proteins of biotype 'protein_coding'
        try {
            //get database connection
            Connection con = ensemblMart.getConnection();

            //execute query
            /**
             * Mod Kenny
             * Ensembl 60 has no translation_stable_id in the transcript_main table.
             * As we are looking for translationids, I therefore adapt the logic to the latest database schema.
             *
             * Therefore, we fetch the translationids from the gene_ensembl__translation_main table, and group on the stable_id_1070 column which holds the protein accessions.
             *
             */
//            String queryStatement = "SELECT DISTINCT translation_stable_id FROM " + ensemblMartTableName + " WHERE biotype = 'protein_coding'";

            String lTranslationIDColumn = "stable_id_1070";
            String queryStatement = "SELECT DISTINCT " + lTranslationIDColumn + " FROM " + ensemblMartTableName + " WHERE biotype_1064 = 'protein_coding'";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(queryStatement);

            while (rs.next()) {
                String ensemblId = rs.getString(lTranslationIDColumn);
                retVal.add(ensemblId);
            }

            rs.close();
            s.close();
        } catch (SQLException e) {
            logger.error(e);
        }

        return retVal;
    }

    /**
     * Fetches IDs of protein entries from SigPep which are not in the set of Ensembl translations of
     * biotype 'protein_coding'.
     *
     * @param ensemblProteinCodingTranslationIds
     *         Ensembl IDs of Ensembl translations of biotype 'protein_coding'
     * @return set of SigPep protein IDs
     * @throws DatabaseException if an exception occurs during communication with the SigPep database
     */
    private Set<Integer> fetchNonProteinCodingProteinIds(Set<String> ensemblProteinCodingTranslationIds) throws DatabaseException {
        Set<Integer> retVal = new TreeSet<Integer>();

        try {
            //get database connection
            Connection con = this.getConnection();

            //create statement
            Statement s = con.createStatement();

            //set Ensembl IDs as parameter values of SQL statements
            String fetchNonProteinCodingEnsemblTranslations = "SELECT protein_id FROM protein WHERE protein_accession NOT IN (:ensemblIds)";
            fetchNonProteinCodingEnsemblTranslations = SqlUtil.setParameterSet(fetchNonProteinCodingEnsemblTranslations, "ensemblIds", ensemblProteinCodingTranslationIds);

            //fetch IDs
            ResultSet rs = s.executeQuery(fetchNonProteinCodingEnsemblTranslations);
            while (rs.next()) {
                int proteinId = rs.getInt("protein_id");
                retVal.add(proteinId);
            }
            rs.close();
        } catch (SQLException e) {
            throw new DatabaseException("Exception while fetching protein_ids from SigPep database.", e);
        }

        return retVal;
    }

    /**
     * Deletes all database records related to the proteins whose ID is passed as an argument.
     * <p/>
     * Will delete entries from tables <code>protein</code>, <code>protein2gene</code>,
     * <code>protein2organism</code>, <code>protein2sequence</code>,
     * <code>protein_sequence</code>, <code>peptide</code> and
     * <code>sequence2signature_protease</code>.
     *
     * @param proteinIds IDs of the proteins whose records will be deleted from the database
     * @return a map with the table name as key and the number of entries deleted from the table as value
     * @throws DatabaseException if an exception occurs during access to SigPep
     */
    private Map<String, Integer> deleteRelatedDatbaseEntries(Set<Integer> proteinIds) throws DatabaseException {
        Map<String, Integer> retVal = new TreeMap<String, Integer>();

        try {
            //DELETE statements
            String deleteFromProtein = "DELETE FROM protein WHERE protein_id IN (:proteinIds)";
            String deleteFromProtein2Gene = "DELETE FROM protein2gene WHERE protein_id NOT IN (SELECT protein_id FROM protein)";
            String deleteFromProtein2Organism = "DELETE FROM protein2organism WHERE protein_id NOT IN (SELECT protein_id FROM protein)";
            String deleteFromProtein2Sequence = "DELETE FROM protein2sequence WHERE protein_id NOT IN (SELECT protein_id FROM protein)";
            String deleteFromSequence = "DELETE FROM protein_sequence WHERE sequence_id NOT IN (SELECT sequence_id FROM protein2sequence)";
            String deleteFromPeptide = "DELETE FROM peptide WHERE sequence_id NOT IN (SELECT sequence_id FROM protein_sequence)";
            String deleteFromSequence2SignatureProtease = "DELETE FROM sequence2signature_protease WHERE sequence_id NOT IN (SELECT sequence_id FROM protein_sequence)";
            String deleteFromSignaturePeptide = "DELETE FROM signature_peptide WHERE peptide_id NOT IN (SELECT peptide_id FROM peptide)";

            //set parameter values of DELETE FROM protein... statement
            deleteFromProtein = SqlUtil.setParameterSet(deleteFromProtein, "proteinIds", proteinIds);

            //get database connection and create statement
            Connection con = this.getConnection();
            Statement s = con.createStatement();

            //execute DELETE statements
            int updateCount;
            updateCount = s.executeUpdate(deleteFromProtein);
            retVal.put("protein", updateCount);

            updateCount = s.executeUpdate(deleteFromProtein2Gene);
            retVal.put("protein2gene", updateCount);

            updateCount = s.executeUpdate(deleteFromProtein2Organism);
            retVal.put("protein2organism", updateCount);

            updateCount = s.executeUpdate(deleteFromProtein2Sequence);
            retVal.put("protein2seqeuence", updateCount);

            updateCount = s.executeUpdate(deleteFromSequence);
            retVal.put("sequence", updateCount);

            updateCount = s.executeUpdate(deleteFromPeptide);
            retVal.put("peptide", updateCount);

            updateCount = s.executeUpdate(deleteFromSequence2SignatureProtease);
            retVal.put("sequence2signature_protease", updateCount);

            updateCount = s.executeUpdate(deleteFromSignaturePeptide);
            retVal.put("signature_peptide", updateCount);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return retVal;
    }

    /**
     * Tries to estblish a connection to the SigPep database in specified intervals.
     *
     * @param interval the time in milliseconds between connection attempts
     * @param attempts the number of times an attempts should be made to establish a connection
     * @return a JDBC connection
     * @throws java.sql.SQLException if an exception occurs while attempting to connect to the database
     */
    public Connection getConnection(long interval, int attempts) throws SQLException {
        Connection sigPepDatabaseConnection = null;

        for (int a = 0; a < attempts; a++) {
            try {
                sigPepDatabaseConnection = this.getConnection();
            } catch (SQLException e) {
                if (e.getErrorCode() == ERROR_CODE_TOO_MANY_CONNECTIONS) {
                    try {
                        logger.info(e.getMessage() + " Reattempting to connect in " + interval / 1000 + " seconds.");
                        Thread.sleep(interval);
                    } catch (InterruptedException ie) {
                        logger.error("Exception while attempting to establish JDBC connection to SigPep database.", ie);
                    }
                } else {
                    throw e;
                }
            }

            if (sigPepDatabaseConnection != null) {
                break;
            }
        }

        return sigPepDatabaseConnection;
    }


    /**
     * @TODO: JavaDoc missing
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            SigPepDatabase sigPepDb = new SigPepDatabase("root", "".toCharArray(), 9823);
//            SigPepDatabase sigPepDb = new SigPepDatabase("root", "".toCharArray(), 9606);
//            sigPepDb.importSpliceEvents(46);
        } catch (DatabaseException e) {
            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (EnshException e) {
//            e.printStackTrace();
        }
    }
}
