package com.compomics.sigpep.persistence.rdbms.helper.impl;

import be.proteomics.dbtoolkit.io.DBLoaderLoader;
import be.proteomics.dbtoolkit.io.interfaces.DBLoader;
import be.proteomics.util.protein.Protein;
import org.apache.log4j.Logger;
import com.compomics.sigpep.persistence.rdbms.SigPepDatabase;
import com.compomics.sigpep.persistence.rdbms.helper.DigestProcessor;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of DigestProcessor to process DBToolkit digests
 * of an Ensembl FASTA sequence library.
 * <p/>
 * Required input files are the Ensembl FASTA formatted sequence library used as
 * input for the in silico digest and the digest output file(s) generated
 * by DBToolkit.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: Michael Mueller<br/>
 * Date: 13-Aug-2007<br/>
 * Time: 18:01:26<br/>
 */
public class EnsemblDBToolkitDigestProcessor implements DigestProcessor {

    private static final String usage = "EnsemblDBToolkitDigestProcessor <input directory> [<outputdirectory>]";

    private static Logger logger = Logger.getLogger(EnsemblDBToolkitDigestProcessor.class);

    private URL sequenceFileUrl;
    private Map<String, URL> digestFileUrl;
    private URL outputDirectoryUrl;

    private int organismId = 1;
    private int proteaseId = 1;
    private int geneId = 1;
    private int proteinId = 1;
    private int peptideId = 1;
    private int sequenceId = 1;

    private Map<String, Integer> organisms = new TreeMap<String, Integer>();
    private Map<String, Integer> proteases = new TreeMap<String, Integer>();
    private Map<String, Integer> peptides = new HashMap<String, Integer>();
    private Map<String, Integer> proteins = new TreeMap<String, Integer>();
    private Map<String, Integer> sequences = new TreeMap<String, Integer>();
    private Map<Integer, Integer> geneId2OrganismId = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> proteinId2Known = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> proteinId2GeneId = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> proteinId2OrganismId = new HashMap<Integer, Integer>();
    private Map<String, Integer> genes = new TreeMap<String, Integer>();
    private Map<Integer, Integer> proteinId2SequenceId = new HashMap<Integer, Integer>();

    //output filenames
    private static String fileNameGeneTable = "gene.tsv";
    private static String fileNameGene2organismTable = "gene2organism.tsv";
    private static String fileNameOrganismTable = "organism.tsv";
    private static String fileNamePeptideTable = "peptide.tsv";
    private static String fileNamePeptide2proteaseTable = "peptide2protease.tsv";
    private static String fileNameProteaseTable = "protease.tsv";
    private static String fileNameProteinTable = "protein.tsv";
    private static String fileNameProtein2geneTable = "protein2gene.tsv";
    private static String fileNameProtein2organismTable = "protein2organism.tsv";
    private static String fileNameProtein2sequenceTable = "protein2sequence.tsv";
    private static String fileNameProteinSequenceTable = "protein_sequence.tsv";

    private Map<String, PrintWriter> outputFiles = new HashMap<String, PrintWriter>();

    private int ncbiTaxonId;

    /**
     * Constructs a digest processor to process DBToolkit in silico digests
     * of an Ensembl FASTA sequence library.
     */
    public EnsemblDBToolkitDigestProcessor(){        
    }

    /**
     * Constructs a digest processor to process DBToolkit in silico digests
     * of an Ensembl FASTA sequence library.
     *
     * @param sequenceFileUrl   the URL of the Ensembl FASTA sequence database
     * @param digestFileUrl     a map of protease names and digest file URLs
     * @param ouputDirectoryUrl the URL of the output directory
     */
    public EnsemblDBToolkitDigestProcessor(URL sequenceFileUrl, Map<String, URL> digestFileUrl, URL ouputDirectoryUrl) {
        this.sequenceFileUrl = sequenceFileUrl;
        this.digestFileUrl = digestFileUrl;
        this.outputDirectoryUrl = ouputDirectoryUrl;
    }

