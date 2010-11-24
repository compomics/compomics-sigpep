package com.compomics.sigpep.analysis.query;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.dbtools.DatabaseException;
import com.compomics.dbtools.SqlUtil;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.persistence.rdbms.SigPepDatabase;
import com.compomics.sigpep.util.DelimitedTableWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Apr-2008<br/>
 * Time: 16:40:32<br/>
 */
public class SequenceCoverage {


    /**
     * the log4j logger
     */
    private static Logger logger = Logger.getLogger(SequenceCoverage.class);

    private SigPepDatabase sigPepDatabase;

    /**
     * Constructs an object to query SigPep for the coverage of the proteome by signature peptides.
     *
     * @param sigPepDatabase the SigPep database
     */
    public SequenceCoverage(SigPepDatabase sigPepDatabase) {
        this.sigPepDatabase = sigPepDatabase;
    }

    /**
     * Creates a report of the coverage of the proteome by signature peptides.
     *
     * @param outputFileName where the report goes
     * @param proteaseNames  the names of the proteaseFilter to combine
     */
    public void reportSequenceCoverageByProteaseCombination(String outputFileName, Set<String> proteaseNames) {

        logger.info("creating signature peptide coverage report...");

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory)appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);
        SigPepQueryService service = session.createSigPepQueryService();
        int totalProteinCount = service.getProteinCount();
        int totalGeneCount = service.getGeneCount();
        int totalProteinAltspliceCount = -1;
        int totalGeneAltspliceCount = -1;

        try {

            Connection c = this.sigPepDatabase.getConnection();
            Statement s = c.createStatement();

            //create temporary tables
            try {
                s.execute("CREATE TEMPORARY TABLE signature_peptides(peptide_id INT UNSIGNED PRIMARY KEY)");
                s.execute("CREATE TEMPORARY TABLE gene_altsplice(gene_id INT UNSIGNED PRIMARY KEY)");
                s.execute("CREATE TEMPORARY TABLE sequence_altsplice(sequence_id INT UNSIGNED PRIMARY KEY)");
            } catch (SQLException e) {
                logger.error(e);
            }
            //alternatively spliced genes (sequence level)

            try {
                s.execute("INSERT INTO gene_altsplice(gene_id) " +
                        "   SELECT g2pro.gene_id " +
                        "    FROM gene2protein g2pro, " +
                        "         protein2sequence pro2seq " +
                        "   WHERE g2pro.protein_id=pro2seq.protein_id " +
                        "GROUP BY g2pro.gene_id " +
                        "HAVING count(pro2seq.sequence_id) > 1");
            } catch (SQLException e) {
                logger.error(e);
            }
            try {
                s.execute("INSERT INTO sequence_altsplice(sequence_id) " +
                        "                     SELECT DISTINCT protein2sequence.sequence_id " +
                        "                     FROM gene2protein, " +
                        "                          protein2sequence " +
                        "                     WHERE gene2protein.protein_id=protein2sequence.protein_id " +
                        "                       AND gene2protein.gene_id IN ( " +
                        "                     SELECT gene_id " +
                        "                     FROM gene_altsplice " +
                        "                     )");
            }
            catch (SQLException e) {
                logger.error(e);
            }

            try {
                //signature peptides
                s.execute(SqlUtil.setParameterSet("INSERT INTO signature_peptides(peptide_id) " +
                        "SELECT peptide_degeneracy.peptide_id " +
                        "              FROM " +
                        "             (SELECT pf.peptide_id, count(distinct pf.sequence_id) as sequence_count " +
                        "                FROM peptide_feature pf, protease2peptide_feature pf2prot , protease prot " +
                        "               WHERE pf.peptide_feature_id=pf2prot.peptide_feature_id " +
                        "                 AND pf2prot.protease_id=prot.protease_id " +
                        "                 AND prot.name IN (:proteaseCombination) " +
                        "            GROUP BY pf.peptide_id) peptide_degeneracy " +
                        "             WHERE peptide_degeneracy.sequence_count = 1", "proteaseCombination", proteaseNames));
            } catch (SQLException e) {
                logger.error(e);
            }

            //get alternatively spliced sequence IDs
            Set<Integer> sequenceIdAltSplice = new HashSet<Integer>();

            ResultSet rsAltSplice = s.executeQuery("SELECT sequence_id FROM sequence_altsplice");
            while(rsAltSplice.next()){
                sequenceIdAltSplice.add(rsAltSplice.getInt("sequence_id"));
            }
            rsAltSplice.close();

            ResultSet rsCoverage = s.executeQuery(SqlUtil.setParameterSet(
                    "SELECT pf.sequence_id, CHAR_LENGTH(seq.aa_sequence) AS sequence_length, pf.peptide_id , pf.pos_start, pf.pos_end " +
                            "  FROM peptide_feature pf, " +
                            "       protein_sequence seq, " +
                            "       protease2peptide_feature prot2pf, " +
                            "       protease prot," +
                            "       signature_peptides sp " +
                            "WHERE pf.sequence_id=seq.sequence_id " +
                            "  AND pf.peptide_feature_id=prot2pf.peptide_feature_id " +
                            "  AND prot2pf.protease_id=prot.protease_id " +
                            "  AND prot.name IN (:proteaseCombination)" +
                            "  AND pf.peptide_id=sp.peptide_id", "proteaseCombination", proteaseNames));

            PrintWriter pw  = new PrintWriter(outputFileName);
            DelimitedTableWriter dtw = new DelimitedTableWriter(pw, "\t", false);

            Map<Integer, SequenceCoverageObject> result = new TreeMap<Integer, SequenceCoverageObject>();


            while (rsCoverage.next()) {

                int sequenceId = rsCoverage.getInt("sequence_id");
                int sequenceLength = rsCoverage.getInt("sequence_length");
                int peptideId = rsCoverage.getInt("peptide_id");
                int start = rsCoverage.getInt("pos_start");
                int end = rsCoverage.getInt("pos_end");

                SequenceCoverageObject sco = null;
                if(result.containsKey(sequenceId)){
                    sco = result.get(sequenceId);
                } else {
                    sco = new SequenceCoverageObject(sequenceId);
                }

                sco.addPeptideId(peptideId);
                sco.setSequenceCovered(start, end);
                sco.setSequenceLength(sequenceLength);

                result.put(sequenceId, sco);


            }


            rsCoverage.close();
            s.close();

            dtw.writeHeader("sequence_id", "sequence_length", "coverage", "peptide_count");
            for(SequenceCoverageObject sco : result.values()){

                boolean altSplice = sequenceIdAltSplice.contains(sco.getSequenceId());

                dtw.writeRow(sco.getSequenceId(),
                        sco.getSequenceLength(),
                        sco.getCoverage().size(), 
                        sco.getPeptideIds().size(),
                        altSplice);


            }

            pw.close();

        }
        catch (FileNotFoundException e) {
            logger.error(e);
        }
        catch (SQLException e) {
            logger.error(e);
        }

    }

    class SequenceCoverageObject {

        private int sequenceId;
        private Set<Integer> coverage = new TreeSet<Integer>();
        private Set<Integer> peptideIds = new HashSet<Integer>();
        private int sequenceLength;

        SequenceCoverageObject(int sequenceId) {
            this.sequenceId = sequenceId;
        }

        public int getSequenceLength() {
            return sequenceLength;
        }

        public void setSequenceLength(int sequenceLength) {
            this.sequenceLength = sequenceLength;
        }

        public int getSequenceId() {
            return sequenceId;
        }

        public void setSequenceId(int sequenceId) {
            this.sequenceId = sequenceId;
        }

        public void addPeptideId(int peptideId) {
            this.peptideIds.add(peptideId);
        }

        public Set<Integer> getCoverage() {
            return coverage;
        }

        public Set<Integer> getPeptideIds() {
            return peptideIds;
        }

        public void setSequenceCovered(int from, int to) {
            for (int i = from; i <= to; i++) {
                coverage.add(i);
            }
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SequenceCoverageObject)) return false;

            SequenceCoverageObject that = (SequenceCoverageObject) o;

            if (sequenceId != that.sequenceId) return false;

            return true;
        }

        public int hashCode() {
            return sequenceId;
        }
    }

    public static void main(String[] args) {

        try {
            SigPepDatabase sigPepDb = new SigPepDatabase("mmueller", "".toCharArray(), 9606);
            SequenceCoverage sc = new SequenceCoverage(sigPepDb);
            Set<String> proteaseNames = new HashSet<String>();
            //proteaseNames.add("lysc");
            //proteaseNames.add("argc");
            //proteaseNames.add("v8e");
            proteaseNames.add("pepa");
            //proteaseNames.add("v8de");
            //proteaseNames.add("tryp");


            sc.reportSequenceCoverageByProteaseCombination("/home/mmueller/svn/manuscripts/sigpep/data/sequence_coverage_pepa.csv", proteaseNames);


        } catch (DatabaseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
