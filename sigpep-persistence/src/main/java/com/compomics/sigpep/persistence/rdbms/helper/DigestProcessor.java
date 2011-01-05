package com.compomics.sigpep.persistence.rdbms.helper;

import java.net.URL;
import java.util.Map;

/**
 * Processes a protein sequence database and the corresponding
 * in silico digests for import into SigPep database.
 * <p/>
 * Calling the <code>processFiles()</code> method will parse the sequence library
 * and the digests, normalise the content and write it to TAB separates files (.tsv)
 * ready for loading into the SigPep database schema using MySQL's LOAD DATA INFILE
 * statement.
 * <p/>
 * The output files are named according to the table they are to be imported into.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 07-May-2009<br/>
 * Time: 14:06:56<br/>
 */
public interface DigestProcessor {

    URL getSequenceFileUrl();

    void setSequenceFileUrl(URL sequenceFileUrl);

    Map<String, URL> getDigestFileUrl();

    void setDigestFileUrl(Map<String, URL> digestFileUrl);

    URL getOutputDirectoryUrl();

    void setOutputDirectoryUrl(URL ouputDirectoryUrl);

    boolean processFiles();

    void setProteases(Map<String, Integer> aProteases);
}
