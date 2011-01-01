package com.compomics.sigpep.analysis.query;

/**
 * @TODO: All code in this class is commented out...
 *
 * Provides methods to create various reports regarding the coverage of the proteome by
 * signature peptides.
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 31-Aug-2007<br>
 * Time: 10:17:52<br>
 */
public class ProteomeCoverage {

//    private static NamedQueryAccess namedQueries = NamedQueryAccess.getInstance();
//
//    /**
//     * the log4j logger
//     */
//    private static Logger logger = Logger.getLogger(ProteomeCoverage.class);
//
//    private static final String SQL_QUERY_PEPTIDE_COUNT_PROTEASE_COMBINATION = namedQueries.getString("query.peptideCountForProteaseCombination");
//    private static final String SQL_QUERY_SIGNATURE_PEPTIDE_COUNT_PROTEASE_COMBINATION = namedQueries.getString("query.signaturePeptideCountForProteaseCombination");
//    private static final String SQL_QUERY_SIGNATURE_PEPTIDE_ALTSPLICE_COUNT_PROTEASE_COMBINATION =
//            "    SELECT count(peptide_degeneracy.peptide_id) " +
//            "    FROM  " +
//            "    (SELECT pf.peptide_id, count(distinct pro2seq.sequence_id) as sequence_count " +
//            "    FROM peptide_feature pf, protease2peptide_feature pf2prot, protease prot, protein2sequence pro2seq, protein_altsplice alt " +
//            "    WHERE pf.peptide_feature_id=pf2prot.peptide_feature_id " +
//            "    AND pf2prot.protease_id=prot.protease_id " +
//            "    AND prot.name IN (:proteaseCombination) " +
//            "    AND pro2seq.sequence_id=pf.sequence_id " +
//            "    AND pro2seq.protein_id=alt.protein_id " +
//            "    GROUP BY pf.peptide_id) peptide_degeneracy " +
//            "    WHERE peptide_degeneracy.sequence_count = 1 " +
//            "    GROUP BY peptide_degeneracy.sequence_count";
//    private static final String SQL_QUERY_PROTEOME_COVERAGE_PROTEASE_COMBINATION = namedQueries.getString("query.proteomeCoverageForProteaseCombination");
//    private static final String SQL_QUERY_GENOME_COVERAGE_PROTEASE_COMBINATION = namedQueries.getString("query.genomeCoverageForProteaseCombination");
//    private static final String SQL_QUERY_PROTEOME_ALTSPLICE_COVERAGE_PROTEASE_COMBINATION =
//            "   SELECT count(distinct p2s.protein_id) as proteome_coverage " +
//            "     FROM  " +
//            "     (SELECT pf.peptide_id, count(distinct pf.sequence_id) as sequence_count " +
//            "     FROM peptide_feature pf, protease2peptide_feature pf2prot, protease prot " +
//            "     WHERE pf.peptide_feature_id=pf2prot.peptide_feature_id " +
//            "     AND pf2prot.protease_id=prot.protease_id " +
//            "     AND prot.name IN (:proteaseCombination) " +
//            "     GROUP BY pf.peptide_id) peptide_degeneracy, " +
//            "     peptide_feature pf, " +
//            "     protein2sequence p2s, " +
//            "     protein_altsplice " +
//            "     WHERE peptide_degeneracy.sequence_count = 1 " +
//            "      AND peptide_degeneracy.peptide_id=pf.peptide_id " +
//            "     AND pf.sequence_id=p2s.sequence_id " +
//            "     AND p2s.protein_id=protein_altsplice.protein_id";
//
//    private static final String SQL_QUERY_GENOME_ALTSPLICE_COVERAGE_PROTEASE_COMBINATION =
//            "    SELECT count(distinct g2pro.gene_id) as genome_coverage " +
//            "    FROM  " +
//            "    (SELECT pf.peptide_id, count(distinct g2pro.gene_id) as gene_count " +
//            "    FROM peptide_feature pf, protease2peptide_feature pf2prot, protease prot, protein2sequence pro2seq, gene2protein g2pro " +
//            "    WHERE pf.peptide_feature_id=pf2prot.peptide_feature_id " +
//            "    AND pf2prot.protease_id=prot.protease_id " +
//            "    AND prot.name IN (:proteaseCombination) " +
//            "    AND pro2seq.sequence_id=pf.sequence_id " +
//            "    AND pro2seq.protein_id=g2pro.protein_id " +
//            "    GROUP BY pf.peptide_id) peptide_degeneracy, " +
//            "    peptide_feature pf, " +
//            "    protein2sequence p2s, " +
//            "    gene2protein g2pro, " +
//            "    gene_altsplice " +
//            "    WHERE peptide_degeneracy.gene_count = 1 " +
//            "    AND peptide_degeneracy.peptide_id=pf.peptide_id " +
//            "    AND pf.sequence_id=p2s.sequence_id " +
//            "    AND p2s.protein_id=g2pro.protein_id " +
//            "    AND gene_altsplice.gene_id=g2pro.gene_id";
//
//
//    private static final String SQL_QUERY_SEQUENCE_COVERAGE_BY_PEPTIDE_SET = namedQueries.getString("query.sequenceCoverageByPeptideSet");
//
//    private static final String SQL_SELECT_MAX_ISOFORM_COUNT =
//            "SELECT gene_id, count(protein_id) AS 'isoform_count' \n" +
//                    "FROM protein2gene\n" +
//                    "GROUP BY gene_id\n" +
//                    "ORDER BY isoform_count DESC";
//
//    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_SPLICE_ISOFORM_COVERAGE =
//            "SELECT p2g.gene_id, \n" +
//                    "count(distinct p2g.protein_id) AS 'splice_isoform_count', \n" +
//                    "count(distinct p2sp.protein_id) AS 'splice_isoform_with_sigpep_count'\n" +
//                    "FROM protein2gene p2g\n" +
//                    "LEFT JOIN protein2signature_protease p2sp ON (p2g.protein_id=p2sp.protein_id)\n" +
//                    "GROUP BY p2g.gene_id";
//
//    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_SPLICE_ISOFORM_COVERAGE_BY_PROTEASE =
//            "SELECT p2g.gene_id, \n" +
//                    "count(distinct p2g.protein_id) AS 'splice_isoform_count', \n" +
//                    "count(distinct p2sp.protein_id) AS 'splice_isoform_with_sigpep_count'\n" +
//                    "FROM protein2gene p2g\n" +
//                    "LEFT JOIN protein2signature_protease p2sp ON (p2g.protein_id=p2sp.protein_id AND p2sp.protease_id = ?)\n" +
//                    "GROUP BY p2g.gene_id";
//
//    private SigPepDatabase sigPepDatabase;
//
//    /**
//     * Constructs an object to query SigPep for the coverage of the proteome by signature peptides.
//     *
//     * @param sigPepDatabase the SigPep database
//     * @throws DatabaseException if an exception occurs while communicating with the SigPep database
//     */
//    public ProteomeCoverage(SigPepDatabase sigPepDatabase) throws DatabaseException {
//        this.sigPepDatabase = sigPepDatabase;
//    }
//
////    public void reportPeptideSetProteomeCoverageSummary(String proteaseFilter, Set<Integer> peptideIds, String outputFileName) {
////
////        List<String> proteaseList = Arrays.asList(proteaseFilter.split(","));
////        String sql = SqlUtil.setParameterSet(SQL_QUERY_SEQUENCE_COVERAGE_BY_PEPTIDE_SET, "peptideIds", peptideIds);
////        sql = SqlUtil.setParameterSet(sql, "protease", new HashSet<String>(proteaseList));
////        reportProteomeCoverageSummary(sql, outputFileName);
////
////    }
////
////    public void reportSignaturePeptideProteomeCoverageSummary(String outputFilePath) {
////
////        reportProteomeCoverageSummary(outputFilePath);
////
////    }
//
//    /**
//     * Creates a report of the coverage of the proteome by signature peptides.
//     *
//     * @param outputFileName where the report goes
//     * @param proteaseNames  the names of the proteaseFilter to combine
//     */
//    public void reportProteomeCoverageByProteaseCombination(String outputFileName, Set<String> proteaseNames) {
//
//        logger.info("creating signature peptide coverage report...");
//
//        SigPepQueryService query = new SigPepQuery(this.sigPepDatabase);
//        int totalProteinCount = query.fetchProteinCount();
//        int totalGeneCount = query.fetchGeneCount();
//        int totalProteinAltspliceCount = -1;
//        int totalGeneAltspliceCount = -1;
//
//        try {
//
//            Connection c = this.sigPepDatabase.getConnection();
//            Statement s = c.createStatement();
//
//            //create temporary tables
//            s.execute("CREATE TEMPORARY TABLE gene_altsplice(gene_id INT UNSIGNED PRIMARY KEY)");
//            s.execute("CREATE TEMPORARY TABLE protein_altsplice(protein_id INT UNSIGNED PRIMARY KEY)");
//
//            //alternatively spliced genes (sequence level)
//            s.execute("INSERT INTO gene_altsplice(gene_id)" +
//                    "   SELECT g2pro.gene_id " +
//                    "    FROM gene2protein g2pro," +
//                    "         protein2sequence pro2seq " +
//                    "   WHERE g2pro.protein_id=pro2seq.protein_id" +
//                    "    GROUP BY g2pro.gene_id " +
//                    "    HAVING count(pro2seq.sequence_id) > 1");
//
//            s.execute("INSERT INTO protein_altsplice(protein_id) " +
//                    " SELECT protein_id " +
//                    " FROM gene2protein " +
//                    " WHERE gene_id IN ( " +
//                    " SELECT gene_id " +
//                    " FROM gene_altsplice " +
//                    " )");
//
//            ResultSet rsTotalProteinAtlspliceCount = s.executeQuery("SELECT count(protein_id) FROM protein_altsplice");
//            if(rsTotalProteinAtlspliceCount.next()){
//                totalProteinAltspliceCount = rsTotalProteinAtlspliceCount.getInt(1);
//            }
//            rsTotalProteinAtlspliceCount.close();
//
//            ResultSet rsTotalGeneAltspliceCount = s.executeQuery("SELECT count(gene_id) FROM gene_altsplice");
//            if(rsTotalGeneAltspliceCount.next()){
//                totalGeneAltspliceCount = rsTotalGeneAltspliceCount.getInt(1);
//            }
//            rsTotalGeneAltspliceCount.close();
//
//            PrintWriter pw = new PrintWriter(outputFileName);
//
//            //write table header
//            pw.println("protease_combination\t" +
//                    "sig_pep_freq_abs\t" +
//                    "sig_pep_altsplicefreq_abs\t" +
//                    "pep_freq_abs\t" +
//                    "proteome_coverage_abs\t" +
//                    "proteome_size\t" +
//                    "proteome_coverage_altsplice_abs\t" +
//                    "proteome_altsplice_size\t" +
//                    "genome_coverage_abs\t" +
//                    "genome_size\t" +
//                    "genome_coverage_altsplice_abs\t" +
//                    "genome_altsplice_size");
//
//            logger.info("querying database...");
//
//            for (int size = 1; size <= proteaseNames.size(); size++) {
//
//                Combinations combinations = new Combinations(proteaseNames, size);
//
//                while (combinations.hasMoreElements()) {
//
//                    Object[] combination = combinations.nextElement();
//
//                    Set<Object> proteaseCombination = new HashSet<Object>();
//                    Collections.addAll(proteaseCombination, combination);
//
//                    logger.info("protease combination: " + proteaseCombination);
//
//                    String sqlPeptideCount = SqlUtil.setParameterSet(SQL_QUERY_PEPTIDE_COUNT_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//                    String sqlSignaturePeptideCount = SqlUtil.setParameterSet(SQL_QUERY_SIGNATURE_PEPTIDE_COUNT_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//                    String sqlSignaturePeptideAltspliceCount = SqlUtil.setParameterSet(SQL_QUERY_SIGNATURE_PEPTIDE_ALTSPLICE_COUNT_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//
//                    String sqlProteomeCoverage = SqlUtil.setParameterSet(SQL_QUERY_PROTEOME_COVERAGE_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//                    String sqlProteomeAltspliceCoverage = SqlUtil.setParameterSet(SQL_QUERY_PROTEOME_ALTSPLICE_COVERAGE_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//
//                    String sqlGenomeCoverage = SqlUtil.setParameterSet(SQL_QUERY_GENOME_COVERAGE_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//                    String sqlGenomeAltspliceCoverage = SqlUtil.setParameterSet(SQL_QUERY_GENOME_ALTSPLICE_COVERAGE_PROTEASE_COMBINATION, "proteaseCombination", proteaseCombination);
//
//                    int peptideCount = -1;
//                    int signaturePeptideCount = -1;
//                    int signaturePeptideAltspliceCount = -1;
//                    int proteomeCoverage = -1;
//                    int genomeCoverage = -1;
//                    int proteomeAltspliceCoverage = -1;
//                    int genomeAltspliceCoverage = -1;
//
//                    try {
//                        ResultSet rsPeptideCount = s.executeQuery(sqlPeptideCount);
//                        if (rsPeptideCount.next()) {
//                            peptideCount = rsPeptideCount.getInt(1);
//                        }
//                        rsPeptideCount.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//                    try {
//                        ResultSet rsSignaturePeptideCount = s.executeQuery(sqlSignaturePeptideCount);
//                        if (rsSignaturePeptideCount.next()) {
//                            signaturePeptideCount = rsSignaturePeptideCount.getInt(1);
//                        }
//                        rsSignaturePeptideCount.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//                    try {
//                        ResultSet rsSignaturePeptideAltspliceCount = s.executeQuery(sqlSignaturePeptideAltspliceCount);
//                        if (rsSignaturePeptideAltspliceCount.next()) {
//                            signaturePeptideAltspliceCount = rsSignaturePeptideAltspliceCount.getInt(1);
//                        }
//                        rsSignaturePeptideAltspliceCount.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//
//                    try {
//                        ResultSet rsProteomeCoverage = s.executeQuery(sqlProteomeCoverage);
//                        if (rsProteomeCoverage.next()) {
//                            proteomeCoverage = rsProteomeCoverage.getInt(1);
//                        }
//                        rsProteomeCoverage.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//                    try {
//                        ResultSet rsProteomeAltspliceCoverage = s.executeQuery(sqlProteomeAltspliceCoverage);
//                        if (rsProteomeAltspliceCoverage.next()) {
//                            proteomeAltspliceCoverage = rsProteomeAltspliceCoverage.getInt(1);
//                        }
//                        rsProteomeAltspliceCoverage.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//
//                    try {
//                        ResultSet rsGenomeCoverage = s.executeQuery(sqlGenomeCoverage);
//                        if (rsGenomeCoverage.next()) {
//                            genomeCoverage = rsGenomeCoverage.getInt(1);
//                        }
//                        rsGenomeCoverage.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//
//
//                    try {
//                        ResultSet rsGenomeAltspliceCoverage = s.executeQuery(sqlGenomeAltspliceCoverage);
//                        if (rsGenomeAltspliceCoverage.next()) {
//                            genomeAltspliceCoverage = rsGenomeAltspliceCoverage.getInt(1);
//                        }
//                        rsGenomeAltspliceCoverage.close();
//                    } catch (SQLException e) {
//                        logger.error(e);
//                    }
//
//
//                    logger.info("writing result to file '" + outputFileName + "'...");
//
//                    //get result row
//
//                    //peptides
//                    String protease = Arrays.toString(combination);
//                    int signaturePeptideFrequencyAbsolute = signaturePeptideCount;
//                    int signaturePeptideAltspliceFrequencyAbsolute = signaturePeptideAltspliceCount;
//                    int peptideFrequencyAbsolute = peptideCount;
//
//                    //proteins
//                    int proteomeCoverageAbsolute = proteomeCoverage;
//                    int proteomeSize = totalProteinCount;
//                    int proteomeAltspliceCoverageAbsolute = proteomeAltspliceCoverage;
//                    int proteomeAltspliceSize = totalProteinAltspliceCount;
//
//                    //genes
//                    int genomeCoverageAbsolute = genomeCoverage;
//                    int genomeSize = totalGeneCount;
//                    int genomeAltspliceCoverageAbsolute = genomeAltspliceCoverage;
//                    int genomeAltspliceSize = totalGeneAltspliceCount;
//
//                    //write to output file
//                    pw.println(protease + "\t" +
//                            signaturePeptideFrequencyAbsolute + "\t" +
//                            signaturePeptideAltspliceFrequencyAbsolute + "\t" +
//                            peptideFrequencyAbsolute + "\t" +
//                            proteomeCoverageAbsolute + "\t" +
//                            proteomeSize + "\t" +
//                            proteomeAltspliceCoverageAbsolute + "\t" +
//                            proteomeAltspliceSize + "\t" +
//                            genomeCoverageAbsolute + "\t" +
//                            genomeSize + "\t" +
//                            genomeAltspliceCoverageAbsolute + "\t" +
//                            genomeAltspliceSize);
//                    pw.flush();
//
//                    //clean up
//
//
//                }
//            }
//
//            pw.close();
//            s.close();
//            c.close();
//
//            logger.info("done");
//
//        } catch (FileNotFoundException e) {
//            logger.error(e);
//        } catch (SQLException e) {
//            logger.error(e);
//        }
//
//    }
//
//    /**
//     * Creates a report of the coverage of splice isoforms by signature peptides.
//     */
//    public void reportSignaturePeptideSpliceIsoformCoverageSummary
//            (String
//                    outputFileName) {
//
//        try {
//
//            Statement s = this.sigPepDatabase.getConnection().createStatement();
//            //get max isoform count
//            ResultSet rsMaxIsoformCount = s.executeQuery(SQL_SELECT_MAX_ISOFORM_COUNT);
//            rsMaxIsoformCount.next();
//            int maxIsoformCount = rsMaxIsoformCount.getInt("isoform_count");
//
//            int[][] cases = new int[maxIsoformCount + 1][maxIsoformCount + 1];
//
//            ResultSet rsSpliceIsoformCoverage = s.executeQuery(SQL_SELECT_SIGNATURE_PEPTIDE_SPLICE_ISOFORM_COVERAGE);
//
//            while (rsSpliceIsoformCoverage.next()) {
//
//                int spliceIsoformCount = rsSpliceIsoformCoverage.getInt("splice_isoform_count");
//                int spliceIsoformWithSigPepCount = rsSpliceIsoformCoverage.getInt("splice_isoform_with_sigpep_count");
//
//                cases[spliceIsoformCount][spliceIsoformWithSigPepCount]++;
//
//            }
//
//            rsSpliceIsoformCoverage.close();
//
//            s.close();
//
//            PrintWriter pw = new PrintWriter(outputFileName);
//
//            for (int i = 1; i < cases.length; i++) {
//
//                for (int j = 1; j <= i; j++) {
//
//                    pw.println(i + "\t" + j + "\t" + cases[i][j]);
//                    pw.flush();
//
//                }
//
//            }
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
//    /**
//     * Creates a report of the coverage of splice isoforms by signature peptides for the
//     * individual proteaseFilter.
//     */
//    public void reportSignaturePeptideSpliceIsoformCoverageSummaryByProtease
//            (String
//                    outputFileName) {
//
//        try {
//
//            Statement s = this.sigPepDatabase.getConnection().createStatement();
//            //get max isoform count
//            ResultSet rsMaxIsoformCount = s.executeQuery(SQL_SELECT_MAX_ISOFORM_COUNT);
//            rsMaxIsoformCount.next();
//            int maxIsoformCount = rsMaxIsoformCount.getInt("isoform_count");
//
//            int[][] cases = new int[maxIsoformCount + 1][maxIsoformCount + 1];
//
//            ResultSet rsSpliceIsoformCoverage = s.executeQuery(SQL_SELECT_SIGNATURE_PEPTIDE_SPLICE_ISOFORM_COVERAGE);
//
//            while (rsSpliceIsoformCoverage.next()) {
//
//                int spliceIsoformCount = rsSpliceIsoformCoverage.getInt("splice_isoform_count");
//                int spliceIsoformWithSigPepCount = rsSpliceIsoformCoverage.getInt("splice_isoform_with_sigpep_count");
//
//                cases[spliceIsoformCount][spliceIsoformWithSigPepCount]++;
//
//            }
//
//            rsSpliceIsoformCoverage.close();
//
//            s.close();
//
//            PrintWriter pw = new PrintWriter(outputFileName);
//
//            for (int i = 1; i < cases.length; i++) {
//
//                for (int j = 1; j <= i; j++) {
//
//                    System.out.println(i + "\t" + j + "\t" + cases[i][j]);
//
//                }
//
//            }
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
//
//    public static void main
//            (String
//                    args[]) {
//
//        try {
//
//            int[] taxonIds = new int[]{9606};//, 10090, 10116, 4932, 6239, 7227, 3702, 7955};
//
//
//            for (int taxonId : taxonIds) {
//
//                SigPepDatabase sigPepDb = new SigPepDatabase( "mmueller", "".toCharArray(),taxonId);
//
//                ProteomeCoverage pcq = new ProteomeCoverage(sigPepDb);
//
//                Set<String> proteaseNames = new HashSet<String>();
//                proteaseNames.add("tryp");
//                proteaseNames.add("v8de");
//                proteaseNames.add("v8e");
//                proteaseNames.add("lysc");
//                proteaseNames.add("argc");
//                proteaseNames.add("pepa");
//
//                pcq.reportProteomeCoverageByProteaseCombination("/home/mmueller/svn/manuscripts/sigpep/data/proteome_coverage_by_protease_" + taxonId + "_new.csv", proteaseNames);
//
//            }
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

}
