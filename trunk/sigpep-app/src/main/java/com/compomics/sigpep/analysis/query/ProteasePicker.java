package com.compomics.sigpep.analysis.query;

/**
 * @TODO: JavaDoc missing.
 * @TODO: All code in this class is commented out...
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 15-Oct-2007<br>
 * Time: 09:26:00<br>
 */
public class ProteasePicker {

//    private static final String DEFAULT_PROTEASES = "cnbr,lysc,argc,tryp,pepa,v8de,v8e";
//
//    protected static NamedQueryAccess namedQueries = NamedQueryAccess.getInstance();
//
//    private static final String SQL_SELECT_SIGPEP_COUNT_BY_PROTEASE_PROTEOME = namedQueries.getString("query.sigPepCoverageByProteaseProteome");
//    private static final String SQL_SELECT_SIGPEP_COUNT_BY_PROTEASE_GENOME = namedQueries.getString("query.sigPepCoverageByProteaseGenome");
//
//    private static final String SQL_SELECT_SIGPEP_COUNT_FOR_PROTEASE_COMBINATION = namedQueries.getString("query.signaturePeptideCountForProteaseCombination");
//    private static final String SQL_SELECT_PEPTIDE_COUNT_FOR_PROTEASE_COMBINATION = namedQueries.getString("query.peptideCountForProteaseCombination");
//
//
//
//
//    public ProteasePicker(DataSource sigpepDatabase) throws DatabaseException {
//        super(sigpepDatabase);
//    }
//
//    /**
//     * Returns the number of proteins covered by signature peptides for each
//     * combination of proteaseFilter.
//     *
//     * @return a map with the the protease combinations as keys and the number of proteins with signature peptide as value
//     */
//    public Map<Object[], Integer> getProteomeCoverageByProteaseCombination(Set<String> proteaseNames) {
//
//        //get the coverage of proteins by signature peptides
//        int[][] sigPepProteomeCoverageCount = this.fetchSigPepCountByProteaseProteome(proteaseNames);
//        return getObjectCoverageByProteaseCombination(sigPepProteomeCoverageCount);
//
//    }
//
//    /**
//     * Returns the number of genes covered by signature peptides for each
//     * combination of proteaseFilter.
//     *
//     * @return a map with the the protease combinations as keys and the number of proteins with signature peptide as value
//     */
//    public Map<Object[], Integer> getGenomeCoverageByProteaseCombination(Set<String> proteaseNames) {
//
//        //get the coverage of proteins by signature peptides
//        int[][] sigPepProteomeCoverageCount = this.fetchSigPepCountByProteaseGenome(proteaseNames);
//        return getObjectCoverageByProteaseCombination(sigPepProteomeCoverageCount);
//
//    }
//
//    /**
//     * Returns the number of objects covered by signature peptides for each
//     * combination of proteaseFilter.
//     *
//     * @param sigPepObjectCoverageCount array returned by fetchSigPepCountByProteaseProteome() or fetchSigPepCountByProteaseGenome()
//     * @return a map with the the protease combinations as keys and the number of objects with signature peptide as value
//     */
//    private Map<Object[], Integer> getObjectCoverageByProteaseCombination(int[][] sigPepObjectCoverageCount) {
//
//        Map<Object[], Integer> retVal = new HashMap<Object[], Integer>();
//
//        //convert count to binary true or false
//        int[][] sigPepObjectCoverageBinary = this.countToBinary(sigPepObjectCoverageCount);
//
//        //get protease indexes
//        Set<Integer> proteaseIndexes = new TreeSet<Integer>();
//        for (int i = 1; i < sigPepObjectCoverageBinary.length; i++) {
//            proteaseIndexes.add(i);
//        }
//
//        //get combinations of indicies for all possible combination sizes k
//        Set<Object[]> proteaseIndexCombinations = this.getAllCombinations(proteaseIndexes);
//
//        //check proteome coverage of every combination
//        for (Object[] combination : proteaseIndexCombinations) {
//
//            //get proteome coverage for combination
//            int[][] coverageMatrix = new int[combination.length][];
//            int c = 0;
//            for (int i = 0; i < combination.length; i++) {
//                Integer index = (Integer) combination[i];
//                coverageMatrix[c] = sigPepObjectCoverageBinary[index];
//                c++;
//            }
//
//            //get overall coverage for combination
//            int[] combinationCoverage = ProteasePicker.mergColumnsBinaryMatrix(coverageMatrix);
//            int combinationCoverageSum = SigPepUtil.sumArray(combinationCoverage);
//
//            //add combination and coverage to return value
//            retVal.put(combination, combinationCoverageSum);
//
//
//        }
//
//        return retVal;
//
//    }
//
//    /**
//     * Returns combinations for all possible combination sizes k for a set of
//     * objects.
//     *
//     * @param objects the object to combine
//     * @return the combinations
//     */
//    private Set<Object[]> getAllCombinations(Set<?> objects) {
//
//        Set<Object[]> retVal = new HashSet<Object[]>();
//
//        for (int k = 1; k <= objects.size(); k++) {
//            Combinations combinations = new Combinations(objects, k);
//            while (combinations.hasMoreElements()) {
//                retVal.add(combinations.nextElement());
//            }
//        }
//
//        return retVal;
//
//    }
//
//    /**
//     * Returns the coverage of proteins by signature peptides as a two dimensional
//     * array.
//     * <p/>
//     * The indexes of the first dimension correspond to the SigPep database ID
//     * of the protease and the indexes of the second dimension corresponding to the
//     * SigPep database ID of the protein. As protease and protein IDs start with 1
//     * the length of the first dimension is protease count + 1 and the length of
//     * the second dimension is max(protein_id) + 1.
//     *
//     * @param proteaseNames  the protease to fetch the coverage for
//     * @return the protein coverage
//     */
//    public int[][] fetchSigPepCountByProteaseProteome(Set<String> proteaseNames) {
//
//        //initialize array
//        int m = fetchProteaseCount() + 1;
//        int n = fetchLastProteinId() + 1;
//        int[][] retVal = new int[m][n];
//
//        try {
//
//            //get connection and statement
//            Connection con = this.getSigPepDatabase().getConnection();
//            Statement s = con.createStatement();
//            String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGPEP_COUNT_BY_PROTEASE_PROTEOME, "proteaseNames", proteaseNames);
//
//            //fetch result and fill array
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()) {
//
//                int proteaseId = rs.getInt("protease_id");
//                int proteinId = rs.getInt("protein_id");
//                int sigPepCount = rs.getInt("count");
//
//                retVal[proteaseId][proteinId] = sigPepCount;
//
//            }
//
//            //clean up
//            rs.close();
//            con.close();
//            s.close();
//
//        } catch (SQLException e) {
//            logger.error("Exception while querying database.", e);
//        }
//
//        return retVal;
//
//    }
//
//    /**
//     * Returns the coverage of genes by signature peptides as a two dimensional
//     * array.
//     * <p/>
//     * The indexes of the first dimension correspond to the SigPep database ID
//     * of the protease and the indexes of the second dimension corresponding to the
//     * SigPep database ID of the protein. As protease and gene IDs start with 1
//     * the length of the first dimension is protease count + 1 and the length of
//     * the second dimension is max(protein_id) + 1.
//     *
//     * @return the gene coverage
//     */
//    public int[][] fetchSigPepCountByProteaseGenome(Set<String> proteaseNames) {
//
//        //initialize array
//        int m = fetchProteaseCount() + 1;
//        int n = fetchLastGeneId() + 1;
//        int[][] retVal = new int[m][n];
//
//        try {
//
//            //get connection and statement
//            Connection con = this.getSigPepDatabase().getConnection();
//            Statement s = con.createStatement();
//            String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGPEP_COUNT_BY_PROTEASE_GENOME, "proteaseNames", proteaseNames);
//
//            //fetch result and fill array
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()) {
//
//                int proteaseId = rs.getInt("protease_id");
//                int proteinId = rs.getInt("gene_id");
//                int sigPepCount = rs.getInt("count");
//
//                retVal[proteaseId][proteinId] = sigPepCount;
//
//            }
//
//            //clean up
//            rs.close();
//            con.close();
//            s.close();
//
//        } catch (SQLException e) {
//            logger.error("Exception while querying database.", e);
//        }
//
//        return retVal;
//
//    }
//
//
//    /**
//     * Converts a two dimensional array of integer values into a binary array of zeros.
//     * <p/>
//     * For values in the input array > 0 the output will contain a 1 and for
//     * values < 0 the output array will contain a 0.
//     *
//     * @param array two dimensional array of integer values
//     * @return two dimensional array of zeros and ones
//     */
//    public int[][] countToBinary(int[][] array) {
//
//        int[][] retVal = new int[array.length][];
//        for (int i = 0; i < array.length; i++) {
//            retVal[i] = new int[array[i].length];
//            for (int j = 0; j < array[i].length; j++) {
//                if (array[i][j] > 0) retVal[i][j] = 1;
//            }
//        }
//        return retVal;
//    }
//
//    /**
//     * Merges the second dimension of a two dimensional binary array
//     * of zeros and ones and returns the merged values in a one
//     * dimensional array.
//     * <p/>
//     * The value at index i of the merged array will be 0 if all values
//     * at index i of the second dimension of the input array are zero.
//     * <p/>
//     * The arrays of the second dimension have to be of equal length.
//     *
//     * @param array an array of 0's and 1's
//     * @return the merged array
//     */
//    public static int[] mergColumnsBinaryMatrix(int[][] array) {
//
//        if (array.length == 0)
//            return new int[0];
//
//        int length = array[0].length;
//        for (int i = 1; i < array.length; i++)
//            if (array[i].length != length)
//                throw new IllegalArgumentException("All arrays of the second dimension have to be of equal length.");
//
//        int[] retVal = new int[array[0].length];
//        for (int i = 0; i < array.length; i++) {
//            //if at least one of the values in row j is greater 0 the value of j
//            //of the return value will be 1
//            for (int j = 0; j < array[i].length; j++) {
//                if (array[i][j] > 0) retVal[j] = 1;
//            }
//        }
//        return retVal;
//    }
//
//    /**
//     * Merges the second dimension of a two dimensional integer array
//     * and returns the merged values in a one dimensional array.
//     * <p/>
//     * The value at index i of the merged array will be the sum of values
//     * at index i of the second dimension of the input array.
//     * <p/>
//     * The arrays of the second dimension have to be of equal length.
//     *
//     * @param array an array of 0's and 1's
//     * @return the merged array
//     */
//    public static int[] mergColumnsIntegerMatrix(int[][] array) {
//
//        if (array.length == 0)
//            return new int[0];
//
//        int length = array[0].length;
//        for (int i = 1; i < array.length; i++)
//            if (array[i].length != length)
//                throw new IllegalArgumentException("All arrays of the second dimension have to be of equal length.");
//
//        int[] retVal = new int[array[0].length];
//        for (int i = 0; i < array.length; i++) {
//            //if at least one of the values in row j is greater 0 the value of j
//            //of the return value will be 1
//            for (int j = 0; j < array[i].length; j++) {
//                retVal[j] = retVal[j] + array[i][j];
//            }
//        }
//        return retVal;
//    }
//
//    /**
//     * Returns the number of signature peptides generated by each possible combination of
//     * proteaseFilter as a map with protease combinations as key and the signature peptide
//     * frequency as value.
//     *
//     * @return the signature peptide count for each protease combination
//     */
//    public Map<Object[], Integer> getSignaturePeptideCountByProteaseCombination() {
//
//        Map<Object[], Integer> retVal = new HashMap<Object[], Integer>();
//
//        try {
//
//            //get connection and statement
//            Connection con = this.getSigPepDatabase().getConnection();
//            Statement s = con.createStatement();
//
//            //get combinations of indicies for all possible combination sizes k
//            Set<Integer> proteaseIndexes = this.fetchProteaseIds().keySet();
//
//            Set<Object[]> proteaseIdCombinations = this.getAllCombinations(proteaseIndexes);
//
//            //check proteome coverage of every combination
//            for (Object[] combination : proteaseIdCombinations) {
//
//                Set<Integer> ids = new HashSet<Integer>();
//                for (int i = 0; i < combination.length; i++) {
//                    ids.add((Integer) combination[i]);
//                }
//
//                //fetch result and fill array
//                String sql = SqlUtil.setParameterSet(SQL_SELECT_SIGPEP_COUNT_FOR_PROTEASE_COMBINATION, "proteaseIds", ids);
//
//                ResultSet rs = s.executeQuery(sql);
//                if (rs.next()) {
//                    int count = rs.getInt("count");
//                    retVal.put(combination, count);
//                }
//
//                rs.close();
//
//            }
//
//            //clean up
//
//            con.close();
//            s.close();
//
//        } catch (SQLException e) {
//            logger.error("Exception while fetching signature peptide count for protease combination.");
//        }
//
//        return retVal;
//    }
//
//    /**
//     * Returns the number of signature peptides generated by each possible combination of
//     * proteaseFilter as a map with protease combinations as key and the signature peptide
//     * frequency as value.
//     *
//     * @return the signature peptide count for each protease combination
//     */
//    public Map<Object[], Integer> getPeptideCountByProteaseCombination() {
//
//        Map<Object[], Integer> retVal = new HashMap<Object[], Integer>();
//
//        try {
//
//            //get connection and statement
//            Connection con = this.getSigPepDatabase().getConnection();
//            Statement s = con.createStatement();
//
//            //get combinations of indicies for all possible combination sizes k
//            Set<Integer> proteaseIndexes = this.fetchProteaseIds().keySet();
//
//            Set<Object[]> proteaseIdCombinations = this.getAllCombinations(proteaseIndexes);
//
//            //check proteome coverage of every combination
//            for (Object[] combination : proteaseIdCombinations) {
//
//                Set<Integer> ids = new HashSet<Integer>();
//                for (int i = 0; i < combination.length; i++) {
//                    ids.add((Integer) combination[i]);
//                }
//
//                //fetch result and fill array
//                String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDE_COUNT_FOR_PROTEASE_COMBINATION, "proteaseIds", ids);
//
//                ResultSet rs = s.executeQuery(sql);
//                if (rs.next()) {
//                    int count = rs.getInt("count");
//                    retVal.put(combination, count);
//                }
//
//                rs.close();
//
//            }
//
//            //clean up
//
//            con.close();
//            s.close();
//
//        } catch (SQLException e) {
//            logger.error("Exception while fetching signature peptide count for protease combination.");
//        }
//
//        return retVal;
//    }
//
//    /**
//     * @param proteaseFilter      a comma separated list of protease short names
//     * @param outputFileName the name of the outputfile
//     */
//    public void reportSignaturePeptideCoverageByProteaseCombination(String proteaseFilter, String outputFileName) {
//
//        if(proteaseFilter == null){
//
//            proteaseFilter = DEFAULT_PROTEASES;
//
//        }
//
//        Set<String> proteaseNames = new HashSet<String>(Arrays.asList(proteaseFilter.split(",")));
//
//        logger.info("creating report for proteome/genome coverage by protease combination for species " + Organisms.getInstance().getOrganism(this.getSigPepDatabase().getTaxonId()) + " ...");
//
//        try {
//
//            logger.info("fetching protease IDs...");
//            Map<Integer, String> proteaseIds = this.fetchProteaseIds();
//
//            logger.info("fetching protein count...");
//            int proteinCount = this.fetchProteinCount();
//            logger.info("fetching gene count...");
//            int geneCount = this.fetchGeneCount();
//            logger.info("fetching proteome coverage...");
//            Map<Object[], Integer> proteomeCoverage = this.getProteomeCoverageByProteaseCombination(proteaseNames);
//            Map<String, Integer> proteomeCoverageStringKey = new HashMap<String, Integer>();
//            for (Object[] key : proteomeCoverage.keySet()) {
//                StringBuffer stringKey = new StringBuffer();
//                for (Object proteaseId : key) {
//                    Integer id = (Integer) proteaseId;
//                    String protease = proteaseIds.get(id);
//                    stringKey.append(protease).append(" ");
//                }
//
//                proteomeCoverageStringKey.put(stringKey.toString(), proteomeCoverage.get(key));
//            }
//            logger.info("fetching genome coverage...");
//            Map<Object[], Integer> genomeCoverage = this.getGenomeCoverageByProteaseCombination(proteaseNames);
//            Map<String, Integer> genomeCoverageStringKey = new HashMap<String, Integer>();
//            for (Object[] key : genomeCoverage.keySet()) {
//                StringBuffer stringKey = new StringBuffer();
//                for (Object proteaseId : key) {
//                    Integer id = (Integer) proteaseId;
//                    String protease = proteaseIds.get(id);
//                    stringKey.append(protease).append(" ");
//                }
//
//                genomeCoverageStringKey.put(stringKey.toString(), genomeCoverage.get(key));
//            }
//            logger.info("fetching signature peptide count...");
//            Map<Object[], Integer> signaturePeptideCount = this.getSignaturePeptideCountByProteaseCombination();
//            Map<String, Integer> signaturePeptideCountStringKey = new HashMap<String, Integer>();
//            for (Object[] key : signaturePeptideCount.keySet()) {
//                StringBuffer stringKey = new StringBuffer();
//                for (Object proteaseId : key) {
//                    Integer id = (Integer) proteaseId;
//                    String protease = proteaseIds.get(id);
//                    stringKey.append(protease).append(" ");
//                }
//
//                signaturePeptideCountStringKey.put(stringKey.toString(), signaturePeptideCount.get(key));
//            }
//
//            logger.info("fetching peptide count...");
//            Map<Object[], Integer> peptideCount = this.getPeptideCountByProteaseCombination();
//            Map<String, Integer> peptideCountStringKey = new HashMap<String, Integer>();
//            for (Object[] key : peptideCount.keySet()) {
//                StringBuffer stringKey = new StringBuffer();
//                for (Object proteaseId : key) {
//                    Integer id = (Integer) proteaseId;
//                    String protease = proteaseIds.get(id);
//                    stringKey.append(protease).append(" ");
//                }
//
//                peptideCountStringKey.put(stringKey.toString(), peptideCount.get(key));
//            }
//            String outputFilePath = outputDirectory + "/" + outputFileName;
//            logger.info("writing results to file " + outputFileName + "...");
//
//            PrintWriter pw = new PrintWriter(outputFilePath);
//
//            //write column header
//            pw.println("protease combination\t" +
//                    "protease count\t" +
//                    "proteome coverage\t" +
//                    "proteome coverage %\t" +
//                    "genome coverage\t" +
//                    "genome coverage %\t" +
//                    "peptide count\t" +
//                    "signature peptide count\t" +
//                    "signature peptide frequency %");
//            pw.flush();
//
//            //write column values
//            for (String combination : proteomeCoverageStringKey.keySet()) {
//
//                String proteaseCombination = combination.replace("[", "").replace("]", "");
//                String proteaseCount = "" + proteaseCombination.split(" ").length;
//                int protCoverage = proteomeCoverageStringKey.get(combination);
//                double protCoveragePercent = SigPepUtil.round(((double) protCoverage / proteinCount) * 100, 2);
//                int genCoverage = genomeCoverageStringKey.get(combination);
//                double genCoveragePercent = SigPepUtil.round(((double) genCoverage / geneCount) * 100, 2);
//                int pepCount = peptideCountStringKey.get(combination);
//                int sigPepCount = signaturePeptideCountStringKey.get(combination);
//                double sigPepFrequency = SigPepUtil.round(((double) sigPepCount / pepCount) * 100, 2);
//
//                pw.println(proteaseCombination + "\t" +
//                        proteaseCount + "\t" +
//                        protCoverage + "\t" +
//                        protCoveragePercent + "\t" +
//                        genCoverage + "\t" +
//                        genCoveragePercent + "\t" +
//                        pepCount + "\t" +
//                        sigPepCount + "\t" +
//                        sigPepFrequency);
//                pw.flush();
//
//            }
//
//            pw.close();
//
//
//        } catch (FileNotFoundException e) {
//            logger.error("Exception while reporting genome/proteome coverage for protease combinations.", e);  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//    }
//
//    public static void main(String[] args) {
//
//        for (Integer taxonId : Organisms.getInstance().getNcbiTaxonIds()) {
//
//            if (taxonId == 9606) {
//
//                try {
//
//                    ProteasePicker query = null;
//
//                    switch (args.length) {
//
//                        case (4):
//
//                            query = new ProteasePicker(
//                                    args[0], //DB username
//                                    args[1].toCharArray(), //DB password
//                                    taxonId, //NCBI taxon ID
//                                    args[3]); //output directory
//
//                            query.reportSignaturePeptideCoverageByProteaseCombination(null, "coverage_by_protease_combination_" + Organisms.getInstance().getOrganism(taxonId).replace(" ", "_") + ".tab");
//                            break;
//
//                        case (5):
//
//                            query = new ProteasePicker(
//                                    args[0], //DB username
//                                    args[1].toCharArray(), //DB password
//                                    taxonId, //NCBI taxon ID
//                                    args[3]); //output directory
//
//                            query.reportSignaturePeptideCoverageByProteaseCombination(args[4], "coverage_by_protease_combination_" + Organisms.getInstance().getOrganism(taxonId).replace(" ", "_")+ "_" + args[4]  + ".tab");
//                            break;
//
//                        default:
//                           System.out.println("usage: ProteasePicker <sigpep username> <sigpep password> <NCBI taxon ID> <output directory> [<protease [,protease ...]>]");
//
//                    }
//                } catch (DatabaseException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//            }
//        }
//    }
//
//    /**
//     * +- apt/
//      |  +- index.apt
//      |
//      +- xdoc/
//      |  +- other.xml
//      |
//      +- fml/
//      |  +- general.fml
//      |  +- faq.fml
//      |
//      +- site.xml
//     */
}
