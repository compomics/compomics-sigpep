package com.compomics.sigpep.analysis.query;

/**
 * @TODO: JavaDoc missing.
 * @TODO: All code in this class is commented out...
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 12-Oct-2007<br>
 * Time: 11:46:45<br>
 */
public class PrecursorIonMassOverlap {


//    private static final String SQL_CREATE_TEMPORARY_PEPTIDE_MASS_TABLE =
//            "CREATE TEMPORARY TABLE peptide_mass_temp ( " +
//                    "peptide_id  INT, " +
//                    "mass        DOUBLE, " +
//                    "PRIMARY KEY (peptide_id) " +
//                    ")";
//
//    private static final String SQL_CREATE_TEMPORARY_SIGNATURE_PEPTIDE_MASS_TABLE =
//                "CREATE TEMPORARY TABLE signature_peptide_mass_temp " +
//                        "SELECT pm.peptide_id, pm.mass " +
//                        "FROM peptide_mass pm, " +
//                        "     peptide2protease pep2prot, " +
//                        "     protease prot " +
//                        "WHERE pm.peptide_id=pep2prot.peptide_id " +
//                        "  AND pep2prot.protease_id=prot.protease_id " +
//                        "  AND prot.name IN (:proteaseNames) " +
//                        "  AND pm.peptide_id IN (" +
//                        "SELECT peptide_id FROM signature_peptide " +
//                        ")";
//
//
//    private static final String SQL_SELECT_PEPTIDES_BY_PROTEASE =
//            "SELECT pm.peptide_id, pm.mass " +
//                    "FROM peptide_mass pm, " +
//                    "     peptide2protease pep2prot, " +
//                    "     protease prot " +
//                    "WHERE pm.peptide_id=pep2prot.peptide_id " +
//                    "  AND pep2prot.protease_id=prot.protease_id " +
//                    "  AND prot.name IN (:proteaseNames)";
//
//
//    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_IDS_IN_MASS_RANGE = "SELECT mass, peptide_id FROM signature_peptide_mass_temp WHERE mass >= ? AND mass <= ?";
//
//    private static final String SQL_SELECT_SIGNATURE_PEPTIDE_MASS_FREQUENCY = NamedQueryAccess.getInstance().getString("query.signaturePeptideMassFrequency");
//    private static final String SQL_SELECT_PEPTIDE_MASS_FREQUENCY = NamedQueryAccess.getInstance().getString("query.peptideMassFrequency");
//
//
//    private static final String SQL_SELECT_PEPTIDE_COUNT_IN_MASS_RANGE = "SELECT COUNT(peptide_id) AS count FROM peptide_mass_temp WHERE mass > ? AND mass < ?";
//
//    private Connection conn;
//
//    /**
//     * Constructs an object to query SigPep for the mass overlap between signature peptides and non-signature peptides.
//     *
//     * @param userName        username for SigPep database connections
//     * @param password        password for SigPep database connections
//     * @param ncbiTaxonId     the NCBI taxon ID of the species the namedQueries are for
//     * @param outputDirectory the directory the query results will be written to
//     * @throws DatabaseException if an exception occurs while communicating with the SigPep database
//     */
//    public PrecursorIonMassOverlap(String userName, char[] password, int ncbiTaxonId, String outputDirectory) throws DatabaseException {
//        super(userName, password, ncbiTaxonId, outputDirectory);
//        try {
//            conn = this.sigPepDatabase.getConnection();
//        } catch (SQLException e) {
//            throw new DatabaseException(e);
//        }
//    }
//
//    /**
//     * Returns and ordered map of signature peptide masses and their absolute frequencies.
//     *
//     * @return a map of masses and signature peptide frequencies
//     * @throws SQLException if an exception occurs while querying the database
//     */
//    private Map<Double, Integer> fetchSignaturePeptideMassFrequency() throws SQLException {
//
//        Map<Double, Integer> retVal = new TreeMap<Double, Integer>();
//
//        Statement s = conn.createStatement();
//
//        ResultSet rs = s.executeQuery(SQL_SELECT_SIGNATURE_PEPTIDE_MASS_FREQUENCY);
//
//        while (rs.next()) {
//
//            double mass = rs.getDouble("mass");
//            int count = rs.getInt("count");
//            retVal.put(mass, count);
//
//        }
//
//        rs.close();
//        s.close();
//
//        return retVal;
//
//    }
//
//
//
//
//    /**
//     * Returns and ordered map of peptide masses and their absolute frequencies.
//     *
//     * @return a map of masses and peptide frequencies
//     * @throws SQLException if an exception occurs while querying the database
//     */
//    private Map<Double, Integer> fetchPeptideMassFrequency() throws SQLException {
//
//        Map<Double, Integer> retVal = new TreeMap<Double, Integer>();
//
//        Statement s = conn.createStatement();
//
//        ResultSet rs = s.executeQuery(SQL_SELECT_PEPTIDE_MASS_FREQUENCY);
//
//        while (rs.next()) {
//
//            double mass = rs.getDouble("mass");
//            int count = rs.getInt("count");
//            retVal.put(mass, count);
//
//        }
//
//        rs.close();
//        s.close();
//
//        return retVal;
//
//
//    }
//
//
//    /**
//     * Returns a map of signature peptides and their masses.
//     *
//     * @param min minimum mass
//     * @param max maximum mass
//     * @return map of peptide IDs and respective masses
//     * @throws SQLException if an exception occurs during database access
//     */
//    private Map<Integer, Double> fetchSignaturePeptidesInMassRange(double min, double max) throws SQLException {
//
//        Map<Integer, Double> retVal = new TreeMap<Integer, Double>();
//
//        PreparedStatement ps = conn.prepareStatement(SQL_SELECT_SIGNATURE_PEPTIDE_IDS_IN_MASS_RANGE);
//        ps.setDouble(1, min);
//        ps.setDouble(2, max);
//
//        ResultSet rs = ps.executeQuery();
//
//        while (rs.next()) {
//
//            double mass = rs.getDouble("mass");
//            int id = rs.getInt("peptide_id");
//            retVal.put(id, mass);
//
//        }
//
//        rs.close();
//        ps.close();
//
//        return retVal;
//
//    }
//
//
//
//
//
//
//
//    /**
//     * Creates a temporary table containing only those peptides generated by
//     * the set of proteaseFilter.
//     *
//     * @param proteaseNames the names of the proteaseFilter
//     * @throws SQLException if an exception occurs while creating the temporary table
//     */
//    private void createTemporaryPeptideMassTable(Set<String> proteaseNames) throws SQLException {
//
//
//        String sql = SqlUtil.setParameterSet(SQL_SELECT_PEPTIDES_BY_PROTEASE, "proteaseNames", proteaseNames);
//
//        Statement s = conn.createStatement();
//        s.execute("DROP TABLE IF EXISTS peptide_mass_temp");
//        s.execute(SQL_CREATE_TEMPORARY_PEPTIDE_MASS_TABLE);
//        s.close();
//
//        PreparedStatement insert = conn.prepareStatement("INSERT INTO peptide_mass_temp (peptide_id, mass) VALUES (?,?)");
//        ResultSet rs = s.executeQuery(sql);
//        while (rs.next()) {
//
//            int peptideId = rs.getInt(1);
//            double mass = rs.getDouble(2);
//            insert.setInt(1, peptideId);
//            insert.setDouble(2, mass);
//            insert.addBatch();
//
//        }
//
//        rs.close();
//        s.close();
//
//        insert.executeBatch();
//
//        insert.close();
//
//
//    }
//
//    /**
//     * Creates a temporary table containing only those signature peptides generated by
//     * the set of proteaseFilter.
//     *
//     * @param proteaseNames the names of the proteaseFilter
//     * @throws SQLException if an exception occurs while creating the temporary table
//     */
//    private void createTemporarySignaturePeptideMassTable(Set<String> proteaseNames) throws SQLException {
//
//        String sql = SqlUtil.setParameterSet(SQL_CREATE_TEMPORARY_SIGNATURE_PEPTIDE_MASS_TABLE, "proteaseNames", proteaseNames);
//
//        Statement s = conn.createStatement();
//        s.execute("DROP TABLE IF EXISTS signature_peptide_mass_temp");
//        s.execute(sql);
//
//        s.close();
//
//    }
//
//
//
//
//    public void reportSignaturePeptidePrecursorMassOverlap(String outputFileName, int minimumCharge, int maximumCharge, double accuracy, double minOverlap, String[] proteaseNames) {
//
//        try {
//
//            Set<String> proteaseFilter = new HashSet<String>();
//
//            for (String proteaseName : proteaseNames) {
//                proteaseFilter.add(proteaseName.replaceAll(" ", ""));
//            }
//
//            logger.info("creating temporary peptide mass table " + proteaseFilter + "...");
//            createTemporaryPeptideMassTable(proteaseFilter);
//
//            logger.info("creating temporary signature peptide mass table " + proteaseFilter + "...");
//            createTemporarySignaturePeptideMassTable(proteaseFilter);
//
//            logger.info("fetching signature peptide masses...");
//            Map<Double, Integer> signaturePeptideMassFrequency = this.fetchSignaturePeptideMassFrequency();
//
//            String outputFilePath = outputDirectory + "/" + outputFileName;
//            logger.info("calculating overlapping peptide masses and writing results to file " + outputFilePath + "...");
//
//            PrintWriter pw = new PrintWriter(outputFilePath);
//
//            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_PEPTIDE_COUNT_IN_MASS_RANGE);
//
//            int counter = 0;
//            double previousProgress = 0;
//            for (Double peptideMass : signaturePeptideMassFrequency.keySet()) {
//
//                MassOverChargeRange pmr = new MassOverChargeRangeImpl(peptideMass, minimumCharge, maximumCharge, accuracy);
//
//                int frequency = signaturePeptideMassFrequency.get(peptideMass);
//
//                int overlapFrequency = 0;
//                for (MassOverChargeRange[] overLappingPmr : pmr.getFlankingPeptideMassOverChargeRanges()) {
//
//                    double lowerFlankingMass = overLappingPmr[0].getNeutralPeptideMass();
//                    double upperFlankingMass = overLappingPmr[1].getNeutralPeptideMass();
//
//                    ps.setDouble(1, lowerFlankingMass);
//                    ps.setDouble(2, upperFlankingMass);
//
//                    ResultSet rs = ps.executeQuery();
//                    if (rs.next()) {
//
//                        int massFrequency = rs.getInt("count");
//                        overlapFrequency = overlapFrequency + massFrequency;
//
//                    }
//                    rs.close();
//
//                }
//
//                pw.println(peptideMass + "\t" + frequency + "\t" + overlapFrequency);
//                pw.flush();
//
//                //some user feedback
//                counter++;
//
//                double progress = SigPepUtil.round(((double) counter / (double) signaturePeptideMassFrequency.size()) * 100, 1);
//                if (progress % 1 == 0.0 && progress != previousProgress) {
//                    logger.info((int) progress + " % processed...");
//                    previousProgress = progress;
//                }
//
//            }
//
//            pw.close();
//
//            ps.close();
//
//            logger.info("done");
//
//        } catch (SQLException e) {
//            logger.error(e);
//        }
//        catch (FileNotFoundException e) {
//            logger.error(e);
//        }
//
//    }
//
//
//
//
//
//
//
//public static void main(String[]args){
//
//        String usage="PrecursorIonMassOverlap <dbusername> <dbpassword> <NCBI taxon ID> <output direcotry> <output file name> <minimumCharge> <maximumCharge> <accuracy> <minOverlap>";
//try{
//
//        if(args.length==10){
//        PrecursorIonMassOverlap query=new PrecursorIonMassOverlap(
//        args[0], //DB username
//        args[1].toCharArray(), //DB password
//        new Integer(args[2]), //NCBI taxon ID
//        args[3]); //output directory
//
//query.reportSignaturePeptidePrecursorMassOverlap(args[4],  //outputFileName
//        new Integer(args[5]),   //minimumCharge
//        new Integer(args[6]),   //maximumCharge
//        new Double(args[7]),   //accuracy
//        new Integer(args[8]),
//        args[9].split(","));  //minOverlap
//
//}else{
//        logger.error("Wrong number of arguments. "+usage);
//}
//
//        }catch(DatabaseException e){
//        logger.error(e);
//}
//
//
//        }
}
