package com.compomics.sigpep.analysis.query;

import org.apache.log4j.Logger;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import com.compomics.dbtools.DatabaseException;
import com.compomics.sigpep.persistence.rdbms.SigPepDatabase;

/**
 * @TODO: JavaDoc missing.
 * @TODO: All code in this class is commented out...
 * 
 * Created by IntelliJ IDEA.</br>
 * User: mmueller</br>
 * Date: 04-Sep-2007</br>
 * Time: 11:35:16</br>
 */
public class ProteinsWithoutSignaturePeptide  {

//    private static final String filenameProteinsWithoutSignaturePeptideSummary =
//            "proteins_without_sigpep_summary.tsv";
//
//    private static final String filenameSequencesWithoutSignaturePeptide =
//            "nr_sequences_without_sigpep.tsv";
//
//    private static final String SQL_SELECT_PROTEIN_COUNT =
//            "SELECT COUNT(DISTINCT protein_accession) FROM protein";
//
//    private static final String SQL_SELECT_GENE_COUNT =
//            "SELECT COUNT(DISTINCT gene_accession) FROM gene";
//
//    private static final String SQL_SELECT_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_COUNT =
//            "SELECT COUNT(DISTINCT pro.protein_accession)\n" +
//                    "FROM protein pro\n" +
//                    "WHERE pro.protein_id NOT IN (\n" +
//                    "SELECT protein_id FROM protein2signature_protease\n" +
//                    ")";
//
//    private static final String SQL_SELECT_GENES_WITHOUT_SIGNATURE_PEPTIDE_COUNT =
//            "SELECT COUNT(DISTINCT g.gene_accession)\n" +
//                    "FROM protein pro, \n" +
//                    "     protein2gene pro2g,  \n" +
//                    "     gene g\n" +
//                    "WHERE pro.protein_id = pro2g.protein_id\n" +
//                    "AND pro2g.gene_id = g.gene_id  \n" +
//                    "AND pro.protein_id NOT IN (\n" +
//                    "SELECT protein_id FROM protein2signature_protease\n" +
//                    ")";
//
//
//    private static final String SQL_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_REDUNDANDENT_SEQUENCES =
//            "SELECT COUNT(DISTINCT g.gene_accession) AS 'gene_count', \n" +
//                    "       COUNT(DISTINCT pro.protein_accession) AS 'protein_count', \n" +
//                    "       group_concat(DISTINCT g.gene_accession) AS 'gene_accessions',\n" +
//                    "       group_concat(DISTINCT pro.protein_accession) AS 'protein_accessions',\n" +
//                    "pro.aa_sequence\n" +
//                    "FROM protein pro, \n" +
//                    "     protein2gene pro2g,  \n" +
//                    "     gene g\n" +
//                    "WHERE pro.protein_id = pro2g.protein_id\n" +
//                    "AND pro2g.gene_id = g.gene_id  \n" +
//                    "AND pro.protein_id NOT IN (\n" +
//                    "SELECT protein_id FROM protein2signature_protease\n" +
//                    ")\n" +
//                    "GROUP BY pro.aa_sequence\n" +
//                    "HAVING COUNT(DISTINCT pro.protein_accession) > 1\n" +
//                    "ORDER BY gene_count DESC, protein_count DESC";
//
//    private static final String SQL_SELECT_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_NON_REDUNDANDENT_SEQUENCES =
//            "SELECT COUNT(DISTINCT g.gene_accession) AS 'gene_count', \n" +
//                    "       COUNT(DISTINCT pro.protein_accession) AS 'protein_count', \n" +
//                    "       group_concat(DISTINCT g.gene_accession) AS 'gene_accessions',\n" +
//                    "       group_concat(DISTINCT pro.protein_accession) AS 'protein_accessions',\n" +
//                    "pro.aa_sequence\n" +
//                    "FROM protein pro, \n" +
//                    "     protein2gene pro2g,  \n" +
//                    "     gene g\n" +
//                    "WHERE pro.protein_id = pro2g.protein_id\n" +
//                    "AND pro2g.gene_id = g.gene_id  \n" +
//                    "AND pro.protein_id NOT IN (\n" +
//                    "SELECT protein_id FROM protein2signature_protease\n" +
//                    ")\n" +
//                    "GROUP BY pro.aa_sequence\n" +
//                    "HAVING COUNT(DISTINCT pro.protein_accession) = 1\n" +
//                    "ORDER BY gene_count DESC, protein_count DESC";
//
//    private static final String SQL_SELECT_NON_REDUNDANDENT_SEQUENCES_OF_PROTEINS_WITHOUT_SIGNATURE_PEPTIDES =
//            "SELECT COUNT(DISTINCT g.gene_accession) AS 'gene_count', \n" +
//                    "       COUNT(DISTINCT pro.protein_accession) AS 'protein_count', \n" +
//                    "       group_concat(DISTINCT g.gene_accession) AS 'gene_accessions',\n" +
//                    "       group_concat(DISTINCT pro.protein_accession) AS 'protein_accessions',\n" +
//                    "pro.aa_sequence\n" +
//                    "FROM protein pro, \n" +
//                    "     protein2gene pro2g,  \n" +
//                    "     gene g\n" +
//                    "WHERE pro.protein_id = pro2g.protein_id\n" +
//                    "AND pro2g.gene_id = g.gene_id  \n" +
//                    "AND pro.protein_id NOT IN (\n" +
//                    "SELECT protein_id FROM protein2signature_protease\n" +
//                    ")\n" +
//                    "GROUP BY pro.aa_sequence\n" +
//                    "ORDER BY gene_count DESC, protein_count DESC";
//
//    //log4j logger
//    private static Logger logger = Logger.getLogger(ProteinsWithoutSignaturePeptide.class);
//
//    public ProteinsWithoutSignaturePeptide(String userName, char[] password, int ncbiTaxonId, String outputDirectory) throws DatabaseException {
//        super(userName, password, ncbiTaxonId, outputDirectory);
//    }
//
//    public int fetchGeneCountOrganism() throws SQLException {
//
//        int retVal = 0;
//
//        Statement s = getSigPepDatabase().getConnection().createStatement();
//        ResultSet rs = s.executeQuery(SQL_SELECT_GENE_COUNT);
//
//        if (rs.next())
//            retVal = rs.getInt(1);
//
//        return retVal;
//
//    }
//
//    public int fetchProteinCountOrganism() throws SQLException {
//
//        int retVal = 0;
//
//        Statement s = getSigPepDatabase().getConnection().createStatement();
//        ResultSet rs = s.executeQuery(SQL_SELECT_PROTEIN_COUNT);
//
//        if (rs.next())
//            retVal = rs.getInt(1);
//
//        return retVal;
//
//    }
//
//    public int fetchProteinWithoutSignaturePeptideCount() throws SQLException {
//
//        int retVal = 0;
//
//        Statement s = getSigPepDatabase().getConnection().createStatement();
//        ResultSet rs = s.executeQuery(SQL_SELECT_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_COUNT);
//
//        if (rs.next())
//            retVal = rs.getInt(1);
//
//        return retVal;
//
//    }
//
//    public int fetchGeneWithoutSignaturePeptideCount() throws SQLException {
//
//        int retVal = 0;
//
//        Statement s = getSigPepDatabase().getConnection().createStatement();
//        ResultSet rs = s.executeQuery(SQL_SELECT_GENES_WITHOUT_SIGNATURE_PEPTIDE_COUNT);
//
//        if (rs.next())
//            retVal = rs.getInt(1);
//
//        return retVal;
//
//    }
//
//    public void reportProteinSequencesWithoutSignaturePeptides(String targetDirectory) {
//
//        try {
//
//            logger.info("creating report of sequences without signature peptides...");
//
//            String outputFilename = targetDirectory + "/" + SigPepDatabase.getSpeciesSuffix(ncbiTaxonId) + "_" + filenameSequencesWithoutSignaturePeptide;
//
//            PrintWriter pw = new PrintWriter(outputFilename);
//
//            Statement s = getSigPepDatabase().getConnection().createStatement();
//            logger.info("querying database...");
//
//            ResultSet rs = s.executeQuery(SQL_SELECT_NON_REDUNDANDENT_SEQUENCES_OF_PROTEINS_WITHOUT_SIGNATURE_PEPTIDES);
//
//            logger.info("writing results to " + outputFilename + "...");
//
//            while (rs.next()) {
//
//                String sequence = rs.getString("aa_sequence");
//                int geneCount = rs.getInt("gene_count");
//                int proteinCount = rs.getInt("protein_count");
//
//                pw.println(sequence + "\t" + geneCount + "\t" + proteinCount);
//                pw.flush();
//
//            }
//
//            pw.close();
//            rs.close();
//            s.close();
//
//
//        } catch (SQLException e) {
//            logger.error(e);
//        } catch (FileNotFoundException e) {
//            logger.error(e);
//        }
//    }
//
//    public void reportProteinSequencesWithoutSignaturePeptidesSummary(String targetDirectory) {
//
//        try {
//
//            String outputFilename = targetDirectory + "/" + SigPepDatabase.getSpeciesSuffix(ncbiTaxonId) + "_" + filenameProteinsWithoutSignaturePeptideSummary;
//
//            logger.info("creating summary reprot for proteins without signature peptides...");
//
//            logger.info("querying database...");
//
//            int totalGeneCount = fetchProteinCountOrganism();
//            int totalProteinCount = fetchProteinCountOrganism();
//            int geneWithoutSignaturePeptideCount = fetchGeneWithoutSignaturePeptideCount();
//            int proteinWithoutSignaturePeptideCount = fetchProteinWithoutSignaturePeptideCount();
//
//            Set<String> geneWithRedundantSequence = new TreeSet<String>();
//            Set<String> proteinWithRedundantSequence = new TreeSet<String>();
//
//            Set<String> geneWithNonRedundantSequence = new TreeSet<String>();
//            Set<String> proteinWithNonRedundantSequence = new TreeSet<String>();
//
//            Set<String> sequences = new TreeSet<String>();
//
//            Statement s = getSigPepDatabase().getConnection().createStatement();
//            s.execute("SET group_concat_max_len = 2048");
//
//            ResultSet rs = s.executeQuery(SQL_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_REDUNDANDENT_SEQUENCES);
//
//            while (rs.next()) {
//
//                String sequence = rs.getString("aa_sequence");
//                sequences.add(sequence);
//
//                if (rs.getString("gene_accessions").split(",").length != rs.getInt("gene_count")) {
//                    System.out.println("unequal gene count: " + rs.getString("gene_accessions").split(",").length + " vs " + rs.getInt("gene_count"));
//                }
//
//                if (rs.getString("protein_accessions").split(",").length != rs.getInt("protein_count")) {
//                    System.out.println("unequal protein count: " + rs.getString("protein_accessions").split(",").length + " vs " + rs.getInt("protein_count"));
//                }
//
//                for (String geneAccession : rs.getString("gene_accessions").split(",")) {
//                    geneWithRedundantSequence.add(geneAccession);
//                }
//
//                for (String proteinAccession : rs.getString("protein_accessions").split(",")) {
//                    proteinWithRedundantSequence.add(proteinAccession);
//                }
//
//            }
//
//            rs.close();
//
//            rs = s.executeQuery(SQL_SELECT_PROTEINS_WITHOUT_SIGNATURE_PEPTIDE_NON_REDUNDANDENT_SEQUENCES);
//
//            while (rs.next()) {
//
//                String sequence = rs.getString("aa_sequence");
//                sequences.add(sequence);
//
//                for (String geneAccession : rs.getString("gene_accessions").split(",")) {
//                    geneWithNonRedundantSequence.add(geneAccession);
//                }
//
//                for (String proteinAccession : rs.getString("protein_accessions").split(",")) {
//                    proteinWithNonRedundantSequence.add(proteinAccession);
//                }
//
//            }
//
//            rs.close();
//            s.close();
//
//            logger.info("writing results to " + outputFilename + "...");
//
//            //write report
//            PrintWriter pw = new PrintWriter(outputFilename);
//
//            pw.println("Genes without signature peptide:\t" + geneWithoutSignaturePeptideCount + "\t(" + String.format("%.2f", ((double) geneWithoutSignaturePeptideCount / (double) totalGeneCount) * 100) + "%)");
//            pw.println("Proteins without signature peptide:\t" + proteinWithoutSignaturePeptideCount + "\t(" + String.format("%.2f", ((double) proteinWithoutSignaturePeptideCount / (double) totalProteinCount) * 100) + "%)");
//            pw.println("Genes without signature peptide because of redundant sequence:\t" + geneWithRedundantSequence.size() + "\t(" + String.format("%.2f", ((double) geneWithRedundantSequence.size() / (double) geneWithoutSignaturePeptideCount) * 100) + "%)");
//            pw.println("Proteins without signature peptide because of redundant sequence:\t" + proteinWithRedundantSequence.size() + "\t(" + String.format("%.2f", ((double) proteinWithRedundantSequence.size() / (double) proteinWithoutSignaturePeptideCount) * 100) + "%)");
//            pw.println("Genes without signature peptide but no redundant sequence:\t" + geneWithNonRedundantSequence.size() + "\t(" + String.format("%.2f", ((double) geneWithNonRedundantSequence.size() / (double) geneWithoutSignaturePeptideCount) * 100) + "%)");
//            pw.println("Proteins without signature peptide but no redundant sequence:\t" + proteinWithNonRedundantSequence.size() + "\t(" + String.format("%.2f", ((double) proteinWithNonRedundantSequence.size() / (double) proteinWithoutSignaturePeptideCount) * 100) + "%)");
//            pw.println("Proteins without signature peptide non-redundant sequence count:\t" + sequences.size());
//
//            pw.close();
//
//        } catch (SQLException e) {
//            logger.error(e);
//        } catch (FileNotFoundException e) {
//            logger.error(e);
//        }
//
//    }
//
//    public static void main(String[] args) {
//
//        ProteinsWithoutSignaturePeptide query;
//        try {
//
//            query = new ProteinsWithoutSignaturePeptide(
//                    args[0],
//                    args[1].toCharArray(),
//                    Integer.parseInt(args[2]),
//                    args[3]);
//
//
//            query.reportProteinSequencesWithoutSignaturePeptidesSummary(args[3]);
//            query.reportProteinSequencesWithoutSignaturePeptides(args[3]);
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

}