    /**
     * Creates PrintWriters for all output files and stores them in a HashMap.
     *
     * @throws FileNotFoundException if a file cannot be opened
     */
    private void openOutputFiles() throws FileNotFoundException {

        outputFiles.put(fileNameOrganismTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameOrganismTable));
        outputFiles.put(fileNameProteaseTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProteaseTable));
        outputFiles.put(fileNameGeneTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameGeneTable));
        outputFiles.put(fileNameProteinTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProteinTable));
        outputFiles.put(fileNamePeptideTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNamePeptideTable));
        outputFiles.put(fileNameGene2organismTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameGene2organismTable));
        outputFiles.put(fileNamePeptide2proteaseTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNamePeptide2proteaseTable));
        outputFiles.put(fileNameProtein2sequenceTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProtein2sequenceTable));
        outputFiles.put(fileNameProtein2geneTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProtein2geneTable));
        outputFiles.put(fileNameProtein2organismTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProtein2organismTable));
        outputFiles.put(fileNameProteinSequenceTable, new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProteinSequenceTable));
    }

    /**
     * Flushes all output files created by openOutputFiles().
     */
    private void flushOutputFiles() {

        for (PrintWriter pw : outputFiles.values())
            pw.flush();
    }

    /**
     * Closes all output files created by openOutputFiles().
     */
    private void closeOutputFiles() {

        for (PrintWriter pw : outputFiles.values())
            pw.close();
    }

    /**
     * Processes the FASTA file and the in silico digest output files.
     */
    public boolean processFiles() {

        try {
            createGeneTable();
        } catch (IOException e) {
            throw new RuntimeException("Exception while processing files.", e);
        }

        try {
            createProteinTable();
        } catch (IOException e) {
            throw new RuntimeException("Exception while processing files.", e);
        }

        //open output files
        try {
            openOutputFiles();
        } catch (IOException e) {
            throw new RuntimeException("Unable to process sequence file.", e);
        }

        //process FASTA sequence file
        try {
            if (!processSequences()) {
                throw new RuntimeException("Unable to process sequence file. Cannot proceed to processing of digests.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to process sequence file.", e);
        }
        //process digest files
        try {
            processPeptides();
        } catch (IOException e) {
            throw new RuntimeException("Unable to process sequence file.", e);
        }

        //flush output files
        flushOutputFiles();

        //close output files
        closeOutputFiles();

        //remove redundant rows from files
        removeRedundancy();//removeRedundancy

        return true;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @throws IOException
     */
    private void createGeneTable() throws IOException {

        BufferedReader sequences = new BufferedReader(new InputStreamReader(sequenceFileUrl.openStream()));
        Set<String> accessions = new TreeSet<String>();

        String line;
        while ((line = sequences.readLine()) != null) {

            //if FASTA header

            //ENSP00000386319 pep:known chromosome:NCBI36:MT:8528:9208:1 gene:ENSG00000198744 transcript:ENST00000409008
            if (line.startsWith(">")) {

                for (String token : line.split(" ")) {

                    if (token.startsWith("gene")) {

                        String acc = token.replace("gene:", "");
                        accessions.add(acc);
                    }
                }
            }
        }

        PrintWriter table = new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameGeneTable);

        int id = 1;
        for (String acc : accessions) {

            table.println(id++ + "\t" + acc);
            table.flush();
        }

        table.close();

    }

    /**
     * @TODO: JavaDoc missing
     *
     * @throws IOException
     */
    private void createProteinTable() throws IOException {

        BufferedReader sequences = new BufferedReader(new InputStreamReader(sequenceFileUrl.openStream()));
        Set<String> entries = new TreeSet<String>();

        String line;
        while ((line = sequences.readLine()) != null) {

            //if FASTA header

            //ENSP00000386319 pep:known chromosome:NCBI36:MT:8528:9208:1 gene:ENSG00000198744 transcript:ENST00000409008
            if (line.startsWith(">")) {

                String acc = line.split(" ")[0].replace(">", "");
                String status = line.split(" ")[1].replace("pep:", "");
                int known = 0;
                if(status.equals("known")){
                    known = 1;
                }

                String location = line.split(" ")[2].replace(":", "\t");
                entries.add(acc + "\t" + location + "\t" + known);
            }
        }

        PrintWriter table = new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProteinTable);

        int id = 1;
        for (String entry : entries) {

            table.println(id++ + "\t" + entry);
            table.flush();

        }

        table.close();
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @throws IOException
     */
    private void createProteinSequenceTable() throws IOException {

        DBLoader loader = DBLoaderLoader.loadDB(new File(sequenceFileUrl.getPath()));
        Set<String> sequences = new HashSet<String>();

        String sequenceString;
        while((sequenceString = loader.nextProtein().getSequence().getSequence()) != null){
            sequences.add(sequenceString);
        }
            
        PrintWriter table = new PrintWriter(outputDirectoryUrl.getPath() + "/" + fileNameProteinTable);

        int id = 1;
        for (String sequence : sequences) {
            table.println(id++ + "\t" + sequence);
            table.flush();
        }

        table.close();
    }

    /**
     * Processes sequences in the FASTA sequence library that was used as input for the
     * DBToolkit in silico digest
     *
     * @return true if sequences were processed successfully, false otherwise
     * @throws IOException if an error occurd during file access
     */
    private boolean processSequences() throws IOException {

        boolean sequencesProcessed = false;
        boolean fileFound = false;

        String file = sequenceFileUrl.getPath();

        if (file.endsWith(".fa")) {

            fileFound = true;

            //process organism
            String organismPostFix = file.split("\\.")[0];

            this.ncbiTaxonId = SigPepDatabase.getNcbiTaxonId(organismPostFix.toLowerCase());
            String organism = organismPostFix.split("\\.")[0].replace("_", " ");
            int organismId = getOrganismId(organism);

            //process sequences
            logger.info("Extracting gene, protein amd sequence information from file '" + file + "'...");

            DBLoader loader = DBLoaderLoader.loadDB(new File(sequenceFileUrl.getPath()));
            Protein protein;

            while ((protein = loader.nextProtein()) != null) {

                //get fasta header and sequence
                String header = protein.getHeader().getFullHeaderWithAddenda();
                String sequence = protein.getSequence().getSequence()
                        .replace("*", ""); //remove stop codon character if present

                //parse gene and protein accession
                String geneAccession = extractGeneAccession(header);
                String proteinAccession = extractProteinAccession(header);

                //create IDs
                int geneId = getGeneId(geneAccession);
                int proteinId = getProteinId(proteinAccession);
                int sequenceId = getSequenceId(sequence);

                int known = extractStatus(header);

                //store gene to organism relationship
                geneId2OrganismId.put(geneId, organismId);

                //store protein to gene relationship
                proteinId2GeneId.put(proteinId, geneId);

                //store protein status
                proteinId2Known.put(proteinId, known);

                //store protein to sequence relationship
                proteinId2SequenceId.put(proteinId, sequenceId);

                //store protein to organism relationship
                proteinId2OrganismId.put(proteinId, organismId);
            }

            //close loader
            loader.close();

            logger.info("done (" + geneId + " genes, " +
                    "" + proteinId + " proteins " +
                    "and " + sequenceId + " unique sequences extracted).");

            //set return value
            sequencesProcessed = true;
        }

        //write results to files
        this.writeOrganismTables();
        this.writeGeneTables();
        this.writeProteinTables();
        this.writeSequenceTables();

        return sequencesProcessed;
    }

    /**
     * Processes peptides resulting from in silico digesting the FASTA sequence library
     * with DBToolkit
     *
     * @throws IOException if an exception occurs during file accesss
     */
    private void processPeptides() throws IOException {

        logger.info("Extracting peptide information:");
        //count input files to process
        int fileCount = 0;

        for (String protease : digestFileUrl.keySet()) {

            fileCount++;

            //process digest files
            int processedFileCount = 1;

            int counter = 0;
            String fileName = digestFileUrl.get(protease).getPath();

            //filter for files containing peptides
            // TODO remove mmueler old code
//            if (fileName.endsWith("fa.gz") && !fileName.endsWith("all.fa.gz")) {
            if (fileName.endsWith("fa.gz") || fileName.endsWith("fa") || fileName.endsWith("fas") || fileName.endsWith("fasta")) {

                logger.info("processing file '" + fileName + "' (" + processedFileCount + " of " + fileCount + ")...");

                int proteaseId = getProteaseId(protease);

                try {
                    //iterate over sequences
                    DBLoader loader = DBLoaderLoader.loadDB(new File(fileName));
                    Protein peptide;
                    while ((peptide = loader.nextProtein()) != null) {

                        String header = peptide.getHeader().getFullHeaderWithAddenda();
                        String sequence = peptide.getSequence().getSequence();
                        String proteinAccession = this.extractProteinAccession(header);
                        int[] position = this.extractPosition(header);
                        int start = position[0];
                        int end = position[1];

                        if (proteinAccession != null && start > 0 && end > 0) {

                            int proteinId = 0;
                            int sequenceId = 0;
                            //make sure protein was contained in sequence FASTA file
                            if (proteins.containsKey(proteinAccession))
                                proteinId = getProteinId(proteinAccession);

                            if (proteinId2SequenceId.containsKey(proteinId))
                                sequenceId = proteinId2SequenceId.get(proteinId);

                            int peptideId = getPeptideId(sequence);

                            if (proteinId > 0 && sequenceId > 0 && peptideId > 0) {
                                outputFiles.get(fileNamePeptide2proteaseTable).println(peptideId + "\t" + proteaseId);
                                outputFiles.get(fileNamePeptideTable).println(peptideId + "\t" + sequenceId + "\t" + start + "\t" + end);
                            } else {
                                throw new IOException("Exception while processing peptide.\n" +
                                        "protein_id = " + proteinId + ",\n" +
                                        "sequence_id = " + sequenceId + ",\n" +
                                        "peptide_id = " + peptideId + "\n" +
                                        header + "\n" +
                                        sequence + "\n");
                            }
                        } else {
                            throw new IOException("Exception while parsing peptide information from FASTA entry:\n" +
                                    header + "\n" +
                                    sequence + "\n");
                        }

                        counter++;
                    }

                    loader.close();

                } catch (IOException e) {
                    logger.error(e);
                }

                processedFileCount++;

                logger.info("done (" + counter + " peptides processed; " + peptides.size() + " unique peptides in memory).");
            }
        }

        writeProteaseTables();
    }

    /**
     * Extracts the name of the protease from an in silico digest file name
     *
     * @param fileName the filename
     * @return the protease name
     */
    private String extractProteaseName(String fileName) {
        int tokenCount = fileName.split("\\.").length;
        return fileName.split("\\.")[tokenCount - 3];
    }

    /**
     * Extracts the gene accession from an Ensembl/TAIR FASTA header
     *
     * @param fastaHeader the FASTA header
     * @return the gene accession
     */
    private String extractGeneAccession(String fastaHeader) {

        String retVal = null;

        try {
            if (this.ncbiTaxonId == 3702) { //Arabidopsis
                retVal = fastaHeader.split("\\|")[0]
                        .replace(">", "")
                        .split(" ")[0] //occassionally text follows the accession which we cut off here
                        .split("\\.")[0];
            } else {
                retVal = fastaHeader.split(" ")[3].replace("gene:", "");
            }
        } catch (Exception e) {
            logger.error("Exception while extracting gene accession from FASTA header (" + fastaHeader + ").", e);
        }

        return retVal;
    }

    /**
     * Extracts the protein accession from an Ensembl/TAIR FASTA header
     *
     * @param fastaHeader the FASTA header
     * @return the protein accession
     */
    private String extractProteinAccession(String fastaHeader) {

        String retVal = null;

        try {

            if (this.ncbiTaxonId == 3702) { //Arabidopsis
                return fastaHeader.split("\\|")[0]
                        .replace(">", "")
                        .split(" ")[0];  //occassionally text follows the accession which we cut off here
            } else {
                return fastaHeader.split(" ")[0].replace(">", "");
            }

        } catch (Exception e) {
            logger.error("Exception while extracting protein accession from FASTA header (" + fastaHeader + ").", e);
        }

        return retVal;
    }

    /**
     * Extracts the protein status (known/novel) from an Ensembl FASTA header
     *
     * @param fastaHeader the FASTA header
     * @return 1 if known, 0 otherwise
     */
    private int extractStatus(String fastaHeader) {

        int retVal = 0;
        if (fastaHeader.contains("pep:known"))
            retVal = 1;
        return retVal;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param fastaHeader
     * @return
     */
    private int[] extractPosition(String fastaHeader) {

        int[] retVal = new int[2];
        retVal[0] = 0;
        retVal[1] = 0;

        try {

            Pattern p = Pattern.compile("\\((\\d*)\\-(\\d*)\\)");
            Matcher m = p.matcher(fastaHeader);

            if (m.find()) {

                // String position = fastaHeader.split("\\(")[1].split("\\)")[0];
                //retVal[0] = Integer.parseInt(position.split("-")[0]);
                //retVal[1] = Integer.parseInt(position.split("-")[1]);

                retVal[0] = Integer.parseInt(m.group(1));
                retVal[1] = Integer.parseInt(m.group(2));

            }
        } catch (Exception e) {
            logger.error("Exception while extracting peptide coordinates from FASTA header (" + fastaHeader + ").", e);
        }

        return retVal;
    }

    /**
     * Returns database ID for the organism
     * If the organism hasn't been seen before
     * a new ID is created.
     *
     * @param organism the organism
     * @return the database ID
     */
    private int getOrganismId(String organism) {

        if (!organisms.containsKey(organism))
            organisms.put(organism, organismId++);

        return organisms.get(organism);
    }

    /**
     * Returns database ID for the protease.
     * If the protease hasn't been seen before
     * a new ID is created.
     *
     * @param protease the protease
     * @return the database ID
     */
    private int getProteaseId(String protease) {

        if (!proteases.containsKey(protease))
            proteases.put(protease, proteaseId++);

        return proteases.get(protease);
    }

    /**
     * Returns database ID for gene.
     * If the gene hasn't been seen before
     * a new ID is created.
     *
     * @param gene the gene accession
     * @return the database ID
     */
    private int getGeneId(String gene) {

        //if we haven't seen the accession before
        if (!genes.containsKey(gene))
            genes.put(gene, geneId++);

        return genes.get(gene);
    } 

    /**
     * Returns database ID for protein.
     * If the protein hasn't been seen before
     * a new ID is created.
     *
     * @param protein the protein accession
     * @return the database ID
     */
    private int getProteinId(String protein) {

        if (!proteins.containsKey(protein))
            proteins.put(protein, proteinId++);

        return proteins.get(protein);
    }

    /**
     * Returns database ID for peptide.
     * If the peptide hasn't been seen before
     * a new ID is created.
     *
     * @param peptide the peptide sequence
     * @return the database ID
     */
    private int getPeptideId(String peptide) {

        if (!peptides.containsKey(peptide))
            peptides.put(peptide, peptideId++);

        return peptides.get(peptide);
    }

    /**
     * Returns database ID for sequence.
     * If the sequence hasn't been seen before
     * a new ID is created.
     *
     * @param sequence the sequence
     * @return the database ID
     */
    private int getSequenceId(String sequence) {

        if (!sequences.containsKey(sequence))
            sequences.put(sequence, sequenceId++);

        return sequences.get(sequence);
    }

    /**
     * Writes a tab separated file for import into
     * database table 'organism'.
     */
    private void writeOrganismTables() {

        PrintWriter output = outputFiles.get(fileNameOrganismTable);

        for (String organism : organisms.keySet()) {

            int id = organisms.get(organism);
            output.println(id + "\t" + organism + "\t" + this.getNcbiTaxonId());

        }

        output.flush();
    }

    /**
     * Writes tab separated files for import into
     * database tables 'gene' and 'gene2organism'.
     */
    private void writeGeneTables() {

        //gene table
        for (String accession : genes.keySet()) {

            int id = genes.get(accession);
            outputFiles.get(fileNameGeneTable).println(id + "\t" + accession);
            flushOutputFiles();

        }

        //gene2orgnanism
        for (Integer geneId : geneId2OrganismId.keySet()) {

            int organismId = geneId2OrganismId.get(geneId);
            outputFiles.get(fileNameGene2organismTable).println(geneId + "\t" + organismId);
            flushOutputFiles();

        }
    }

    /**
     * Writes tab separated files for import into
     * database tables 'protein', 'protein2gene',
     * 'protein2organism', and 'protein2sequence'.
     */
    private void writeProteinTables() {

        //protein table
        for (String accession : proteins.keySet()) {
            int id = proteins.get(accession);
            int known = proteinId2Known.get(id);
            outputFiles.get(fileNameProteinTable).println(id + "\t" + accession + "\t" + known);
            flushOutputFiles();
        }

        //protein2gene table
        for (Integer proteinId : proteinId2GeneId.keySet()) {
            int geneId = proteinId2GeneId.get(proteinId);
            outputFiles.get(fileNameProtein2geneTable).println(proteinId + "\t" + geneId);
            flushOutputFiles();
        }

        //protein2organism table
        for (Integer proteinId : proteinId2GeneId.keySet()) {
            int organismId = proteinId2OrganismId.get(proteinId);
            outputFiles.get(fileNameProtein2organismTable).println(proteinId + "\t" + organismId);
            flushOutputFiles();
        }

        //protein2sequence table
        for (Integer proteinId : proteinId2SequenceId.keySet()) {
            int sequenceId = proteinId2SequenceId.get(proteinId);
            outputFiles.get(fileNameProtein2sequenceTable).println(proteinId + "\t" + sequenceId);
            flushOutputFiles();
        }
    }

    /**
     * Writes tab separated file for import into
     * database table 'protein_sequence'.
     */
    private void writeSequenceTables() {

        PrintWriter output = outputFiles.get(fileNameProteinSequenceTable);

        for (String sequence : sequences.keySet()) {

            int id = sequences.get(sequence);
            output.println(id + "\t" + sequence);
            output.flush();
        }
    }

    /**
     * Writes tab separated file for import into
     * database table 'protease'.
     */
    private void writeProteaseTables() {

        PrintWriter output = outputFiles.get(fileNameProteaseTable);

        for (String protease : proteases.keySet()) {

            int id = proteases.get(protease);
            output.println(id + "\t" + protease);
            output.flush();
        }
    }

    /**
     * Removes duplicated rows from the files generated by methods processSequences()
     * and processPeptides() to avoid violation of unique constraints during database
     * import.
     */
    private void removeRedundancy() {

        //get list of files in directory
        for (String file : new File(outputDirectoryUrl.getPath()).list()) {

            //filter for tab separated files
            if (file.endsWith(".tsv")) {

                //create absolute path for input and temporary file
                String inputFile = outputDirectoryUrl.getPath() + "/" + file;
                //String tempFileSort = inputFile + ".sort";
                String tempFile = inputFile + ".unique";

                //create command lines
                StringBuffer commandSort = new StringBuffer();
                commandSort.append("sort --unique ").append(inputFile).append(" --output=").append(tempFile);
                StringBuffer commandMv = new StringBuffer();
                commandMv.append("mv ").append(tempFile).append(" ").append(inputFile);

                try {

                    logger.info("removing redundancy from file '" + inputFile + "'...");

                    //execute sort command
                    int errorValue = 0;
                    if ((errorValue = this.executeCommand(commandSort.toString())) != 0) {

                        //exit if error has occurd
                        logger.error("Error while executing command " + commandSort + ".");
                        System.exit(errorValue);
                    }

                    //execute sort command
                    if ((errorValue = this.executeCommand(commandMv.toString())) != 0) {
                        //exit if error has occurd
                        logger.error("Error while executing command " + commandMv + ".");
                        System.exit(errorValue);
                    }

                } catch (IOException e) {
                    logger.error(e);
                    System.exit(1);
                } catch (InterruptedException e) {
                    logger.error(e);
                    System.exit(1);
                }

                logger.info("done");

            }
        }
    }

    /**
     * Executes a shell command.
     *
     * @param command the command to execute
     * @return the exit value of the process
     * @throws IOException          if the error stream of the executed process cannot be read
     * @throws InterruptedException if an exception occurs when calling waitFor() on the process
     */
    private int executeCommand(String command) throws IOException, InterruptedException {

        int retVal = 0;

        //execute sort command
        Process p = Runtime.getRuntime().exec(command);

        //get error stream
        BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;

        while ((line = brError.readLine()) != null) {
            logger.error(line);
        }

        //wait for process to finish
        p.waitFor();

        //get exit value
        retVal = p.exitValue();

        //destroy process
        p.destroy();

        //return exit value
        return retVal;
    }

    /////////
    //getters
    /////////

    /**
     * Returns the NCBI taxon ID of this instance of EnsemblDBToolkitDigestProcessor
     *
     * @return the NCBI taxon ID
     */
    public int getNcbiTaxonId() {
        return ncbiTaxonId;
    } //end of method

    public URL getSequenceFileUrl() {
        return sequenceFileUrl;
    }

    public void setSequenceFileUrl(URL sequenceFileUrl) {
        this.sequenceFileUrl = sequenceFileUrl;
    }

    public Map<String, URL> getDigestFileUrl() {
        return digestFileUrl;
    }

    public void setDigestFileUrl(Map<String, URL> digestFileUrl) {
        this.digestFileUrl = digestFileUrl;
    }

    public URL getOutputDirectoryUrl() {
        return outputDirectoryUrl;
    }

    public void setOutputDirectoryUrl(URL outputDirectoryUrl) {
        this.outputDirectoryUrl = outputDirectoryUrl;
    }

    /**
     * Processes in silico digests created with DBToolkit.
     * <p/>
     * Usage: EnsemblDBToolkitDigestProcessor <input directory> [<outputdirectory>]
     *
     * @param args array of command line arguments
     */
    public static void main(String args[]) {

//        try {
//
//            if (args.length == 1) {
//
//                String inputDirectory = args[0];
//
//                EnsemblDBToolkitDigestProcessor pd = new EnsemblDBToolkitDigestProcessor(inputDirectory);
//                pd.processFiles();
//
//            } else if (args.length == 2) {
//
//                String inputDirectory = args[0];
//                String outputDirectory = args[1];
//
//                EnsemblDBToolkitDigestProcessor pd = new EnsemblDBToolkitDigestProcessor(inputDirectory, outputDirectory);
//                pd.processFiles();
//
//            } else {
//                logger.error(usage);
//            }
//
//        } catch (IOException e) {
//            logger.error(e);
//        }

    }
}