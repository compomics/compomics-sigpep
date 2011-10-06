package com.compomics.sigpep.persistence.rdbms.helper.impl;

import org.apache.log4j.Logger;
import com.compomics.sigpep.persistence.config.Configuration;
import com.compomics.sigpep.persistence.rdbms.helper.SequenceRetriever;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Implementation that fetches protein sequences from the Ensembl FTP server.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 30-Apr-2009<br/>
 * Time: 18:14:49<br/>
 */
public class EnsemblFtpSequenceRetriever implements SequenceRetriever {

    /**
     * the log4j logger
     */
    private static Logger logger = Logger.getLogger(EnsemblFtpSequenceRetriever.class);

    /**
     * provides access to the persistence layer configuration
     */
    public static Configuration config = Configuration.getInstance();

    /**
     * the URL pattern of the the Ensembl FTP directory containing the sequence file
     */
    public static String ensemblFtpUrl = config.getString("sigpep.db.protein.sequences.ensembl.ftp.url.pattern.ensembl");

    /**
     * the file extension of the FASTA file containing the protein sequences
     */
    public static String ensemblFileExtension = config.getString("sigpep.db.protein.sequences.ensembl.file.extension");


    /**
     * Fetches protein sequences from a database and
     * deposits them in FASTA format in the specified
     * destination file.
     *
     * @param organismScientificName the scientific name of the organism
     * @param organismNcbiTaxonID    the NCBI taxon ID of the organism
     * @param databaseVersion        the version of the database
     * @param destination            the target location
     * @return true if the sequences were fetched successfully, false otherwise
     * @throws RuntimeException if an exception occurs during sequence retrieval
     */
    public boolean fetch(String organismScientificName, int organismNcbiTaxonID, String databaseVersion, URL destination) {

        boolean retVal;

        try {

            URL directoryUrl = createDirectoryUrl(organismScientificName, databaseVersion);
            String sequenceFileName = getSequenceFileName(directoryUrl);

            URL sequenceFileUrl = new URL(directoryUrl.toString() + "/" + sequenceFileName);

            InputStream sequenceFileInputStream = new GZIPInputStream(sequenceFileUrl.openStream());
            OutputStream sequenceFileOutputStream = new FileOutputStream(destination.getPath());

            retVal = readFromTo(sequenceFileInputStream, sequenceFileOutputStream);

            sequenceFileInputStream.close();
            sequenceFileOutputStream.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException("Exception while retrieving protein sequences for organism " + organismScientificName + " from database Ensembl (release " + databaseVersion + ")", e);
        } catch (IOException e) {
            throw new RuntimeException("Exception while retrieving protein sequences for organism " + organismScientificName + " from database Ensembl (release " + databaseVersion + ")", e);
        }

        return retVal;
    }

    /**
     * Inserts the organism and database version specific information
     * in the URL pattern of the ENSEMBL FTP directory.
     *
     * @param organismScientificName the scientific name of the organism
     * @param databaseVersion        the Ensembl release version
     * @return URL                   the FTP directory path as a URL
     * @throws MalformedURLException when malformed URL is created
     */
    private URL createDirectoryUrl(String organismScientificName, String databaseVersion) throws MalformedURLException {

        return new URL(ensemblFtpUrl
                .replace("#release", databaseVersion)
                .replace("#organism", organismScientificName.toLowerCase().replace(" ", "_")));
    }

    /**
     * Fetches the file name of the Ensembl protein sequence FASTA file
     * from the Ensembl FTP server.
     *
     * @param directoryUrl the URL of the directory the file is located in
     * @return the filename
     * @throws IOException thrown if an exception occurs while listing the files in the directory
     */
    private String getSequenceFileName(URL directoryUrl) throws IOException {

        String retVal = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(directoryUrl.openStream()));
        String line;
        while ((line = br.readLine()) != null) {

            if (line.endsWith(ensemblFileExtension)) {

                int tokenCount = line.split("\\s").length;
                retVal = line.split("\\s")[tokenCount - 1];

            }
        }

        br.close();

        return retVal;
    }

    /**
     * Reads and input stream and writes it to the specified output stream.
     *
     * @param fromInputStream the input stream
     * @param toOutputStream  the output stream
     * @return true if the input has been successfully written to the ouptput
     * @throws IOException if an exception occurs while reading/writing to the streams
     */
    private boolean readFromTo(InputStream fromInputStream, OutputStream toOutputStream) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(fromInputStream));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(toOutputStream));
        String line;

        while ((line = br.readLine()) != null) {

            bw.write(line);
            bw.newLine();
            bw.flush();
        }

        return true;
    }
}
