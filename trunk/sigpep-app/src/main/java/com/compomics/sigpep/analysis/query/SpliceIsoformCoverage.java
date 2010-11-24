package com.compomics.sigpep.analysis.query;


import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.compomics.dbtools.Database;
import com.compomics.dbtools.DatabaseException;
import com.compomics.dbtools.SimpleDatabaseFactory;
import com.compomics.dbtools.SqlUtil;
import com.compomics.ensh.Ensh;
import com.compomics.ensh.core.model.Translation;
import com.compomics.ensh.exception.EnshException;
import com.compomics.sigpep.util.DelimitedTableReader;
import com.compomics.sigpep.util.DelimitedTableWriter;
import com.compomics.sigpep.util.ResultSetWriter;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.model.impl.FeaturePeptideImpl;
import com.compomics.sigpep.persistence.rdbms.SigPepDatabase;
import com.compomics.sigpep.persistence.util.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jan-2008<br/>
 * Time: 10:29:12<br/>
 */
public class SpliceIsoformCoverage {

    private static Logger logger = Logger.getLogger(SpliceIsoformCoverage.class);

    private static final String SQL_SELECT_SPLICE_ISOFORM_COVERAGE =
            "SELECT gene.gene_accession, " +
                    "       splice_isoform.splice_isoform_count, " +
                    "       COUNT(DISTINCT protein.protein_id) AS identified_splice_isoform_count" +
                    "  FROM gene, " +
                    "       protein2gene, " +
                    "       protein, " +
                    "       protein2sequence, " +
                    "       peptide, " +
                    "       query_peptide_temp, " +
                    "     (SELECT gene.gene_id, gene.gene_accession, COUNT(DISTINCT protein2gene.protein_id) AS splice_isoform_count " +
                    "        FROM gene, protein2gene " +
                    "       WHERE protein2gene.gene_id=gene.gene_id " +
                    "    GROUP BY gene.gene_accession) splice_isoform " +
                    "WHERE splice_isoform.gene_id=gene.gene_id " +
                    "  AND gene.gene_id=protein2gene.gene_id " +
                    "  AND protein2gene.protein_id=protein2sequence.protein_id " +
                    "  AND protein2gene.protein_id=protein.protein_id " +
                    "  AND protein2sequence.sequence_id=peptide.sequence_id " +
                    "  AND peptide.peptide_id=query_peptide_temp.peptide_id " +
                    "GROUP BY (gene.gene_accession)";

    private static final String SQL_CREATE_TABLE_PEPTIDE_TEMP =
            "CREATE TEMPORARY TABLE query_peptide_temp (" +
                    "peptide_id  INT UNSIGNED " +
                    ")ENGINE=InnoDB";

    private static final String SQL_INSERT_INTO_TABLE_QUERY_PEPTIDE_TEMP =
            "INSERT INTO query_peptide_temp (peptide_id) VALUES (?)";

    private SigPepDatabase sigPepDatabase;

    public SpliceIsoformCoverage(SigPepDatabase sigPepDatabase) {
        this.sigPepDatabase = sigPepDatabase;
    }

    public void reportSpliceIsoformCoverage(Set<Integer> peptideIds, PrintWriter printWriter) {


        try {

            Database inMemoryDatabase = SimpleDatabaseFactory.createHSqlTransientInProcessDatabase("sigpep_temp");
            Connection connectionInMemoryDb = inMemoryDatabase.getConnection();


            logger.info("creating report 'splice isoform coverage'...");

            logger.info("connecting to database...");
            Connection connectionSigPepDb = sigPepDatabase.getConnection();

            logger.info("setting query parameters...");
            String sql = SqlUtil.setParameterSet(SQL_SELECT_SPLICE_ISOFORM_COVERAGE, "peptideIds", peptideIds);

            logger.info("executing query...");
            Statement s = connectionSigPepDb.createStatement();

            logger.info("creating temp table...");
            //create temporary table for query peptide ids
            s.execute(SQL_CREATE_TABLE_PEPTIDE_TEMP);

            //populate table
            logger.info("populating temp table...");
            PreparedStatement ps = connectionSigPepDb.prepareStatement(SQL_INSERT_INTO_TABLE_QUERY_PEPTIDE_TEMP);
            int counter = 0;
            for (Integer peptideId : peptideIds) {

                ps.setInt(1, peptideId);
                ps.addBatch();
                if (++counter % 10000 == 0) {
                    logger.info(counter);
                    logger.info("executing batch...");
                    ps.executeBatch();
                }

            }

            logger.info("executing batch...");
            ps.executeBatch();
            ps.close();

            logger.info("fetching result set...");
            ResultSet rs = s.executeQuery(sql);

            logger.info("writing reusult set to ouput...");
            ResultSetWriter rsw = new ResultSetWriter(rs);
            rsw.write(System.out, "\t", true);

        } catch (SQLException e) {
            logger.error("Exception while reporting splice isoform coverage.", e);
        } catch (IOException e) {
            logger.error("Exception while reporting splice isoform coverage.", e);
        } catch (DatabaseException e) {
            logger.error("Exception while reporting splice isoform coverage.", e);
        }

    }

