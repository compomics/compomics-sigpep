package com.compomics.sigpep.analysis.query;

/**
 * @TODO: JavaDoc missing.
 * @TODO: All code in this class is commented out...
 *
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 16-Aug-2007
 * Time: 14:48:39
 */
public class PeptideQuery  {

//    private Connection connection;
//
//    private InnerLocker iLockable = null;
//
//    private static Logger logger = Logger.getLogger(PeptideQuery.class);
//
//    public PeptideQuery(String dbUserName, char[] dbPassword, int ncbiTaxonId, String outputDirectory) throws DatabaseException {
//        super(dbUserName, dbPassword, ncbiTaxonId, outputDirectory);
//    }
//
//    private List<Integer> fetchSignaturePeptideIds() {
//
//        logger.info("fetching signature peptide ids...");
//
//        List<Integer> retVal = new ArrayList<Integer>();
//
//        String selectSignaturePeptideIds = "SELECT peptide_id FROM peptide2protein " +
//                "GROUP BY peptide_id " +
//                "HAVING count(distinct protein_id) = 1";
//
//        try {
//
//            Statement s = connection.createStatement();
//
//            ResultSet rs = s.executeQuery(selectSignaturePeptideIds);
//
//            int row = 0;
//            while (rs.next()) {
//
//                retVal.add(rs.getInt(1));
//
//            }
//
//            rs.close();
//            s.close();
//
//        } catch (SQLException e) {
//            logger.info(e);
//        }
//
//        logger.info("done...");
//
//        return retVal;
//    }
//
//    public int fetchProteinCountOrganism() {
//
//        try {
//
//            String sql = "SELECT count(distinct protein_accession) FROM protein";
//
//            ResultSet rs = connection.createStatement().executeQuery(sql);
//
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            logger.error(e);
//        }
//
//        return -1;
//    }
//
//    public Map<String, String> fetchGene2ProteinMap() {
//
//        Map<String, String> retVal = new TreeMap<String, String>();
//
//        try {
//
//            String sql = "SELECT g.gene_accession, protein_accession " +
//                    "FROM gene g, " +
//                    "          protein2gene g2p, " +
//                    "          protein p " +
//                    "WHERE g.gene_id=g2p.gene_id " +
//                    "    AND g2p.protein_id=p.protein_id";
//
//            ResultSet rs = connection.createStatement().executeQuery(sql);
//
//            while (rs.next()) {
//
//                String geneAccession = rs.getString("gene_accession");
//                String proteinAccession = rs.getString("protein_accession");
//                if (!retVal.containsKey(geneAccession)) {
//
//                    retVal.put(geneAccession, proteinAccession);
//
//                }
//
//            }
//
//        } catch (SQLException e) {
//            logger.error(e);
//        }
//
//        return retVal;
//
//    }
//
//
//    public Map<PeptideMatch, Set<String>> fetchSignaturePeptideMatches() {
//
//        Map<PeptideMatch, Set<String>> retVal = new TreeMap<PeptideMatch, Set<String>>();
//        logger.info("fetching signature peptide matches...");
//
//        List<Integer> signaturePeptideIds = fetchSignaturePeptideIds();
//        logger.info(signaturePeptideIds.size() + " signature peptides");
//
//        String selectPeptide2Protein2Protease =
//                "SELECT g.gene_accession, " +
//                        "            pro.protein_accession, " +
//                        "            pep2pro.peptide_id, " +
//                        "            pep2pro.pos_start, " +
//                        "            pep2pro.pos_end, " +
//                        "            substr(pro.aa_sequence, pep2pro.pos_start, pep2pro.pos_end-pep2pro.pos_start + 1), " +
//                        "            prot.name " +
//                        " FROM peptide2protein pep2pro, " +
//                        "           protein pro, " +
//                        "           peptide2protease pep2prot, " +
//                        "           protease prot, " +
//                        "           protein2gene p2g, " +
//                        "           gene g " +
//                        "WHERE pep2pro.protein_id=pro.protein_id " +
//                        "        AND pep2pro.peptide_id=pep2prot.peptide_id " +
//                        "        AND pep2prot.protease_id=prot.protease_id " +
//                        "        AND pep2pro.protein_id=p2g.protein_id " +
//                        "        AND p2g.gene_id=g.gene_id " +
//                        "        AND pep2pro.peptide_id IN (?)";
//
//        try {
//
//            //get peptide matches in chuncks of 100,000
//            for (int i = 1; i < signaturePeptideIds.size(); i = i + 100000) {
//
//                int from = i;
//                int to = i + 100000;
//                if (to > signaturePeptideIds.size())
//                    to = signaturePeptideIds.size();
//
//                logger.info(i + " - " + to);
//
//
//                List<Integer> peptideIds = signaturePeptideIds.subList(from, to);
//
//                String statement = selectPeptide2Protein2Protease
//                        .replace("?", peptideIds.toString()
//                                .replace("[", "")
//                                .replace("]", ""));
//
//                Statement s = connection.createStatement();
//                ResultSet rs = s.executeQuery(statement);
//
//                while (rs.next()) {
//
//                    int peptideId = rs.getInt("peptide_id");
//                    String gene = rs.getString("gene_accession");
//                    String protein = rs.getString("protein_accession");
//                    int start = rs.getInt("pos_start");
//                    int end = rs.getInt("pos_end");
//                    String protease = rs.getString("name");
//
//                    PeptideMatch match = new PeptideMatch(peptideId, protein, gene, start, end);
//
//                    if (!retVal.containsKey(match))
//                        retVal.put(match, new TreeSet<String>());
//
//                    Set<String> proteaseFilter = retVal.get(match);
//
//                    if (!proteaseFilter.contains(protease))
//                        proteaseFilter.add(protease);
//
//
//                }
//
//                rs.close();
//                s.close();
//
//            }
//
//        } catch (SQLException e) {
//            logger.info(e);
//        }
//
//        logger.info("done");
//
//        return retVal;
//    }
//
//    public Map<String, Map<String, Set<String>>> fetchProtein2SignaturePeptide2Protease() {
//
//        Map<String, Map<String, Set<String>>> retVal = new TreeMap<String, Map<String, Set<String>>>();
//        logger.info("fetching signature peptide matches...");
//
//        List<Integer> signaturePeptideIds = fetchSignaturePeptideIds();
//        logger.info(signaturePeptideIds.size() + " signature peptides");
//
//        String selectPeptide2Protein2Protease =
//                "SELECT pep2pro.peptide_id, " +
//                        "            pro.protein_accession, " +
//                        "            pep2pro.pos_start, " +
//                        "            pep2pro.pos_end, " +
//                        "            substr(pro.aa_sequence, pep2pro.pos_start, pep2pro.pos_end-pep2pro.pos_start + 1), " +
//                        "            prot.name " +
//                        " FROM peptide2protein pep2pro, " +
//                        "      protein pro, " +
//                        "      peptide2protease pep2prot, " +
//                        "      protease prot " +
//                        "WHERE pep2pro.protein_id=pro.protein_id " +
//                        "        AND pep2pro.peptide_id=pep2prot.peptide_id " +
//                        "        AND pep2prot.protease_id=prot.protease_id " +
//                        "        AND pep2pro.peptide_id IN (?)";
//
//        try {
//
//            //get peptide matches in chuncks of 100,000
//            for (int i = 1; i < signaturePeptideIds.size(); i = i + 100000) {
//
//                int from = i;
//                int to = i + 100000;
//                if (to > signaturePeptideIds.size())
//                    to = signaturePeptideIds.size();
//
//                logger.info(i + " - " + to);
//
//                List<Integer> peptideIds = signaturePeptideIds.subList(from, to);
//
//                String statement = selectPeptide2Protein2Protease
//                        .replace("?", peptideIds.toString()
//                                .replace("[", "")
//                                .replace("]", ""));
//
//                Statement s = connection.createStatement();
//                ResultSet rs = s.executeQuery(statement);
//                //int row = 0;
//                while (rs.next()) {
//
////                    int peptideId = rs.getInt("peptide_id");
//
//                    String sequence = rs.getString("aa_sequence");
//                    String protein = rs.getString("protein_accession");
//                    String protease = rs.getString("name");
//
//                    if (!retVal.containsKey(protein))
//                        retVal.put(protein, new TreeMap<String, Set<String>>());
//
//                    Map<String, Set<String>> peptide2Protease = retVal.get(protein);
//
//                    if (!peptide2Protease.containsKey(sequence))
//                        peptide2Protease.put(sequence, new HashSet<String>());
//
//                    peptide2Protease.get(sequence).add(protease);
////                    if (++row % 10000 == 0)
////                        logger.info(row);
//
//
//                }
//
//                rs.close();
//                s.close();
//
//
//            }
//
//        } catch (SQLException e) {
//            logger.info(e);
//        }
//
//        logger.info("done");
//
//        return retVal;
//    }
//
//    private void doSummaryStatistics() throws IOException {
//
//        Map<PeptideMatch, Set<String>> peptideMatches = fetchSignaturePeptideMatches();
//
//        Map<String, Integer> proteaseCombination2PeptideCount = new TreeMap<String, Integer>();
//
//        Map<String, Set<String>> protein2protease = new TreeMap<String, Set<String>>();
//
//        for (PeptideMatch match : peptideMatches.keySet()) {
//
//            String protein = match.getProteinAccession();
//            Set<String> proteaseFilter = peptideMatches.get(match);
//
//            String proteaseCombination = proteaseFilter.toString().replace("[", "").replace("]", "");
//
//            //increment peptide count
//            if (!proteaseCombination2PeptideCount.containsKey(proteaseCombination))
//                proteaseCombination2PeptideCount.put(proteaseCombination, 0);
//
//            int count = proteaseCombination2PeptideCount.get(proteaseCombination);
//            count++;
//            proteaseCombination2PeptideCount.put(proteaseCombination, count);
//
//            //add protease to protein
//            if (!protein2protease.containsKey(protein))
//                protein2protease.put(protein, new TreeSet<String>());
//
//            protein2protease.get(protein).addAll(proteaseFilter);
//
//        }
//
//        PrintWriter pwProtease2PeptideCount = new PrintWriter("proteaseCombination2PeptideCount.txt");
//
//        for (String proteaseCombination : proteaseCombination2PeptideCount.keySet()) {
//
//            int peptideCount = proteaseCombination2PeptideCount.get(proteaseCombination);
//
//            pwProtease2PeptideCount.println(proteaseCombination + "\t" + peptideCount);
//            pwProtease2PeptideCount.flush();
//
//        }
//
//        pwProtease2PeptideCount.close();
//
//        Map<String, Set<String>> proteaseCombination2Protein = new TreeMap<String, Set<String>>();
//
//        PrintWriter pwProtein2ProteaseCount = new PrintWriter("protein2ProteaseCombination.txt");
//
//        for (String protein : protein2protease.keySet()) {
//
//            Set<String> proteaseFilter = protein2protease.get(protein);
//            String proteaseCombination = proteaseFilter.toString().replace("[", "").replace("]", "");
//
//            pwProtein2ProteaseCount.println(protein + "\t" + proteaseFilter.size() + "\t" + proteaseCombination);
//            pwProtein2ProteaseCount.flush();
//
//
//            if (!proteaseCombination2Protein.containsKey(proteaseCombination))
//                proteaseCombination2Protein.put(proteaseCombination, new TreeSet<String>());
//
//            proteaseCombination2Protein.get(proteaseCombination).add(protein);
//
//        }
//
//        pwProtein2ProteaseCount.close();
//
//        PrintWriter pwProteaseCombination2Protein = new PrintWriter("proteaseCombination2ProteinCount.txt");
//
//        for (String proteaseCompination : proteaseCombination2Protein.keySet()) {
//
//            Set<String> proteins = proteaseCombination2Protein.get(proteaseCompination);
//            pwProteaseCombination2Protein.println(proteaseCompination + "\t" + proteins.size());
//
//            pwProteaseCombination2Protein.flush();
//
//        }
//
//        pwProteaseCombination2Protein.close();
//
//
//    }
//
//    public void printProteinStatistics() throws FileNotFoundException {
//
//        int proteinCount = this.fetchProteinCountOrganism();
//        Map<String, Map<String, Set<String>>> protein2SignaturePeptide2Protease = this.fetchProtein2SignaturePeptide2Protease();
//
//        PrintWriter pwSummaryReport = new PrintWriter(ncbiTaxonId + "_summary_report.txt");
//        PrintWriter pwProteinReport = new PrintWriter(ncbiTaxonId + "_protein_report.txt");
//        PrintWriter pePeptideReport = new PrintWriter(ncbiTaxonId + "_peptide_report.txt");
//
//        //print header for peptide report
//        pwProteinReport.println("protein\targc\tlysc\tpepa\ttryp\tv8de\tv8e\tcnbr\ttotal");
//        pePeptideReport.println("peptide\targc\tlysc\tpepa\ttryp\tv8de\tv8e\tcnbr");
//
//        int peptideCount = 0;
//        int argcPeptideCount = 0;
//        int lyscPeptideCount = 0;
//        int pepaPeptideCount = 0;
//        int trypPeptideCount = 0;
//        int v8dePeptideCount = 0;
//        int v8ePeptideCount = 0;
//        int cnbrPeptideCount = 0;
//
//        for (String protein : protein2SignaturePeptide2Protease.keySet()) {
//
//            Map<String, Set<String>> peptide2Protease = protein2SignaturePeptide2Protease.get(protein);
//
//            int[] peptideCountByEnzym = new int[7];
//            peptideCountByEnzym[0] = 0;
//            peptideCountByEnzym[1] = 0;
//            peptideCountByEnzym[2] = 0;
//            peptideCountByEnzym[3] = 0;
//            peptideCountByEnzym[4] = 0;
//            peptideCountByEnzym[5] = 0;
//            peptideCountByEnzym[6] = 0;
//
//            for (String peptide : peptide2Protease.keySet()) {
//
//                peptideCount++;
//
//                Set<String> proteaseFilter = peptide2Protease.get(peptide);
//
//                pePeptideReport.print(peptide + "\t");
//
//                if (proteaseFilter.contains("argc")) {
//                    argcPeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[0]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("lysc")) {
//                    lyscPeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[1]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("pepa")) {
//                    pepaPeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[2]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("tryp")) {
//                    trypPeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[3]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("v8de")) {
//                    v8dePeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[4]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("v8e")) {
//                    v8ePeptideCount++;
//                    pePeptideReport.print("1\t");
//                    peptideCountByEnzym[5]++;
//                } else {
//                    pePeptideReport.print("0\t");
//                }
//
//                if (proteaseFilter.contains("cnbr")) {
//                    cnbrPeptideCount++;
//                    pePeptideReport.print("1\n");
//                    peptideCountByEnzym[6]++;
//                } else {
//                    pePeptideReport.print("0\n");
//                }
//
//                pePeptideReport.flush();
//
//            }
//
//            //print the protein id and the number of unique peptides
//            pwProteinReport.println(protein + "\t"
//                    + peptideCountByEnzym[0] + "\t"
//                    + peptideCountByEnzym[1] + "\t"
//                    + peptideCountByEnzym[2] + "\t"
//                    + peptideCountByEnzym[3] + "\t"
//                    + peptideCountByEnzym[4] + "\t"
//                    + peptideCountByEnzym[5] + "\t"
//                    + peptideCountByEnzym[6] + "\t"
//                    + peptide2Protease.size());
//
//            pwProteinReport.flush();
//        }
//
//        int proteinWithSigPep = protein2SignaturePeptide2Protease.size();
//
//        pwSummaryReport.println("organism: " + SigPepDatabase.getSpeciesSuffix(ncbiTaxonId).replace("sigpep_", ""));
//        pwSummaryReport.println("protein count organism = " + proteinCount);
//        pwSummaryReport.println("protein with signature peptide count = " + proteinWithSigPep + " (" + String.format("%.2f", ((double) proteinWithSigPep / (double) proteinCount) * 100) + "%)");
//        pwSummaryReport.println("peptide      count = " + peptideCount);
//        pwSummaryReport.println("argc peptide count = " + argcPeptideCount + "(" + String.format("%.2f", ((double) argcPeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("lysc peptide count = " + lyscPeptideCount + "(" + String.format("%.2f", ((double) lyscPeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("pepa peptide count = " + pepaPeptideCount + "(" + String.format("%.2f", ((double) pepaPeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("tryp peptide count = " + trypPeptideCount + "(" + String.format("%.2f", ((double) trypPeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("v8de peptide count = " + v8dePeptideCount + "(" + String.format("%.2f", ((double) v8dePeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("v8e  peptide count = " + v8ePeptideCount + "(" + String.format("%.2f", ((double) v8ePeptideCount / (double) peptideCount) * 100) + "%)");
//        pwSummaryReport.println("cnbr  peptide count = " + cnbrPeptideCount + "(" + String.format("%.2f", ((double) cnbrPeptideCount / (double) peptideCount) * 100) + "%)");
//
//        pwSummaryReport.close();
//        pePeptideReport.close();
//        pwProteinReport.close();
//    }
//
//    public static void translationsWithSignaturePeptideVersusTotalTranslationCount(String outputfile){
//
//
//
//    }
//
//    public static void main(String[] args) {
//
//        //7955 7227 9606 10090 10116 4932
//
//        for (String arg : args) {
//            System.out.println("taxId = " + arg);
//            int ncbiTaxonId = Integer.parseInt(arg);
//            PeptideQuery pq = null;
//            try {
//                pq = new PeptideQuery("mmueller", "".toCharArray(), ncbiTaxonId, "");
//            } catch (DatabaseException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            InnerLocker lock = new InnerLocker();
//            pq.setLockable(lock);
//            Thread t = new Thread(pq);
//            t.start();
//            while (!lock.isComplete()) {
//                System.out.println("Waiting for completion.");
//            }
//            System.out.println("All done.");
//
//        }
//
//
//    }
//
//    public void setLockable(InnerLocker aLockable) {
//        iLockable = aLockable;
//    }
//
//    public void setupDatabase() {
//        try {
//
//            //printProteinStatistics();
//
//            doSummaryStatistics();
//
//            System.out.println("I'm done, releasing lock!");
//            iLockable.setComplete();
//
//        } catch (IOException e) {
//
//            logger.error(e);
//
//        }
//    }
//
//
//    private static class InnerLocker {
//        private boolean isComplete = false;
//
//        public synchronized boolean isComplete() {
//            while (!isComplete) {
//                try {
//                    wait();
//                } catch (InterruptedException ie) {
//
//                }
//            }
//            return isComplete;
//        }
//
//        public synchronized void setComplete() {
//            this.isComplete = true;
//            notifyAll();
//        }
//    }
//
//
//    class PeptideMatch implements Comparable {
//
//        private int peptideId;
//        private String proteinAccession;
//        private String geneAccession;
//        private int start;
//        private int end;
//
//
//        public PeptideMatch(int peptideId, String proteinAccession, String geneAccession, int start, int end) {
//            this.peptideId = peptideId;
//            this.proteinAccession = proteinAccession;
//            this.geneAccession = geneAccession;
//            this.start = start;
//            this.end = end;
//        }
//
//        public int getPeptideId() {
//            return peptideId;
//        }
//
//        public void setPeptideId(int peptideId) {
//            this.peptideId = peptideId;
//        }
//
//        public String getProteinAccession() {
//            return proteinAccession;
//        }
//
//        public void setProteinAccession(String proteinAccession) {
//            this.proteinAccession = proteinAccession;
//        }
//
//        public String getGeneAccession() {
//            return geneAccession;
//        }
//
//        public void setGeneAccession(String geneAccession) {
//            this.geneAccession = geneAccession;
//        }
//
//        public int getStart() {
//            return start;
//        }
//
//        public void setStart(int start) {
//            this.start = start;
//        }
//
//        public int getEnd() {
//            return end;
//        }
//
//        public void setEnd(int end) {
//            this.end = end;
//        }
//
//        public int compareTo(Object o) {
//
//            //order by ontolgy, identifier, name
//            PeptideMatch them = (PeptideMatch) o;
//            if (!this.getProteinAccession().equals(them.getProteinAccession()))
//                return this.getProteinAccession().compareTo(them.getProteinAccession());
//            if (this.getStart() != (them.getStart())) return this.getStart() - them.getStart();
//            return this.getEnd() - them.getEnd();
//
//        }
//
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            PeptideMatch that = (PeptideMatch) o;
//
//            if (end != that.end) return false;
//            if (peptideId != that.peptideId) return false;
//            if (start != that.start) return false;
//            if (!geneAccession.equals(that.geneAccession)) return false;
//            if (!proteinAccession.equals(that.proteinAccession)) return false;
//
//            return true;
//        }
//
//        public int hashCode() {
//            int result;
//            result = peptideId;
//            result = 31 * result + proteinAccession.hashCode();
//            result = 31 * result + geneAccession.hashCode();
//            result = 31 * result + start;
//            result = 31 * result + end;
//            return result;
//        }
//    }
}
