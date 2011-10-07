package com.compomics.sigpep.persistence.rdbms.helper;

import com.compomics.dbtools.DatabaseException;
import com.compomics.sigpep.persistence.config.Configuration;
import com.compomics.sigpep.persistence.rdbms.SigPepDatabase;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and populates a schema for the specified organism.
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 25-Sep-2007<br>
 * Time: 16:32:30<br>
 */
public class SigPepSetup {

    /**
     * provides access to the persistence layer configuration
     */
    private static Configuration config = Configuration.getInstance();
    /**
     * the singleton instance
     */
    private static SigPepSetup ourInstance = new SigPepSetup();
    /**
     * helper to initialise database
     */
    private DatabaseInitialiser databaseInitialiser = createDatabaseInititaliser();
    /**
     * the sig pep database
     */
    private SigPepDatabase sigPepDatabase;
    private boolean downloadSequences = true;
    private boolean doDigest = true;
    private boolean processDigest = true;
    private boolean createSchema = true;
    private boolean persistDigest = true;
    private boolean cleanUpTables = true;
    private boolean createIndices = true;
    private boolean importSpliceEvents = true;
    /**
     * The log4j logger
     */
    private static Logger logger = Logger.getLogger(SigPepSetup.class);

    /**
     * Default constructor.
     */
    public SigPepSetup() {
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public static SigPepSetup getInstance() {
        return ourInstance;
    }

    /**
     * Starts the setup process.
     *
     * @param adminUsername
     * @param adminPassword
     * @param workingDirectory
     * @param organismScientificName
     * @param organismNcbiTaxonId
     * @param sequenceDatabaseName
     * @param sequenceDatabaseVersion
     */
    public void setupDatabase(String adminUsername,
                              String adminPassword,
                              String workingDirectory,
                              String organismScientificName,
                              int organismNcbiTaxonId,
                              String sequenceDatabaseName,
                              String sequenceDatabaseVersion,
                              double lowMass,
                              double highMass,
                              int missedCleavages,
                              String... protease) {

        boolean workingDirectoryIsCreated;
        boolean directoryStructureIsCreated;
        boolean databaseIsInitialised;
        boolean sequencesRetrieved;
        boolean sequencesDigested;
        boolean digestsProcessed;

        String speciesSuffix = SigPepDatabase.getSpeciesSuffix(organismNcbiTaxonId);
        System.out.println(speciesSuffix); // @TODO: remove?
        String subFolderOrganism = buildOrganismSubDirectoryName(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
        String speciesSubdirectory = workingDirectory + "/" + subFolderOrganism;
        String inputDirectory = speciesSubdirectory + "/" + config.getString("sigpep.db.setup.folder.database");

        // create working directory
        logger.info("-----------------------------------------------------");
        logger.info("creating working directory...");

        workingDirectoryIsCreated = createWorkingDirectory(workingDirectory);

        if (!workingDirectoryIsCreated) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        // create subfolders
        logger.info("-----------------------------------------------------");
        logger.info("creating directory structure...");

        directoryStructureIsCreated = createDirectoryStructure(workingDirectory, organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);

        if (!directoryStructureIsCreated) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        // initialise database if not yet initialised
        logger.info("-----------------------------------------------------");
        logger.info("initialising database...");

        databaseInitialiser.setAdminUsername(adminUsername);
        databaseInitialiser.setAdminPassword(adminPassword);

        if (!databaseInitialiser.isInitialised()) {
            logger.info("creating catalogue schema " + config.getString("sigpep.db.schema.catalog") + " at " + config.getString("sigpep.db.url"));
            databaseIsInitialised = databaseInitialiser.initialise();
            if (!databaseIsInitialised) {
                logger.info("unable to initialise database");
            }
        } else {
            logger.info("database at " + config.getString("sigpep.db.url") + " initialised already");
            databaseIsInitialised = true;
        }

        if (!databaseIsInitialised) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        // Setup the database schema.
        try {
            logger.info("setting up SigPep for " + speciesSuffix.replace("_", " "));

            sigPepDatabase = new SigPepDatabase(adminUsername, adminPassword.toCharArray(), organismNcbiTaxonId);

            if (createSchema) {
                logger.info("creating SigPep schema...");
                sigPepDatabase.createSchema();
                logger.info("done");
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // retrieving protein sequences
        logger.info("-----------------------------------------------------");
        logger.info("retrieving protein sequences...");

//        sequencesRetrieved = retrieveSequences(workingDirectory, organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
        sequencesRetrieved = true;

        if (!sequencesRetrieved) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        // digest protein sequences
        logger.info("-----------------------------------------------------");
        logger.info("digesting protein sequences...");

//        sequencesDigested = digestSequences(workingDirectory, organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion, lowMass, highMass, missedCleavages, protease);
        sequencesDigested = true;

        if (!sequencesDigested) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        // process digests
        logger.info("-----------------------------------------------------");
        logger.info("processing sequences...");

        digestsProcessed = processDigests(workingDirectory, organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion, protease);
        digestsProcessed = true;

        if (!digestsProcessed) {
            logger.info("exit");
            logger.info("-----------------------------------------------------");
            return;
        } else {
            logger.info("done");
            logger.info("-----------------------------------------------------");
        }

        try {
            // Persist the digest into the database.
            if (persistDigest) {
                logger.info("persisting digests...");
                sigPepDatabase.persistDigest(inputDirectory);
                logger.info("done");
            }

            if (createIndices) {
                logger.info("creating indices...");
                sigPepDatabase.createIndices();
                logger.info("done");
            }

            if (cleanUpTables) {
                logger.info("removing sequences not of biotype 'protein_coding'...");

                Map<String, Integer> updateCount = sigPepDatabase.cleanupTables(sequenceDatabaseVersion);
                for (String table : updateCount.keySet()) {
                    int rowCount = updateCount.get(table);
                    logger.info(rowCount + " rows of table " + table + " affected.");
                }
                logger.info("done...");
            }

// @TODO: import splice events?
//
//            if (importSpliceEvents) {
//
//                logger.info("importing splice events from Ensembl...");
//                sigPepDatabase.importSpliceEvents(ensemblVersion);
//                logger.info("done");
//            }

        } catch (IOException e) {
            logger.error(e);
        } catch (SQLException e) {
            logger.error(e);
        } catch (DatabaseException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    /**
     * Returns true if the sequences are to be downloaded.
     *
     * @return true if the sequences are to be downloaded
     */
    public boolean downloadSequences() {
        return downloadSequences;
    }

    /**
     * Set if the sequences are to be downloaded.
     *
     * @param downloadSequences
     */
    public void setDownloadSequences(boolean downloadSequences) {
        this.downloadSequences = downloadSequences;
    }

    /**
     * Returns true of the digest is on.
     *
     * @return true of the digest is on
     */
    public boolean doDigest() {
        return doDigest;
    }

    /**
     * Set digest on or off.
     *
     * @param doDigest
     */
    public void setDoDigest(boolean doDigest) {
        this.doDigest = doDigest;
    }

    /**
     * Returns if the digest is to be processed.
     *
     * @return f the digest is to be processed
     */
    public boolean processDigest() {
        return processDigest;
    }

    /**
     * Set if he digest is to be processed.
     *
     * @param processDigest
     */
    public void setProcessDigest(boolean processDigest) {
        this.processDigest = processDigest;
    }

    /**
     * Returns if the schema is to be created.
     *
     * @return if the schema is to be created.
     */
    public boolean createSchema() {
        return createSchema;
    }

    /**
     * Set if the schema is to be created.
     *
     * @param createSchema
     */
    public void setCreateSchema(boolean createSchema) {
        this.createSchema = createSchema;
    }

    /**
     * Returns if the digest is to persist.
     *
     * @return if the digest is to persist
     */
    public boolean persistDigest() {
        return persistDigest;
    }

    /**
     * Set if the digest is to persist.
     *
     * @param persistDigest
     */
    public void setPersistDigest(boolean persistDigest) {
        this.persistDigest = persistDigest;
    }

    /**
     * Returns if the tables are to be cleaned up.
     *
     * @return if the tables are to be cleaned up
     */
    public boolean cleanUpTables() {
        return cleanUpTables;
    }

    /**
     * Set if the tables are to be cleaned up.
     *
     * @param cleanUpTables
     */
    public void setCleanUpTables(boolean cleanUpTables) {
        this.cleanUpTables = cleanUpTables;
    }

    /**
     * Returns if indices are to be created.
     *
     * @return if indices are to be created
     */
    public boolean createIndices() {
        return createIndices;
    }

    /**
     * Set if indices are to be created.
     *
     * @param createIndices
     */
    public void setCreateIndices(boolean createIndices) {
        this.createIndices = createIndices;
    }

    /**
     * Returns if slice events are to be imported.
     *
     * @return if slice events are to be imported
     */
    public boolean importSpliceEvents() {
        return importSpliceEvents;
    }

    /**
     * Set if slice events are to be imported.
     *
     * @param importSpliceEvents
     */
    public void setImportSpliceEvents(boolean importSpliceEvents) {
        this.importSpliceEvents = importSpliceEvents;
    }

    /**
     * @param args
     * @TODO: JavaDoc missing
     */
    public static void main(String[] args) {
        String lOrganismScientificName = null;
        int lOrganismNcbiTaxonId = 0;
        String lSequenceDatabaseName = "Ensembl";
        String lSequenceDatabaseVersion = "64";
//        String[] lProteases = new String[]{"Trypsin", "Lys-C", "Lys-N", "Arg-C", "Asp-N"};
//        String[] lProteases = new String[]{"Trypsin", "Lys-C", "Arg-C", "Asp-N"};
//        String[] lProteases = new String[]{"Trypsin", "Lys-C", "Arg-C", "PepsinA"};
        String[] lProteases = new String[]{"Trypsin"};
//        String[] lProteases = new String[]{"Trypsin", "Lys-C", "Lys-N", "Arg-C", "Asp-N"};
        int lMissedCleavages = 0;
        int lHighMass = 4000;
        int lLowMass = 600;

//        String workingDirectory = "C:/temp";
        String workingDirectory = "/Users/kennyhelsens/tmp/sigpep_setup/";

        //intialize database
        SigPepSetup sigpepSetup = SigPepSetup.getInstance();
        DatabaseInitialiser databaseInitializer = sigpepSetup.getDatabaseInitialiser();
        databaseInitializer.setAdminUsername(config.getString("sigpep.db.username"));
        databaseInitializer.setAdminPassword(config.getString("sigpep.db.password"));
        if (!databaseInitializer.isInitialised()) {
            databaseInitializer.initialise();
        }

        //loop organisms and create schemas
        Map<Integer, String> organismMap = databaseInitializer.getOrganismMap();
        for (Integer i : organismMap.keySet()) {
            lOrganismScientificName = organismMap.get(i);

            logger.info("Creating schema for organism " + organismMap.get(i));
            lOrganismNcbiTaxonId = i;

            boolean isSelectedOrganism = true;
            isSelectedOrganism = lOrganismScientificName.equals("homo sapiens");

            if (isSelectedOrganism) {
                sigpepSetup.setupDatabase(config.getString("sigpep.db.username"),
                        config.getString("sigpep.db.password"),
                        workingDirectory,
                        lOrganismScientificName,
                        lOrganismNcbiTaxonId,
                        lSequenceDatabaseName,
                        lSequenceDatabaseVersion,
                        lLowMass,
                        lHighMass,
                        lMissedCleavages,
                        lProteases);
            }

        }

    }

    /**
     * Set the database initialiser.
     *
     * @param databaseInitialiser
     */
    public void setDatabaseInitialiser(DatabaseInitialiser databaseInitialiser) {
        this.databaseInitialiser = databaseInitialiser;
    }

    /**
     * Factory method to create the database initialiser as configured in
     * the sigpep-persistence.properties file.
     *
     * @return the database initialiser
     */
    protected DatabaseInitialiser createDatabaseInititaliser() {

        DatabaseInitialiser retVal;
        String initialiserClass = config.getString("sigpep.db.setup.database.initialiser.class");

        try {
            retVal = (DatabaseInitialiser) Class.forName(initialiserClass).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

    /**
     * Factory method to create the sequence retriever as configured in
     * the sigpep-persistence.properties file.
     *
     * @param databaseName the name of the database to create the retriever for
     * @return the database initialiser
     */
    protected SequenceRetriever createSequenceRetriever(String databaseName) {

        SequenceRetriever retVal;
        String retrieverClass;

        if (databaseName.equalsIgnoreCase("Ensembl")) {
            retrieverClass = config.getString("sigpep.db.setup.sequence.retriever.class");
        } else {
            throw new RuntimeException("Unsupported database: " + databaseName);
        }

        try {
            retVal = (SequenceRetriever) Class.forName(retrieverClass).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

    /**
     * Factory method to create the proteolytic digest as configured in
     * the sigpep-persistence.properties file.
     *
     * @return the proteolytic digest
     */
    protected ProteolyticDigest createProteolyticDigest() {

        logger.info("creating proteolytic digest");
        ProteolyticDigest retVal;
        String digestClass = config.getString("sigpep.db.setup.proteolytic.digest.class");

        try {
            retVal = (ProteolyticDigest) Class.forName(digestClass).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

    /**
     * Factory method to create the proteolytic digest as configured in
     * the sigpep-persistence.properties file.
     *
     * @return the proteolytic digest
     */
    protected DigestProcessor createDigestProcessor() {

        DigestProcessor retVal;
        String processorClass = config.getString("sigpep.db.setup.digest.processor.class");

        try {
            retVal = (DigestProcessor) Class.forName(processorClass).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

    /**
     * Builds a file name for the protein sequence FASTA file with the pattern
     * <organism_scientific_name>_<organism_ncbi_taxon_id>_<sequence_database_name>_<sequence_database_release>.protein.fa.
     *
     * @param organismScientificName the scientific name of the organism
     * @param organismTaxonId        the NCBI taxon ID of the organism
     * @param databaseName           the name of the sequence database
     * @param databaseVersion        the release version of the sequence database
     * @return the file name
     */
    protected String buildSequenceFilename(String organismScientificName, int organismTaxonId, String databaseName, String databaseVersion) {
        return organismScientificName.replace(" ", "_") + "_" + organismTaxonId + "_" + databaseName + "_" + databaseVersion + "_protein.fa";
    }

    /**
     * Builds a file name for the protein sequence FASTA file with the pattern
     * <organism_scientific_name>_<organism_ncbi_taxon_id>_<sequence_database_name>_<sequence_database_release>.protein.fa.
     *
     * @param organismScientificName the scientific name of the organism
     * @param organismTaxonId        the NCBI taxon ID of the organism
     * @param databaseName           the name of the sequence database
     * @param databaseVersion        the release version of the sequence database
     * @param protease               the digesting protease
     * @return the file name
     */
    protected String buildDigestFilename(String organismScientificName, int organismTaxonId, String databaseName, String databaseVersion, String protease) {
        return organismScientificName.replace(" ", "_") + "_" + organismTaxonId + "_" + databaseName.toLowerCase() + "_" + databaseVersion + "_" + protease.toLowerCase() + "_peptide.fa";
    }

    /**
     * @param organismScientificName
     * @param organismTaxonId
     * @param databaseName
     * @param databaseVersion
     * @return
     * @TODO: JavaDoc missing
     */
    protected String buildOrganismSubDirectoryName(String organismScientificName, int organismTaxonId, String databaseName, String databaseVersion) {
        return organismScientificName.toLowerCase().replace(" ", "_") + "_" + organismTaxonId + "_" + databaseName.toLowerCase() + "_" + databaseVersion;
    }

    /**
     * @param workingDirectory
     * @return
     * @TODO: JavaDoc missing
     */
    protected boolean createWorkingDirectory(String workingDirectory) {

        File workingDirectoryFile = new File(workingDirectory);
        if (!workingDirectoryFile.exists()) {
            if (workingDirectoryFile.mkdir()) {
                logger.info("directory '" + workingDirectoryFile + "' created");
            } else {
                logger.info("unable to create directory '" + workingDirectoryFile + "'");

                return false;
            }
        } else {
            logger.info("directory '" + workingDirectoryFile + "' exists already. Using existing directory.");
        }

        return true;
    }

    /**
     * @param workingDirectory
     * @param organismScientificName
     * @param organismTaxonId
     * @param sequenceDatabaseName
     * @param sequenceDatabaseVersion
     * @return
     * @TODO: JavaDoc missing
     */
    protected boolean createDirectoryStructure(String workingDirectory, String organismScientificName, int organismTaxonId, String sequenceDatabaseName, String sequenceDatabaseVersion) {

        // create sub-directory for organism and database combination
        String subDirectory = buildOrganismSubDirectoryName(organismScientificName, organismTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
        File subDirectoryFile = new File(workingDirectory + "/" + subDirectory);
        if (!subDirectoryFile.exists()) {
            if (subDirectoryFile.mkdir()) {
                logger.info("sub-directory '" + subDirectory + "' created");

            } else {
                logger.info("unable to create directory '" + subDirectoryFile.getAbsolutePath() + "'");
                return false;
            }
        } else {
            logger.info("sub-directory '" + subDirectory + "' exists already. Using existing directory.");
        }

        // create sub-directory for sequence data
        String subSubDirectorySequence = subDirectory + "/" + config.getString("sigpep.db.setup.folder.sequence");
        File subSubDirectorySequenceFile = new File(workingDirectory + "/" + subSubDirectorySequence);
        if (!subSubDirectorySequenceFile.exists()) {
            if (subSubDirectorySequenceFile.mkdir()) {
                logger.info("sub-directory '" + subSubDirectorySequence + "' created");

            } else {
                logger.info("unable to create directory '" + subSubDirectorySequenceFile.getAbsolutePath() + "'");
                return false;
            }
        } else {
            logger.info("sub-directory '" + subSubDirectorySequence + "' exists already. Using existing directory.");
        }

        // create sub-directory for digests
        String subSubDirectoryDigest = subDirectory + "/" + config.getString("sigpep.db.setup.folder.digest");
        File subSubDirectoryDigestFile = new File(workingDirectory + "/" + subSubDirectoryDigest);
        if (!subSubDirectoryDigestFile.exists()) {
            if (subSubDirectoryDigestFile.mkdir()) {
                logger.info("sub-directory '" + subSubDirectoryDigest + "' created");
            } else {
                logger.info("unable to create sub-directory '" + subSubDirectoryDigestFile.getAbsolutePath() + "'");
                return false;
            }
        } else {
            logger.info("sub-directory '" + subSubDirectoryDigest + "' exists already. Using existing directory.");
        }

        // create sub-directory for database files
        String subSubDirectoryDatabase = subDirectory + "/" + config.getString("sigpep.db.setup.folder.database");
        File subSubDirectoryDatabaseFile = new File(workingDirectory + "/" + subSubDirectoryDatabase);
        if (!subSubDirectoryDatabaseFile.exists()) {
            if (subSubDirectoryDatabaseFile.mkdir()) {
                logger.info("sub-directory '" + subSubDirectoryDatabase + "' created");

            } else {
                logger.info("unable to create sub-directory '" + subSubDirectoryDatabaseFile.getAbsolutePath() + "'");
                return false;
            }
        } else {
            logger.info("sub-directory '" + subSubDirectoryDatabase + "' exists already. Using existing directory.");
        }

        return true;
    }

    /**
     * @param workingDirectory
     * @param organismScientificName
     * @param organismNcbiTaxonId
     * @param sequenceDatabaseName
     * @param sequenceDatabaseVersion
     * @return
     * @TODO: JavaDoc missing
     */
    protected boolean retrieveSequences(String workingDirectory, String organismScientificName, int organismNcbiTaxonId, String sequenceDatabaseName, String sequenceDatabaseVersion) {

        SequenceRetriever sequenceRetriever = createSequenceRetriever(sequenceDatabaseName);

        logger.info("fetching sequences for organism " + organismScientificName + " from database " + sequenceDatabaseName + " (release " + sequenceDatabaseVersion + ").");

        try {

            String destinationFilename = buildSequenceFilename(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String destinationFolderName = buildOrganismSubDirectoryName(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String subFolderName = config.getString("sigpep.db.setup.folder.sequence");
            File destinationFile = new File(workingDirectory + "/" + destinationFolderName + "/" + subFolderName + "/" + destinationFilename);

            URL destinationURL = destinationFile.toURI().toURL();
            sequenceRetriever.fetch(organismScientificName, organismNcbiTaxonId, sequenceDatabaseVersion, destinationURL);

        } catch (Exception e) {
            logger.error("exception occured while retrieving sequences.", e);
            logger.info("unable to retrieve sequences");
            return false;
        }

        return true;
    }

    /**
     * @param workingDirectory
     * @param organismScientificName
     * @param organismNcbiTaxonId
     * @param sequenceDatabaseName
     * @param sequenceDatabaseVersion
     * @param lowMass
     * @param highMass
     * @param missedCleavages
     * @param protease
     * @return
     * @TODO: JavaDoc missing
     */
    protected boolean digestSequences(
            String workingDirectory,
            String organismScientificName,
            int organismNcbiTaxonId,
            String sequenceDatabaseName,
            String sequenceDatabaseVersion,
            double lowMass,
            double highMass,
            int missedCleavages,
            String... protease) {

        ProteolyticDigest digest = createProteolyticDigest();

        try {

            String sequenceFilename = buildSequenceFilename(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String subFolderOrganism = buildOrganismSubDirectoryName(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String subFolderSequence = config.getString("sigpep.db.setup.folder.sequence");
            File sequenceFile = new File(workingDirectory + "/" + subFolderOrganism + "/" + subFolderSequence + "/" + sequenceFilename);
            URL sequenceFileURL = sequenceFile.toURI().toURL();

            for (String p : protease) {

                String digestFilename = buildDigestFilename(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion, p);
                String subFolderDigest = config.getString("sigpep.db.setup.folder.digest");
                File digestFile = new File(workingDirectory + "/" + subFolderOrganism + "/" + subFolderDigest + "/" + digestFilename);
                URL digestFileURL = digestFile.toURI().toURL();

                digest.setEnzyme(p);
                digest.setLowMass(lowMass);
                digest.setHighMass(highMass);
                digest.setMissedCleavages(missedCleavages);
                digest.setEnzyme(p);

                logger.info("digesting sequence database '" + sequenceFileURL.toString() + "' with " + p);
                digest.digestSequenceDatabase(sequenceFileURL, digestFileURL);
            }

        } catch (Exception e) {
            logger.error("exception occured while digesting sequences.", e);
            return false;
        }

        return true;
    }

    /**
     * @param workingDirectory
     * @param organismScientificName
     * @param organismNcbiTaxonId
     * @param sequenceDatabaseName
     * @param sequenceDatabaseVersion
     * @param protease
     * @return
     * @TODO: JavaDoc missing
     */
    boolean processDigests(
            String workingDirectory,
            String organismScientificName,
            int organismNcbiTaxonId,
            String sequenceDatabaseName,
            String sequenceDatabaseVersion,
            String... protease) {

        Connection sigPepDatabaseConnection = null;
        PreparedStatement lStatement = null;
        try {

            String sequenceFilename = buildSequenceFilename(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String subFolderOrganism = buildOrganismSubDirectoryName(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion);
            String subFolderSequence = config.getString("sigpep.db.setup.folder.sequence");
            File sequenceFile = new File(workingDirectory + "/" + subFolderOrganism + "/" + subFolderSequence + "/" + sequenceFilename);
            URL sequenceFileURL = sequenceFile.toURI().toURL();

            String subFolderDatabase = config.getString("sigpep.db.setup.folder.database");
            URL outputDirectoryURL = new File(workingDirectory + "/" + subFolderOrganism + "/" + subFolderDatabase).toURI().toURL();

            Map<String, URL> protease2Url = new HashMap<String, URL>();

            for (String p : protease) {

                String digestFilename = buildDigestFilename(organismScientificName, organismNcbiTaxonId, sequenceDatabaseName, sequenceDatabaseVersion, p);
                String subFolderDigest = config.getString("sigpep.db.setup.folder.digest");
                File digestFile = new File(workingDirectory + "/" + subFolderOrganism + "/" + subFolderDigest + "/" + digestFilename);
                URL digestFileURL = digestFile.toURI().toURL();

                protease2Url.put(p, digestFileURL);
            }

            sigPepDatabaseConnection = sigPepDatabase.getConnection();
            lStatement = sigPepDatabaseConnection.prepareStatement("SELECT protease_id, name, full_name FROM protease");
            ResultSet resultSet = lStatement.executeQuery();

            Map<String, Integer> proteases = new HashMap<String, Integer>();
            while (resultSet.next()) {
                int id = resultSet.getInt("protease_id");
                String name = resultSet.getString("name");
                String fullname = resultSet.getString("full_name");
                proteases.put(name, id);
                proteases.put(fullname, id);
            }

            DigestProcessor processor = createDigestProcessor();

            processor.setSequenceFileUrl(sequenceFileURL);
            processor.setDigestFileUrl(protease2Url);
            processor.setOutputDirectoryUrl(outputDirectoryURL);
            processor.setProteases(proteases);

            return processor.processFiles();

        } catch (Exception e) {
            logger.error("exception occured while procession digests.", e);
            return false;
        } finally {
            try {
                lStatement.close();
                sigPepDatabaseConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public DatabaseInitialiser getDatabaseInitialiser() {
        return databaseInitialiser;
    }
}