    public void reportSpliceEventCoverage(){

        String query =
                "SELECT DISTINCT gene.gene_accession, \n" +
                "                prot.protein_accession, \n" +
                "                prot.known AS protein_known,\n" +
                "                spe.splice_event_id,\n" +
                "                spe.pos_start AS exon_end, \n" +
                "                pep.peptide_id, \n" +
                "                pep.pos_start AS peptide_start, \n" +
                "                pep.pos_end AS peptide_end, \n" +
                "                CHAR_LENGTH(seq.aa_sequence) sequence_length\n" +
                "FROM gene gene,\n" +
                "     protein2gene gene2prot, \n" +
                "     protein prot, \n" +
                "     protein2sequence prot2seq, \n" +
                "     protein_sequence seq,\n" +
                "     splice_event_location spe,\n" +
                "     peptide pep\n" +
                "WHERE gene.gene_id=gene2prot.gene_id\n" +
                "  AND gene2prot.protein_id=prot.protein_id \n" +
                "  AND prot.protein_id=prot2seq.protein_id\n" +
                "  AND prot2seq.sequence_id=seq.sequence_id\n" +
                "  AND seq.sequence_id=spe.sequence_id\n" +
                "  AND (seq.sequence_id=pep.sequence_id AND pep.pos_start < spe.pos_start AND pep.pos_end > spe.pos_end) \n" +
                "ORDER BY gene_accession, protein_accession, spe.pos_start";

    }

    public void reportSpliceEventCoverage(Set<Integer> peptideIds, PrintWriter printWriter) {

        logger.info("starting splice event analysis...");
         int ncbiTaxonId = 9606;
        try {

            int tableColumnCount = 8;
            DelimitedTableWriter table = new DelimitedTableWriter(printWriter, tableColumnCount, "\t", false);

            SessionFactory enshSessionFactory = Ensh.getSessionFactory(ncbiTaxonId, 45);

            Session enshSession = enshSessionFactory.openSession();

            Query enshQuery = enshSession.createQuery("from Translation where stableId.stableId=:stableId");

            Connection sigPepConnection = sigPepDatabase.getConnection();
            Session sigPepSession = HibernateUtil.getSessionFactory(ncbiTaxonId).openSession(sigPepConnection);

            Query sigPepQuery = sigPepSession.createQuery("from Gene");

            int geneCount = 0;

            for (Iterator<Gene> genes = sigPepQuery.iterate(); genes.hasNext();) {

                Gene gene = genes.next();
                geneCount++;

                if (geneCount > 19000) {


                    String geneAccession = gene.getPrimaryDbXref().getAccession();
                    int spliceIsoformCount = gene.getProteins().size();
                    boolean alternativelySpliced = false;
                    if (spliceIsoformCount > 1) {
                        alternativelySpliced = true;
                    }

                    for (Protein protein : gene.getProteins()) {

                        ProteinSequence sequence = protein.getSequence();
                        int associatedProteinCount = sequence.getProteins().size();

                        String proteinAccession = protein.getPrimaryDbXref().getAccession();

                        //fetch Ensembl entry for protein
                        enshQuery.setParameter("stableId", proteinAccession);
                        Translation translation = (Translation) enshQuery.uniqueResult();

                        for (PeptideFeature peptide : sequence.getPeptides()) {

                            FeaturePeptideImpl pepImpl = (FeaturePeptideImpl) peptide;
                            int peptideId = pepImpl.getId();
                            if (peptideIds.contains(peptideId)) {

                                SequenceLocation peptideLocation = peptide.getLocation();

                                //check if peptide location crosses exon boundary
                                if (peptideLocation.getSequence().equals(sequence) && translation != null) {

                                    int peptideStart = peptideLocation.getStart();
                                    int peptideEnd = peptideLocation.getEnd();

                                    String event = "";
                                    String eventLocation = "";

                                    int exonCount = 0;
                                    for (Integer boundary : translation.getExonBoundaries().keySet()) {

                                        exonCount++;

                                        //check if peptide sequence crosses exon boundary...
                                        if (peptideStart < boundary && peptideEnd > boundary) {

                                            //...if yes check if splice site or alternative splice site

                                            //... if the gene is alternatively spliced and there are more
                                            //splice isoforms then there are proteins associated with this
                                            //sequence the peptide provides evidence for an alternative
                                            //splice event
                                            if (alternativelySpliced && spliceIsoformCount > associatedProteinCount) {

                                                event = "alt_splice_site";
                                                eventLocation = exonCount + "_" + (exonCount + 1) + "[" + boundary + "]";

                                            } else {

                                                event = "splice_site";
                                                eventLocation = exonCount + "_" + (exonCount + 1) + "[" + boundary + "]";

                                            }

                                            //...if it does not cross exon boundary it provides only
                                            //evidence for exon usage
                                        } else {

                                            event = "exon_usage";
                                            eventLocation = "" + exonCount;

                                        }

                                    }

                                    Object[] rowValues = new Object[tableColumnCount];
                                    rowValues[0] = geneAccession;
                                    rowValues[1] = alternativelySpliced;
                                    rowValues[2] = proteinAccession;
                                    rowValues[3] = peptideId;
                                    rowValues[4] = peptideStart;
                                    rowValues[5] = peptideEnd;
                                    rowValues[6] = event;
                                    rowValues[7] = eventLocation;
                                    table.writeRow(rowValues);


                                }


                            }

                            sigPepSession.evict(peptide);

                        }

                        enshSession.evict(translation);
                        sigPepSession.evict(sequence);
                        sigPepSession.evict(protein);

                    }

                    sigPepSession.evict(gene);

                    if (geneCount % 100 == 0) {
                        logger.info(geneCount + " genes processed...");
                    }

                }

            }

            logger.info(geneCount + " genes processed...");

        } catch (SQLException e) {
            logger.error(e);
        } catch (EnshException e) {
            logger.error(e);
        }

        logger.info("done...");
    }

    public void createSpliceAnalysisSummary(URL inputFile, PrintWriter pw) {

        try {
        

            SessionFactory enshSessionFactory = Ensh.getSessionFactory(9606, 45);
            Session enshSession = enshSessionFactory.openSession();
            Query enshQuery = enshSession.createQuery("from Translation");

            //get protein status
            logger.info("fetching Ensembl translation status...");
            HashMap<String, String> ensemblTranslationStatus = new HashMap<String, String>();
            for (Iterator<Translation> translations = enshQuery.iterate(); translations.hasNext();) {

                Translation translation = translations.next();
                String stableId = translation.getStableId().getStableId();
                String status = translation.getTranscript().getStatus();
                ensemblTranslationStatus.put(stableId, status);

            }

            logger.info("fetching Ensembl gene status...");
            enshQuery = enshSession.createQuery("from Gene");
            HashMap<String, String> ensemblGeneStatus = new HashMap<String, String>();
            for (Iterator<com.compomics.ensh.core.model.Gene> genes = enshQuery.iterate(); genes.hasNext();) {

                com.compomics.ensh.core.model.Gene gene = genes.next();
                String stableId = gene.getStableId().getStableId();
                String status = gene.getStatus();
                ensemblGeneStatus.put(stableId, status);

            }

            enshSession.close();

            logger.info("parsing splice analsyis results...");
            InputStream is = inputFile.openStream();
            DelimitedTableReader dtr = new DelimitedTableReader(is, "\t");
            DelimitedTableWriter dtw = new DelimitedTableWriter(pw, 11, "\t", false);

            Set<String> allSigPep = new HashSet<String>();
            Set<String> exonUsageSigPep = new HashSet<String>();
            Set<String> spliceSiteUsageSigPep = new HashSet<String>();
            Set<String> altSpliceSiteUsageSigPep = new HashSet<String>();

            Set<String> allSigPepKnown = new HashSet<String>();
            Set<String> exonUsageSigPepKnown = new HashSet<String>();
            Set<String> spliceSiteUsageSigPepKnown = new HashSet<String>();
            Set<String> altSpliceSiteUsageSigPepKnown = new HashSet<String>();

            Set<String> allSigPepNovel = new HashSet<String>();
            Set<String> exonUsageSigPepNovel = new HashSet<String>();
            Set<String> spliceSiteUsageSigPepNovel = new HashSet<String>();
            Set<String> altSpliceSiteUsageSigPepNovel = new HashSet<String>();

            Set<String> allProtein = new HashSet<String>();
            Set<String> exonUsageProtein = new HashSet<String>();
            Set<String> spliceSiteUsageProtein = new HashSet<String>();
            Set<String> altSpliceSiteUsageProtein = new HashSet<String>();

            Set<String> allProteinKnown = new HashSet<String>();
            Set<String> exonUsageProteinKnown = new HashSet<String>();
            Set<String> spliceSiteUsageProteinKnown = new HashSet<String>();
            Set<String> altSpliceSiteUsageProteinKnown = new HashSet<String>();

            Set<String> allProteinNovel = new HashSet<String>();
            Set<String> exonUsageProteinNovel = new HashSet<String>();
            Set<String> spliceSiteUsageProteinNovel = new HashSet<String>();
            Set<String> altSpliceSiteUsageProteinNovel = new HashSet<String>();

            Set<String> allGene = new HashSet<String>();
            Set<String> exonUsageGene = new HashSet<String>();
            Set<String> spliceSiteUsageGene = new HashSet<String>();
            Set<String> altSpliceSiteUsageGene = new HashSet<String>();

            Set<String> allGeneKnown = new HashSet<String>();
            Set<String> exonUsageGeneKnown = new HashSet<String>();
            Set<String> spliceSiteUsageGeneKnown = new HashSet<String>();
            Set<String> altSpliceSiteUsageGeneKnown = new HashSet<String>();

            Set<String> allGeneNovel = new HashSet<String>();
            Set<String> exonUsageGeneNovel = new HashSet<String>();
            Set<String> spliceSiteUsageGeneNovel = new HashSet<String>();
            Set<String> altSpliceSiteUsageGeneNovel = new HashSet<String>();

            Set<String> allGeneAltSplice = new HashSet<String>();
            Set<String> exonUsageGeneAltSplice = new HashSet<String>();
            Set<String> spliceSiteUsageGeneAltSplice = new HashSet<String>();
            Set<String> altSpliceSiteUsageGeneAltSplice = new HashSet<String>();

            //ENSG00000198939 false   ENSP00000354453 1857228 282     290     exon_usage

            for (Iterator<String[]> rows = dtr.read(); rows.hasNext();) {

                String[] row = rows.next();

                String ensemblGeneId = row[0];
                String geneAltSplice = row[1];
                String ensemblProteinId = row[2];
                String sigPepId = row[3];
                String event = row[6];

                //get ensembl object status
                String geneStatus = ensemblGeneStatus.get(ensemblGeneId);
                String proteinStatus = ensemblTranslationStatus.get(ensemblProteinId);

                allSigPep.add(sigPepId);
                if (proteinStatus == null) {

                    logger.info("no status for " + ensemblProteinId);

                } else if (proteinStatus.equalsIgnoreCase("known")) {

                    allSigPepKnown.add(sigPepId);
                    allProteinKnown.add(ensemblProteinId);

                } else {

                    allSigPepNovel.add(sigPepId);
                    allProteinNovel.add(ensemblProteinId);

                }

                if (Boolean.parseBoolean(geneAltSplice)) {
                    allGeneAltSplice.add(ensemblGeneId);
                }

                if (geneStatus == null) {

                    logger.info("no status for " + ensemblGeneId);

                } else if (geneStatus.equalsIgnoreCase("known")) {

                    allGeneKnown.add(ensemblGeneId);

                } else {

                    allGeneNovel.add(ensemblGeneId);

                }


                if (event.startsWith("exon_usage")) {

                    exonUsageSigPep.add(sigPepId);
                    exonUsageProtein.add(ensemblProteinId);

                    if (proteinStatus == null) {

                        logger.info("no status for " + ensemblProteinId);

                    } else if (proteinStatus.equalsIgnoreCase("known")) {

                        exonUsageSigPepKnown.add(sigPepId);
                        exonUsageProteinKnown.add(ensemblProteinId);

                    } else {

                        exonUsageSigPepNovel.add(sigPepId);
                        exonUsageProteinNovel.add(ensemblProteinId);

                    }


                    exonUsageGene.add(ensemblGeneId);
                    if (Boolean.parseBoolean(geneAltSplice)) {
                        exonUsageGeneAltSplice.add(ensemblGeneId);
                    }

                    if (geneStatus == null) {

                        logger.info("no status for " + ensemblGeneId);

                    } else if (geneStatus.equalsIgnoreCase("known")) {

                        exonUsageGeneKnown.add(ensemblGeneId);

                    } else {

                        exonUsageGeneNovel.add(ensemblGeneId);

                    }

                } else if (event.startsWith("splice_site")) {

                    spliceSiteUsageSigPep.add(sigPepId);
                    spliceSiteUsageProtein.add(ensemblProteinId);

                    if (proteinStatus == null) {

                        logger.info("no status for " + ensemblProteinId);

                    } else if (proteinStatus.equalsIgnoreCase("known")) {

                        spliceSiteUsageSigPepKnown.add(sigPepId);
                        spliceSiteUsageProteinKnown.add(ensemblProteinId);

                    } else {

                        spliceSiteUsageSigPepNovel.add(sigPepId);
                        spliceSiteUsageProteinNovel.add(ensemblProteinId);

                    }


                    spliceSiteUsageGene.add(ensemblGeneId);
                    if (Boolean.parseBoolean(geneAltSplice)) {
                        spliceSiteUsageGeneAltSplice.add(ensemblGeneId);
                    }

                    if (geneStatus == null) {

                        logger.info("no status for " + ensemblGeneId);

                    } else if (geneStatus.equalsIgnoreCase("known")) {

                        spliceSiteUsageGeneKnown.add(ensemblGeneId);

                    } else {

                        spliceSiteUsageGeneNovel.add(ensemblGeneId);

                    }


                } else if (event.startsWith("alt_splice_site")) {

                    altSpliceSiteUsageSigPep.add(sigPepId);
                    altSpliceSiteUsageProtein.add(ensemblProteinId);

                    if (proteinStatus == null) {

                        logger.info("no status for " + ensemblProteinId);

                    } else if (proteinStatus.equalsIgnoreCase("known")) {

                        altSpliceSiteUsageSigPepKnown.add(sigPepId);
                        altSpliceSiteUsageProteinKnown.add(ensemblProteinId);

                    } else {

                        altSpliceSiteUsageSigPepNovel.add(sigPepId);
                        altSpliceSiteUsageProteinNovel.add(ensemblProteinId);

                    }


                    altSpliceSiteUsageGene.add(ensemblGeneId);
                    if (Boolean.parseBoolean(geneAltSplice)) {
                        altSpliceSiteUsageGeneAltSplice.add(ensemblGeneId);
                    }

                    if (geneStatus == null) {

                        logger.info("no status for " + ensemblGeneId);

                    } else if (geneStatus.equalsIgnoreCase("known")) {

                        altSpliceSiteUsageGeneKnown.add(ensemblGeneId);

                    } else {

                        altSpliceSiteUsageGeneNovel.add(ensemblGeneId);

                    }

                }

            }

            is.close();

            String[] header = new String[11];
            header[0] = "event";
            header[1] = "signature peptide";
            header[2] = "signature peptide known";
            header[3] = "signature peptide novel";
            header[4] = "protein total";
            header[5] = "protein known";
            header[6] = "proteni novel";
            header[7] = "gene total";
            header[8] = "gene alt splice";
            header[9] = "gene known";
            header[10] = "gene novel";
            dtw.writeHeader(header);

            String[] rowExonUsage = new String[11];
            rowExonUsage[0] = "exon_usage";
            rowExonUsage[1] = "" + exonUsageSigPep.size();
            rowExonUsage[2] = "" + exonUsageSigPepKnown.size();
            rowExonUsage[3] = "" + exonUsageSigPepNovel.size();
            rowExonUsage[4] = "" + exonUsageProtein.size();
            rowExonUsage[5] = "" + exonUsageProteinKnown.size();
            rowExonUsage[6] = "" + exonUsageProteinNovel.size();
            rowExonUsage[7] = "" + exonUsageGene.size();
            rowExonUsage[8] = "" + exonUsageGeneAltSplice.size();
            rowExonUsage[9] = "" + exonUsageGeneKnown.size();
            rowExonUsage[10] = "" + exonUsageGeneNovel.size();
            dtw.writeRow(rowExonUsage);

            String[] rowSpliceSiteUsage = new String[11];
            rowSpliceSiteUsage[0] = "splice_site";
            rowSpliceSiteUsage[1] = "" + spliceSiteUsageSigPep.size();
            rowSpliceSiteUsage[2] = "" + spliceSiteUsageSigPepKnown.size();
            rowSpliceSiteUsage[3] = "" + spliceSiteUsageSigPepNovel.size();
            rowSpliceSiteUsage[4] = "" + spliceSiteUsageProtein.size();
            rowSpliceSiteUsage[5] = "" + spliceSiteUsageProteinKnown.size();
            rowSpliceSiteUsage[6] = "" + spliceSiteUsageProteinNovel.size();
            rowSpliceSiteUsage[7] = "" + spliceSiteUsageGene.size();
            rowSpliceSiteUsage[8] = "" + spliceSiteUsageGeneAltSplice.size();
            rowSpliceSiteUsage[9] = "" + spliceSiteUsageGeneKnown.size();
            rowSpliceSiteUsage[10] = "" + spliceSiteUsageGeneNovel.size();
            dtw.writeRow(rowSpliceSiteUsage);

            String[] rowAltSpliceSiteUsage = new String[11];
            rowAltSpliceSiteUsage[0] = "alt_splice_site";
            rowAltSpliceSiteUsage[1] = "" + altSpliceSiteUsageSigPep.size();
            rowAltSpliceSiteUsage[2] = "" + altSpliceSiteUsageSigPepKnown.size();
            rowAltSpliceSiteUsage[3] = "" + altSpliceSiteUsageSigPepNovel.size();
            rowAltSpliceSiteUsage[4] = "" + altSpliceSiteUsageProtein.size();
            rowAltSpliceSiteUsage[5] = "" + altSpliceSiteUsageProteinKnown.size();
            rowAltSpliceSiteUsage[6] = "" + altSpliceSiteUsageProteinNovel.size();
            rowAltSpliceSiteUsage[7] = "" + altSpliceSiteUsageGene.size();
            rowAltSpliceSiteUsage[8] = "" + altSpliceSiteUsageGeneAltSplice.size();
            rowAltSpliceSiteUsage[9] = "" + altSpliceSiteUsageGeneKnown.size();
            rowAltSpliceSiteUsage[10] = "" + altSpliceSiteUsageGeneNovel.size();
            dtw.writeRow(rowAltSpliceSiteUsage);

            String[] rowAll = new String[11];
            rowAll[0] = "all";
            rowAll[1] = "" + allSigPep.size();
            rowAll[2] = "" + allSigPepKnown.size();
            rowAll[3] = "" + allSigPepNovel.size();
            rowAll[4] = "" + allProtein.size();
            rowAll[5] = "" + allProteinKnown.size();
            rowAll[6] = "" + allProteinNovel.size();
            rowAll[7] = "" + allGene.size();
            rowAll[8] = "" + allGeneAltSplice.size();
            rowAll[9] = "" + allGeneKnown.size();
            rowAll[10] = "" + allGeneNovel.size();
            dtw.writeRow(rowAll);


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (EnshException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


    }

    public static void main(String[] args) {

//        try {
//
//            Set<Integer> peptideIds = new HashSet<Integer>();
//            BufferedReader br = new BufferedReader(new FileReader("/home/mmueller/data/sigpep/barcodes_9606_zmin2_zmax2_acc1_tryp_mox_true.tab"));
//            String line;
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//
//                peptideIds.add(new Integer(line.split("\t")[0]));
//                count++;
//            }
//
//            String output = "/home/mmueller/data/sigpep/barcodes_9606_zmin2_zmax2_acc1_tryp_mox_true_splice_analysis_greater_19000.tab";
//            logger.info(peptideIds.size() + " peptide IDs");
//
//            SigPepDatabase sigPepDb = new SigPepDatabase(args[0], args[1].toCharArray(), new Integer(args[2]));
//
//            SpliceIsoformCoverage sic = new SpliceIsoformCoverage(sigPepDb);
//            sic.reportSpliceEventCoverage(peptideIds, new PrintWriter(output));
//
//        } catch (DatabaseException e) {
//            logger.error(e);
//        } catch (IOException e) {
//            logger.error(e);
//        }


        try {

            SigPepDatabase sigPepDb = new SigPepDatabase(args[0], args[1].toCharArray(), new Integer(args[2]));
            SpliceIsoformCoverage sic = new SpliceIsoformCoverage(sigPepDb);

            sic.createSpliceAnalysisSummary(
                    new File("/home/mmueller/data/sigpep/barcodes_9606_zmin2_zmax2_acc1_tryp_mox_true_splice_analysis.tab").toURI().toURL(),
                    new PrintWriter(System.out));

        } catch (DatabaseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
